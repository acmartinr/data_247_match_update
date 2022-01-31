package controllers;

import com.google.inject.Inject;
import models.*;
import org.apache.commons.lang.WordUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.db.dao.DataDAO;
import services.db.dao.ListDAO;
import services.db.dao.UserDAO;
import services.db.entity.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static controllers.Application.STATES_BY_TIMEZONE;

public class Data extends Controller {

    private DataDAO dataDAO;
    private UserDAO userDAO;
    private ListDAO listDAO;

    public static String[] CONSUMER_KEYWORD_COLUMNS = { "FN", "LN", "ADDR", "PHONE" };
    public static String[] CONSUMER_2_KEYWORD_COLUMNS = { "PERSONFIRSTNAME", "PERSONLASTNAME", "PRIMARYADDRESS", "PHONE",  "EMAIL" };
    public static String[] BUSINESS_KEYWORD_COLUMNS = { "TITLE", "INDUSTRY", "ADDRESS",
            "COMPANY_NAME", "CONTACT_NAME", "SIC_CODE", "PHONE", "WWW" };
    public static String[] DIRECTORY_KEYWORD_COLUMNS = { "INDUSTRY", "ADDRESS", "COMPANY_NAME", "PHONE", "websites" };
    public static String[] EVERYDATA_KEYWORD_COLUMNS = { "firstname", "lastname","email","source"};
    public static String[] CRAIGSLIST_KEYWORD_COLUMNS = { "INDUSTRY", "PHONE", "website", "phone" };
    public static String[] WHOIS_KEYWORD_COLUMNS = {"WEBSITE", "email", "NAME", "BUSINESS", "ADDRESS", "PHONE"};
    public static String[] SEARCH_ENGINE_KEYWORD_COLUMNS = {"WEBSITE", "PHONE"};
    public static String[] INSTAGRAM_KEYWORD_COLUMNS = {"username", "fullname", "email", "website", "category", "phone"};
    public static String[] OPTIN_KEYWORD_COLUMNS = {"firstname", "lastname", "address", "email", "source", "phone"};
    public static String[] NEWOPTIN_KEYWORD_COLUMNS = {"firstname", "lastname", "address", "email", "clean_source", "phone"};
    public static String[] FACEBOOK_KEYWORD_COLUMNS = {"first_name", "last_name", "gender", "city", "status", "phone","job"};
    public static String[] AUTO_KEYWORD_COLUMNS = {"first_name", "last_name", "address", "phone", "make", "model", "vin"};
    public static String[] BLACKLIST_KEYWORD_COLUMNS = {"phone"};
    public static String[] HEALTH_BUYER_KEYWORD_COLUMNS = {"email", "phone", "name"};
    public static String[] LINKEDIN_KEYWORD_COLUMNS = {"email", "companyName", "firstName", "lastName", "title", "address", "fax", "website", "linkedInId"};
    public static String[] BUSINESS_DETAILED_KEYWORD_COLUMNS = { "title", "address", "company", "name", "SIC_CODE", "PHONE", "WWW" };
    public static String[] STUDENT_KEYWORD_COLUMNS = { "firstName", "lastName", "address", "phone", "email", "ip" };
    public static String[] HEALTH_INSURANCE_KEYWORD_COLUMNS = { "firstName", "lastName", "address", "phone", "email", "ip" };

    public static String[] DIRECTORY_RESTRICTED_WWW = {"buzzfile", "bing", "merchant", "superpages2019", "hotfrog", "dexknows", "Merchant", "yelp2019", "bing2019", "porch", "yelp2018", "angieslist2019", "infoUSA", "manta"};

    public static Map<Integer, String> messages = new HashMap();
    public static Map<Integer, Boolean> needToResetMatching = new ConcurrentHashMap();

    public static Set<String> NUMERIC_COLUMNS = new HashSet() {{
        add("CREDIT_LINES");
        add("CREDIT_RANGE_NEW");
        add("HH_SIZE");
        add("LOR");
        add("NUM_ADULTS");
        add("NUM_KIDS");
        add("AREA_CODE");
        add("ZIP_CODE");
        add("ZIPCODE");
    }};

    public static Map<String, String> COLUMN_REPLACE_MAP = new HashMap() {{
        put("ZIP", "ZIP_CODE");
        put("SIC_CODE", "SIC");
        put("ADDRESS", "ADDR");
    }};

    public static Map<String, String> COLUMN_CHECK_REPLACE = new HashMap() {{
        put("address", "addr");
    }};

    public static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    public static DateFormat dobFormat = new SimpleDateFormat("MM.dd.yyyy");

    @Inject
    public Data(DataDAO dataDAO, UserDAO userDAO, ListDAO listDAO) {
        this.dataDAO = dataDAO;
        this.userDAO = userDAO;
        this.listDAO = listDAO;
    }

    public Result message(int userId) {
        return ok(Json.toJson(Response.OK(messages.get(userId))));
    }

    public Result saveMatchingList() throws Exception {
        ListEntity list = Json.fromJson(request().body().asJson(), ListEntity.class);
        int userId = list.getUserId();
        list.setUserId(userDAO.findUserByName("system_staff").getId());

        listDAO.saveListWithoutItems(list);
        listDAO.updateMatchedById(list.getId(), true);

        BufferedReader reader = new BufferedReader(new FileReader(new File(list.getFilePath())));
        String header = reader.readLine();

        int threads = 1;
        AtomicInteger finished = new AtomicInteger();
        AtomicLong paramsCount = new AtomicLong();

        if (userId == 41) {
            threads = 4;
        }

        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                List<String> columns = list.getColumns();
                List<String> savedColumns = list.getSavedColumnsList();

                List<String> systemColumns = new LinkedList();
                systemColumns.addAll(columns);
                systemColumns.removeAll(savedColumns);

                List<String> params = readNextParamsButch(reader);

                while (params.size() > 0) {
                    List<List<DataRequest.Entity>> orConditions = new LinkedList();

                    for (String param : params) {
                        String[] paramParts = param.split(",");
                        if (paramParts.length == columns.size()) {
                            List<DataRequest.Entity> andCondition = generateMatchingAndConditions(
                                    list.isFilterDNC(), systemColumns, columns, paramParts);

                            orConditions.add(andCondition);
                            dataDAO.insertMatchedRecords(
                                    orConditions, generateData(Arrays.asList(paramParts), columns, savedColumns),
                                    list.getTableName(), list.getId());

                            orConditions.clear();
                            andCondition.clear();
                        }
                    }


                    long paramsCountValue = paramsCount.addAndGet(params.size());
                    try {
                        Logger.info(String.format("[Saving %s]: handled uploaded file lines: %d", list.getName(), paramsCountValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    params = readNextParamsButch(reader);
                }

                finished.incrementAndGet();
            }).start();
        }

        while (finished.get() < threads) {
            Thread.sleep(10000);
        }

        list.setUserId(userId);
        listDAO.updateListUserId(list);

        return ok(Json.toJson(Response.OK()));
    }

    private String generateData(List<String> params,
                                List<String> orderedColumns,
                                List<String> savedColumns) {
        String result = "";
        for (String savedColumn: savedColumns) {
            savedColumn = savedColumn.trim();
            result = result + params.get(orderedColumns.indexOf(savedColumn));

            if (savedColumns.indexOf(savedColumn) < savedColumns.size() - 1) {
                result = result + ",";
            }
        }

        return result;
    }


    public Result resetMatching(int userId) {
        needToResetMatching.put(userId, true);
        return ok(Json.toJson(Response.OK()));
    }

    public Result matching() {
        MatchingRequest request = Json.fromJson(request().body().asJson(), MatchingRequest.class);

        try {
            List<String> columns = request.getColumns();
            long count = dataDAO.getMatchingCount(columns,request.getSelectedTable(),request.getTableName());
            System.out.println(count);
            System.out.println(request.getTableName());

            return ok(Json.toJson(Response.OK(new DataResponse(count,""))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        needToResetMatching.remove(request.getUserId());
        messages.remove(request.getUserId());

        return ok(Json.toJson(Response.ERROR()));
    }

    /*
        public Result matching_old() {
            MatchingRequest request = Json.fromJson(request().body().asJson(), MatchingRequest.class);
            System.out.println(request.getSelectedTable());
            AtomicInteger currentLines = new AtomicInteger();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(request.getFilePath())));
                String header = reader.readLine();
                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(request.getFilePath()));
                lineNumberReader.skip(Long.MAX_VALUE);
                int totalLines = lineNumberReader.getLineNumber();
                lineNumberReader.close();

               // System.out.println("total lines"+totalLines);

                int threads = 1;
                AtomicInteger finished = new AtomicInteger();
                AtomicLong count = new AtomicLong();
                AtomicLong paramsCount = new AtomicLong();

                if (request.getUserId() == 41) {
                    threads = 4;
                }

                for (int i = 0; i < threads; i++) {
                    new Thread(() -> {
                        List<String> columns = request.getColumns();
                        List<String> saveColumns = getSaveColumns(request.getSaveFields());

                        List<String> systemColumns = new LinkedList();
                        systemColumns.addAll(columns);
                        systemColumns.removeAll(saveColumns);

                        if (header.split(",").length == columns.size() &&
                                checkSaveColumns(saveColumns, header) &&
                                checkHeader(systemColumns, request.getTableName())) {

                            List<String> params = readNextParamsButch(reader);


                            //          System.out.println("Curret progress "+currentLines.addAndGet(currentLines.get() + params.size()) *100/totalLines);

                            while (params.size() > 0) {
                                if (needToResetMatching.containsKey(request.getUserId())) {
                                    messages.remove(request.getUserId());

                                    count.set(0);
                                    break;
                                }

                                List<List<DataRequest.Entity>> orConditions =
                                        generateMatchingOrCondition(params, request, systemColumns, columns);
                                if (orConditions.size() > 0) {
                                    count.addAndGet(dataDAO.getMatchingCount(orConditions, request.getTableName()));
                                }

                                long paramsCountValue = paramsCount.addAndGet(params.size());
                                //messages.put(request.getUserId(), String.format("Handled uploaded file lines: %d", paramsCountValue));
                                //System.out.println(paramsCountValue);
                                messages.put(request.getUserId(), String.format("Process progress: %d ", paramsCountValue*100/totalLines) + " %");

                                params = readNextParamsButch(reader);
                            }

                            finished.incrementAndGet();
                        }
                    }).start();
                }

                while (finished.get() < threads) {
                    Thread.sleep(10000);
                }

                needToResetMatching.remove(request.getUserId());
                messages.remove(request.getUserId());

                return ok(Json.toJson(Response.OK(new DataResponse(count.get(),""))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            needToResetMatching.remove(request.getUserId());
            messages.remove(request.getUserId());

            return ok(Json.toJson(Response.ERROR()));
        }


     */
    private List<List<DataRequest.Entity>> generateMatchingOrCondition(List<String> params,
                                                                       MatchingRequest request,
                                                                       List<String> systemColumns,
                                                                       List<String> columns) {
        List<List<DataRequest.Entity>> orConditions = new LinkedList();
        for (String param : params) {
            String[] paramParts = param.split(",");
            if (paramParts.length == columns.size()) {
                List<DataRequest.Entity> andCondition = generateMatchingAndConditions(
                        request.isFilterDNC(), systemColumns, columns, paramParts);
                orConditions.add(andCondition);
            }
        }

        return orConditions;
    }

    private List<DataRequest.Entity> generateMatchingAndConditions(boolean filterDNC,
                                                                   List<String> systemColumns,
                                                                   List<String> columns,
                                                                   String[] paramParts) {
        List<DataRequest.Entity> andCondition = new LinkedList();
        for (int index = 0; index < systemColumns.size(); index++) {
            String column = systemColumns.get(index);
            String value = paramParts[columns.indexOf(column)];

            if (column.equalsIgnoreCase("personlastname") ||
                    column.equalsIgnoreCase("personfirstname")) {
                value = value.toUpperCase();
            }

            if (NUMERIC_COLUMNS.contains(column.toUpperCase())) {
                try {
                    andCondition.add(new DataRequest.Entity(column, Integer.parseInt(value), "="));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("ADDR".equalsIgnoreCase(column) || "address".equalsIgnoreCase(column) || "primaryaddress".equalsIgnoreCase(column)) {
                String[] valueParts = value.split(" ");
                if (valueParts.length > 0 && isNumber(valueParts[0])) {
                    andCondition.add(new DataRequest.Entity(column, valueParts[0] + " %", "ILIKE"));
                } else {
                    andCondition.add(new DataRequest.Entity(column, value + "%", "ILIKE"));
                }
            } else if ("city".equalsIgnoreCase(column) || "cityname".equalsIgnoreCase(column)) {
                andCondition.add(new DataRequest.Entity(column, WordUtils.capitalize(value.toLowerCase()), "="));
            } else {
                andCondition.add(new DataRequest.Entity(column, value, "="));
            }

            if (filterDNC) {
                andCondition.add(new DataRequest.Entity("dnc", false, "="));
            }
        }

        return andCondition;
    }

    private boolean isNumber(String value) {
        long parsedValue = -1l;

        try { parsedValue = Long.parseLong(value); }
        catch (Exception e) { /**/ }

        return parsedValue != -1l;
    }

    private boolean checkSaveColumns(List<String> saveColumns, String header) {
        List<String> columns = new LinkedList();
        for (String column: saveColumns) {
            columns.add(column.trim().toUpperCase());
        }

        String[] headerColumns = header.split(",");
        for (String column: headerColumns) {
            columns.remove(column.trim().toUpperCase());
        }

        return columns.size() == 0;
    }

    private List<String> getSaveColumns(String saveFields) {
        List<String> saveColumns = new LinkedList();
        if (saveFields.trim().length() > 0) {
            String[] parts = saveFields.split(",");

            for (String part: parts) {
                part = part.trim();

                if (part.length() > 0) {
                    saveColumns.add(part);
                }
            }
        }

        return saveColumns;
    }

    private String[] generateColumns(String[] columns) {
        String[] result = new String[columns.length];
        for (int  i = 0; i < columns.length; i++) {
            String column = columns[i];

            if (COLUMN_REPLACE_MAP.containsKey(column.toUpperCase())) {
                result[i] = COLUMN_REPLACE_MAP.get(column.toUpperCase());
            } else {
                result[i] = column;
            }
        }

        return result;
    }

    synchronized public static List<String> readNextParamsButch(BufferedReader reader) {
        List<String> result = new LinkedList();

        try {
            String paramsLine;
            while ((paramsLine = reader.readLine()) != null) {
                result.add(paramsLine);

                if (result.size() == 100) {
                    return result;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    private boolean checkHeader(List<String> columns, String tableName) {
        DataTable table = dataDAO.getTableByName(tableName);
        if (table != null) {
            switch (table.getType()) {
                case DataTable.CONSUMERS:
                    return checkColumns(columns, Consumer.class);
                case DataTable.CONSUMERS2:
                case DataTable.CONSUMERS3:
                    return checkColumns(columns, Consumer2.class);
                case DataTable.HEALTH_BUYER:
                    return checkColumns(columns, HealthBuyer.class);
                case DataTable.BUSINESS:
                case DataTable.BUSINESS2:
                    return checkColumns(columns, Business.class);
                case DataTable.CRAIGSLIST:
                    return checkColumns(columns, CraigsList.class);
                case DataTable.DIRECTORY:
                    return checkColumns(columns, Directory.class);
                case DataTable.WHOIS:
                    return checkColumns(columns, WhoIs.class);
                case DataTable.SEARCH_ENGINE:
                    return checkColumns(columns, SearchEngine.class);
                case DataTable.INSTAGRAM:
                    return checkColumns(columns, Instagram.class);
                case DataTable.INSTAGRAM2020:
                    return checkColumns(columns, Instagram2020.class);
                case DataTable.OPTIN:
                    return checkColumns(columns, OptIn.class);
                case DataTable.BUSINESS_DETAILED:
                    return checkColumns(columns, BusinessDetailed.class);
                case DataTable.STUDENT:
                    return checkColumns(columns, Student.class);
                case DataTable.HEALTH_INSURANCE_LEAD:
                    return checkColumns(columns, HealthInsuranceLead.class);
            }
        }

        return false;
    }

    private boolean checkColumns(List<String> columns, Class clazz) {
        List<String> columnsList = new LinkedList();

        for (String column: columns) {
            column = column.toLowerCase().trim();
            if (COLUMN_CHECK_REPLACE.containsKey(column) && clazz.equals(Consumer.class)) {
                columnsList.add(COLUMN_CHECK_REPLACE.get(column));
            } else {
                columnsList.add(column.trim().toLowerCase());
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            columnsList.remove(field.getName().toLowerCase());
        }

        return columnsList.size() == 0;
    }

    public Result dataSources(int userId) {
        User user = userDAO.findUserById(userId);
        if (user.getRole() == User.ROLE_ADMIN || user.getRole() == User.ROLE_MANAGER) {
            return ok(Json.toJson(Response.OK(updateDataSources(dataDAO.findAllDataSources()))));
        } else {
            return ok(Json.toJson(Response.OK(updateDataSources(dataDAO.findAllDataSourcesForUser(user)))));
        }
    }

    private List<DataSource> updateDataSources(List<DataSource> dataSources) {
        dataSources = Administration.filterDataSources(dataSources);

        dataSources.sort((left, right) -> left.getTitle().compareToIgnoreCase(right.getTitle()));

        DataSource dataSource = getDataSourceWithName(dataSources, "consumer2018");
        if (dataSource != null) {
            dataSources.remove(dataSource);
            dataSources.add(0, dataSource);
        }

        dataSource = getDataSourceWithName(dataSources, "consumer");
        if (dataSource != null) {
            dataSources.remove(dataSource);
            dataSources.add(0, dataSource);
        }

        return dataSources;
    }

    private DataSource getDataSourceWithName(List<DataSource> dataSources, String name) {
        for (DataSource dataSource: dataSources) {
            if (dataSource.getName().equalsIgnoreCase(name)) {
                return dataSource;
            }
        }

        return null;
    }

    public Result getDetailedData() {
        DetailedDataRequest request = Json.fromJson(request().body().asJson(), DetailedDataRequest.class);
        List<DataRequest.Entity> countOrConditions = getCountOrConditionsList(request.getRequest(), request.getSearchRequest().getDataType());

        if (request.getListId() != null) {
            ListEntity list = listDAO.findListById(request.getListId());

            if (list.getPcnt() > 0) {
                long count = dataDAO.getDetailedRecordsCountByPurchasedList(
                        list.getId(),
                        list.getTableName(),
                        countOrConditions,
                        countOrConditions.get(0).getKey());
                return ok(Json.toJson(Response.OK(count)));
            } else {
                long count = dataDAO.getDetailedRecordsCountByNonPurchasedList(
                        list.getId(),
                        list.getTableName(),
                        countOrConditions,
                        countOrConditions.get(0).getKey());
                return ok(Json.toJson(Response.OK(count)));
            }
        } else {
            List<List<DataRequest.Entity>> orConditions = getOrConditionList(request.getSearchRequest());
            List<List<List<DataRequest.Entity>>> andConditions = getAndConditionsList(request.getSearchRequest());

            long count = dataDAO.getDetailedRecordsCount(
                    orConditions,
                    andConditions,
                    countOrConditions,
                    countOrConditions.get(0).getKey(),
                    request.getSearchRequest().getSelectedLists(),
                    request.getSearchRequest().getUploadedLists(),
                    request.getSearchRequest().getTableName(),
                    request.getSearchRequest().getDataType(),
                    request.getSearchRequest().isUnique(),
                    request.getSearchRequest().isUniqueEmails(),
                    request.getSearchRequest().isUniqueBusinessName(),
                    request.getSearchRequest().isFilterDNC(),
                    request.getSearchRequest().isFilterEmptyPhone(),
                    request.getSearchRequest().isFilterEmail(),
                    request.getSearchRequest().isFilterBusinessEmail(),
                    request.getSearchRequest().isBlackListMatch(),
                    request.getSearchRequest().isConsumerMatch(),
                    request.getSearchRequest().isCraigslistMatch(),
                    request.getSearchRequest().isDirectoryMatch(),
                    request.getSearchRequest().isOptinMatch(),
                    request.getSearchRequest().isBusinessDetailedMatch(),
                    request.getSearchRequest().isCallLeadsMatch(),
                    request.getSearchRequest().isFilterWebsite());

            return ok(Json.toJson(Response.OK(count)));
        }
    }

    private List<DataRequest.Entity> getCountOrConditionsList(DetailedRequest request, int dataType) {
        List<DataRequest.Entity> results = new LinkedList();

        if ("timeZone".equals(request.getType())) {
            String[] states = STATES_BY_TIMEZONE.get(request.getValue());
            for (String state: states) {
                results.add(new DataRequest.Entity("ST_CODE", getStateCode(state), "="));
            }
        }

        if ("state".equals(request.getType())) {
            results.add(new DataRequest.Entity("ST_CODE", getStateCode(request.getValue()), "="));
        }

        if ("county".equals(request.getType())) {
            if (dataType == DataTable.CONSUMERS2 || dataType == DataTable.CONSUMERS3) {
                results.add(new DataRequest.Entity("COUNTYNAME", request.getValue().split("_")[0], "="));
                results.add(new DataRequest.Entity("COUNTYNAME", request.getValue().split("_")[0].toUpperCase(), "="));
            } else {
                results.add(new DataRequest.Entity("COUNTY", request.getValue().split("_")[0], "="));
                results.add(new DataRequest.Entity("COUNTY", request.getValue().split("_")[0].toUpperCase(), "="));
            }
        }

        if ("city".equals(request.getType())) {
            if (dataType == DataTable.CONSUMERS2 || dataType == DataTable.CONSUMERS3) {
                results.add(new DataRequest.Entity("CITYNAME", request.getValue().split("_")[0], "="));
                results.add(new DataRequest.Entity("CITYNAME", request.getValue().split("_")[0].toUpperCase(), "="));
            } else {
                results.add(new DataRequest.Entity("CITY", request.getValue().split("_")[0], "="));
                results.add(new DataRequest.Entity("CITY", request.getValue().split("_")[0].toUpperCase(), "="));
            }
        }

        if ("zip".equals(request.getType())) {
            String zip = request.getValue().toLowerCase();
            String key = "ZIP_CODE";
            Logger.info("ZIP_CODE");
            if (dataType == DataTable.CONSUMERS2 ||
                    dataType == DataTable.CONSUMERS3 ||
                    dataType == DataTable.INSTAGRAM ||
                    dataType == DataTable.INSTAGRAM2020) {
                key = "ZIPCODE";
                Logger.info("Other type");
            }else if(dataType == DataTable.NEWOPTIN){
                key = "zip4";
                Logger.info("Opt-in type");
            }

            if (zip.contains("x")) {
                results.add(new DataRequest.Entity(key, parseInt(zip.replaceAll("x", "0")), ">="));
                results.add(new DataRequest.Entity(key, parseInt(zip.replaceAll("x", "9")), "<="));
            } else {
                results.add(new DataRequest.Entity(key, parseInt(zip), "="));
            }
        }

        if ("areaCode".equals(request.getType())) {
            if (dataType == DataTable.CONSUMERS2 ||
                    dataType == DataTable.CONSUMERS3 ||
                    dataType == DataTable.INSTAGRAM ||
                    dataType == DataTable.INSTAGRAM2020 ||
                    dataType == DataTable.OPTIN ||
                    dataType == DataTable.AUTO ||
                    dataType == DataTable.BLACKLIST ||
                    dataType == DataTable.CALLLEADS) {
                results.add(new DataRequest.Entity("AREACODE", parseInt(request.getValue()), "="));
            } else {
                results.add(new DataRequest.Entity("AREA_CODE", parseInt(request.getValue()), "="));
            }
        }


        return results;
    }

    private static Integer parseInt(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { e.printStackTrace(); }

        return 0;
    }

    public Result geographic() {
        DataRequest request = Json.fromJson(request().body().asJson(), DataRequest.class);
        List< List< DataRequest.Entity > > orConditions = getOrConditionList(request);
        List<List<List<DataRequest.Entity>>> andConditions = getAndConditionsList(request);
        System.out.println(request.isEverydataMatch());
        if(request.getDataType() != DataTable.CONSUMERS2 && request.getDataType() != DataTable.CONSUMERS3){
            request.setLocalNumbers(false);
        }
        DataResponse recordsCount = new DataResponse(dataDAO.getRecordsCount(
                orConditions,
                andConditions,
                request.getSelectedLists(),
                request.getUploadedLists(),
                request.getTableName(),
                request.getDataType(),
                request.isUnique(),
                request.isLocalNumbers(),
                request.isFbHispanicLName(),
                request.isRemoveCorps(),
                request.isConfirmed(),
                request.isBusinessMatch(),
                request.isBusinessMatch2(),
                request.isConsumerMatch2018(),
                request.isConsumerMatch2019(),
                request.isWhoisMatch(),
                request.isHealthBuyersMatch(),
                request.isHealthInsuranceMatch(),
                request.isInstagramMatch(),
                request.isUniqueEmails(),
                request.isUniqueBusinessName(),
                request.isFilterDNC(),
                request.isFilterEmptyPhone(),
                request.isFilterEmail(),
                request.isFilterBusinessEmail(),
                request.isBlackListMatch(),
                request.isConsumerMatch(),
                request.isCraigslistMatch(),
                request.isDirectoryMatch(),
                request.isOptinMatch(),
                request.isBusinessDetailedMatch(),
                request.isCallLeadsMatch(),
                request.isFacebookMatch(),
                request.isEverydataMatch(),
                request.isFilterWebsite()),request.getCountNote());

        return ok(Json.toJson(Response.OK(recordsCount)));
    }

    private List<List<List<DataRequest.Entity>>> getAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = initAndConditions(request);
        if (request.getDataType() == DataTable.CONSUMERS) {
            andConditions.addAll(getConsumersAndConditionsList(request));
            addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), CONSUMER_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.BUSINESS || request.getDataType() == DataTable.BUSINESS2){
            andConditions.addAll(getBusinessAndConditionsList(request));
            addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), BUSINESS_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.DIRECTORY){
            andConditions.addAll(getBusinessAndConditionsList(request));
            andConditions.addAll(getRestrictedSourcesConditionsList());
            addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), DIRECTORY_KEYWORD_COLUMNS);
        }else if (request.getDataType() == DataTable.PHILDIRECTORY){
            andConditions.addAll(getBusinessAndConditionsList(request));
            andConditions.addAll(getRestrictedSourcesConditionsList());
            addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), DIRECTORY_KEYWORD_COLUMNS);
        }else if (request.getDataType() == DataTable.EVERYDATA){
            andConditions.addAll(getBusinessAndConditionsList(request));
            addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), EVERYDATA_KEYWORD_COLUMNS);
        }
        else if (request.getDataType() == DataTable.CRAIGSLIST) {
            andConditions.addAll(getBusinessAndConditionsList(request));
            addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), CRAIGSLIST_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.WHOIS) {
            andConditions.addAll(Data.getBusinessAndConditionsList(request));
            addWhoisDateCondition(andConditions);

            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.WHOIS_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.SEARCH_ENGINE) {
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.SEARCH_ENGINE_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
            andConditions.addAll(getConsumers2AndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.CONSUMER_2_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.INSTAGRAM || request.getDataType() == DataTable.INSTAGRAM2020) {
            andConditions.addAll(getInstagramAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.INSTAGRAM_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.OPTIN) {
            andConditions.addAll(getOptInAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.OPTIN_KEYWORD_COLUMNS);
        }else if (request.getDataType() == DataTable.NEWOPTIN) {
            andConditions.addAll(getNewOptInAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.NEWOPTIN_KEYWORD_COLUMNS);
        }
        else if (request.getDataType() == DataTable.FACEBOOK) {
            andConditions.addAll(getFacebookAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.FACEBOOK_KEYWORD_COLUMNS);
        }
        else if (request.getDataType() == DataTable.AUTO) {
            andConditions.addAll(getAutoAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.AUTO_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.BLACKLIST) {
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.BLACKLIST_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.CALLLEADS) {
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.BLACKLIST_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.HEALTH_BUYER) {
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.HEALTH_BUYER_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.LINKEDIN) {
            andConditions.addAll(getBusinessAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.LINKEDIN_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.BUSINESS_DETAILED) {
            andConditions.addAll(getBusinessAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.BUSINESS_DETAILED_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.STUDENT) {
            andConditions.addAll(getStudentAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.STUDENT_KEYWORD_COLUMNS);
        } else if (request.getDataType() == DataTable.HEALTH_INSURANCE_LEAD) {
            andConditions.addAll(getHealthInsuranceLeadAndConditionsList(request));
            Data.addKeywordConditions(andConditions, request.getKeywords(),
                    request.getKeywordsColumns(), Data.HEALTH_INSURANCE_KEYWORD_COLUMNS);
        }

        return andConditions;
    }

    public static void addWhoisDateCondition(List<List<List<DataRequest.Entity>>> andConditions) {
        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        List<DataRequest.Entity> partAndConditions = new LinkedList();
        partAndConditions.add(new DataRequest.Entity("date", 1483218000000l, ">"));
        partOrConditions.add(partAndConditions);
        andConditions.add(partOrConditions);
    }

    public static List<List<List<DataRequest.Entity>>> getRestrictedSourcesConditionsList() {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        for (int i = 0; i < DIRECTORY_RESTRICTED_WWW.length; i++) {
            String restrictedSource = DIRECTORY_RESTRICTED_WWW[i];

            List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("www", restrictedSource, "!="));
            partOrConditions.add(partAndConditions);

            andConditions.add(partOrConditions);
        }

        return andConditions;
    }

    public static List<List<List<DataRequest.Entity>>> getAutoAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (String model: request.getModels()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("model", model, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String make: request.getMakes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("make", make, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getYearsRange().size() > 0) {
            partOrConditions.add(generateSimpleDateConditions("year", convertYears(request.getYearsRange())));
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getDates().size() > 0) {
            Long startDate = request.getDates().get(0);
            Long endDate = -1l;
            if (request.getDates().size() > 1) {
                endDate = request.getDates().get(1);
            }

            if (endDate < startDate) {
                Long temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("date", startDate, ">="));
            if (endDate != -1) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(endDate);
                calendar.add(Calendar.MONTH, 1);
                partAndConditions.add(new DataRequest.Entity("date", calendar.getTimeInMillis(), "<="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    public static List<List<List<DataRequest.Entity>>> getInstagramAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (String category: request.getCategories()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("category", category, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    public static List<List<List<DataRequest.Entity>>> getOptInAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        if (request.getDates().size() > 0) {
            Long startDate = request.getDates().get(0);
            Long endDate = -1l;
            if (request.getDates().size() > 1) {
                endDate = request.getDates().get(1);
            }

            if (endDate < startDate) {
                Long temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("date", startDate, ">="));
            if (endDate != -1) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(endDate);
                calendar.add(Calendar.MONTH, 1);
                partAndConditions.add(new DataRequest.Entity("date", calendar.getTimeInMillis(), "<="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getAgesRange().size() > 1) {
            long from = request.getAgesRange().get(0);
            long to = request.getAgesRange().get(1);

            if (to < from) {
                long tmp = to;
                to = from;
                from = tmp;
            }

            from = System.currentTimeMillis() - from * 365 * 24 * 60 * 60 * 1000;
            to = System.currentTimeMillis() - to * 365 * 24 * 60 * 60 * 1000;

            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("dobDate", from, "<"));
            partAndConditions.add(new DataRequest.Entity("dobDate", to, ">"));
            partOrConditions.add(partAndConditions);

            if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        }

        partOrConditions = new LinkedList();
        for (String category: request.getSources()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("sourceCriteria", category, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    public static String[] asArray(List< String > columns) {
        String[] result = new String[ columns.size() ];
        for (int i = 0; i < columns.size(); i++) {
            result[ i ] = columns.get(i);
        }

        return result;
    }

    public static void addKeywordConditions(List< List< List< DataRequest.Entity > > > andConditions,
                                            List<String> keywords,
                                            List<List<String>> columnNames,
                                            String[] arrayDefaultColumnNames) {
        List<String> defaultColumnNames = new LinkedList();
        for (String column: arrayDefaultColumnNames) {
            defaultColumnNames.add(column);
        }

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);
            List<String> columns = columnNames.get(i);
            if (columns.size() == 0) {
                columns = defaultColumnNames;
            }

            if (keyword != null && keyword.length() > 0) {
                for (String column : columns) {
                    List<DataRequest.Entity> condition = new LinkedList();

                    if (keyword.startsWith("\"") && keyword.endsWith("\"")) {
                        condition.add( new DataRequest.Entity( column, "\\y" + keyword.replace("\"", "") + "\\y", "~* " ) );
                    } else {
                        condition.add(new DataRequest.Entity(column, keyword, "~* "));
                    }

                    partOrConditions.add(condition);
                }
            }
        }
        if (partOrConditions.size() > 0) {
            andConditions.add(partOrConditions);
        }
    }

    public static List < List< List<DataRequest.Entity > > > getHealthInsuranceLeadAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        LinkedList partOrConditions = new LinkedList();
        if (request.getDates().size() > 0) {
            Long startDate = request.getDates().get(0);
            Long endDate = -1l;
            if (request.getDates().size() > 1) {
                endDate = request.getDates().get(1);
            }

            if (endDate < startDate) {
                Long temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("date", startDate, ">="));
            if (endDate != -1) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(endDate);
                calendar.add(Calendar.MONTH, 1);
                partAndConditions.add(new DataRequest.Entity("date", calendar.getTimeInMillis(), "<="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getAgesRange().size() > 1) {
            long from = request.getAgesRange().get(0);
            long to = request.getAgesRange().get(1);

            if (to < from) {
                long tmp = to;
                to = from;
                from = tmp;
            }

            from = System.currentTimeMillis() - from * 365 * 24 * 60 * 60 * 1000;
            to = System.currentTimeMillis() - to * 365 * 24 * 60 * 60 * 1000;

            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("DOB_DATE", from, "<"));
            partAndConditions.add(new DataRequest.Entity("DOB_DATE", to, ">"));
            partOrConditions.add(partAndConditions);

            if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        }

        return andConditions;
    }


    public static List < List< List<DataRequest.Entity > > > getStudentAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        LinkedList partOrConditions = new LinkedList();
        if (request.getDates().size() > 0) {
            Long startDate = request.getDates().get(0);
            Long endDate = -1l;
            if (request.getDates().size() > 1) {
                endDate = request.getDates().get(1);
            }

            if (endDate < startDate) {
                Long temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("date", startDate, ">="));
            if (endDate != -1) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(endDate);
                calendar.add(Calendar.MONTH, 1);
                partAndConditions.add(new DataRequest.Entity("date", calendar.getTimeInMillis(), "<="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String category: request.getSources()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("source", category, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    public static List < List< List<DataRequest.Entity > > > getBusinessAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        if (request.getDataType() == DataTable.BUSINESS_DETAILED) {
            for (String sale : request.getSales()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();

                if (sale.contains("to")) {
                    long leftSale = getLeftSale(sale);
                    long rightSale = getRightSale(sale);

                    partAndConditions.add(new DataRequest.Entity("revenue", leftSale, ">="));
                    partAndConditions.add(new DataRequest.Entity("revenue", rightSale, "<"));
                } else {
                    partAndConditions.add(new DataRequest.Entity("revenue", 500000, "<"));
                }

                partOrConditions.add(partAndConditions);
            }

            if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        } else {
            for (String sale : request.getSales()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();
                partAndConditions.add(new DataRequest.Entity("ANNUAL_SALES", sale, "="));
                partOrConditions.add(partAndConditions);
            }
            if (partOrConditions.size() > 0) {
                andConditions.add(partOrConditions);
            }
        }

        partOrConditions = new LinkedList();
        if (request.getDataType() == DataTable.BUSINESS_DETAILED) {
            for (String count : request.getEmployeeCount()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();

                if (count.contains("to")) {
                    count = count.replace(",", "");
                    String[] countParts = count.split(" to ");

                    int leftCount = Integer.parseInt(countParts[0]);
                    int rightCount = Integer.parseInt(countParts[1]);

                    partAndConditions.add(new DataRequest.Entity("employeesOnSite", leftCount, ">="));
                    partAndConditions.add(new DataRequest.Entity("employeesOnSite", rightCount, "<="));
                } else {
                    partAndConditions.add(new DataRequest.Entity("employeesOnSite", 10000, ">"));
                }

                partOrConditions.add(partAndConditions);
            }

            if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        } else {
            for (String count : request.getEmployeeCount()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();
                partAndConditions.add(new DataRequest.Entity("EMPLOYEE", count, "="));
                partOrConditions.add(partAndConditions);
            }
            if (partOrConditions.size() > 0) {
                andConditions.add(partOrConditions);
            }
        }

        partOrConditions = new LinkedList();
        for (String companyType: request.getCompanyTypes()) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity(companyType, 1, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String creditScore: request.getCreditScores()) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("creditScore", creditScore, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getYearsRange().size() > 0) {
            partOrConditions.add(generateSimpleDateConditions("yearFounded", convertYears(request.getYearsRange())));
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String title: request.getTitles()) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("TITLE", title, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (Integer sic: request.getSics()) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("SIC", sic, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String section: request.getSections()) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("website", "%/" + section + "/%", "ILIKE"));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getDates().size() > 0) {
            Long startDate = request.getDates().get(0);
            Long endDate = -1l;
            if (request.getDates().size() > 1) {
                endDate = request.getDates().get(1);
            }

            if (endDate < startDate) {
                Long temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

            List< DataRequest.Entity > partAndConditions = new LinkedList();
            if (request.getDataType() == DataTable.EVERYDATA) {
                partAndConditions.add(new DataRequest.Entity("dob", startDate, ">="));
            }else{
                partAndConditions.add(new DataRequest.Entity("date", startDate, ">="));
            }
            if (endDate != -1) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(endDate);
                calendar.add(Calendar.MONTH, 1);
                if (request.getDataType() == DataTable.EVERYDATA) {
                    partAndConditions.add(new DataRequest.Entity("dob", calendar.getTimeInMillis(), "<="));
                    partAndConditions.add(new DataRequest.Entity("dob", -9999999999L, "<>"));
                }else{
                    partAndConditions.add(new DataRequest.Entity("date", calendar.getTimeInMillis(), "<="));
                }
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getFromSic() != null && request.getToSic() != null) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("SIC", request.getFromSic(), ">="));
            partAndConditions.add(new DataRequest.Entity("SIC", request.getToSic(), "<="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String title: request.getIndustries()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();

            if (request.getDataType() == DataTable.BUSINESS_DETAILED) {
                partAndConditions.add(new DataRequest.Entity("sic", Integer.parseInt(title), "="));
            } else {
                partAndConditions.add(new DataRequest.Entity("INDUSTRY", title, "="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }


        partOrConditions = new LinkedList();
        for (String title: request.getSources()) {
            List< DataRequest.Entity > partAndConditions = new LinkedList();
            if (request.getDataType() == DataTable.DIRECTORY || request.getDataType() == DataTable.PHILDIRECTORY) {
                partAndConditions.add(new DataRequest.Entity("www", title, "="));
            }else{
                partAndConditions.add(new DataRequest.Entity("source", title, "="));
            }
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    private static long getRightSale(String sale) {
        switch (sale) {
            case "$500, 000 to $1 million":
                return 1000000;
            case "$1 to 2.5 million":
                return 2500000;
            case "$2.5 to 5 million":
                return 5000000;
            case "$5 to 10 million":
                return 10000000;
            case "$10 to 20 million":
                return 20000000;
            case "$20 to 50 million":
                return 50000000;
            case "$50 to 100 million":
                return 100000000;
            case "$100 To 500 Million":
                return 500000000;
            case "$500 Million To $1 Billion":
                return 1000000000;
        }

        return 0;
    }

    private static long getLeftSale(String sale) {
        switch (sale) {
            case "$500, 000 to $1 million":
                return 500000;
            case "$1 to 2.5 million":
                return 1000000;
            case "$2.5 to 5 million":
                return 2500000;
            case "$5 to 10 million":
                return 5000000;
            case "$10 to 20 million":
                return 10000000;
            case "$20 to 50 million":
                return 20000000;
            case "$50 to 100 million":
                return 50000000;
            case "$100 To 500 Million":
                return 100000000;
            case "$500 Million To $1 Billion":
                return 500000000;
        }

        return 0;
    }

    public static List<List<List<DataRequest.Entity>>> getConsumers2AndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (String ageStr: request.getGenders()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PERSONGENDER", ageStr, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String carrier: request.getConsumerCarriers()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("carrier_name", carrier, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getAgesRange().size() > 1) {
            long from = request.getAgesRange().get(0);
            long to = request.getAgesRange().get(1);

            if (to < from) {
                long tmp = to;
                to = from;
                from = tmp;
            }

            from = System.currentTimeMillis() - from * 365 * 24 * 60 * 60 * 1000;
            to = System.currentTimeMillis() - to * 365 * 24 * 60 * 60 * 1000;

            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", from, "<"));
            partAndConditions.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", to, ">"));
            partOrConditions.add(partAndConditions);

            if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        }

        partOrConditions = new LinkedList();
        if (request.getDobs() != null) {
            if (request.getDobs().size() == 1) {
                try {
                    List<DataRequest.Entity> partAndConditions = new LinkedList();
                    long dob = dobFormat.parse(request.getDobs().get(0)).getTime();

                    partAndConditions.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", dob, "<"));
                    partAndConditions.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", dob - (24 * 60 * 60 * 1000 - 60 * 1000), ">"));
                    partOrConditions.add(partAndConditions);
                } catch (Exception e) { e.printStackTrace(); }
            } else if (request.getDobs().size() == 2) {
                try {
                    List<DataRequest.Entity> partAndConditions = new LinkedList();
                    long leftDob = dobFormat.parse(request.getDobs().get(0)).getTime();
                    long rightDob = dobFormat.parse(request.getDobs().get(1)).getTime();

                    if (leftDob > rightDob) {
                        long tmp = leftDob;
                        leftDob = rightDob;
                        rightDob = tmp;
                    }

                    partAndConditions.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", rightDob, "<"));
                    partAndConditions.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", leftDob - (24 * 60 * 60 * 1000 - 60 * 1000), ">"));
                    partOrConditions.add(partAndConditions);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getMaritalStatuses()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PERSONMARITALSTATUS", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getEthnicCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ETHNICCODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getLanguageCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("LANGUAGECODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getEthnicGroups()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ETHNICGROUP", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getReligionCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("RELIGIONCODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getHispanicCountryCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("HISPANICCOUNTRYCODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getProperties()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            if ("creditCardNewIssue".equals(item)) {
                partAndConditions.add(new DataRequest.Entity(item, "B", "="));
            } else {
                partAndConditions.add(new DataRequest.Entity(item, true, "="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getRating()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CREDITRATING", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getActiveLines()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NUMBEROFLINESOFCREDIT", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getRange()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CREDIT_RANGEOFNEWCREDIT", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getPropertyType()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("DWELLINGTYPE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getOwnerType()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("HOMEOWNERPROBABILITYMODEL", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getLengthOfResidence()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("LENGTHOFRESIDENCE", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getNumberOfPersonInLivingUnit()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NUMBEROFPERSONSINLIVINGUNIT", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getNumberOfChildren()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NUMBEROFCHILDREN", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getInferredHouseHoldRank()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("INFERREDHOUSEHOLDRANK", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getNumberOfAdults()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NUMBEROFADULTS", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getGenerationsInHouseHold()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("GENERATIONSINHOUSEHOLD", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getSewer()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("SEWER", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getWater()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("WATER", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getOccupationGroups()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("OCCUPATIONGROUP", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getPersonOccupations()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PERSONOCCUPATION", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getPersonEducations()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PERSONEDUCATION", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getBusinessOwners()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("BUSINESSOWNER", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getEstimatedIncome()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ESTIMATEDINCOMECODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getNetWorthes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NETWORTH", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getPropertyOwned()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("INVESTMENTESTIMATEDRESIDENTIALPROPERTIESOWNED", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getHomePurchasePrices()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("HOMEPURCHASEPRICE", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getHomePurchasedDates().size() > 0) {
            partOrConditions.add(generateDateConditions("HOMEPURCHASEDATE", request.getHomePurchasedDates()));
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getHomeYearBuilt().size() > 0) {
            partOrConditions.add(generateSimpleDateConditions("HOMEYEARBUILT", convertYears(request.getHomeYearBuilt())));
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getEstimatedCurrentHomeValueCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ESTIMATEDCURRENTHOMEVALUECODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getMortgageAmountInThousands()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("MORTGAGEAMOUNTINTHOUSANDS", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getMortgageLenderNames()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("MORTGAGELENDERNAME", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (containsRangeMortgageRate(request)) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();

            partAndConditions.add(new DataRequest.Entity("MORTGAGERATE", "    ", "!="));
            partAndConditions.add(new DataRequest.Entity("MORTGAGERATE", "", "!="));

            String fromValue = getRangeMortgageValue(request, "f");
            if (fromValue != null) {
                partAndConditions.add(new DataRequest.Entity("MORTGAGERATE", fromValue, ">="));
            }

            String toValue = getRangeMortgageValue(request, "t");
            if (toValue != null) {
                partAndConditions.add(new DataRequest.Entity("MORTGAGERATE", toValue, "<="));
            }

            partOrConditions.add(partAndConditions);
            andConditions.add(partOrConditions);
        } else {
            for (String item : request.getMortgageRate()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();
                partAndConditions.add(new DataRequest.Entity("MORTGAGERATE", item, "="));
                partOrConditions.add(partAndConditions);
            }
            if (partOrConditions.size() > 0) {
                andConditions.add(partOrConditions);
            }
        }

        partOrConditions = new LinkedList();
        if (containsMostRecentRangeMortgageRate(request)) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();

            partAndConditions.add(new DataRequest.Entity("MOSTRECENTMORTGAGEINTERESTRATE", "U", "!="));
            partAndConditions.add(new DataRequest.Entity("MOSTRECENTMORTGAGEINTERESTRATE", "000000", "!="));

            String fromValue = getMostRecentRangeMortgageValue(request, "f");
            if (fromValue != null) {
                partAndConditions.add(new DataRequest.Entity("MOSTRECENTMORTGAGEINTERESTRATE", fromValue, ">="));
            }

            String toValue = getMostRecentRangeMortgageValue(request, "t");
            if (toValue != null) {
                partAndConditions.add(new DataRequest.Entity("MOSTRECENTMORTGAGEINTERESTRATE", toValue, "<="));
            }

            partOrConditions.add(partAndConditions);
            andConditions.add(partOrConditions);
        } else {
            for (String item : request.getMostRecentMortgageInterestRates()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();
                partAndConditions.add(new DataRequest.Entity("MOSTRECENTMORTGAGEINTERESTRATE", formatMortgageValue(item), "="));
                partOrConditions.add(partAndConditions);
            }

            if (partOrConditions.size() > 0) {
                andConditions.add(partOrConditions);
            }
        }

        partOrConditions = new LinkedList();
        for (String item: request.getMortgageRateTypes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("MORTGAGERATETYPE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getMortgageLoanTypes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("MORTGAGELOANTYPE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getTransactionTypes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("TRANSACTIONTYPE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getDeedDatesOfRefinance().size() > 0) {
            partOrConditions.add(generateDateConditions("DEEDDATEOFREFINANCE", request.getDeedDatesOfRefinance()));
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getRefinanceAmountInThousands()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("REFINANCEAMOUNTINTHOUSANDS", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getRefinanceLeaderNames()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("REFINANCELENDERNAME", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getRefinanceLoanTypes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("REFINANCELOANTYPE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getRefinanceRateTypes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("REFINANCERATETYPE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getCensusMedianHomeValue()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CENSUSMEDIANHOMEVALUE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getCensusMedianHouseHoldIncome()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CENSUSMEDIANHOUSEHOLDINCOME", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getCraIncomeClassificationCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CRA_INCOMECLASSIFICATIONCODE", Integer.parseInt(item), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getPurchaseMortgageDates().size() > 0) {
            partOrConditions.add(generateSimpleDateConditions("PURCHASEMORTGAGEDATE", convertDates(request.getPurchaseMortgageDates())));
        }

        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getMostRecentLenderCodes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("MOSTRECENTLENDERCODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getPurchaseLenderNames()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PURCHASELENDERNAME", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getLoanToValues().size() > 0) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            if (request.getLoanToValues().size() == 1) {
                partAndConditions.add(new DataRequest.Entity(
                        "LOANTOVALUE", formatLoanValue(request.getLoanToValues().get(0)), "="));
            } else {
                partAndConditions.add(new DataRequest.Entity(
                        "LOANTOVALUE", formatLoanValue(request.getLoanToValues().get(0)), ">="));
                partAndConditions.add(new DataRequest.Entity(
                        "LOANTOVALUE", formatLoanValue(request.getLoanToValues().get(1)), "<="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getDpv()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("DPV_CODE", item, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (Integer item: request.getNumberOfSources()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();

            if (item == 10) {
                partAndConditions.add(new DataRequest.Entity("NUMBEROFSOURCES", item, ">="));
            } else {
                partAndConditions.add(new DataRequest.Entity("NUMBEROFSOURCES", item, "="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String item: request.getChildrenAgeGender()) {
            if (item.endsWith("FEMALE") || item.endsWith("MALE")) {
                continue;
            }

            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity(item, "Y", "="));

            if (request.getChildrenAgeGender().contains(item + "FEMALE")) {
                partAndConditions.add(new DataRequest.Entity(item + "FEMALE", "Y", "="));
            }

            if (request.getChildrenAgeGender().contains(item + "MALE")) {
                partAndConditions.add(new DataRequest.Entity(item + "MALE", "Y", "="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        for (String model: request.getModels()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("model", model, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String make: request.getMakes()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("make", make, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getYearsRange().size() > 0) {
            partOrConditions.add(generateSimpleDateConditions("year", convertYears(request.getYearsRange())));
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    private static boolean containsRangeMortgageRate(DataRequest request) {
        for (String rate: request.getMortgageRate()) {
            if (rate.startsWith("f") || rate.startsWith("t")) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsMostRecentRangeMortgageRate(DataRequest request) {
        for (String rate: request.getMostRecentMortgageInterestRates()) {
            if (rate.startsWith("f") || rate.startsWith("t")) {
                return true;
            }
        }

        return false;
    }

    private static String getRangeMortgageValue(DataRequest request, String prefix) {
        for (String rate: request.getMortgageRate()) {
            if (rate.startsWith(prefix)) {
                String value = rate.split(prefix)[1];

                while (value.length() < 4) {
                    value = "0" + value;
                }

                return value;
            }
        }

        return null;
    }

    private static String getMostRecentRangeMortgageValue(DataRequest request, String prefix) {
        for (String rate: request.getMostRecentMortgageInterestRates()) {
            if (rate.startsWith(prefix)) {
                String value = rate.split(prefix)[1];

                return formatMortgageValue(value);
            }
        }

        return null;
    }

    private static String formatLoanValue(String value) {
        if (value.length() == 1) {
            return "00" + value;
        } else if (value.length() == 2) {
            return "0" + value;
        } else {
            return value;
        }
    }

    private static String formatMortgageValue(String value) {
        if (value.length() < 6) {
            value = value + "00";
            while (value.length() < 6) {
                value = "0" + value;
            }
        }

        return value;
    }

    private static List<DataRequest.Entity> generateSimpleDateConditions(String fieldName,
                                                                         List<Long> dates) {
        List<DataRequest.Entity> partAndConditions = new LinkedList();
        if (dates.size() == 2) {
            if (dates.get(0) != -1 && dates.get(1) != -1) {
                partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(0), ">="));
                partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(1), "<="));
            } else if (dates.get(0) != -1) {
                if (dates.get(0) < 0) {
                    partAndConditions.add(new DataRequest.Entity(fieldName, -dates.get(0), "<="));
                } else {
                    partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(0), "="));
                }
            } else if (dates.get(1) != -1) {
                if (dates.get(1) < 0) {
                    partAndConditions.add(new DataRequest.Entity(fieldName, -dates.get(1), ">="));
                } else {
                    partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(1), "="));
                }
            }

            partAndConditions.add(new DataRequest.Entity(fieldName,0, ">"));
        }

        return partAndConditions;
    }

    private static List<Long> convertDates(List<Long> dates) {
        List<Long> result = new LinkedList();
        for (Long date: dates) {
            if (date == -1) {
                result.add(date);
            } else {
                try {
                    Long value = new Long(Integer.parseInt(dateFormat.format(new Date(date))));
                    if (Math.abs(date) % 1000 == 999) {
                        result.add(-value);
                    } else {
                        result.add(value);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        return result;
    }

    private static List<Long> convertYears(List<Long> dates) {
        List<Long> result = new LinkedList();
        for (Long date: dates) {
            if (date == -1) {
                result.add(date);
            } else {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(date);

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                if (Math.abs(date) % 1000 == 999) {
                    result.add(-new Long(calendar.get(Calendar.YEAR)));
                } else {
                    result.add(new Long(calendar.get(Calendar.YEAR)));
                }
            }
        }

        return result;
    }

    private static List<DataRequest.Entity> generateDateConditions(String fieldName,
                                                                   List<Long> dates) {
        List<DataRequest.Entity> partAndConditions = new LinkedList();

        if (dates.size() == 2) {
            if (dates.get(0) != -1 && dates.get(1) != -1) {
                partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(0), ">"));
                partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(1), "<"));
            } else if (dates.get(0) != -1) {
                if (Math.abs(dates.get(0)) % 1000 == 999) {
                    partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(0), "<"));
                } else {
                    partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(0), "="));
                }
            } else if (dates.get(1) != -1) {
                if (Math.abs(dates.get(1)) % 1000 == 999) {
                    partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(1), ">"));
                } else {
                    partAndConditions.add(new DataRequest.Entity(fieldName, dates.get(1), "="));
                }
            }

            partAndConditions.add(new DataRequest.Entity(fieldName, -4763791620335l, ">"));
        }

        return partAndConditions;
    }

    public static List < List< List<DataRequest.Entity > > > getFacebookAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();
        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (String carrierStr: request.getFacebookJobs()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("job", carrierStr, "="));

            if (partAndConditions.size() > 0) {
                partOrConditions.add(partAndConditions);
            }
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String gender: request.getFacebookGenders()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("gender", gender, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String status: request.getFacebookStatus()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("status", "%"+status+"%", "ilike"));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String lastName: request.getFacebookHLName()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("last_name", "%"+lastName+"%", "ilike"));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }
    public static List < List< List<DataRequest.Entity > > > getNewOptInAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();
        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (String carrierStr: request.getCarriersBrands()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("carrier", carrierStr, "="));

            if (partAndConditions.size() > 0) {
                partOrConditions.add(partAndConditions);
            }
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        for (String sourceStr: request.getDomainSources()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("clean_source", sourceStr, "="));
            if (partAndConditions.size() > 0) {
                partOrConditions.add(partAndConditions);
            }
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        if (request.getDatime().size() > 0) {
            Long startDate = request.getDatime().get(0);
            Long endDate = -1l;
            if (request.getDatime().size() > 1) {
                endDate = request.getDatime().get(1);
            }

            if (endDate < startDate) {
                Long temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

            List< DataRequest.Entity > partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("date", startDate, ">="));
            if (endDate != -1) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(endDate);
                calendar.add(Calendar.MONTH, 1);
                partAndConditions.add(new DataRequest.Entity("date", calendar.getTimeInMillis(), "<="));
            }

            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }
    public static List < List< List<DataRequest.Entity > > > getConsumersAndConditionsList(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        List<List<DataRequest.Entity>> partOrConditions = new LinkedList();
        for (String ageStr: request.getAges()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("AGE", Integer.parseInt(ageStr), "="));

            if (partAndConditions.size() > 0) {
                partOrConditions.add(partAndConditions);
            }
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String gender: request.getGenders()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("GENDER", gender, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String education: request.getEducations()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("EDUC", education, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String newWorth: request.getNetWorth()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NET_WORTH", newWorth.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String rating: request.getCreditRating()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CREDIT_RATING", rating.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String lineStr: request.getCreditLines()) {
            Integer line = Integer.parseInt(lineStr);
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CREDIT_LINES", line, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String lineStr: request.getCreditRanges()) {
            Integer line = Integer.parseInt(lineStr);
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("CREDIT_RANGE_NEW", line, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String group: request.getEthnicityGroups()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ETHNIC_GRP", group.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String language: request.getEthnicityLanguages()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ETHNIC_LANG", language.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String religion: request.getEthnicityReligions()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("ETHNIC_RELIG", religion.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String sizeStr: request.getHouseholdSize()) {
            Integer size = Integer.parseInt(sizeStr);
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("HH_SIZE", size, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String income: request.getHouseholdIncome()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("HH_INCOME", income.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String veteran: request.getResidenceVeteran()) {
            boolean value = false;
            if ("y".equals(veteran)) {
                value = true;
            }

            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("VET_IN_HH", value, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }


        partOrConditions = new LinkedList();
        for (String type: request.getResidenceType()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            if ("a".equalsIgnoreCase(type)) {
                partAndConditions.add(new DataRequest.Entity("DWELL_TYP", "S", "="));
            } else if ("d".equalsIgnoreCase(type)) {
                partAndConditions.add(new DataRequest.Entity("DWELL_TYP", "M", "="));
            } else if ("b".equalsIgnoreCase(type)) {
                partAndConditions.add(new DataRequest.Entity("APT", "", "!="));
            }

            partOrConditions.add(partAndConditions);

            partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("PROP_TYPE", type.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String ownership: request.getResidenceOwnership()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            //partAndConditions.add(new DataRequest.Entity("HOME_OWNR_SRC", ownership.toUpperCase(), "="));
            //partOrConditions.add(partAndConditions);

            partAndConditions.add(new DataRequest.Entity("HOME_OWNR", ownership.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String marital: request.getResidenceMarital()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("HH_MARITAL_STAT", marital.toUpperCase(), "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String childrenCountStr: request.getResidenceChildren()) {
            Integer childrenCount = Integer.parseInt(childrenCountStr);
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("NUM_KIDS", childrenCount, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        partOrConditions = new LinkedList();
        for (String lengthStr: request.getResidenceLength()) {
            Integer length = Integer.parseInt(lengthStr);
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            if (length == 14) {
                partAndConditions.add(new DataRequest.Entity("LOR", 15, "<"));
            } else {
                partAndConditions.add(new DataRequest.Entity("LOR", 15, ">="));

            }
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        List<String> interestsList = request.getInterests();
        partOrConditions = new LinkedList();
        if (interestsList.contains("travel")) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("INT_TRAV_CASINO", true, "="));
            partOrConditions.add(partAndConditions);
        }

        if (interestsList.contains("gambles")) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("INT_TRAV_GENL", true, "="));
            partOrConditions.add(partAndConditions);
        }
        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    public static List<List<List<DataRequest.Entity>>> initAndConditions(DataRequest request) {
        List<List<List<DataRequest.Entity>>> andConditions = new LinkedList();

        if (request.getOmittedCities() != null ) {
            for (String city : request.getOmittedCities()) {
                List<List<DataRequest.Entity>> orConditions = new LinkedList();
                List<DataRequest.Entity> mainConditions = new LinkedList();

                if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
                    mainConditions.add(new DataRequest.Entity("CITYNAME", city.split("_")[0], "!="));
                } else {
                    mainConditions.add(new DataRequest.Entity("CITY", city.split("_")[0], "!="));
                }

                orConditions.add(mainConditions);
                andConditions.add(orConditions);
            }

            for (String city : request.getOmittedCities()) {
                List<List<DataRequest.Entity>> orConditions = new LinkedList();
                List<DataRequest.Entity> mainConditions = new LinkedList();

                if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
                    mainConditions.add(new DataRequest.Entity("CITYNAME", city.split("_")[0].toUpperCase(), "!="));
                } else {
                    mainConditions.add(new DataRequest.Entity("CITY", city.split("_")[0].toUpperCase(), "!="));
                }

                orConditions.add(mainConditions);
                andConditions.add(orConditions);
            }
        }

        if (request.getOmittedStates() != null) {
            for (String state : request.getOmittedStates()) {
                List<List<DataRequest.Entity>> orConditions = new LinkedList();
                List<DataRequest.Entity> mainConditions = new LinkedList();

                mainConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(state), "!="));

                orConditions.add(mainConditions);
                andConditions.add(orConditions);
            }
        }

        if (request.getOmittedZipCodes() != null) {
            for (Integer zip: request.getOmittedZipCodes()) {
                List<List<DataRequest.Entity>> orConditions = new LinkedList();
                List<DataRequest.Entity> mainConditions = new LinkedList();

                if (request.getDataType() == DataTable.CONSUMERS2 ||
                        request.getDataType() == DataTable.CONSUMERS3 ||
                        request.getDataType() == DataTable.INSTAGRAM ||
                        request.getDataType() == DataTable.INSTAGRAM2020 ||
                        request.getDataType() == DataTable.STUDENT){
                    mainConditions.add(new DataRequest.Entity("ZIPCODE", zip, "!="));
                } else {
                    mainConditions.add(new DataRequest.Entity("ZIP_CODE", zip, "!="));
                }

                orConditions.add(mainConditions);
                andConditions.add(orConditions);
            }
        }

        if (request.getOmittedAreaCodes() != null) {
            for (Integer areaCode: request.getOmittedAreaCodes()) {
                List<List<DataRequest.Entity>> orConditions = new LinkedList();
                List<DataRequest.Entity> mainConditions = new LinkedList();

                if (request.getDataType() == DataTable.CONSUMERS2 ||
                        request.getDataType() == DataTable.CONSUMERS3 ||
                        request.getDataType() == DataTable.INSTAGRAM ||
                        request.getDataType() == DataTable.INSTAGRAM2020 ||
                        request.getDataType() == DataTable.OPTIN ||
                        request.getDataType() == DataTable.AUTO ||
                        request.getDataType() == DataTable.BLACKLIST ||
                        request.getDataType() == DataTable.CALLLEADS ||
                        request.getDataType() == DataTable.STUDENT) {
                    mainConditions.add(new DataRequest.Entity("AREACODE", areaCode, "!="));
                } else {
                    mainConditions.add(new DataRequest.Entity("AREA_CODE", areaCode, "!="));
                }

                orConditions.add(mainConditions);
                andConditions.add(orConditions);
            }
        }

        if (request.getPhoneTypes() != null) {
            LinkedList partOrConditions = new LinkedList();
            for (Integer phoneType : request.getPhoneTypes()) {
                List<DataRequest.Entity> partAndConditions = new LinkedList();
                partAndConditions.add(new DataRequest.Entity("phoneType", phoneType, "="));
                partOrConditions.add(partAndConditions);
            }

            if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }
        }

        LinkedList partOrConditions = new LinkedList();
        for (String phone: request.getCarriersPhones()) {
            List<DataRequest.Entity> partAndConditions = new LinkedList();
            partAndConditions.add(new DataRequest.Entity("phone", phone + "%", "LIKE"));
            partOrConditions.add(partAndConditions);
        }

        if (partOrConditions.size() > 0) { andConditions.add(partOrConditions); }

        return andConditions;
    }

    public static List< List< DataRequest.Entity > > getOrConditionList(DataRequest request) {
        List< List< DataRequest.Entity > > orConditions = new LinkedList();

        for (String state: request.getStates()) {
            List< DataRequest.Entity > andConditions = new LinkedList();
            andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(state), "="));
            orConditions.add(andConditions);
        }

        for (String timeZone: request.getTimeZones()) {
            String[] states = STATES_BY_TIMEZONE.get(timeZone);
            for (String state: states) {
                List<DataRequest.Entity> andConditions = new LinkedList();
                andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(state), "="));
                orConditions.add(andConditions);
            }
        }

        for (String county: request.getCounties()) {
            List< DataRequest.Entity > andConditions = new LinkedList();

            if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
                andConditions.add(new DataRequest.Entity("COUNTYNAME", county.split("_")[0], "="));

            }else{
                andConditions.add(new DataRequest.Entity("COUNTY", county.split("_")[0], "="));
            }

            andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(county.split("_")[ 1 ]), "="));
            orConditions.add(andConditions);
        }

        for (String county: request.getCounties()) {
            List< DataRequest.Entity > andConditions = new LinkedList();

            if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
                andConditions.add(new DataRequest.Entity("COUNTYNAME", county.split("_")[0].toUpperCase(), "="));
            } else {
                andConditions.add(new DataRequest.Entity("COUNTY", county.split("_")[0].toUpperCase(), "="));
            }

            andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(county.split("_")[ 1 ]), "="));
            orConditions.add(andConditions);
        }

        for (String city: request.getCities()) {
            List< DataRequest.Entity > andConditions = new LinkedList();

            if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
                andConditions.add(new DataRequest.Entity("CITYNAME", city.split("_")[0], "="));
            } else {
                andConditions.add(new DataRequest.Entity("CITY", city.split("_")[0], "="));
            }

            andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(city.split("_")[ 1 ]), "="));
            orConditions.add(andConditions);
        }

        for (String city: request.getCities()) {
            List< DataRequest.Entity > andConditions = new LinkedList();

            if (request.getDataType() == DataTable.CONSUMERS2 || request.getDataType() == DataTable.CONSUMERS3) {
                andConditions.add(new DataRequest.Entity("CITYNAME", city.split("_")[0].toUpperCase(), "="));
            } else {
                andConditions.add(new DataRequest.Entity("CITY", city.split("_")[0].toUpperCase(), "="));
            }

            andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(city.split("_")[1]), "="));
            orConditions.add(andConditions);
        }

        for (String zip: request.getZipCodes()) {
            List< DataRequest.Entity > andConditions = new LinkedList();

            String key = "ZIP_CODE";
            if (request.getDataType() == DataTable.CONSUMERS2 ||
                    request.getDataType() == DataTable.CONSUMERS3 ||
                    request.getDataType() == DataTable.INSTAGRAM ||
                    request.getDataType() == DataTable.INSTAGRAM2020 ||
                    request.getDataType() == DataTable.LINKEDIN ||
                    request.getDataType() == DataTable.STUDENT) {
                key = "ZIPCODE";
            }
            else if(request.getDataType() == DataTable.NEWOPTIN){
                key = "zip4";
            }

            zip = zip.toLowerCase();
            if (zip.contains("x")) {
                andConditions.add(new DataRequest.Entity(key, parseInt(zip.replaceAll("x", "0")), ">="));
                andConditions.add(new DataRequest.Entity(key, parseInt(zip.replaceAll("x", "9")), "<="));
            } else {
                andConditions.add(new DataRequest.Entity(key, parseInt(zip), "="));
            }

            orConditions.add(andConditions);
        }

        for (Integer areaCode: request.getAreaCodes()) {
            List< DataRequest.Entity > andConditions = new LinkedList();

            if (request.getDataType() == DataTable.CONSUMERS2 ||
                    request.getDataType() == DataTable.CONSUMERS3 ||
                    request.getDataType() == DataTable.INSTAGRAM ||
                    request.getDataType() == DataTable.INSTAGRAM2020 ||
                    request.getDataType() == DataTable.OPTIN ||
                    request.getDataType() == DataTable.AUTO ||
                    request.getDataType() == DataTable.BLACKLIST ||
                    request.getDataType() == DataTable.CALLLEADS ||
                    request.getDataType() == DataTable.STUDENT) {
                andConditions.add(new DataRequest.Entity("AREACODE", areaCode, "="));
                String stateShort = Application.AREA_CODE_TO_STATE_MAP.get(areaCode);
                andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(stateShort), "="));
            } else {
                andConditions.add(new DataRequest.Entity("AREA_CODE", areaCode, "="));
                String stateShort = Application.AREA_CODE_TO_STATE_MAP.get(areaCode);
                andConditions.add(new DataRequest.Entity("ST_CODE", getStateCode(stateShort), "="));
            }

            orConditions.add(andConditions);
        }

        return orConditions;
    }

    private static Integer getStateCode(String state) {
        for (int i = 0; i < Application.STATE_CODES.length; i++) {
            if (state.equals(Application.STATE_CODES[ i ])) {
                return i;
            }
        }
        return null;
    }
}

