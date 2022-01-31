package controllers;

import com.google.inject.Inject;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import models.*;
import org.mybatis.guice.transactional.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.EmailService;
import services.db.dao.DataDAO;
import services.db.dao.ListDAO;
import services.db.dao.UserDAO;
import services.db.entity.*;
import services.db.entity.PaymentRequest;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static play.libs.Json.toJson;

public class Administration extends Controller {

    private final UserDAO userDAO;
    private final DataDAO dataDAO;
    private final ListDAO listDAO;
    private final EmailService emailService;

    private String tempDirectoryPath;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat exportDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private int[] freeForManualPaymentsResellers = {811, 134, 542};

    @Inject
    public Administration(UserDAO userDAO,
                           DataDAO dataDAO,
                           ListDAO listDAO,
                           EmailService emailService) {
        this.userDAO = userDAO;
        this.dataDAO = dataDAO;
        this.listDAO = listDAO;
        this.emailService = emailService;

        initTempDirectoryPath();
    }

    private void initTempDirectoryPath() {
        try {
            File tempFile = File.createTempFile("test", "test");
            tempDirectoryPath = tempFile.getParentFile().getAbsolutePath();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Result getResellers() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        List<User> resellers = userDAO.findUsersWithRole(User.ROLE_RESELLER);
        return ok(Json.toJson(Response.OK(resellers)));
    }

    public Result getUserList() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        UserListRequest request = Json.fromJson(request().body().asJson(), UserListRequest.class);
        if (currentUser.getResellerNumber() > 0) {
            request.setResellerId(currentUser.getId());
        }

        int userCount = userDAO.getUserListCount(request);
        List<User> userList = userDAO.getUserList(request);

        return ok(Json.toJson(new UserListResponse(userCount, userList)));
    }

    public Result getCommentsList() {
        User currentUser = getCurrentUser();
        if (currentUser.getAdmin() == 0) {
            return forbidden();
        }

        CommentListRequest request = Json.fromJson(request().body().asJson(), CommentListRequest.class);
        int commentsCount = userDAO.getCommentListCount(request);
        List<Comment> comments = userDAO.getCommentList(request);

        return ok(Json.toJson(new CommentListResponse(commentsCount, comments)));
    }

    public Result updateComment() {
        User currentUser = getCurrentUser();
        if (currentUser.getAdmin() == 0) {
            return forbidden();
        }

        Comment comment = Json.fromJson(request().body().asJson(), Comment.class);
        userDAO.updateComment(comment);

        return ok(Json.toJson(Response.OK()));
    }

    public Result deleteComment(long commentId) {
        User currentUser = getCurrentUser();
        if (currentUser.getAdmin() == 0) {
            return forbidden();
        }

        userDAO.deleteCommentById(commentId);
        return ok(Json.toJson(Response.OK()));
    }

    public Result getPaymentRequestDetails(Integer id) {
        return ok(Json.toJson(Response.OK(userDAO.getPaymentRequestById(id))));
    }

    public Result payRequestedPayment() {
        InvoicePaymentRequest invoicePaymentRequest = Json.fromJson(request().body().asJson(), InvoicePaymentRequest.class);

        synchronized (Administration.class) {
            PaymentRequest paymentRequest = userDAO.getPaymentRequestById(invoicePaymentRequest.getInvoiceId());
            if (paymentRequest.isPaid()) {
                return ok(Json.toJson(Response.OK()));
            }

            try {
                Stripe.apiKey = userDAO.getStripePrivateKeyByResellerId(0);

                Map<String, Object> params = new HashMap();
                params.put("amount", (int) (paymentRequest.getAmount() * 100));
                params.put("currency", "usd");
                params.put("description", String.valueOf(paymentRequest.getId()));
                params.put("source", invoicePaymentRequest.getToken());

                Charge.create(params);

                userDAO.updatePaymentRequestPaidStatusById(paymentRequest.getId(), true);
                emailService.sendPaymentRequestedPaidEmail(paymentRequest);

                return ok(Json.toJson(Response.OK()));
            } catch (Exception e) {
                e.printStackTrace();
                return ok(Json.toJson(Response.ERROR(e.getMessage())));
            }
        }
    }

    public Result getPaymentRequestsList() {
        StringListRequest request = Json.fromJson(request().body().asJson(), StringListRequest.class);

        PaymentRequestListResponse paymentListResponse = new PaymentRequestListResponse(
            userDAO.getPaymentRequestListCount(request),
            userDAO.getPaymentRequestList(request)
       );

        return ok(Json.toJson(Response.OK(paymentListResponse)));
    }

    public Result addPaymentRequest() {
        PaymentRequest paymentRequest = Json.fromJson(request().body().asJson(), PaymentRequest.class);

        if (paymentRequest.getId() > 0) {
            userDAO.updatePaymentRequest(paymentRequest);
        } else {
            paymentRequest.setDate(System.currentTimeMillis());
            userDAO.savePaymentRequest(paymentRequest);
        }

        return ok(Json.toJson(Response.OK()));
    }

    public Result deletePaymentRequest(int id) {
        userDAO.removePaymentRequestById(id);
        return ok(Json.toJson(Response.OK()));
    }

    public Result getRegistrationRequestsList() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        UserListRequest request = Json.fromJson(request().body().asJson(), UserListRequest.class);
        if (currentUser.getResellerNumber() > 0) {
            request.setResellerId(currentUser.getId());
        }

        int registrationRequestCount = userDAO.getRegistrationRequestListCount(request);
        List<RegistrationRequest> registrationRequestList = userDAO.getRegistrationRequestList(request);

        return ok(Json.toJson(new RegistrationRequestListResponse(registrationRequestCount, registrationRequestList)));
    }

    public Result verifyRegistrationRequestManually(int id) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        RegistrationRequest registrationRequest = userDAO.findRegistrationRequestById(id);
        userDAO.removeRegistrationRequest(registrationRequest);

        User user = User.fromRegistrationRequest(registrationRequest);
        User reseller = userDAO.findUserById(user.getResellerId());
        if (reseller != null) {
            if (reseller.getDomains() != null && reseller.getDomains().contains("makedatalist.com ")) {
                //user.setBalance(0.0f);
            }

            if ("daniel@multimedialists.com".equals(reseller.getEmail())) {
                user.setFilterDNC(true);
            }
        }

        userDAO.saveUser(user);
        //dataDAO.blockDirectoryAndCraigslistDataSourcesForUser(user.getId());

        return ok(Json.toJson(Response.OK()));
    }

    public Result cancelRegistrationRequest(int id) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        userDAO.removeRegistrationRequestById(id);

        return ok(Json.toJson(Response.OK()));
    }

    public Result getPaymentList() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        PaymentListRequest request = Json.fromJson(request().body().asJson(), PaymentListRequest.class);
        if (currentUser.getResellerNumber() > 0) {
            request.setResellerId(currentUser.getId());
        }

        int paymentCount = userDAO.getPaymentListCount(request);
        List< Payment > paymentList = userDAO.getPaymentList(request);

        return ok(Json.toJson(new PaymentListResponse(paymentCount, paymentList)));
    }

    public Result getUserAutocomplete() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        if (isReseller()) {
            return ok(Json.toJson(userDAO.getUserAutocompleteForReseller(getCurrentUser().getId())));
        } else {
            return ok(Json.toJson(userDAO.getUserAutocomplete()));
        }
    }

    public Result getUserPaymentAutocomplete() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        if (isReseller()) {
            return ok(Json.toJson(userDAO.getUserPaymentAutocompleteForReseller(getCurrentUser().getId())));
        } else {
            return ok(Json.toJson(userDAO.getUserAutocomplete()));
        }
    }

    private boolean isReseller() {
        return User.ROLE_RESELLER == getCurrentUser().getRole();
    }

    @Transactional
    public Result addPayment() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        Payment payment = Json.fromJson(request().body().asJson(), Payment.class);
        int resellerId = getResellerId();
        User reseller = userDAO.findUserById(resellerId);

        if (payment.isAllUsers()) {
            List<User> users = userDAO.getAllUsers();
            if (isReseller()) {
                users = userDAO.getResellerUsers(getCurrentUser().getId());
            }

            float totalAmount = users.size() * payment.getAmount();
            if (resellerId > 0) {
                if (totalAmount > reseller.getBalance() && !isFreeManualPayment(resellerId)) {
                    return ok(Json.toJson(Response.ERROR("balance")));
                }
            }

            for (User user: users) {
                if (resellerId > 0 && !isFreeManualPayment(resellerId)) {
                    addResellerPayment(reseller, payment, false);
                }

                if ("system_staff".equalsIgnoreCase(user.getUsername())) {
                    continue;
                }

                addPaymentToUser(user, payment, true);
            }

            return ok(Json.toJson(Response.OK()));
        } else {
            User dbUser = userDAO.findUserByName(payment.getUsername());
            if (resellerId > 0) {
                if (payment.getAmount() > reseller.getBalance() && !isFreeManualPayment(resellerId)) {
                    return ok(Json.toJson(Response.ERROR("balance")));
                }
            }

            if (dbUser == null) {
                return ok(Json.toJson(Response.ERROR()));
            } else {
                if (resellerId > 0 && !isFreeManualPayment(resellerId)) {
                    addResellerPayment(reseller, payment, false);
                }
                addPaymentToUser(dbUser, payment, true);

                return ok(Json.toJson(Response.OK()));
            }
        }
    }

    private boolean isFreeManualPayment(int resellerId) {
        for (int id: freeForManualPaymentsResellers) {
            if (id == resellerId) {
                return true;
            }
        }

        return false;
    }

    private void addResellerPayment(User reseller, Payment payment, boolean manual) {
        reseller = userDAO.findUserById(reseller.getId());

        userDAO.updateBalanceByUserId(reseller.getId(), reseller.getBalance() - payment.getAmount());
        userDAO.insertPayment(new Payment(reseller.getId(), Payment.Type.SPEND, payment.getAmount(), manual));
    }

    private void addPaymentToUser(User user, Payment payment, boolean manual) {
        user = userDAO.findUserById(user.getId());

        userDAO.updateBalanceByUserId(user.getId(), user.getBalance() + payment.getAmount());
        userDAO.insertPayment(new Payment(user.getId(), Payment.Type.ADD_FUNDS, payment.getAmount(), manual));
    }

    private boolean checkAccess() {
        User user = getCurrentUser();
        if (user != null && user.getRole() >= User.ROLE_MANAGER) {
            return true;
        } else {
            return false;
        }
    }

    private User getCurrentUser() {
        String authHeader = request().getHeader("auth");
        if (authHeader != null) {
            try {
                String[] parts = authHeader.split(":");
                Integer id = Integer.parseInt(parts[ 0 ]);

                return userDAO.findUserById(id);
            } catch (Exception e) { e.printStackTrace(); }
        }

        return null;
    }

    public Result blockUser(Integer id) {
        if (!checkAccess()) {
            return forbidden();
        }

        userDAO.updateStatusById(id, User.Status.BLOCKED);
        return ok();
    }

    public Result unblockUser(Integer id) {
        if (!checkAccess()) {
            return forbidden();
        }

        User user = userDAO.findUserById(id);
        if (user != null) {
            if (user.getVerified() == 0) {
                userDAO.updateStatusById(id, User.Status.NOT_VERIFIED);
            } else {
                userDAO.updateStatusById(id, User.Status.ACTIVE);
            }
        }

        return ok();
    }

    public Result updateUserNote() {
        if (getCurrentUser().getRole() == User.ROLE_USER) {
            return forbidden();
        }

        UserNoteRequest request = Json.fromJson(request().body().asJson(), UserNoteRequest.class);
        userDAO.updateUserNoteAndStatus(request);

        return ok(Json.toJson(Response.OK()));
    }

    public Result getValidateRegistrationRequestsData() {
        return ok(Json.toJson(Response.OK(getCurrentUser().isValidateRegistrationRequests())));
    }

    public Result updateUser() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        User user = Json.fromJson(request().body().asJson(), User.class);
        User dbUser = userDAO.findUserByName(user.getUsername());
        if (dbUser != null && dbUser.getId() != user.getId()) {
            return ok(Json.toJson(Response.ERROR("username")));
        }

        dbUser = userDAO.findUserByPhone(user.getPhone());
        if (dbUser != null && dbUser.getId() != user.getId()) {
            return ok(Json.toJson(Response.ERROR("phone")));
        }

        dbUser = userDAO.findUserByEmail(user.getEmail());
        if (dbUser != null && dbUser.getId() != user.getId()) {
            return ok(Json.toJson(Response.ERROR("email")));
        }


        dbUser = userDAO.findUserById(user.getId());
        dbUser.setUsername(user.getUsername());
        dbUser.setPhone(user.getPhone());
        dbUser.setEmail(user.getEmail());
        dbUser.setCompanyName(user.getCompanyName());
        dbUser.setAddress(user.getAddress());
        dbUser.setRestriction(user.isRestriction());
        dbUser.setDomains(user.getDomains());
        dbUser.setRole(user.getRole());
        dbUser.setAdmin(user.getRole() == User.ROLE_ADMIN ? 1 : 0);
        dbUser.setSupportPhone(user.getSupportPhone());
        dbUser.setFilterDNC(user.isFilterDNC());
        dbUser.setFilterEmptyPhone(user.isFilterEmptyPhone());
        dbUser.setFilterEmail(user.isFilterEmail());
        dbUser.setValidateRegistrationRequests(user.isValidateRegistrationRequests());
        dbUser.setAllowManageMoney(user.isAllowManageMoney());
        dbUser.setIp(user.getIp());
        dbUser.setInvitationDomain(user.getInvitationDomain());
        dbUser.setResellerId(user.getResellerId());
        dbUser.setMultipleGeographicParametersEnabled(user.isMultipleGeographicParametersEnabled());
        dbUser.setAllowCustomersKeyword(user.isAllowCustomersKeyword());
        dbUser.setAllowBusinessKeyword(user.isAllowBusinessKeyword());
        dbUser.setAllowCarriersSearch(user.isAllowCarriersSearch());
        dbUser.setAllowMatchResponder(user.isAllowMatchResponder());
        dbUser.setAllowMatchCraigslist(user.isAllowMatchCraigslist());
        dbUser.setAllowMatchConsumers(user.isAllowMatchConsumers());
        dbUser.setAllowMatchOptIn(user.isAllowMatchOptIn());
        dbUser.setAllowMatchDirectory(user.isAllowMatchDirectory());
        dbUser.setAllowMatchBusinessDetailed(user.isAllowMatchBusinessDetailed());
        dbUser.setAllowMatchingLists(user.isAllowMatchingLists());
        dbUser.setAllowDetailedBusinessKeywords(user.isAllowDetailedBusinessKeywords());
        dbUser.setAllowPayments(user.isAllowPayments());
        dbUser.setAllowDataSourceItemsPrices(user.isAllowDataSourceItemsPrices());
        dbUser.setAllowTransferToSuppression(user.isAllowTransferToSuppression());
        dbUser.setAllowBusinessEmailFilter(user.isAllowBusinessEmailFilter());
        dbUser.setListAdditionalCodeEnabled(user.isListAdditionalCodeEnabled());
        dbUser.setNotificationEmail(user.getNotificationEmail());
        dbUser.setAllowedMatchesList(user.getAllowedMatchesList());
        if (user.getRole() == User.ROLE_RESELLER && user.getResellerNumber() == 0) {
            int resellerNumber = userDAO.findMaxResellerNumber();
            dbUser.setResellerNumber(resellerNumber + 1);
        }

        if (user.getRole() != User.ROLE_RESELLER && user.getResellerNumber() > 0) {
            dbUser.setResellerNumber(0);

            userDAO.updateUsersForRemovedReseller(user.getId());
        }

        if (user.getRole() != User.ROLE_USER) {
            dbUser.setResellerId(0);
        }

        userDAO.updateUser(dbUser);

        if (user.getNewPassword() != null && user.getNewPassword().length() > 0 &&
                getCurrentUser().getAdmin() > 0) {
            userDAO.updateUserPassword(user.getId(), user.getNewPassword());
        }

        return ok(Json.toJson(Response.OK()));
    }

    @Transactional
    public Result deleteUser(Integer id) {
        if (!checkAccess()) {
            return forbidden();
        }

        User user = userDAO.findUserById(id);
        if (user != null) {
            userDAO.deleteUserById(id);

            if (user.getRole() == User.ROLE_RESELLER) {
                userDAO.updateUsersForRemovedReseller(user.getId());
            }
        }

        return ok();
    }

    public Result loginAsUser() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_USER) {
            return forbidden();
        }

        User user = Json.fromJson(request().body().asJson(), User.class);
        User dbUser = userDAO.findUserById(user.getId());
        if (dbUser != null) {
            dbUser.setPassword(null);
            dbUser.setOriginalRole(currentUser.getRole());
            return ok(toJson(Response.OK(dbUser)));
        } else {
            return forbidden();
        }
    }

    public Result exportUsers() throws Exception {
        if (!checkAccess()) {
            return forbidden();
        }

        UserListRequest request = Json.fromJson(request().body().asJson(), UserListRequest.class);
        request.setLimit(-1);
        List< User > userList = userDAO.getUserList(request);

        File file = generateUsersFile(userList);
        return ok(Json.toJson(Response.OK(file.getName())));
    }

    public Result exportLists() throws Exception {
        if (!checkAccess()) {
            return forbidden();
        }

        PagedListsRequest request = Json.fromJson(request().body().asJson(), PagedListsRequest.class);
        request.setLimit(-1);
        request.setUserId(-1);

        List<ListEntity> lists = listDAO.getPurchasedLists(request);

        File file = generateListsFile(lists);
        return ok(Json.toJson(Response.OK(file.getName())));
    }

    public Result exportRegistrationRequests() throws Exception {
        if (!checkAccess()) {
            return forbidden();
        }

        UserListRequest request = Json.fromJson(request().body().asJson(), UserListRequest.class);
        request.setLimit(-1);
        List<RegistrationRequest> registrationRequestList = userDAO.getRegistrationRequestList(request);

        File file = generateRegistrationRequestsFile(registrationRequestList);
        return ok(Json.toJson(Response.OK(file.getName())));
    }

    public Result getPricesList() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        List<Price> results;
        if (isReseller()) {
            results = dataDAO.getAllPricesForReseller(getCurrentUser().getId());
        } else {
            results = dataDAO.getAllPrices();
        }

        return ok(Json.toJson(Response.OK(results)));
    }

    public Result sendEmail() {
        User currentUser = getCurrentUser();
        if (currentUser == null || currentUser.getAdmin() == 0) {
            return forbidden();
        }

        SendEmailRequest request = Json.fromJson(request().body().asJson(), SendEmailRequest.class);

        if (request.getEmails().size() > 0) {
            for (String email: request.getEmails()) {
                if (email != null && email.length() > 0) {
                    emailService.sendBulkEmail(email, request);
                }
            }
        } else {
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                if (user.getEmail() != null && user.getEmail().length() > 0) {
                    emailService.sendBulkEmail(user.getEmail(), request);
                }
            }
        }

        return ok(Json.toJson(Response.OK()));
    }

    public Result getPricesListForUser(int userId) {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        List<Price> results;
        if (isReseller()) {
            results = dataDAO.getAllPricesForReseller(getCurrentUser().getId());
        } else {
            results = dataDAO.getAllPrices();
        }

        List<Price> userPrices = dataDAO.getAllPricesForUser(userId);
        for (Price resultPrice: results) {
            for (Price userPrice: userPrices) {
                if (resultPrice.getDataSource() == userPrice.getDataSource() &&
                    resultPrice.getType() == userPrice.getType()) {
                    resultPrice.setPrice(userPrice.getPrice());
                    resultPrice.setId(userPrice.getId());
                }
            }
        }

        return ok(Json.toJson(Response.OK(results)));
    }

    public Result updatePrice() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        Price price = Json.fromJson(request().body().asJson(), Price.class);

        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_RESELLER) {
            Price resellerPrice = dataDAO.getPriceByTypeAndUserId(
                    price.getType(), price.getDataSource(), currentUser.getId());
            if (resellerPrice == null) {
                resellerPrice = dataDAO.getPriceByTypeAndUserId(
                        price.getType(), price.getDataSource(), 0);
            }

            if (resellerPrice.getPrice() > price.getPrice()) {
                return ok(Json.toJson(Response.ERROR("lower")));
            }
        }

        if (price.getUserId() == 0) {
            dataDAO.updatePrice(price);
        } else {
            dataDAO.updateUserPrice(price);
        }

        return ok(Json.toJson(Response.OK()));
    }

    public Result dataSources() {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == User.ROLE_ADMIN) {
            return ok(Json.toJson(Response.OK(filterDataSources(
                    dataDAO.findAllDataSources()))));
        } else if (currentUser.getRole() == User.ROLE_RESELLER) {
            return ok(Json.toJson(Response.OK(filterDataSources(dataDAO.findAllDataSourcesForUser(currentUser)))));
        }

        return forbidden();
    }

    static public List<DataSource> filterDataSources(List<DataSource> dataSources) {
        Iterator<DataSource> it = dataSources.iterator();
        while (it.hasNext()) {
            DataSource dataSource = it.next();
            String dataSourceName = dataSource.getName();

            if ("consumers".equalsIgnoreCase(dataSourceName) ||
                "AUTO".equalsIgnoreCase(dataSourceName) ||
                "linked in".equalsIgnoreCase(dataSourceName) ||
                "opt in".equalsIgnoreCase(dataSourceName) ||
                "opt-in".equalsIgnoreCase(dataSourceName) ||
                "blacklist".equalsIgnoreCase(dataSourceName) ||
                "student".equalsIgnoreCase(dataSourceName) ||
                "instagram".equalsIgnoreCase(dataSourceName) ||
                //"business".equalsIgnoreCase(dataSourceName) ||
                "search engine".equalsIgnoreCase(dataSourceName)) {
                it.remove();
            }
        }

        dataSources.sort(Comparator.comparing(DataSource::getTitle));
        return dataSources;
    }

    public Result updateDataSourceVisibility(Integer id, Boolean visible) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == User.ROLE_ADMIN) {
            dataDAO.updateDataSourceVisibility(id, visible);
            return ok(Json.toJson(Response.OK()));
        } else if (currentUser.getRole() == User.ROLE_RESELLER) {
            dataDAO.updateResellerDataSourceVisibility(currentUser.getId(), id, visible);
            return ok(Json.toJson(Response.OK()));
        }

        return forbidden();
    }

    public Result updateDataSourceBlockedState() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.ROLE_ADMIN && currentUser.getRole() != User.ROLE_RESELLER) {
            return forbidden();
        }

        UpdateDataSourceBlockedUsersRequest request = Json.fromJson(
                request().body().asJson(), UpdateDataSourceBlockedUsersRequest.class);

        dataDAO.updateDataSourceBlockedState(request.getId(), request.getUserIds());

        return ok(Json.toJson(Response.OK()));
    }

    public Result updateDataSourceBlockedUsers() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.ROLE_ADMIN && currentUser.getRole() != User.ROLE_RESELLER) {
            return forbidden();
        }

        UpdateDataSourceBlockedUsersRequest request = Json.fromJson(
                request().body().asJson(), UpdateDataSourceBlockedUsersRequest.class);
        dataDAO.updateDataSourceBlockedUsers(request.getId(), request.getUserIds());

        return ok(Json.toJson(Response.OK()));
    }

    public Result getBlockedUsersForDataSource(Integer id,Boolean state) {
        User user = getCurrentUser();
        if (user.getRole() == User.ROLE_ADMIN || user.getRole() == User.ROLE_RESELLER) {
            return ok(Json.toJson(Response.OK(dataDAO.getBlockedUsersForDataSource(id,state))));
        }

        return forbidden();
    }

    private File generateListsFile(List<ListEntity> lists) throws Exception {
        File file = File.createTempFile("lists", "csv");
        PrintWriter writer = new PrintWriter(file);

        String[] headers = new String[] { "list name", "username", "user email", "user phone", "reseller", "table name", "count", "date" };
        for (String header: headers) {
            writer.append(header);
            if (!headers[headers.length - 1].equals(header)) {
                writer.append(",");
            }
        }
        writer.append("\n");
        for (ListEntity list: lists) {
            writer.append(list.getName().replace(",", ""));
            writer.append(",");
            writer.append(list.getUsername().replace(",", ""));
            writer.append(",");
            writer.append(list.getUserEmail());
            writer.append(",");
            writer.append(list.getUserPhone());
            writer.append(",");
            writer.append(list.getReseller() == null ? "none" : list.getReseller());
            writer.append(",");
            writer.append(list.getTableName().replace(",", ""));
            writer.append(",");
            writer.append(new Long(list.getCnt()).toString());
            writer.append(",");
            writer.append(formatDate(list.getDate()));

            writer.append("\n");
        }

        writer.flush();
        writer.close();

        return file;
    }

    private File generateUsersFile(List< User > userList) throws Exception {
        File file = File.createTempFile("users", "csv");
        PrintWriter writer = new PrintWriter(file);

        String[] headers = new String[] { "username", "email", "phone", "reseller", "company name", "address/state", "balance", "status", "creation date", "last activity date" };
        for (String header: headers) {
            writer.append(header);
            if (!headers[headers.length - 1].equals(header)) {
                writer.append(",");
            }
        }
        writer.append("\n");
        for (User user: userList) {
            writer.append(user.getUsername().replace(",", ""));
            writer.append(",");
            writer.append(user.getEmail());
            writer.append(",");
            writer.append(user.getPhone());
            writer.append(",");
            writer.append(user.getReseller() == null ? "none" : user.getReseller());
            writer.append(",");
            writer.append(user.getCompanyName().replace(",", ""));
            writer.append(",");
            writer.append(user.getAddress().replace(",", ""));
            writer.append(",");
            writer.append(String.format("%.2f", user.getBalance()));
            writer.append(",");
            writer.append(getStatusAsString(user.getNoteStatus()));
            writer.append(",");
            writer.append(formatDate(user.getDate()));
            writer.append(",");
            writer.append(formatDate(user.getLastActivityDate()));

            writer.append("\n");
        }

        writer.flush();
        writer.close();

        return file;
    }

    private String formatDate(long date) {
        if (date > 0) {
            return dateFormat.format(new Date(date));
        } else {
            return "unknown";
        }
    }

    private String getStatusAsString(String status) {
        switch (status) {
            case "0":
                return "registered";
            case "1":
                return "demoed";
            case "2":
                return "sampled";
            case "3":
                return "purchased";
            case "4":
                return "buyer";
            case "5":
                return "not interested";
            case "6":
                return "wrong number";
            case "7":
                return "Phils account";

        }

        return null;
    }

    private File generateRegistrationRequestsFile(List<RegistrationRequest> registrationRequestList) throws Exception {
        File file = File.createTempFile("registration_requests", "csv");
        PrintWriter writer = new PrintWriter(file);

        String[] headers = new String[] { "username", "email", "phone", "company name", "address", "date" };
        for (String header: headers) {
            writer.append(header);
            if (!headers[headers.length - 1].equals(header)) {
                writer.append(",");
            }
        }
        writer.append("\n");

        for (RegistrationRequest registrationRequest: registrationRequestList) {
            writer.append(registrationRequest.getUsername().replace(",", ""));
            writer.append(",");
            writer.append(registrationRequest.getEmail());
            writer.append(",");
            writer.append(registrationRequest.getPhone());
            writer.append(",");
            writer.append(registrationRequest.getCompanyName().replace(",", ""));
            writer.append(",");
            writer.append(registrationRequest.getAddress().replace(",", ""));
            writer.append(",");
            writer.append(formatDate(registrationRequest.getDate()));

            writer.append("\n");
        }

        writer.flush();
        writer.close();

        return file;
    }

    public Result downloadFile(String filename) throws Exception {
        File file = new File(tempDirectoryPath, filename);
        String fileName = "users (" + dateFormat.format(new Date()) + ").csv";
        if (file.getName().contains("requests")) {
            fileName = "registration requests (" + dateFormat.format(new Date()) + ").csv";
        } else if (file.getName().contains("lists")) {
            fileName = "purchased lists (" + dateFormat.format(new Date()) + ").csv";
        }

        response().setHeader("Content-disposition","attachment; filename=" + fileName);
        return ok(new FileInputStream(file)).as("text/csv");
    }

    public Result getSettingsValue(String key) {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        String value = dataDAO.getSettingsValueByKey(key, getResellerId());
        if (value != null && value.length() > 0) {
            return ok(Json.toJson(Response.OK(Float.parseFloat(value))));
        } else {
            return ok(Json.toJson(Response.ERROR()));
        }
    }

    private int getResellerId() {
        User user = getCurrentUser();

        int resellerId = 0;
        if (isReseller()) {
            resellerId = user.getId();
        } else if (user.getResellerId() != 0) {
            resellerId = user.getResellerId();
        }

        return resellerId;
    }

    public Result getSettingsValues(String key) {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        List<Float> values = new LinkedList();
        List<String> strValues = dataDAO.getSettingsValuesByKey(key + "%", getResellerId());
        for (String value: strValues) {
            values.add(Float.parseFloat(value));
        }

        return ok(Json.toJson(Response.OK(values)));
    }

    public Result updateSettings() {
        if (!checkAccess() && !isReseller()) {
            return forbidden();
        }

        int resellerId = getResellerId();

        Setting setting = Json.fromJson(request().body().asJson(), Setting.class);
        Setting dbSetting = dataDAO.getSettingByKey(setting.getKey(), resellerId);

        if (dbSetting == null) {
            dataDAO.insertSetting(setting, resellerId);
        } else {
            dataDAO.updateSettingValueByKey(setting.getKey(), setting.getValue(), resellerId);
        }

        return ok(Json.toJson(Response.OK()));
    }

}
