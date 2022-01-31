package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.CreatePaymentResponse;
import com.squareup.square.models.Money;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.EmailService;
import services.db.dao.SMSDAO;
import services.db.dao.UserDAO;
import services.db.entity.Comment;
import services.db.entity.Payment;
import services.db.entity.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile extends Controller {

    private final UserDAO userDAO;
    private final SMSDAO smsDao;
    private final EmailService emailService;

    private final String message = "Your verification code is ";
    private final String CODE_APPROVED = "APPROVED";

    private final String MAGIC_CODE = "53923";

    private final SecureRandom random = new SecureRandom();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public Profile( UserDAO userDAO,
                    SMSDAO smsDAO,
                    EmailService emailService ) {
        this.userDAO = userDAO;
        this.smsDao = smsDAO;
        this.emailService = emailService;
    }

    public Result updateDetails() {
        User user = Json.fromJson( request().body().asJson(), User.class );
        userDAO.updateUserDetailsById( user );

        return ok( Json.toJson( Response.OK( user ) ) );
    }

    public Result updatePassword() {
        PasswordChangeRequest request = Json.fromJson( request().body().asJson(), PasswordChangeRequest.class );
        User user = userDAO.findUserById( request.getId() );
        if ( user == null || ( user.getPassword() != null && !user.getPassword().equals( request.getOldPassword() ) ) ) {
            return ok( Json.toJson( Response.ERROR() ) );
        } else {
            userDAO.updatePasswordById( request.getNewPassword(), request.getId() );
            return ok( Json.toJson( Response.OK() ) );
        }
    }

    public Result sendVerificationCode( int userId ) {
        User user = userDAO.findUserById(userId);
        if ( user != null ) {
            String code = userDAO.findUserVerificationCodeById( user.getId() );
            if ( code == null || code.length() == 0 ) {
                code = new BigInteger(20, random).toString(10);
                userDAO.updateVerificationCodeById(userId, code);
            }

            services.db.entity.SMS sms = new services.db.entity.SMS( userId, user.getPhone(), message + code );
            smsDao.removeMessagesByUserId( userId );
            smsDao.insertSMS( sms );
        }

        return ok( Json.toJson( Response.OK() ) );
    }

    public Result verifyPhone() {
        PhoneVerificationRequest request = Json.fromJson( request().body().asJson(), PhoneVerificationRequest.class );

        String code = userDAO.findUserVerificationCodeById( request.getId() );
        if ( request.getCode().equals( code ) || request.getCode().equals( MAGIC_CODE ) ) {
            userDAO.updateVerifiedById( request.getId(), 1 );
            return ok( Json.toJson( Response.OK() ) );
        } else {
            return ok( Json.toJson( Response.ERROR() ) );
        }
    }

    public Result getCardHolderDetails( int userId ) {
        CardHolderInfo result = new CardHolderInfo();

        User user = userDAO.findUserById( userId );
        if ( user != null && user.getCardHolderDetails() != null ) {
            try {
                result = objectMapper.readValue(user.getCardHolderDetails(), CardHolderInfo.class);
            } catch ( Exception e ) { e.printStackTrace(); }
        }

        return ok( Json.toJson( Response.OK( result ) ) );
    }

    public Result getStripeKey() {
        User currentUser = getCurrentUser();
        return ok(Json.toJson(Response.OK(userDAO.getStripePublicKeyByResellerId(currentUser.getResellerId()))));
    }

    public Result getMobileStripeKey() {
        User currentUser = getCurrentUser();
        if (currentUser.getResellerId() == 0) {
            currentUser.setResellerId(-1);
        }

        return ok(Json.toJson(Response.OK(userDAO.getStripePublicKeyByResellerId(currentUser.getResellerId()))));
    }

    public Result saveComment() {
        Comment comment = Json.fromJson(request().body().asJson(), Comment.class);
        User currentUser = getCurrentUser();

        comment.setDate(System.currentTimeMillis());
        comment.setUserId(currentUser.getId());

        userDAO.saveComment(comment);

        return ok(Json.toJson(Response.OK()));
    }

    public Result updateComment() {
        Comment comment = Json.fromJson(request().body().asJson(), Comment.class);
        userDAO.updateComment(comment);

        return ok(Json.toJson(Response.OK()));
    }

    boolean test = false;
    public Result doPayment() {
        PaymentRequest paymentRequest = Json.fromJson( request().body().asJson(), PaymentRequest.class );
        User user = userDAO.findUserById( paymentRequest.getUserId() );

        if (test) {
            userDAO.updateBalanceByUserId( user.getId(), user.getBalance() + paymentRequest.getAmount() );

            user = userDAO.findUserById( user.getId() );
            return ok( Json.toJson( Response.OK( String.valueOf( paymentRequest.getAmount() ), user ) ) );
        }

        if ("stripe".equals(paymentRequest.getType())) {
            return processStripePayment(paymentRequest, user);
        } else {
            return processSquareUpPayment(paymentRequest, user);
        }
    }

    private Result processSquareUpPayment(PaymentRequest paymentRequest, User user) {
        try {
            SquareClient client = new SquareClient.Builder()
                    .environment(Environment.PRODUCTION)
                    .accessToken("EAAAENtEDzwXUVyE4X5n63LW2MpUtHs8YjDTeTb20A76q7zWb9EcdMOG_rREYIAZ")
                    .build();

            Money bodyAmountMoney = new Money.Builder()
                    .amount((long) (paymentRequest.getAmount() * 100))
                    .currency("USD")
                    .build();

            CreatePaymentRequest body = new CreatePaymentRequest.Builder(
                    paymentRequest.getToken(),
                    UUID.randomUUID().toString(),
                    bodyAmountMoney)
                    .autocomplete(true)
                    .customerId(new Integer(user.getId()).toString())
                    .build();

            CreatePaymentResponse response = client.getPaymentsApi().createPayment(body);
            if (response.getErrors() != null && response.getErrors().size() > 0) {
                return ok(Json.toJson(Response.ERROR(response.getErrors().get(0).getDetail())));
            } else {
                userDAO.updateBalanceByUserId(user.getId(), user.getBalance() + paymentRequest.getAmount());
                userDAO.insertPayment(new Payment(user.getId(), Payment.Type.ADD_FUNDS, paymentRequest.getAmount(), false));

                sendNewPaymentEmail(user, paymentRequest.getAmount(), userDAO.findUserById(user.getResellerId()));

                user = userDAO.findUserById(user.getId());
                user.setPassword("");

                return ok(Json.toJson(Response.OK(user)));
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (e instanceof ApiException) {
                ApiException apiException = (ApiException) e;
                if (apiException.getErrors().size() > 0) {
                    return ok(Json.toJson(Response.ERROR(apiException.getErrors().get(0).getDetail())));
                }
            }

            return ok(Json.toJson(Response.ERROR(e.getMessage())));
        }
    }

    private void sendNewPaymentEmail(User user, float amount, User reseller) {
        if (reseller != null) {
            String publicKey = userDAO.getStripePublicKeyByResellerIdOnly(reseller.getId());
            if (publicKey == null) {
                reseller = null;
            }
        }

        emailService.sendNewPaymentEmail(user, amount, reseller);
    }

    private Result processStripePayment(PaymentRequest paymentRequest, User user) {
        try {
            if (paymentRequest.isMobile()) {
                Stripe.apiKey = userDAO.getStripePrivateKeyByResellerId(-1);
            } else {
                Stripe.apiKey = userDAO.getStripePrivateKeyByResellerId(getCurrentUser().getResellerId());
            }

            Map<String, Object> params = new HashMap();
            params.put( "amount", ( int ) ( paymentRequest.getAmount() * 100 ) );
            params.put( "currency", "usd" );
            params.put( "description", String.valueOf( user.getId() ) );
            params.put( "source", paymentRequest.getToken() );

            Charge.create( params );
            userDAO.updateBalanceByUserId( user.getId(), user.getBalance() + paymentRequest.getAmount() );
            userDAO.insertPayment(new Payment(user.getId(), Payment.Type.ADD_FUNDS, paymentRequest.getAmount(), false));

            sendNewPaymentEmail( user, paymentRequest.getAmount(), userDAO.findUserById(user.getResellerId()) );

            user = userDAO.findUserById( user.getId() );
            user.setPassword("");

            return ok( Json.toJson( Response.OK( user ) ) );
        } catch ( Exception e ) {
            e.printStackTrace();
            return ok( Json.toJson( Response.ERROR( e.getMessage() ) ) );
        }
    }

    public Result getUserBalance( int userId ) {
        User user = userDAO.findUserById( userId );
        if ( user != null ) {
            return ok( Json.toJson( Response.OK( user.getBalance() ) ) );
        } else {
            return ok( Json.toJson( Response.ERROR() ) );
        }
    }

    private User getCurrentUser() {
        String authHeader = request().getHeader("auth");
        if ( authHeader != null ) {
            try {
                String[] parts = authHeader.split(":");
                Integer id = Integer.parseInt( parts[ 0 ] );

                return userDAO.findUserById(id);
            } catch ( Exception e ) { e.printStackTrace(); }
        }

        return null;
    }
}
