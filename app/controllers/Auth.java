package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.*;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.EmailService;
import services.db.dao.DataDAO;
import services.db.dao.UserDAO;
import services.db.entity.RegistrationRequest;
import services.db.entity.User;

import java.util.*;

import static play.libs.Json.toJson;

@Singleton
public class Auth extends Controller {

    private final EmailService emailService;
    private final UserDAO userDAO;
    private final DataDAO dataDAO;

    private final String registrationNameError = "registration.error.name";
    private final String registrationEmailError = "registration.error.email";
    private final String registrationPhoneError = "registration.error.phone";
    private final String registrationIpError = "registration.error.ip";

    private final Map<String, Integer> tokens = new LinkedHashMap() {

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 100;
        }

    };

    @Inject
    public Auth(EmailService emailService, UserDAO userDAO, DataDAO dataDAO) {
        this.emailService = emailService;
        this.userDAO = userDAO;
        this.dataDAO = dataDAO;
    }

    public Result login() {
        if (userDAO.checkIpBlocked(request().remoteAddress())) {
            return ok(toJson(Response.ERROR()));
        }

        LoginRequest request = Json.fromJson( request().body().asJson(), LoginRequest.class );

        User user = userDAO.findUserByNameOrEmailAndPassword( request.getName(), request.getPassword() );
        if ( user != null && !User.Status.BLOCKED.equals( user.getStatus() ) ) {
            String token = UUID.randomUUID().toString();
            user.setPassword(token);

            tokens.put(token, user.getId());
            return ok( toJson( Response.OK( user ) ) );
        }

        return ok( toJson( Response.ERROR() ) );
    }

    public Result loginWithToken(String token) {
        Integer userId = tokens.get(token);
        if (userId != null) {
            tokens.remove(token);

            User user = userDAO.findUserById(userId);
            if (user != null) {
                user.setPassword(null);
                return ok( toJson( Response.OK( user ) ) );
            }
        }

        return ok( toJson( Response.ERROR() ) );
    }

    public Result logout() {
        return ok();
    }

    public Result validateRegistration() {
        RegistrationRequest request = Json.fromJson( request().body().asJson(), RegistrationRequest.class );

        User user = userDAO.findUserByName( request.getUsername() );
        if ( user != null ) {
            return ok( toJson( Response.ERROR( registrationNameError ) ) );
        }

        user = userDAO.findUserByEmail( request.getEmail() );
        if ( user != null ) {
            return ok( toJson( Response.ERROR( registrationEmailError ) ) );
        }

        user = userDAO.findUserByPhone( request.getPhone() );
        if ( user != null ) {
            return ok( toJson( Response.ERROR( registrationPhoneError ) ) );
        }

        if (userDAO.checkIpBlocked(request().remoteAddress())) {
            return ok( toJson( Response.ERROR( registrationIpError ) ) );
        }

        /*user = userDAO.findUserByIp( request().remoteAddress() );
        if (user != null && !PHIL_IP_ADDRESS.equals(request().remoteAddress())) {
            return ok( toJson( Response.ERROR( registrationIpError ) ) );
        }*/

        return ok( toJson( Response.OK() ) );
    }

    public Result register() {
        if (userDAO.checkIpBlocked(request().remoteAddress())) {
            return ok(toJson(Response.ERROR()));
        }

        RegistrationRequest request = Json.fromJson(request().body().asJson(), RegistrationRequest.class);
        if (request.getDetails() != null) {
            userDAO.insertRegistrationDetails(request.getEmail(), request.getDetails());
        }

        request.setUuid(UUID.randomUUID().toString());
        request.setIp(request().remoteAddress());
        request.setDate(System.currentTimeMillis());

        User reseller;
        if (request.getResellerNumber() > 0) {
            int resellerId = userDAO.findResellerIdByNumber(request.getResellerNumber());
            request.setResellerId(resellerId);

            reseller = userDAO.findUserById(resellerId);
        } else {
            reseller = getResellerIdByHostName();
            if (reseller != null) {
                request.setResellerId(reseller.getId());
            }
        }

        userDAO.saveRegistrationRequest(request);

        emailService.sendRegistrationEmail(request, reseller);
        return ok(toJson(Response.OK()));
    }

    public Result registerMobile() {
        RegistrationRequest request = Json.fromJson(request().body().asJson(), RegistrationRequest.class);
        request.setUuid(UUID.randomUUID().toString());
        request.setIp(request().remoteAddress());
        request.setDate(System.currentTimeMillis());

        User user = User.fromRegistrationRequest(request);
        user.setBalance(0);
        userDAO.saveUser( user );

        return ok( toJson( Response.OK() ) );
    }

    private User getResellerIdByHostName() {
        String requestHost = request().getHeader("Origin");
        Logger.info("Registration ORIGIN: " + requestHost);
        if (requestHost != null) {
            requestHost = requestHost.
                    replace("http://", "").
                    replace("https://", "").
                    replace("www.", "");
            List<User> users = userDAO.findResellersByHost(requestHost);
            for (User user : users) {
                String domains = user.getDomains();
                if (domains != null && domains.length() > 0) {
                    String[] domainArray = domains.split(",");
                    for (String domain : domainArray) {
                        if (requestHost.equalsIgnoreCase(domain.trim())) {
                            User reseller = userDAO.findUserById(user.getId());
                            reseller.setDomains(domain.trim());

                            return reseller;
                        }
                    }
                }
            }
        }

        return null;
    }

    public Result checkIfRestricted(Integer userId){
        User user = userDAO.findUserById(userId);
        boolean restriction = user.isRestriction();
        return ok(toJson(Response.OK(user.getRole() < User.ROLE_MANAGER && restriction)));
    }

    public Result recoverPassword( String email ) {
        String token = UUID.randomUUID().toString();
        userDAO.updateTokenByEmail(email, token);

        User reseller = getResellerIdByHostName();
        emailService.sendRecoveryPasswordEmail(email, token, reseller);

        return ok();
    }

    public Result checkPasswordRecoveryToken( String token ) {
        User user = userDAO.findUserByToken( token );
        if ( user == null ) {
            return ok( Json.toJson( Response.ERROR() ) );
        } else {
            return ok( Json.toJson( Response.OK() ) );
        }
    }

    public Result changePassword() {
        ChangePasswordRequest request = Json.fromJson( request().body().asJson(), ChangePasswordRequest.class );
        if ( request.getToken() != null && request.getToken().length() > 0 ) {
            User user = userDAO.findUserByToken( request.getToken() );
            if ( user != null ) {
                userDAO.updatePasswordByToken( request.getToken(), request.getPassword() );
                return ok( Json.toJson( Response.OK( user.getUsername() ) ) );
            }
        }

        return ok( Json.toJson( Response.ERROR() ) );
    }

    public Result completeRegistration() {
        RegistrationCompleteRequest request = Json.fromJson( request().body().asJson(), RegistrationCompleteRequest.class );
        RegistrationRequest registrationRequest = userDAO.findRegistrationRequestByUUID(request.getToken());
        if ( registrationRequest == null ) {
            return ok( toJson( Response.ERROR() ) );
        } else {
            userDAO.removeRegistrationRequest( registrationRequest );

            User user = User.fromRegistrationRequest( registrationRequest );
            User reseller = userDAO.findUserById(user.getResellerId());
            if (reseller != null) {
                if (reseller.getDomains() != null && reseller.getDomains().contains("makedatalist.com")) {
                    //user.setBalance(0.0f);
                }

                if ("daniel@multimedialists.com".equalsIgnoreCase(reseller.getEmail())) {
                    user.setFilterDNC(true);
                }

                if ("mike@exclusiveliveleads.com".equalsIgnoreCase(reseller.getEmail())) {
                    user.setAllowPayments(true);
                }
            }

            userDAO.saveUser( user );
            emailService.sendRegistrationCompletedEmail(user, reseller);
            //dataDAO.blockDirectoryAndCraigslistDataSourcesForUser(user.getId());

            return ok( toJson( Response.OK() ) );
        }
    }
}
