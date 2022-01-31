package controllers;

import com.google.common.io.Files;
import com.google.inject.Inject;
import heplers.StaticConstants;
import models.*;
import org.mybatis.guice.transactional.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.EmailService;
import services.db.dao.DataDAO;
import services.db.dao.ListDAO;
import services.db.dao.UserDAO;
import services.db.entity.*;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Lists extends Controller {

    private final ListDAO listDAO;
    private final UserDAO userDAO;
    private final DataDAO dataDAO;

    private static final String NEW_LINE = "\n";
    private static final String SEPARATOR = ",";

    private static final Integer MINUS_ONE = -1;
    private static final Integer ZERO = 0;

    private final int MAX_LIST_SIZE = 1000000;
    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private static final Map< Integer, DownloadListRequest > registeredDownloadRequestsMap = new ConcurrentHashMap();
    private EmailService emailService = new EmailService();

    private int[] resellersShouldPayForUsers = {811};

    @Inject
    public Lists(ListDAO listDAO,
                 UserDAO userDAO,
                 DataDAO dataDAO) {
        this.listDAO = listDAO;
        this.userDAO = userDAO;
        this.dataDAO = dataDAO;
    }

    public Result getPurchasedListsByUserId(int userId, int dataType) {
        return ok(Json.toJson(Response.OK(listDAO.getPurchasedListsByUserId(userId, dataType))));
    }

    public Result getPurchasedListsByUsername() {
        if (request().remoteAddress().equals("67.205.171.63")) {
            CommonRequest request = Json.fromJson(request().body().asJson(), CommonRequest.class);
            User user = userDAO.findUserByName(request.getUsername());

            if (user != null) {
                return ok(Json.toJson(Response.OK(listDAO.getPurchasedListsByUserId(user.getId(), -1))));
            } else {
                return ok(Json.toJson(Response.OK(new LinkedList())));
            }
        } else {
            return forbidden();
        }
    }

    public Result getNonPurchasedListsByUserId(int userId) {
        return ok(Json.toJson(Response.OK(listDAO.getNonPurchasedListsByUserId(userId))));
    }

    public Result getPagedPurchasedLists() {
        PagedListsRequest request = Json.fromJson(request().body().asJson(), PagedListsRequest.class);
        return ok(Json.toJson(Response.OK(
                new PagedListResponse(
                        listDAO.getPurchasedListsCount(request),
                        listDAO.getPurchasedLists(request)))));
    }

    public Result getPagedAllPurchasedLists() {
        PagedListsRequest request = Json.fromJson(request().body().asJson(), PagedListsRequest.class);

        request.setUserId(-1);
        return ok(Json.toJson(Response.OK(
                new PagedListResponse(
                        listDAO.getPurchasedListsCount(request),
                        listDAO.getPurchasedLists(request)))));
    }

    public Result getPagedNonPurchasedLists() {
        PagedListsRequest request = Json.fromJson(request().body().asJson(), PagedListsRequest.class);
        return ok(Json.toJson(Response.OK(
                new PagedListResponse(
                        listDAO.getNonPurchasedListsCount(request),
                        listDAO.getNonPurchasedLists(request)))));
    }

    public Result allMatchLists() {
        return ok(Json.toJson(Response.OK(listDAO.getMatchTables())));
    }

    public Result getPurchasedListsLogs() {
        PagedListsRequest request = Json.fromJson(request().body().asJson(), PagedListsRequest.class);

        return ok(Json.toJson(Response.OK(
                new PagedListResponse(
                        listDAO.getPagedPurchaseListsLogsCount(request),
                        listDAO.getPagedPurchasedListsLogs(request)))));
    }
    public Result getEmailListSent() {
        PagedListsRequest request = Json.fromJson(request().body().asJson(), PagedListsRequest.class);

        return ok(Json.toJson(Response.OK(
                new PagedListResponse(
                        listDAO.getPagedSentListsCount(request),
                        listDAO.getPagedSentListsLogs(request)))));
    }
    public Result saveList() {
        ListEntity list = Json.fromJson(request().body().asJson(), ListEntity.class);
        listDAO.saveList(list);
        userDAO.updateLastActivityDateByUserId(list.getUserId(), System.currentTimeMillis());

        return ok(Json.toJson(Response.OK())) ;
    }

    public Result transferToSuppression(Integer id) {
        ListEntity list = listDAO.findListById(id);
        if (list != null) {
            list.setDate(System.currentTimeMillis());
            listDAO.insertUploadedList(list);
            listDAO.copyPhonesToUploadedList(id, list.getId(), list.getTableName());

            return ok(Json.toJson(Response.OK()));
        } else {
            return ok(Json.toJson(Response.ERROR()));
        }
    }

    public Result saveAndBuyListFromApplication() {
        if (request().remoteAddress().equals("67.205.171.63")) {
            ListEntity list = Json.fromJson(request().body().asJson(), ListEntity.class);

            User user = userDAO.findUserByName(list.getUsername());
            if (user == null) {
                user = createApplicationUser(list.getUsername());
            }

            List<ListEntity> lists = listDAO.getPurchasedListsByUserId(user.getId(), -1);
            list.setName("List " + (lists.size() + 1));
            list.setUserId(user.getId());

            listDAO.saveList(list);
            userDAO.updateLastActivityDateByUserId(list.getUserId(), System.currentTimeMillis());

            BuyListRequest buyListRequest = new BuyListRequest();
            buyListRequest.setListId(list.getId());
            buyListRequest.setTotal(list.getPurchasedTotal());
            buyListRequest.setUserId(user.getId());

            if (user != null && list != null) {
                listDAO.updateCountById(list.getId(), list.getCnt() - list.getPurchasedTotal());

                ListEntity purchasedList = list.copy();
                purchasedList.setCnt(list.getPurchasedTotal());
                purchasedList.setPcnt(0);

                listDAO.saveListWithoutItems(purchasedList);
                buyListRequest.setNewListId(purchasedList.getId());
                buyListRequest.setTotal(list.getPurchasedTotal());

                listDAO.updateDateByListId(System.currentTimeMillis(), purchasedList.getId());
                listDAO.updatePurchasedCountById(purchasedList.getId(), list.getPurchasedTotal());

                listDAO.insertBoughtListItems(buyListRequest);
                listDAO.deleteBoughtListItems(buyListRequest);

                listDAO.updateStatusById(purchasedList.getId(), 2);
                listDAO.deleteListById(list.getId());

                emailService.sendStartCampaignEmail(user, purchasedList, list.getMessage());

                return ok(Json.toJson(Response.OK()));
            }

            return ok(Json.toJson(Response.ERROR()));
        } else {
            return forbidden();
        }
    }

    private User createApplicationUser(String username) {
        User user = new User();

        user.setUsername(username);
        user.setEmail(username + "@reachowners.app");
        user.setPhone(username);
        user.setCompanyName("ReachOwners");
        user.setAddress("");
        user.setPromoCode("");
        user.setPassword("");
        user.setVerified(0);
        user.setIp("");
        user.setBalance(0.0f);
        user.setDate(System.currentTimeMillis());
        user.setStatus(User.Status.ACTIVE);
        user.setRestriction(true);
        user.setResellerId(0);
        user.setRole(User.ROLE_USER);

        userDAO.saveUser(user);

        return user;
    }


    public Result saveAndBuyList() {
        ListEntity list = Json.fromJson(request().body().asJson(), ListEntity.class);

        if (!list.getName().startsWith("APP_")) {
            list.setName("APP_" + list.getName());
        }

        listDAO.saveList(list);
        userDAO.updateLastActivityDateByUserId(list.getUserId(), System.currentTimeMillis());

        BuyListRequest buyListRequest = new BuyListRequest();
        buyListRequest.setListId(list.getId());
        buyListRequest.setTotal(list.getTotal());
        buyListRequest.setUserId(list.getUserId());

        return buyListWithRequest(buyListRequest);
    }

    public Result butOptInLists() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        while (true) {
            ListEntity list = listDAO.findListByName("Opt In - Consumers matched list");
            if (list != null) {
                //   Logger.info("Buying list records: {}", list.getCnt());

                BuyListRequest buyListRequest = new BuyListRequest();
                buyListRequest.setListId(list.getId());
                buyListRequest.setTotal(1000000l);
                buyListRequest.setUserId(list.getUserId());

                buyListWithRequest(buyListRequest);
            } else {
                break;
            }

        }

        return ok();
    }

    public Result getTableItemPrice(String tableName, Integer userId) {
        DataTable table = dataDAO.getTableByName(tableName);

        Price price = dataDAO.getPriceByTypeAndUserId(table.getPhoneType(), table.getType(), userId);
        if (price != null) {
            return ok(Json.toJson(Response.OK(price.getPrice())));
        }

        price = dataDAO.getPriceByType(table.getPhoneType(), table.getType(), 0);
        if (price != null) {
            return ok(Json.toJson(Response.OK(price.getPrice())));
        }

        return ok(Json.toJson(Response.OK(0.001f)));
    }

    public Result updateList() {
        ListEntity list = Json.fromJson(request().body().asJson(), ListEntity.class);
        listDAO.updateList(list);

        return ok(Json.toJson(Response.OK())) ;
    }

    public Result deleteList(int id) {
        listDAO.deleteListById(id);
        return ok(Json.toJson(Response.OK()));
    }

    public Result getListItemPrice(int listId) {
        ListEntity list = listDAO.findListById(listId);
        int resellerId = getResellerId(list.getUserId());

        return ok(Json.toJson(Response.OK(calculateListItemPrice(listId, list.getUserId(), resellerId))));
    }

    private float calculateListItemPrice(int listId, int userId, int resellerId) {
        ListEntity list = listDAO.findListById(listId);
        String tableName = list.getTableName();
        DataTable table = dataDAO.getTableByName(tableName);

        if (listDAO.isListMatched(listId)) {
            try { return Float.parseFloat(
                    dataDAO.getSettingByKey(
                            "matched_price_" + table.getType(),
                            resellerId).getValue()); }
            catch (Exception e) {e.printStackTrace();}
        }

        Price price = dataDAO.getPriceByTypeAndUserId(table.getPhoneType(), table.getType(), userId);
        if (price != null) {
            return price.getPrice();
        }

        price = dataDAO.getPriceByType(table.getPhoneType(), table.getType(), resellerId);
        if (price != null) {
            return price.getPrice();
        }

        return 0.001f;
    }

    private int getResellerId(int userId) {
        User user = userDAO.findUserById(userId);
        return user.getResellerId();

        /*int resellerId = 0;
        if (user.getRole() == User.ROLE_RESELLER) {
            resellerId = user.getId();
        } else if (user.getResellerId() != 0) {
            resellerId = user.getResellerId();
        }

        return resellerId;*/
    }

    @Transactional
    public Result buyList() {
        BuyListRequest request = Json.fromJson(request().body().asJson(), BuyListRequest.class);
        return buyListWithRequest(request);
    }

    public Result buyListWithRequest(BuyListRequest request) {
        ListEntity list = listDAO.findListById(request.getListId());
        int resellerId = getResellerId(list.getUserId());

        float itemPrice = calculateListItemPrice(request.getListId(), list.getUserId(), resellerId);
        float resellerItemPrice = calculateListItemPrice(request.getListId(), resellerId, 0);

        float totalPrice = request.getTotal() * itemPrice;
        float resellerTotalPrice = request.getTotal() * resellerItemPrice;

        User user = userDAO.findUserById(request.getUserId());
        if (resellerId > 0) {
            User reseller = userDAO.findUserById(resellerId);
            String resellerStripeKey = userDAO.getStripePublicKeyByResellerIdOnly(resellerId);

            if ((resellerShouldPayForUser(reseller != null ? reseller.getId() : 0) &&
                    reseller.getBalance() < resellerTotalPrice ) ||
                    (resellerStripeKey != null && resellerStripeKey.length() > 0 &&
                            reseller != null && reseller.getBalance() < resellerTotalPrice)) {
                return ok(Json.toJson(Response.ERROR("reseller balance")));
            }
        }

        if (user != null && list != null && user.getBalance() >= totalPrice) {
            listDAO.updateCountById(request.getListId(), list.getCnt() - request.getTotal());

            List<ListEntity> lists = listDAO.findListWithNameTemplateAndTableName(list.getName() + "-part%", list.getTableName());
            list = list.copy();
            if (list.getCnt() != request.getTotal() || lists.size() > 0) {
                int partIndex = lists.size() + 1;
                list.setName(String.format("%s-part%d", list.getName(), partIndex));
            }

            list.setCnt(request.getTotal());
            list.setPcnt(0);

            listDAO.saveListWithoutItems(list);
            try{
                listDAO.savePurchaseList(list);
            }catch (Exception e){
                e.printStackTrace();
            }

            request.setNewListId(list.getId());

            listDAO.updateDateByListId(System.currentTimeMillis(), request.getNewListId());
            listDAO.updatePurchasedCountById(request.getNewListId(), list.getPcnt() + request.getTotal());

            listDAO.insertBoughtListItems(request);
            listDAO.deleteBoughtListItems(request);

            ListEntity baseList = listDAO.findListById(request.getListId());
            if (baseList.getCnt() == 0) {
                listDAO.deleteListById(baseList.getId());
            }

            if (list.getCnt() - request.getTotal() == 0) {
                listDAO.updateStatusById(request.getNewListId(), 2);
            } else {
                listDAO.updateStatusById(request.getNewListId(), 1);
            }

            if (resellerId > 0) {
                User reseller = userDAO.findUserById(resellerId);
                if (reseller != null) {
                    String resellerStripeKey = userDAO.getStripePublicKeyByResellerIdOnly(resellerId);

                    if (resellerShouldPayForUser(resellerId) ||
                            resellerStripeKey != null && resellerStripeKey.length() > 0) {
                        userDAO.updateBalanceByUserId(resellerId, reseller.getBalance() - resellerItemPrice * request.getTotal());
                        userDAO.insertPayment(new Payment(resellerId, Payment.Type.SPEND, resellerItemPrice * request.getTotal(), false));
                    }

                    if (reseller.getNotificationEmail() != null && reseller.getNotificationEmail().length() > 0) {
                        emailService.sendListPurchasedEmail(reseller, user, list, request.getTotal(), itemPrice * request.getTotal());
                    }
                }
            }

            userDAO.updateBalanceByUserId(user.getId(), user.getBalance() - itemPrice * request.getTotal());
            userDAO.insertPayment(new Payment(user.getId(), Payment.Type.SPEND, itemPrice * request.getTotal(), false));

            return ok(Json.toJson(Response.OK()));
        }

        return ok(Json.toJson(Response.ERROR()));

    }

    private boolean resellerShouldPayForUser(int resellerId) {
        for (int id: resellersShouldPayForUsers) {
            if (id == resellerId) {
                return true;
            }
        }

        return false;
    }

    public Result getPreparedCount(int listId) {
        return ok(Json.toJson(Response.OK(0)));
    }

    public Result prepareListForDownloading() {
        DownloadListRequest request = Json.fromJson(request().body().asJson(), DownloadListRequest.class);
        User user = userDAO.findUserById(request.getUserId());

        ListEntity list = listDAO.findListById(request.getListId());
        if (list == null || (list.getUserId() != request.getUserId() && user.getRole() == User.ROLE_USER)) {
            return ok(Json.toJson(Response.ERROR()));
        }

        registeredDownloadRequestsMap.put(list.getId(), request);
        return ok(Json.toJson(Response.OK(list.getId())));
    }

    public Result sendDownloadListEmail() {
        DownloadListRequest request = Json.fromJson(request().body().asJson(), DownloadListRequest.class);
        User user = userDAO.findUserById(request.getUserId());

        ListEntity list = listDAO.findListById(request.getListId());
        if (list == null || (list.getUserId() != request.getUserId() && user.getRole() == User.ROLE_USER)) {
            return ok(Json.toJson(Response.ERROR()));
        }

        registeredDownloadRequestsMap.put(list.getId(), request);

        User reseller = userDAO.findUserById(user.getResellerId());
        String requestHost = request().getHeader("Origin");
        if (reseller != null && requestHost != null) {
            requestHost = requestHost.
                    replace("http://", "").
                    replace("https://", "").
                    replace("www.", "");

            String domains = reseller.getDomains();
            if (domains != null && domains.length() > 0) {
                String[] domainArray = domains.split(",");
                for (String domain : domainArray) {
                    if (requestHost.equalsIgnoreCase(domain.trim())) {
                        reseller.setDomains(domain.trim());
                    }
                }
            }
        }
        try{
            listDAO.saveSentEmailList(list.getDate(),list.getName(),user.getEmail(),request.getEmailAddress());
        }catch (Exception e){
            e.printStackTrace();
        }

        emailService.sendDownloadListEmail(list, request.getEmailAddress(), reseller);
        return ok(Json.toJson(Response.OK()));
    }

    private void generateConsumersListCSVFile(ListEntity list,
                                              DownloadListRequest request,
                                              PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Consumer.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List< Consumer > consumers = dataDAO.getConsumerListByPurchasedList(listId, tableName, offset, limit);
        while (consumers.size() > 0) {
            for (int k = 0; k < consumers.size(); k++) {
                Consumer consumer = consumers.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("DOB_DATE") || field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Consumer.class).getReadMethod().invoke(consumer);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(consumer.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + consumers.size();
            consumers = dataDAO.getConsumerListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void writeCode(PrintWriter printWriter, String code) {
        if (code != null && code.length() > 0) {
            printWriter.append(SEPARATOR);
            printWriter.append(code);
        }
    }

    private void writeSavedData(String data, List<String> savedColumns,
                                List<String> columns, PrintWriter printWriter) {
        List<String> dataValues = getDataValues(data);
        for (int i = 0; i < savedColumns.size(); i++) {
            if (columns.contains(savedColumns.get(i)) ||
                    columns.contains(savedColumns.get(i).toUpperCase())) {
                printWriter.append(SEPARATOR).append("\"" + dataValues.get(i) + "\"");
            }
        }
    }

    private List<String> getDataValues(String data) {
        List<String> results = new LinkedList();

        if (data != null && data.length() > 0) {
            String[] parts = data.split(",");

            for (String part: parts) {
                results.add(part.trim());
            }
        }

        return results;
    }

    private void writeCSVFileHeader(List<String> columns, Field[] fields,
                                    PrintWriter printWriter, List<String> savedColumns,
                                    String code) {
        writeCSVFileHeader(columns, fields, printWriter, savedColumns, code, null);
    }

    private void writeCSVFileHeader(List<String> columns, Field[] fields,
                                    PrintWriter printWriter, List<String> savedColumns,
                                    String code, DataTable table) {
        if (columns.size() > 1) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field == null || field.getName() == null ||
                        (field.getName().equals("DOB_DATE") && (table == null || table.getType() != DataTable.HEALTH_INSURANCE_LEAD)) ||
                        field.getName().equals("id")) {
                    continue;
                }

                writeColumnName(printWriter, field, table);
                if (i + 1 < fields.length) {
                    printWriter.append(SEPARATOR);
                }
            }

            for (String savedColumn: savedColumns) {
                if (columns.contains(savedColumn) || columns.contains(savedColumn.toUpperCase())) {
                    printWriter.append(SEPARATOR).append(savedColumn);
                }
            }

            if (code != null && code.length() > 0) {
                printWriter.append(SEPARATOR).append("ADDITIONAL CODE");
            }

            printWriter.append(NEW_LINE);
        }
    }

    private void generateConsumers2ListCSVFile(ListEntity list,
                                               DownloadListRequest request,
                                               PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Consumer2.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<Consumer2> consumers = dataDAO.getConsumer2ListByPurchasedList(listId, tableName, offset, limit, request.isNotPurchased());
        while (consumers.size() > 0) {
            for (int k = 0; k < consumers.size(); k++) {
                Consumer2 consumer = consumers.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field == null || field.getName() == null || field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Consumer2.class).getReadMethod().invoke(consumer);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(consumer.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + consumers.size();
            consumers = dataDAO.getConsumer2ListByPurchasedList(listId, tableName, offset, limit, request.isNotPurchased());
        }
    }

    public static void writeColumnName(PrintWriter printWriter, Field field, DataTable table) {
        if ("phoneType".equals(field.getName())) {
            printWriter.append("PHONE TYPE");
        } else if ("dnc".equals(field.getName())) {
            printWriter.append("DNC INFO");
        } else if (table != null && table.getType() == DataTable.DIRECTORY && "WWW".equals(field.getName())) {
            printWriter.append("SOURCE");
        } else if ("WWW".equals(field.getName())) {
            printWriter.append("WEBSITE");
        } else if ("SOURCE".equalsIgnoreCase(field.getName())) {
            printWriter.append("WEBSITE");
        } else if ("DOB_DATE".equalsIgnoreCase(field.getName())) {
            printWriter.append("AGE");
        } else {
            printWriter.append(field.getName().toUpperCase());
        }
    }

    private Field[] generateFieldsArray(Field[] declaredFields, List< String > columns, List<String> savedColumns) {
        List<Field> result = new LinkedList();

        for (String column: columns) {
            boolean found = false;

            if (isSavedColumnsContain(savedColumns, column)) {
                continue;
            }

            for (Field field: declaredFields) {
                if (field.getName().equalsIgnoreCase(column)) {
                    result.add(field);

                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println(column);
            }

        }

        Field[] resultArray = new Field[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }


        return resultArray;
    }

    private boolean isSavedColumnsContain(List<String> savedColumns, String column) {
        for (String savedColumn: savedColumns) {
            if (column.equalsIgnoreCase(savedColumn)) {
                return true;
            }
        }

        return false;
    }

    private static String handleValue(String value) {
        return value.equals("-1") ? "" :
                value.replace("\"", "").
                        replace(", ", " ").
                        replace(",", " ");
    }

    private void generateFacebookListCSVFile(ListEntity list,
                                             DownloadListRequest request,
                                             PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Facebook.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List< Facebook > facebooks = dataDAO.getFacebookListByPurchasedList(listId, tableName, offset, limit);
        while (facebooks.size() > 0) {
            for (int k = 0; k < facebooks.size(); k++) {
                Facebook facebook = facebooks.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("DOB_DATE") || field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Facebook.class).getReadMethod().invoke(facebook);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(facebook.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + facebooks.size();
            facebooks = dataDAO.getFacebookListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateBusinessListCSVFile(ListEntity list,
                                             DownloadListRequest request,
                                             PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Business.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List< Business > businesses = dataDAO.getBusinessListByPurchasedList(listId, tableName, offset, limit);
        while (businesses.size() > 0) {
            for (int k = 0; k < businesses.size(); k++) {
                Business business = businesses.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("DOB_DATE") || field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Business.class).getReadMethod().invoke(business);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(business.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + businesses.size();
            businesses = dataDAO.getBusinessListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    public static void writeFieldValue(PrintWriter printWriter, Field field, Object value) {
        if ("phoneType".equalsIgnoreCase(field.getName()) ||
                "conPhoneType".equalsIgnoreCase(field.getName()) ||
                "bisPhoneType".equalsIgnoreCase(field.getName())) {
            printWriter.append((value != null && !MINUS_ONE.equals(value)) ? (ZERO.equals(value) ? "\"landline\"" : "\"mobile\"") :  "\"\"");
        } else if ("dnc".equalsIgnoreCase(field.getName())) {
            printWriter.append(Boolean.FALSE.equals(value) ? "\"clean\"" : "\"DNC\"");
        }else if ("www".equalsIgnoreCase(field.getName())) {
            try {
                if (value.toString().length() > 4) {
                    String s = value.toString().subSequence(value.toString().length() - 4, value.toString().length()).toString();
                    if (s.contains("2021")) {
                        printWriter.append(value.toString().replace("2021", ""));
                    } else if (s.contains("2020")) {
                        printWriter.append(value.toString().replace("2020", ""));
                    } else if (s.contains("2019")) {
                        printWriter.append(value.toString().replace("2019", ""));
                    } else {
                        printWriter.append(value.toString());
                    }
                } else {
                    printWriter.append(value.toString());
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } else if ("industry".equalsIgnoreCase(field.getName())) {
            try {
                if (value.toString().contains("point_o")) {
                    String industry = "";
                    int pos = value.toString().indexOf("point_o");
                    for(int i =0;i<pos;i++){
                        industry+= value.toString().charAt(i);
                    }
                    System.out.println("Index of str" + pos);
                    printWriter.append((industry));
                }else {
                    printWriter.append((value.toString()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if ("dobDate".equalsIgnoreCase(field.getName()) ||
                "date".equalsIgnoreCase(field.getName()) ||
                "PERSONDATEOFBIRTHDATE".equalsIgnoreCase(field.getName()) ||
                "HOMEPURCHASEDATE".equalsIgnoreCase(field.getName())) {
            String result = "";

            try {
                if ((long)value > -2204717475555l && (long)value != 0) {
                    Date date = new Date((long) value + 24 * 60 * 60 * 1000l);
                    result = dateFormat.format(date);
                }
            } catch (Exception e) {e.printStackTrace();}

            printWriter.append(result);
        } else if ("dob_date".equalsIgnoreCase(field.getName())) {
            String result = "";
            if (value != null) {
                result = (System.currentTimeMillis() - (long)value) / (365l * 24 * 60 * 60 * 1000) + "";
            }

            printWriter.append(result);
        } else if ("PersonOccupation".equalsIgnoreCase(field.getName())) {
            if (StaticConstants.personOccupations.containsKey(value)) {
                printWriter.append(StaticConstants.personOccupations.get(value));
            } else {
                printWriter.append(value.toString());
            }
        } else {
            printWriter.append(value != null ? "\"" + handleValue(value.toString()) + "\"" : "");
        }
    }

    private void generateEverydataListCSVFile(ListEntity list,
                                              DownloadListRequest request,
                                              PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();
        DataTable table = dataDAO.getTableByName(tableName);

        Field[] fields = generateFieldsArray(Everydata.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode(), table);

        long offset = 0;
        int limit = 100000;
        List< Everydata > everydatas = dataDAO.getEverydataListByPurchasedList(listId, tableName, offset, limit);
        while (everydatas.size() > 0) {
            for (int k = 0; k < everydatas.size(); k++) {
                Everydata everydata = everydatas.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Everydata.class).getReadMethod().invoke(everydata);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(everydata.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + everydatas.size();
            everydatas = dataDAO.getEverydataListByPurchasedList(listId, tableName, offset, limit);
        }
    }
    private void generateDirectoryListCSVFile(ListEntity list,
                                              DownloadListRequest request,
                                              PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();
        DataTable table = dataDAO.getTableByName(tableName);

        Field[] fields = generateFieldsArray(Directory.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode(), table);

        long offset = 0;
        int limit = 100000;
        List< Directory > directories = dataDAO.getDirectoryListByPurchasedList(listId, tableName, offset, limit);
        while (directories.size() > 0) {
            for (int k = 0; k < directories.size(); k++) {
                Directory directory = directories.get(k);
                //Clean industry field
                try{
                    String industryField = "";
                    String[] industrieSplitted = directory.getINDUSTRY().replace(","," ").split(" ");
                    for(int i =0;i<industrieSplitted.length;i++){
                        if(i==industrieSplitted.length ||  i==industrieSplitted.length-1 && (industrieSplitted[i].equals("p") || industrieSplitted[i].equals("po") || industrieSplitted[i].equals("poi") || industrieSplitted[i].equals("poin") || industrieSplitted[i].equals("point"))){

                        }else{
                            industryField+=industrieSplitted[i]+" ";
                        }

                    }
                    directory.setINDUSTRY(industryField);
                }catch (Exception e){

                }

                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Directory.class).getReadMethod().invoke(directory);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(directory.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + directories.size();
            directories = dataDAO.getDirectoryListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateWhoIsListCSVFile(ListEntity list,
                                          DownloadListRequest request,
                                          PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(WhoIs.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List< WhoIs > whoIsList = dataDAO.getWhoIsListByPurchasedList(listId, tableName, offset, limit);
        while (whoIsList.size() > 0) {
            for (int k = 0; k < whoIsList.size(); k++) {
                WhoIs whoIsItem = whoIsList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];

                    Object value = new PropertyDescriptor(field.getName(), WhoIs.class).getReadMethod().invoke(whoIsItem);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(whoIsItem.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + whoIsList.size();
            whoIsList = dataDAO.getWhoIsListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateCraigslistLisCSVFile(ListEntity list,
                                              DownloadListRequest request,
                                              PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(CraigsList.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List< CraigsList > craigslists = dataDAO.getCraiglistByPurchasedList(listId, tableName, offset, limit);
        while (craigslists.size() > 0) {
            for (int k = 0; k < craigslists.size(); k++) {
                CraigsList craigslistItem = craigslists.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];

                    Object value = new PropertyDescriptor(field.getName(), CraigsList.class).getReadMethod().invoke(craigslistItem);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(craigslistItem.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + craigslists.size();
            craigslists = dataDAO.getCraiglistByPurchasedList(listId, tableName, offset, limit);
        }
    }

    public Result downloadListDirectly() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        DownloadListRequest request = Json.fromJson(request().body().asJson(), DownloadListRequest.class);
        ListEntity list = listDAO.findListById(request.getListId());

        return generateListFile(list, request);
    }

    public Result downloadList(String fileName, int listId) throws Exception {
        DownloadListRequest request = registeredDownloadRequestsMap.get(listId);
        ListEntity list = listDAO.findListById(listId);

        if (request == null) {
            if ("DirectoryMobile".equals(list.getTableName())) {
                request = new DownloadListRequest(
                        list.getId(),
                        list.getUserId(),
                        new String[] {"COMPANY_NAME", "contact_name", "ADDRESS", "CITY", "STATE", "ZIP", "PHONE", "phoneType", "industry"},
                        "");
            } else {
                return ok("Your download list link is not valid anymore. Please use another link.");
            }
        }

        return generateListFile(list, request);
    }

    private Result generateListFile(ListEntity list, DownloadListRequest request) throws Exception {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);

        Thread readerThread = new Thread(() -> {
            try {
                PrintWriter printWriter = new PrintWriter(pos);
                if (list.getType() == DataTable.CONSUMERS) {
                    generateConsumersListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.BUSINESS || list.getType() == DataTable.BUSINESS2) {
                    generateBusinessListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.DIRECTORY) {
                    generateDirectoryListCSVFile(list, request, printWriter);
                }else if (list.getType() == DataTable.PHILDIRECTORY) {
                    generateDirectoryListCSVFile(list, request, printWriter);
                }else if (list.getType() == DataTable.EVERYDATA) {
                    generateEverydataListCSVFile(list, request, printWriter);
                }  else if (list.getType() == DataTable.CRAIGSLIST) {
                    generateCraigslistLisCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.WHOIS) {
                    generateWhoIsListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.SEARCH_ENGINE) {
                    generateSearchEngineListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.CONSUMERS2 || list.getType() == DataTable.CONSUMERS3) {
                    generateConsumers2ListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.INSTAGRAM) {
                    generateInstagramListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.INSTAGRAM2020) {
                    generateInstagram2020ListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.OPTIN) {
                    generateOptInListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.FACEBOOK) {
                    generateFacebookListCSVFile(list, request, printWriter);
                }else if (list.getType() == DataTable.NEWOPTIN) {
                    generateNewOptInListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.AUTO) {
                    generateAutoListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.BLACKLIST || list.getType() == DataTable.CALLLEADS) {
                    generateBlackListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.LINKEDIN) {
                    generateLinkedInListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.BUSINESS_DETAILED) {
                    generateBusinessDetailedListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.STUDENT) {
                    generateStudentListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.HEALTH_BUYER) {
                    generateHealthBuyersListCSVFile(list, request, printWriter);
                } else if (list.getType() == DataTable.HEALTH_INSURANCE_LEAD) {
                    generateHealthInsuranceLeadsListCSVFile(list, request, printWriter);
                }

                printWriter.flush();
                pos.close();
            } catch (Exception ex) {
                ex.printStackTrace();

                try {
                    pos.close();
                } catch (Exception e) {}
            }
        });
        readerThread.start();

        response().setHeader("Content-disposition", "attachment; filename=" +
                generateFileName(list.getName()) + ".csv");
        return ok(pis).as("text/csv");
    }

    private void generateHealthBuyersListCSVFile(ListEntity list,
                                                 DownloadListRequest request,
                                                 PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(HealthBuyer.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<HealthBuyer> healthBuyerList = dataDAO.getHealthBuyersByPurchasedList(listId, tableName, offset, limit);
        while (healthBuyerList.size() > 0) {
            for (int k = 0; k < healthBuyerList.size(); k++) {
                HealthBuyer healthBuyer = healthBuyerList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), HealthBuyer.class).getReadMethod().invoke(healthBuyer);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(healthBuyer.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + healthBuyerList.size();
            healthBuyerList = dataDAO.getHealthBuyersByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateHealthInsuranceLeadsListCSVFile(ListEntity list,
                                                         DownloadListRequest request,
                                                         PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();
        DataTable table = dataDAO.getTableByName(tableName);

        Field[] fields = generateFieldsArray(HealthInsuranceLead.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode(), table);

        long offset = 0;
        int limit = 100000;
        List<HealthInsuranceLead> healthInsuranceLeadsList = dataDAO.getHealthInsuranceLeadsByPurchasedList(listId, tableName, offset, limit);
        while (healthInsuranceLeadsList.size() > 0) {
            for (int k = 0; k < healthInsuranceLeadsList.size(); k++) {
                HealthInsuranceLead healthInsuranceLead = healthInsuranceLeadsList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), HealthInsuranceLead.class).getReadMethod().invoke(healthInsuranceLead);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(healthInsuranceLead.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + healthInsuranceLeadsList.size();
            healthInsuranceLeadsList = dataDAO.getHealthInsuranceLeadsByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private String generateFileName(String name) {
        String result = "";
        for (Character character: name.toCharArray()) {
            if (!Character.isLetterOrDigit(character)) {
                result = result + "-";
            } else {
                result = result + character;
            }
        }

        while (result.indexOf("--") != -1) {
            result = result.replace("--", "");
        }

        return result;
    }

    private void generateInstagramListCSVFile(ListEntity list,
                                              DownloadListRequest request,
                                              PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Instagram.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<Instagram> instagramRecords = dataDAO.getInstagramListByPurchasedList(listId, tableName, offset, limit);
        while (instagramRecords.size() > 0) {
            for (int k = 0; k < instagramRecords.size(); k++) {
                Instagram instagramRecord = instagramRecords.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Instagram.class).getReadMethod().invoke(instagramRecord);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(instagramRecord.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + instagramRecords.size();
            instagramRecords = dataDAO.getInstagramListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateInstagram2020ListCSVFile(ListEntity list,
                                                  DownloadListRequest request,
                                                  PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Instagram2020.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<Instagram2020> instagramRecords = dataDAO.getInstagram2020ListByPurchasedList(listId, tableName, offset, limit, request.isNotPurchased());
        while (instagramRecords.size() > 0) {
            for (int k = 0; k < instagramRecords.size(); k++) {
                Instagram2020 instagramRecord = instagramRecords.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Instagram2020.class).getReadMethod().invoke(instagramRecord);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(instagramRecord.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + instagramRecords.size();
            instagramRecords = dataDAO.getInstagram2020ListByPurchasedList(listId, tableName, offset, limit, request.isNotPurchased());
        }
    }

    private void generateAutoListCSVFile(ListEntity list,
                                         DownloadListRequest request,
                                         PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Auto.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<Auto> autoList = dataDAO.getAutoListByPurchasedList(listId, tableName, offset, limit);
        while (autoList.size() > 0) {
            for (int k = 0; k < autoList.size(); k++) {
                Auto auto = autoList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Auto.class).getReadMethod().invoke(auto);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(auto.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + autoList.size();
            autoList = dataDAO.getAutoListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateLinkedInListCSVFile(ListEntity list,
                                             DownloadListRequest request,
                                             PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(LinkedIn.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<LinkedIn> linkedInList = dataDAO.getLinkedInByPurchasedList(listId, tableName, offset, limit);
        while (linkedInList.size() > 0) {
            for (int k = 0; k < linkedInList.size(); k++) {
                LinkedIn linkedIn = linkedInList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), LinkedIn.class).getReadMethod().invoke(linkedIn);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData("", new LinkedList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + linkedInList.size();
            linkedInList = dataDAO.getLinkedInByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateBusinessDetailedListCSVFile(ListEntity list,
                                                     DownloadListRequest request,
                                                     PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(BusinessDetailed.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<BusinessDetailed> businessDetailedList = dataDAO.getBusinessDetailedByPurchasedList(listId, tableName, offset, limit);
        while (businessDetailedList.size() > 0) {
            for (int k = 0; k < businessDetailedList.size(); k++) {
                BusinessDetailed businessDetailed = businessDetailedList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), BusinessDetailed.class).getReadMethod().invoke(businessDetailed);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData("", new LinkedList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + businessDetailedList.size();
            businessDetailedList = dataDAO.getBusinessDetailedByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateStudentListCSVFile(ListEntity list,
                                            DownloadListRequest request,
                                            PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(Student.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<Student> studentList = dataDAO.getStudentByPurchasedList(listId, tableName, offset, limit);
        while (studentList.size() > 0) {
            for (int k = 0; k < studentList.size(); k++) {
                Student student = studentList.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), Student.class).getReadMethod().invoke(student);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(student.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + studentList.size();
            studentList = dataDAO.getStudentByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateBlackListCSVFile(ListEntity list,
                                          DownloadListRequest request,
                                          PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(BlackList.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<BlackList> blackLists = dataDAO.getBlackListByPurchasedList(listId, tableName, offset, limit);
        while (blackLists.size() > 0) {
            for (int k = 0; k < blackLists.size(); k++) {
                BlackList blackList = blackLists.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), BlackList.class).getReadMethod().invoke(blackList);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData("", new LinkedList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + blackLists.size();
            blackLists = dataDAO.getBlackListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateNewOptInListCSVFile(ListEntity list,
                                             DownloadListRequest request,
                                             PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(NewOptIn.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<NewOptIn> optInRecords = dataDAO.getNewOptInListByPurchasedList(listId, tableName, offset, limit);
        while (optInRecords.size() > 0) {
            for (int k = 0; k < optInRecords.size(); k++) {
                NewOptIn optIn = optInRecords.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), NewOptIn.class).getReadMethod().invoke(optIn);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(optIn.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + optInRecords.size();
            optInRecords = dataDAO.getNewOptInListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateOptInListCSVFile(ListEntity list,
                                          DownloadListRequest request,
                                          PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(OptIn.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List<OptIn> optInRecords = dataDAO.getOptInListByPurchasedList(listId, tableName, offset, limit);
        while (optInRecords.size() > 0) {
            for (int k = 0; k < optInRecords.size(); k++) {
                OptIn optIn = optInRecords.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field != null && field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), OptIn.class).getReadMethod().invoke(optIn);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(optIn.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + optInRecords.size();
            optInRecords = dataDAO.getOptInListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    private void generateSearchEngineListCSVFile(ListEntity list,
                                                 DownloadListRequest request,
                                                 PrintWriter printWriter) throws Exception {
        List<String> columns = request.getColumns();

        int listId = list.getId();
        String tableName = list.getTableName();

        Field[] fields = generateFieldsArray(SearchEngine.class.getDeclaredFields(), columns, list.getSavedColumnsList());
        writeCSVFileHeader(columns, fields, printWriter, list.getSavedColumnsList(), request.getCode());

        long offset = 0;
        int limit = 100000;
        List< SearchEngine > searchEngineRecords = dataDAO.getSearchEngineListByPurchasedList(listId, tableName, offset, limit);
        while (searchEngineRecords.size() > 0) {
            for (int k = 0; k < searchEngineRecords.size(); k++) {
                SearchEngine searchEngineRecord = searchEngineRecords.get(k);
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[ i ];
                    if (field.getName().equals("id")) {
                        continue;
                    }

                    Object value = new PropertyDescriptor(field.getName(), SearchEngine.class).getReadMethod().invoke(searchEngineRecord);
                    writeFieldValue(printWriter, field, value);
                    if (i + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                writeSavedData(searchEngineRecord.getData(), list.getSavedColumnsList(), columns, printWriter);

                writeCode(printWriter, request.getCode());
                printWriter.append(NEW_LINE);
            }

            offset = offset + searchEngineRecords.size();
            searchEngineRecords = dataDAO.getSearchEngineListByPurchasedList(listId, tableName, offset, limit);
        }
    }

    public Result getUploadedListsByUserId(int userId) {
        User user = userDAO.findUserById(userId);
        if (user != null) {
            if (user.getRole() == User.ROLE_ADMIN || user.getRole() == User.ROLE_MANAGER) {
                return ok(Json.toJson(Response.OK(listDAO.getAllUploadedLists())));
            } else {
                return ok(Json.toJson(Response.OK(listDAO.getUploadedListsByUserId(userId))));
            }
        }

        return ok(Json.toJson(Response.ERROR()));
    }

    public Result getAllUploadedListsByUserId(int userId) {
        List<ListEntity> lists = listDAO.getUploadedListsByUserId(userId);

        User user = userDAO.findUserByName("system_staff");
        if (user != null) {
            lists.addAll(listDAO.getUploadedListsByUserId(user.getId()));
        }

        return ok(Json.toJson(Response.OK(lists)));
    }

    public Result updateUploadedList() {
        ListEntity listEntity = Json.fromJson(request().body().asJson(), ListEntity.class);
        listDAO.updateNameOfUploadedList(listEntity);
        return ok(Json.toJson(Response.OK()));
    }

    public Result deleteUploadedList(int listId) {
        listDAO.deleteUploadedList(listId);
        return ok(Json.toJson(Response.OK()));
    }


    //creating a generic function that converts the Array into List
    public static <T> List<T> ArrayToListConversion(T array[])
    {
//creating the constructor of the List class
        List<T> list = new ArrayList<>();
//using for-each loop to iterate over the array
        for (T t : array)
        {
//adding each element to the List
            list.add(t);
        }
//returns the list converted into Array
        return list;
    }

    // function to generate a random string of length n
    static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + ""
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
    public Result uploadTempFile(){
        try {
            List< String > tableColumns = new LinkedList<>();
            Http.MultipartFormData<File> body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart<File> file = body.getFile("file");

            String columnsTable = body.asFormUrlEncoded().get("columns")[0];
            File listFile = new File(file.getFile().getPath());
            BufferedReader reader = new BufferedReader(new FileReader(listFile));
            String line = reader.readLine();
            //String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            String[] parts = columnsTable.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            String[] headerLine = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            if(parts.length != headerLine.length){
                return ok(Json.toJson("{code:'uneven_fields'}"));
            }else{
                for(String part:parts){
                    //    System.out.println(part);
                    tableColumns.add(part.toLowerCase());
                }
                // tableColumns.add("phone");
                //  tableColumns.add("company_name");
                List<List<String>> rows = new LinkedList<>();

                line = reader.readLine();
                if (file != null) {
                    int count = 0;
                    String uniqueID = getAlphaNumericString(20);
                    //String uniqueID = UUID.randomUUID().toString();
                    listDAO.createGenericTempTable("test_"+uniqueID,"testTableIndex_"+uniqueID,tableColumns);
                    while (line != null) {
                        List<String> row =  ArrayToListConversion(line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));
                        rows.add(row);
                        count++;
                        if (rows.size() % 100 == 2) {
                            // System.out.println("size 100");
                            listDAO.insertUploadedTmpFileItems("test_"+uniqueID,rows,tableColumns);
                            rows.clear();
                        }

                        line = reader.readLine();
                    }
                    Date d = new Date();
                    listDAO.insertMatchFiles("test_"+uniqueID,count,d.getTime());



                    reader.close();
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return ok(Json.toJson("{code:'ok'}"));
    }


    public Result checkFile() {
        try {
            Http.MultipartFormData< File > body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart< File > file = body.getFile("file");
            if (file != null) {
                BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));
                String line = reader.readLine();
                reader.close();

                File tempFile = File.createTempFile("consumer", "temp");
                Files.copy(file.getFile(), tempFile);

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                List<String> handledParts = new LinkedList();
                for (String part: parts) {
                    handledParts.add(part.replaceAll("\"", ""));
                }

                return ok(Json.toJson(Response.OK(tempFile.getAbsolutePath(), handledParts)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok(Json.toJson(Response.ERROR()));
    }

    public Result uploadFile() {
        UploadFileRequest request = Json.fromJson(request().body().asJson(), UploadFileRequest.class);

        ListEntity listEntity;
        long initCount = 0l;
        if (request.getListId() == 0) {
            listEntity = new ListEntity(
                    request.getName(), request.getUserId(), -1, System.currentTimeMillis());
            listDAO.insertUploadedList(listEntity);
        } else {
            listEntity = listDAO.findUploadedListById(request.getListId());
            initCount = listEntity.getCnt();

            listDAO.updateCountOfUploadedListById(-1, request.getListId());
        }

        parseAndInsertListData(listEntity, request, initCount);

        return ok(Json.toJson(Response.OK()));
    }

    private void parseAndInsertListData(ListEntity listEntity, UploadFileRequest request, long initCount) {
        Thread thread = new Thread(() -> {
            try {
                int listId = listEntity.getId();
                long count = initCount;
                long printedCount = 0;

                List< UploadedListItem > items = new LinkedList();

                Map<Long, Set<Long>> listsPhones = getUploadedListsPhones(listEntity);

                File listFile = new File(request.getFilePath());
                BufferedReader reader = new BufferedReader(new FileReader(listFile));
                String line = reader.readLine();
                while (line != null) {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    String phone = parts[ request.getColumn() ].replace("\"", "");
                    if (!phone.equalsIgnoreCase("phone")) {
                        if (Application.isPhoneUnique(phone, listsPhones, 2)) {
                            items.add(new UploadedListItem(listId, phone));
                            if (items.size() == 100) {
                                listDAO.insertUploadedListItems(items);
                            }

                            if (count % 1000 == 0 && printedCount != count) {
                                //       Logger.info("Inserting uploaded list {} phones: {}", listEntity.getName(), count);
                                printedCount = count;
                            }

                            count++;
                        }
                    }
                    line = reader.readLine();
                }

                if (items.size() > 0) {
                    listDAO.insertUploadedListItems(items);
                }

                listsPhones.clear();
                listDAO.updateCountOfUploadedListById(count, listId);
                listFile.delete();

                //  Logger.info("Updating uploaded list {} done: {}", listEntity.getName(), count);
            } catch (Exception e) { e.printStackTrace(); }
        });
        thread.start();
    }

    private Map<Long, Set<Long>> getUploadedListsPhones(ListEntity listEntity) {
        int offset = 0;
        int limit = 10000;

        Map<Long, Set<Long>> result = new HashMap();

        List<String> phones = listDAO.getUploadedListsPhones(listEntity.getId(), offset, limit);
        while(phones.size() > 0) {
            for (String phone: phones) {
                Application.isPhoneUnique(phone, result, 2);
            }

            offset = offset + limit;
            //  Logger.info("Getting uploaded list phones {}: {}", listEntity.getName(), offset);

            phones = listDAO.getUploadedListsPhones(listEntity.getId(), offset, limit);
        }

        return result;
    }
}
