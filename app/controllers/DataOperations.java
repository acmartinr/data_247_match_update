package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.DataRequest;
import models.DataTable;
import services.db.entity.MatchedResult;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.db.dao.DataDAO;
import services.db.dao.ListDAO;
import services.db.dao.UserDAO;
import services.db.entity.*;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class DataOperations extends Controller {

    private static final String NEW_LINE = "\n";
    private static final String SEPARATOR = ",";

    private static final Integer MINUS_ONE = -1;
    private static final Integer ZERO = 0;

    private DataDAO dataDAO;
    private ListDAO listDAO;
    private UserDAO userDAO;
    private Application application;

    @Inject
    public DataOperations(DataDAO dataDAO,
                          ListDAO listDAO,
                          UserDAO userDAO,
                          Application application) {
        this.dataDAO = dataDAO;
        this.listDAO = listDAO;
        this.userDAO = userDAO;

        this.application = application;
    }

    private static final Set<String> WHO_IS_IGNORED_FIELDS = new HashSet() {{
        add("id" );
        add("mock" );
        add("date" );
        add("hash" );
        add("ST_CODE" );
        add("country" );
        add("strDate" );
        add("ZIP" );
    }};

    public Result updateStates(String tableName) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();
        updateStatesForTable(dataDAO.getTableByName(tableName));

        return ok();
    }

    private void updateStatesForTable(DataTable table) {
        long maxId;

        try {
            maxId = dataDAO.getTableMaxId(table.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        long batch = 100000;
        AtomicLong atomicId = new AtomicLong(0);
        final AtomicInteger finished = new AtomicInteger(0);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                long id = atomicId.getAndAdd(batch);
                while (id < maxId + batch) {
                    dataDAO.updateStates(table.getName(), id, id + batch);

                    id = atomicId.getAndAdd(batch);
                    Logger.info("{}: {}",
                            table.getName(),
                            id);
                }

                finished.incrementAndGet();
            }).start();
        }

        while (finished.get() != 6) {
            try { Thread.sleep(5000); }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public Result updateMakesModels() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByPhoneTypeAndTypeAndNotRemoved(DataTable.COMMON, DataTable.AUTO);
        Map<String, Set<String>> makeToModelsMap = new HashMap();

        for (DataTable table: tables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(table.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long batch = 1000;
            long id = 0;

            while (id < maxId + batch) {
                List<Auto> autoRecords = dataDAO.getAutoListWithLimitAndOffset(table.getName(), id, id + batch);

                id = id + batch;
                Logger.info("Updating makes and models. {}: {}/{}",
                        table.getName(),
                        id,
                        maxId);

                for (Auto auto: autoRecords) {
                    if (auto != null && auto.getMODEL() != null && auto.getMODEL().length() > 0 &&
                        auto.getMAKE() != null && auto.getMAKE().length() > 0 && !"0".equals(auto.getMAKE())) {
                        if (!makeToModelsMap.containsKey(auto.getMAKE())) {
                            makeToModelsMap.put(auto.getMAKE(), new HashSet());
                        }

                        makeToModelsMap.get(auto.getMAKE()).add(auto.getMODEL());
                    }
                }
            }

            for (Map.Entry<String, Set<String>> entry: makeToModelsMap.entrySet()) {
                if (entry.getValue().size() > 100) {
                    dataDAO.insertAutoMake(entry.getKey());
                    dataDAO.insertAutoModel(entry.getKey(), entry.getValue());

                    Logger.info("Inserting makes and models. {} -> {}",
                            entry.getKey(),
                            entry.getValue().size());
                }
            }
        }

        return ok();
    }

    public Result updateSourceCriteria() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();
        List<DataTable> tables = dataDAO.getTablesByType(DataTable.OPTIN);
        for (DataTable table: tables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(table.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long batch = 10000;
            long id = 0;

            while (id < maxId + batch) {
                dataDAO.updateSourceCriteria(table.getName(), id, id + batch);

                id = id + batch;
                Logger.info("Updating source criteria. {}: {}",
                        table.getName(),
                        id);
            }
        }

        return ok();
    }

    public Result updateCountiesByTableName(String tableName) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();
        DataTable table = dataDAO.getTableByName(tableName);

        long maxId;

        try {
            maxId = dataDAO.getTableMaxId(table.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        }

        long batch = 10000;
        long id = 0;

        while (id < maxId + batch) {
            dataDAO.updateCountiesForIdRange(table.getName(), "state", id, id + batch);

            id = id + batch;
            Logger.info("Updating counties. {}: {}",
                    table.getName(),
                    id);
        }

        return ok();
    }

    public Result updateCountiesByType(int type) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();
        List<DataTable> tables = dataDAO.getTablesByType(type);
        for (DataTable table: tables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(table.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long batch = 10000;
            long id = 0;

            while (id < maxId + batch) {
                dataDAO.updateCountiesForIdRange(table.getName(), "state", id, id + batch);

                id = id + batch;
                Logger.info("Updating counties. {}: {}",
                        table.getName(),
                        id);
            }
        }

        return ok();
    }

    public Result updateInstagramStates() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();
        List<DataTable> tables = dataDAO.getTablesByType(DataTable.INSTAGRAM);
        for (DataTable table: tables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(table.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long batch = 1000;
            long id = 0;

            while (id < maxId + batch) {
                List<Instagram> instagramRecords = dataDAO.getInstagramRecords(table.getName(), id, id + batch);

                Iterator<Instagram> iterator = instagramRecords.iterator();
                while (iterator.hasNext()) {
                    Instagram instagramRecord = iterator.next();
                    if (instagramRecord.getSt_code() != null) {
                        iterator.remove();
                    } else if (instagramRecord.getAreaCode() != null) {
                        String state = Application.AREA_CODE_TO_STATE_MAP.get(instagramRecord.getAreaCode());
                        if (state != null) {
                            instagramRecord.setState(state);
                        }
                    }
                }
                if (instagramRecords.size() > 0) {
                    dataDAO.updateInstagramStateInfo(table.getName(), instagramRecords);
                }

                id = id + batch;
                Logger.info("{}: {}",
                        table.getName(),
                        id);
            }
        }

        return ok();
    }

    public Result generatePhoneListByTableName(String tableName) throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = new LinkedList();
        tables.add(dataDAO.getTableByName(tableName));

        generateUniquePhoneListForTables(tables);
        return ok();
    }

    public Result generatePhoneListByType(int type) throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> allTables = dataDAO.getTablesByPhoneTypeAndTypeAndNotRemoved(DataTable.COMMON, type);
        generateUniquePhoneListForTables(allTables);
        return ok();
    }

    public Result removeOldLists(long date) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<ListEntity> lists = listDAO.getOldLists(date);
        for (ListEntity list: lists) {
            List<ListEntity> purchasedLists = listDAO.findPurchasedLists(list.getName());
            long lastPurchasedDate = getLastPurchasedDate(purchasedLists);
            if (purchasedLists.size() == 0 || lastPurchasedDate < date) {
                listDAO.deleteListById(list.getId());

                Logger.info("Removing old lists: {} was removed", list.getName());
            }
        }

        return ok();
    }

    private long getLastPurchasedDate(List<ListEntity> purchasedLists) {
        long date = 0;

        for (ListEntity list: purchasedLists) {
            if (list.getDate() > date) {
                date = list.getDate();
            }
        }

        return date;
    }

    public Result filterClosedRecords() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> allTables = dataDAO.getTablesByType(DataTable.DIRECTORY);
        for (DataTable table: allTables) {
            filterClosedRecordsInTable(table, allTables.indexOf(table), allTables.size());
        }

        return ok();
    }

    private void filterClosedRecordsInTable(DataTable table, int tableIndex, int tablesCount) {
        long maxId;

        try {
            maxId = dataDAO.getTableMaxId(table.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        long batch = 100000;
        long id = 1;

        while (id < maxId + batch) {
            dataDAO.removeRecordsWithKeyWord(table.getName(), "closed", new String[]{"COMPANY_NAME"}, id, id + batch);

            id = id + batch;
            Logger.info("{}, ({}/{}), {}",
                    table.getName(),
                    tableIndex + 1,
                    tablesCount,
                    id - 1);
        }
    }

    public Result filterPoliceRecords() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> allTables = dataDAO.getAllTables();
        for (DataTable table: allTables) {
            filterRecordsByKeywordInTable(table, "police", allTables.indexOf(table), allTables.size());
        }

        return ok();
    }

    public Result filterNullRecords() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> allTables = dataDAO.getAllTables();
        for (DataTable table: allTables) {
            filterRecordsByKeywordInTable(table, "NULL", allTables.indexOf(table), allTables.size());
        }

        return ok();
    }

    private void filterRecordsByKeywordInTable(DataTable table, String keyword, int tableIndex, int tablesCount) {
        long maxId;

        try {
            maxId = dataDAO.getTableMaxId(table.getName());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        long batch = 100000;
        long id = 1;

        while (id < maxId + batch) {
            if (table.getType() == DataTable.CONSUMERS) {
                dataDAO.removeRecordsWithKeyWord(table.getName(), keyword, Data.CONSUMER_KEYWORD_COLUMNS, id, id + batch);
            } else if (table.getType() == DataTable.BUSINESS || table.getType() == DataTable.BUSINESS2) {
                dataDAO.removeRecordsWithKeyWord(table.getName(), keyword, Data.BUSINESS_KEYWORD_COLUMNS, id, id + batch);
            } else if (table.getType() == DataTable.DIRECTORY){
                dataDAO.removeRecordsWithKeyWord(table.getName(), keyword, Data.DIRECTORY_KEYWORD_COLUMNS, id, id + batch);
            } else if (table.getType() == DataTable.CRAIGSLIST) {
                dataDAO.removeRecordsWithKeyWord(table.getName(), keyword, Data.CRAIGSLIST_KEYWORD_COLUMNS, id, id + batch);
            } else if (table.getType() == DataTable.WHOIS) {
                dataDAO.removeRecordsWithKeyWord(table.getName(), keyword, Data.WHOIS_KEYWORD_COLUMNS, id, id + batch);
            } else if (table.getType() == DataTable.SEARCH_ENGINE) {
                dataDAO.removeRecordsWithKeyWord(table.getName(), keyword, Data.SEARCH_ENGINE_KEYWORD_COLUMNS, id, id + batch);
            }

            id = id + batch;
            Logger.info("{}, ({}/{}), {}",
                    table.getName(),
                    tableIndex + 1,
                    tablesCount,
                    id - 1);
        }
    }

    public Result generateBusinessMatchingFile() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        DataTable optInTable = dataDAO.getTableByName("OptInOriginal");
        DataTable consumersTable = dataDAO.getTableByName("ConsumersOriginal");

        ListEntity list = createOptInMatchedList(optInTable);

        long maxId = dataDAO.getTableMaxId(consumersTable.getName());
        long batch = 200;
        final AtomicLong id = new AtomicLong(0);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                long currentId = id.getAndAdd(batch);
                while (currentId < maxId + batch) {
                    List<Consumer2> consumers = dataDAO.getConsumers2ForOptInListByIdRange(consumersTable.getName(), currentId, currentId + batch);

                    List<List<DataRequest.Entity>> orConditions = new LinkedList();
                    for (Consumer2 consumer : consumers) {
                        List<DataRequest.Entity> andCondition = new LinkedList();
                        andCondition.add(new DataRequest.Entity("zipCode", consumer.getZIPCODE(), "="));
                        andCondition.add(new DataRequest.Entity("firstName", capitalize(consumer.getPERSONFIRSTNAME()), "="));
                        andCondition.add(new DataRequest.Entity("lastName", capitalize(consumer.getPERSONLASTNAME()), "="));
                        andCondition.add(new DataRequest.Entity("address", consumer.getPRIMARYADDRESS() + "%", "ILIKE"));

                        orConditions.add(andCondition);
                    }

                    dataDAO.insertMatchedRecords(orConditions, "", list.getTableName(), list.getId());
                    Logger.info("Generating Opt In matched list: {}/{}", currentId + batch, maxId);

                    currentId = id.getAndAdd(batch);
                }
            }).start();
        }

        return ok();
    }

    public Result generateOptInMatchingFile() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        DataTable optInTable = dataDAO.getTableByName("OptInOriginal");
        DataTable consumersTable = dataDAO.getTableByName("ConsumersOriginal");

        ListEntity list = createOptInMatchedList(optInTable);

        long maxId = dataDAO.getTableMaxId(consumersTable.getName());
        long batch = 200;
        final AtomicLong id = new AtomicLong(0);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                long currentId = id.getAndAdd(batch);
                while (currentId < maxId + batch) {
                    List<Consumer2> consumers = dataDAO.getConsumers2ForOptInListByIdRange(consumersTable.getName(), currentId, currentId + batch);

                    List<List<DataRequest.Entity>> orConditions = new LinkedList();
                    for (Consumer2 consumer : consumers) {
                        List<DataRequest.Entity> andCondition = new LinkedList();
                        andCondition.add(new DataRequest.Entity("zipCode", consumer.getZIPCODE(), "="));
                        andCondition.add(new DataRequest.Entity("firstName", capitalize(consumer.getPERSONFIRSTNAME()), "="));
                        andCondition.add(new DataRequest.Entity("lastName", capitalize(consumer.getPERSONLASTNAME()), "="));
                        andCondition.add(new DataRequest.Entity("address", consumer.getPRIMARYADDRESS() + "%", "ILIKE"));

                        orConditions.add(andCondition);
                    }

                    dataDAO.insertMatchedRecords(orConditions, "", list.getTableName(), list.getId());
                    Logger.info("Generating Opt In matched list: {}/{}", currentId + batch, maxId);

                    currentId = id.getAndAdd(batch);
                }
            }).start();
        }

        return ok();
    }

    private String capitalize(String value) {
        if (value != null && value.length() > 2) {
            String first = value.substring(0, 1);
            String tail = value.substring(1);

            return first.toUpperCase() + tail.toLowerCase().trim();
        } else {
            return value;
        }
    }

    private ListEntity createOptInMatchedList(DataTable optInTable) {
        ListEntity list = new ListEntity(
                "Opt In - Consumers matched list",
                userDAO.findUserByName("serfeo").getId(),
                100,
                System.currentTimeMillis());

        list.setTableName(optInTable.getName());
        list.setType(DataTable.OPTIN);
        list.setRequest("{}");

        listDAO.saveList(list);
        listDAO.updateMatchedById(list.getId(), true);

        return list;
    }

    public Result generateCustomMatchingFile(int stateCode) throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> consumersTables = dataDAO.getTablesByPhoneTypeAndTypeAndNotRemoved(DataTable.COMMON, DataTable.CONSUMERS);
        List<DataTable> businessTables = dataDAO.getTablesByPhoneTypeAndTypeAndNotRemoved(DataTable.COMMON, DataTable.BUSINESS);

        if (consumersTables.size() > 0 && businessTables.size() > 0) {
            generateConsumerBusinessMatchingFile(
                    getNonArchivedTable(consumersTables),
                    getNonArchivedTable(businessTables),
                    stateCode);
        }

        return ok();
    }

    private DataTable getNonArchivedTable(List<DataTable> tables) {
        for (DataTable dataTable: tables) {
            if (!dataTable.getName().contains("archived")) {
                return dataTable;
            }
        }

        return null;
    }

    private void generateConsumerBusinessMatchingFile(DataTable consumersTable,
                                                      DataTable businessTable,
                                                      int stateCode) throws Exception {
        long offset = 0;
        long limit = 10000;

        PrintWriter printWriter = new PrintWriter(new File("/home/makemydata/workspace/consumer_business_matched_la_ca.csv"));
        long maxId = dataDAO.getTableMaxId(businessTable.getName());

        Field[] fields = MatchedResult.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[ i ];
            Lists.writeColumnName(printWriter, field, null);
            if (i + 1 < fields.length) {
                printWriter.append(SEPARATOR);
            }
        }

        printWriter.append(NEW_LINE);
        List<MatchedResult> results = dataDAO.getMatchedConsumerBusinessData(consumersTable, businessTable, offset, offset + limit, stateCode);
        while (true) {
            for (int i = 0; i < results.size(); i++) {
                MatchedResult result = results.get(i);
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    Object value = new PropertyDescriptor(field.getName(), MatchedResult.class).
                            getReadMethod().invoke(result);
                    Lists.writeFieldValue(printWriter, field, value);
                    if (j + 1 < fields.length) {
                        printWriter.append(SEPARATOR);
                    }
                }

                printWriter.append(NEW_LINE);
            }

            Logger.info("Consumers table name: {}, business table name: {}, handled records: {}",
                    consumersTable.getName(),
                    businessTable.getName(),
                    offset);

            printWriter.flush();
            offset = offset + limit;
            if (offset > maxId) {
                break;
            }

            results = dataDAO.getMatchedConsumerBusinessData(consumersTable, businessTable, offset, offset + limit, stateCode);
        }

        printWriter.close();
    }

    public Result generatePhoneList() throws Exception {
        if (true) return notFound();

        List<DataTable> allTables = dataDAO.getTablesByPhoneTypeAndNotRemoved(DataTable.COMMON);
        generateUniquePhoneListForTables(allTables);
        return ok();
    }

    private void generateUniquePhoneListForTables(List<DataTable> allTables) throws Exception {
        PrintWriter printWriter = new PrintWriter(new File("/home/makemydata/workspace/phones.csv"));

        Map<Integer, Set<Long>> phones = new HashMap();

        long batch = 100000;
        for (int i = 10; i < 100; i++) {
            phones.put(i, new HashSet());
        }

        for (DataTable dataTable : allTables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(dataTable.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long id = 0;
            while (id < (maxId - batch)) {
                List<String> dbPhones = dataDAO.getPhoneByTableNameAndIdRange(dataTable.getName(), id, id + batch);
                for (String phone : dbPhones) {
                    if (phone != null && phone.length() > 2) {
                        try {
                            int key = (int)parseLong(phone.substring(0, 2));
                            if (key != -1 && phones.containsKey(key)) {
                                phones.get(key).add(parseLong(phone));
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }

                id = id + batch;
                Logger.info("Table name: {}, table index: {}, handled records: {}",
                        dataTable.getName(),
                        allTables.indexOf(dataTable),
                        id);
            }
        }

        for (int i = 10; i < 100; i++) {
            Set<Long> phonesSet = phones.get(i);
            for (Long phone: phonesSet) {
                printWriter.append(String.valueOf(phone));
                printWriter.append(NEW_LINE);
            }
        }

        printWriter.flush();
        printWriter.close();
    }

    public Result updateDataHash() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> allTables = dataDAO.getNonRemovedTables();
        for (DataTable table: allTables) {
            int tableIndex = allTables.indexOf(table);

            long maxId = dataDAO.getTableMaxId(table.getName());
            long batch = 5000;
            long id = 1;

            while (id < maxId + batch) {
                dataDAO.updateTableHash(table.getName(), id, id + batch);
                id = id + batch;

                Logger.info("{}, ({}/{}), {}",
                        table.getName(),
                        tableIndex + 1,
                        allTables.size(),
                        id);
            }
        }

        List<ListEntity> lists = listDAO.getAllLists();
        for (ListEntity listEntity: lists) {
            dataDAO.updateListItemsHash(listEntity.getId(), listEntity.getTableName());

            int listIndex = lists.indexOf(listEntity);
            Logger.info("{}, ({}/{})",
                    listEntity.getName(),
                    listIndex + 1,
                    allTables.size());
        }

        return ok();
    }

    public Result updateDNCByDataSourceType(int type) throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        Map<Long, Set<Long>> cleanPhonesMap = generateCleanPhonesMap();

        List<DataTable> allTables = dataDAO.getTablesByType(type);
        updateDNCInTables(allTables, cleanPhonesMap);

        cleanPhonesMap.clear();
        return ok();
    }

    public Result updateHotFrogDNC() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        Map<Long, Set<Long>> cleanPhonesMap = generateCleanPhonesMap();

        List<DataTable> allTables = dataDAO.getTablesByType(DataTable.DIRECTORY);
        updateHotFrogDNCInTables(allTables, cleanPhonesMap);

        cleanPhonesMap.clear();
        return ok();
    }

    private void updateHotFrogDNCInTables(List<DataTable> tables, Map<Long, Set<Long>> cleanPhonesMap) {
        long batch = 1000;

        for (DataTable dataTable: tables) {
            long maxId = getTableMaxId(dataTable);
            if (maxId == -1) {
                continue;
            }

            long id = 0;
            List<PhoneEntity> phoneEntities = dataDAO.getHotFrogPhoneEntityList(dataTable.getName(), id, id + batch);
            while (id < (maxId + batch)) {
                Iterator<PhoneEntity> it = phoneEntities.iterator();
                List<PhoneEntity> dncEntities = filterPhoneEntities(it, cleanPhonesMap);
                updatePhoneDNC(dataTable.getName(), phoneEntities, dncEntities);

                id = id + batch;
                Logger.info("Table name: {}, table index: {}, handled records: {}",
                        dataTable.getName(),
                        tables.indexOf(dataTable),
                        id);

                phoneEntities = dataDAO.getHotFrogPhoneEntityList(dataTable.getName(), id, id + batch);
            }
        }
    }

    public Result filterSearchEngineRecords() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByType(DataTable.SEARCH_ENGINE);
        filterSearchEngineRecordsInTables(tables);

        return ok();
    }

    private void filterSearchEngineRecordsInTables(List<DataTable> tables) {
        for (DataTable dataTable: tables) {
            long limit = 50000;
            long offset = 0;

            long previousPhone = 0l;
            int sequenceSize = 0;

            List<Integer> tempRecordsForDelete = new LinkedList();
            List<Long> tempPhonesForDelete = new LinkedList();

            boolean last = false;
            List<Integer> recordsForDelete = new LinkedList();
            List<Long> phonesForDelete = new LinkedList();

            List<SearchEngine> records = dataDAO.getSearchEngineListOrderedByPhone(dataTable.getName(), offset, limit);
            offset = offset + records.size();

            Iterator<SearchEngine> it = records.iterator();
            while (it.hasNext()) {
                SearchEngine record = it.next();
                it.remove();

                long recordPhone = parseLong(record.getPhone());
                if (previousPhone == recordPhone - 1) {
                    sequenceSize = sequenceSize + 1;
                    tempRecordsForDelete.add(record.getId());
                    tempPhonesForDelete.add(recordPhone);
                } else if (previousPhone == recordPhone) {
                    tempRecordsForDelete.add(record.getId());
                    tempPhonesForDelete.add(recordPhone);
                } else {
                    if (sequenceSize > 2) {
                        recordsForDelete.addAll(tempRecordsForDelete);
                        phonesForDelete.addAll(tempPhonesForDelete);
                    }

                    tempRecordsForDelete.clear();
                    tempPhonesForDelete.clear();

                    sequenceSize = 0;
                }

                previousPhone = recordPhone;
                if (!last && records.size() == 0) {
                    records = dataDAO.getSearchEngineListOrderedByPhone(dataTable.getName(), offset, limit);
                    it = records.iterator();

                    offset = offset + records.size();
                    last = records.size() == 0;

                    Logger.info("Table name: {}, handled records: {}",
                            dataTable.getName(),
                            offset);
                }

                if (recordsForDelete.size() > 500) {
                    dataDAO.bulkDeleteSearchEngineRecords(dataTable.getName(), recordsForDelete);
                    offset = offset - recordsForDelete.size();
                    recordsForDelete.clear();

                    for (Long phone: phonesForDelete) {
                        Logger.info("Table name: {}, phone: {}",
                                dataTable.getName(),
                                phone);
                    }
                    phonesForDelete.clear();
                }
            }

            if (sequenceSize > 2) {
                recordsForDelete.addAll(tempRecordsForDelete);
                phonesForDelete.addAll(tempPhonesForDelete);
            }

            if (recordsForDelete.size() > 0) {
                dataDAO.bulkDeleteSearchEngineRecords(dataTable.getName(), recordsForDelete);
                for (Long phone: phonesForDelete) {
                    Logger.info("Table name: {}, phone: {}",
                            dataTable.getName(),
                            phone);
                }
            }

            Logger.info("Table name: {}, search engine filtering done",
                    dataTable.getName());
        }

        Logger.info("Search engine filtering done");
    }

    public Result updateBusinessOwner() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        Map<Integer, Set<Long>> businessPhones = generateBusinessPhones();

        List<DataTable> consumersTables = dataDAO.getTablesByType(DataTable.CONSUMERS2);
        for (DataTable dataTable : consumersTables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(dataTable.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long id = 0;
            long batch = 10000l;

            List<Consumer2> consumersForUpdate = new LinkedList();

            while (id < (maxId - batch)) {
                List<Consumer2> dbConsumers = dataDAO.getConsumers2ListByIdRange(dataTable.getName(), id, id + batch);
                for (Consumer2 consumer: dbConsumers) {
                    if ("U".equalsIgnoreCase(consumer.getBUSINESSOWNER())) {
                        String phone = consumer.getPHONE();
                        if (phone != null && phone.length() > 2) {
                            try {
                                int key = (int)parseLong(phone.substring(0, 2));
                                if (key != -1 && businessPhones.containsKey(key)) {
                                    boolean isBusinessPhone = businessPhones.get(key).contains(phone);

                                    if (isBusinessPhone) {
                                        consumer.setBUSINESSOWNER("8");
                                        consumersForUpdate.add(consumer);
                                    }
                                }
                            } catch (Exception e) {e.printStackTrace();}
                        }

                    }

                }

                dataDAO.updateBusinessOwner(dataTable.getName(), consumersForUpdate);

                id = id + batch;
                Logger.info("Table name: {}, table index: {}, handled records: {}",
                        dataTable.getName(),
                        consumersTables.indexOf(dataTable),
                        id);
            }
        }



        return ok();
    }

    public Result generateDirectoryUniqueCompanies() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        ListEntity list = new ListEntity(
                "directory unique name",
                userDAO.findUserByName("serfeo").getId(),
                100,
                System.currentTimeMillis());

        list.setTableName("DirectoryOriginal");
        list.setType(DataTable.DIRECTORY);
        list.setRequest("{}");

        listDAO.saveList(list);
        listDAO.updateMatchedById(list.getId(), true);

        BufferedReader reader = new BufferedReader(new FileReader(new File("/home/makemydata/workspace/companies.csv")));

        List<String> params = readNextParamsButch(reader);
        while (params.size() > 0) {
            for (String companyName : params) {
                dataDAO.insertDirectoryUserItems(list.getId(), companyName);
            }

            params = readNextParamsButch(reader);
        }


        return ok();
    }

    private Map<Integer, Set<Long>> generateBusinessPhones() {
        Map<Integer, Set<Long>> phones = new HashMap();

        long batch = 10000;
        for (int i = 10; i < 100; i++) {
            phones.put(i, new HashSet());
        }

        List<DataTable> allTables = dataDAO.getAllTables();
        Iterator<DataTable> it = allTables.iterator();
        while (it.hasNext()) {
            DataTable dataTable = it.next();
            if (dataTable.getType() == DataTable.CONSUMERS ||
                    dataTable.getType() == DataTable.CONSUMERS2 ||
                    dataTable.getType() == DataTable.CRAIGSLIST) {
                it.remove();
            }
        }

        for (DataTable dataTable : allTables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(dataTable.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long id = 0;
            while (id < (maxId - batch)) {
                List<String> dbPhones = dataDAO.getPhoneByTableNameAndIdRange(dataTable.getName(), id, id + batch);
                for (String phone : dbPhones) {
                    if (phone != null && phone.length() > 2) {
                        try {
                            int key = (int)parseLong(phone.substring(0, 2));
                            if (key != -1 && phones.containsKey(key)) {
                                phones.get(key).add(parseLong(phone));
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }

                id = id + batch;
                Logger.info("Table name: {}, table index: {}, handled records: {}",
                        dataTable.getName(),
                        allTables.indexOf(dataTable),
                        id);
            }
        }

        return phones;
    }

    public Result trimStringFields(int type) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByType(type);
        for (DataTable table: tables) {
            trimStringFieldsInTables(table);
        }

        return ok();
    }

    private void trimStringFieldsInTables(DataTable dataTable) {
        long maxId = getTableMaxId(dataTable);
        if (maxId == -1) { return; }

        AtomicInteger completed = new AtomicInteger();
        AtomicLong id = new AtomicLong(0);
        long batch = 100000l;

        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                long currentId = id.getAndAdd(batch);
                while (currentId < (maxId + batch)) {
                    dataDAO.trimFields(dataTable.getName(), currentId, currentId + batch);

                    Logger.info("Table name: {}, handled records: {}",
                            dataTable.getName(),
                            currentId + batch);

                    currentId = id.addAndGet(batch);
                }

                completed.incrementAndGet();
            }).start();
        }

        while (completed.get() != 4) {
            try { Thread.sleep(10000); }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public Result updatePhonesForSearchEngine() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByType(DataTable.SEARCH_ENGINE);
        updatePhonesForSearchEngineInTables(tables);

        return ok();
    }

    private void updatePhonesForSearchEngineInTables(List<DataTable> tables) {
        for (DataTable dataTable: tables) {
            long id = 0;
            long maxId = dataDAO.getTableMaxId(dataTable.getName());
            long batch = 10000;

            while (id < maxId + batch) {
                dataDAO.updateSearchEnginePhones(dataTable.getName(), id, id + batch);
                id = id + batch;

                Logger.info("Table name: {}, updated records: {}/{}",
                        dataTable.getName(),
                        id,
                        maxId);
            }
        }
    }

    public Result moveWrongPhones() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByPhoneTypeAndNotRemoved(DataTable.MOBILE);
        moveWrongPhonesForTables(tables);

        return ok();
    }

    private void moveWrongPhonesForTables(List<DataTable> tables) {
        for (DataTable mobileDataTable: tables) {
            String name = mobileDataTable.getName();
            int index = tables.indexOf(mobileDataTable);

            String landLineTableName = name.replace("_mobile", "_landline");
            DataTable landLineDataTable = dataDAO.getTableByName(landLineTableName);

            if (landLineDataTable != null) {
                moveData(mobileDataTable, landLineDataTable, 0, index, tables.size());
                moveData(landLineDataTable, mobileDataTable, 1, index, tables.size());
            }
        }
    }

    public Result moveWrongPhonesForType(int type) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByPhoneTypeAndNotRemoved(DataTable.MOBILE);
        Iterator<DataTable> it = tables.iterator();
        while (it.hasNext()) {
            if (it.next().getType() != type) {
                it.remove();
            }
        }

        moveWrongPhonesForTables(tables);
        return ok();
    }

    private void moveData(DataTable sourceDataTable,
                          DataTable destDataTable,
                          int phoneType,
                          int tableIndex,
                          int tableCount) {
        long maxId = dataDAO.getTableMaxId(sourceDataTable.getName());
        long batch = 5000;
        long id = 1;

        while (id < maxId + batch) {
            dataDAO.moveData(sourceDataTable.getName(), destDataTable.getName(), phoneType, id, id + batch);
            id = id + batch;

            Logger.info("{} -> {}, ({}/{}), {}",
                    sourceDataTable.getName(),
                    destDataTable.getName(),
                    tableIndex + 1,
                    tableCount,
                    id);
        }
    }

    public Result updateAllPhoneTypes() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getAllTables();
        for (DataTable table: tables) {
            application.updatePhoneTypes(table.getName());
        }

        return ok();
    }

    public Result updateLenders() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        Set<String> lenders = new HashSet();

        String tableName = "consumersOriginal";
        long maxId = dataDAO.getTableMaxId(tableName);

        long id = 0;
        long batch = 100000l;

        while (id < (maxId - batch)) {
            List<Consumer2> dbConsumers = dataDAO.getConsumers2LendersListByIdRange(tableName, id, id + batch);
            for (Consumer2 consumer: dbConsumers) {
                lenders.add(consumer.getMORTGAGELENDERNAME());
                lenders.add(consumer.getPURCHASELENDERNAME());
                lenders.add(consumer.getREFINANCELENDERNAME());
            }

            id = id + batch;
            Logger.info("Table name: {}, handled records: {}",
                    tableName,
                    id);
        }

        dataDAO.insertLenders(lenders);

        return ok();
    }

    public Result updateEmptyPhones() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getAllTables();
        for (DataTable table: tables) {
            long maxId;

            try {
                maxId = dataDAO.getTableMaxId(table.getName());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            long batch = 100000;
            long id = 1;

            while (id < (maxId - batch)) {
                dataDAO.updateEmptyPhoneValues(table.getName(), id, id + batch);

                id = id + batch;
                Logger.info("Table name: {}, table index: {}, handled records: {}",
                        table.getName(),
                        tables.indexOf(table),
                        id);
            }
        }

        return ok();
    }

    public Result updatePhoneTypesByDataSourceType(int type) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getAllTablesByType(type);
        for (DataTable table: tables) {
            application.updatePhoneTypes(table.getName());
        }

        return ok();
    }

    public Result updateDNC() throws Exception {
        if (true) return notFound();

        Map<Long, Set<Long>> cleanPhonesMap = generateCleanPhonesMap();

        List<DataTable> allTables = dataDAO.getAllTables();
        updateDNCInTables(allTables, cleanPhonesMap);

        cleanPhonesMap.clear();
        return ok();
    }

    public Result copyDirectoryNewSources() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<String> directoryTables = new LinkedList();
        directoryTables.add("DirectoryOriginal");
        directoryTables.add("DirectoryMobile");
        directoryTables.add("DirectoryLandlines");

        List<String> directoryNewSourcesTables = new LinkedList();
        directoryNewSourcesTables.add("DirectoryNewSourcesOriginal");
        directoryNewSourcesTables.add("DirectoryNewSourcesMobile");
        directoryNewSourcesTables.add("DirectoryNewSourcesLandlines");

        for (int i = 0; i < directoryNewSourcesTables.size(); i++) {
            String directoryNewSource = directoryNewSourcesTables.get(i);
            DataTable dataTable = dataDAO.getTableByName(directoryNewSource);

            long id = 0;
            long batch = 10000;
            long maxId = getTableMaxId(dataTable);

            while (id < (maxId + batch)) {
                dataDAO.copyNewSourcesData(directoryNewSource, directoryTables.get(i), id, id + batch);

                Logger.info("Inserting new sources data. Table name: {}, handled records: {}",
                        dataTable.getName(),
                        id + batch);

                id = id + batch;
            }
        }

        return ok();
    }

    public Result updateTableDNCAndPhoneTypeRecords(String tableName, Long offset) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        Map<Long, Set<Long>> cleanPhonesMap = generateCleanPhonesMap();
        //Map<Long, Set<Long>> landLinePhones = Application.getLandLinePhones(3);

        DataTable dataTable = dataDAO.getTableByName(tableName);

        long batch = 10000;
        long maxId = getTableMaxId(dataTable);
        if (maxId == -1) {
            return ok();
        }

        AtomicLong id = new AtomicLong(offset);
        AtomicInteger finished = new AtomicInteger(0);

        for (int i = 0; i < 6; i++) {
            Thread thread = new Thread(() -> {
                long currentId = id.getAndAdd(batch);
                List<PhoneEntity> phoneEntities = dataDAO.getPhoneEntityList(dataTable.getName(), currentId, currentId + batch);

                // update DNC and phone type
                while (id.get() < (maxId + batch)) {
                    List<PhoneEntity> entitiesForDNCUpdate = new LinkedList();
                    entitiesForDNCUpdate.addAll(phoneEntities);

                    Iterator<PhoneEntity> it = entitiesForDNCUpdate.iterator();
                    List<PhoneEntity> cleanPhoneEntities = filterCleanPhoneEntities(it, cleanPhonesMap);

                    List<Long> ids = new LinkedList();
                    for (PhoneEntity phoneEntity: cleanPhoneEntities) {
                        ids.add(phoneEntity.getId());
                    }

                    if (ids.size() > 0) {
                        dataDAO.updatePhoneEntityDNC(tableName, ids, false);
                    }

                    /*List<PhoneEntity> entitiesForPhoneTypeUpdate = new LinkedList();
                    entitiesForPhoneTypeUpdate.addAll(phoneEntities);

                    it = entitiesForPhoneTypeUpdate.iterator();
                    filterPhoneTypeEntities(it, landLinePhones);
                    if (phoneEntities.size() > 0) {
                        dataDAO.updatePhoneEntities(dataTable.getName(), phoneEntities);
                    }*/

                    Logger.info("Updating DNC. Table name: {}, handled records: {}",
                            dataTable.getName(),
                            currentId + batch);

                    currentId = id.addAndGet(batch);
                    phoneEntities = dataDAO.getPhoneEntityList(dataTable.getName(), currentId, currentId + batch);
                }

                finished.incrementAndGet();
            });
            thread.start();
        }

        while (finished.get() != 6) {
            try { Thread.sleep(10000); }
            catch (Exception e) { e.printStackTrace(); }
        }

        cleanPhonesMap.clear();
        //landLinePhones.clear();

        return ok();
    }

    private void filterPhoneTypeEntities(Iterator<PhoneEntity> it, Map<Long, Set<Long>> landLinePhones) {
        while (it.hasNext()) {
            PhoneEntity phoneEntity = it.next();
            if (phoneEntity.getPhoneType() == null || -1 == phoneEntity.getPhoneType()) {
                Integer oldPhoneType = phoneEntity.getPhoneType();
                if (phoneEntity.getPHONE() != null && phoneEntity.getPHONE().length() > 3) {
                    String startChars = phoneEntity.getPHONE().substring(0, 3);

                    Long code = -1l;
                    try {
                        code = Long.parseLong(startChars);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (code == -1) {
                        continue;
                    }

                    Set<Long> landLinePhoneSet = landLinePhones.get(code);
                    if (landLinePhoneSet != null) {
                        Long numPhone = -1l;
                        try {
                            numPhone = Long.parseLong(phoneEntity.getPHONE());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (numPhone == -1) {
                            continue;
                        }
                        if (landLinePhoneSet.contains(numPhone)) {
                            phoneEntity.setPhoneType(0);
                        } else {
                            phoneEntity.setPhoneType(1);
                        }
                    } else {
                        phoneEntity.setPhoneType(-1);
                    }
                }

                if (oldPhoneType != null && oldPhoneType.equals(phoneEntity.getPhoneType())) {
                    it.remove();
                }
            } else {
                it.remove();
            }
        }
    }

    public Result updateSicCodes() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> tables = dataDAO.getTablesByType(DataTable.BUSINESS);
        for (DataTable table: tables) {
            long maxId = getTableMaxId(table);
            if (maxId == -1) {
                continue;
            }

            long id = 0;
            long batch = 10000;
            while (id < (maxId - batch)) {
                dataDAO.updateBusinessTableSic(table.getName(), id, id + batch);

                id = id + batch;
                Logger.info("Table name: {}, table index: {}, handled records: {}",
                        table.getName(),
                        tables.indexOf(table),
                        id);
            }
        }

        return ok();
    }

    public Result updateConsumersNames() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<DataTable> dataTables = new LinkedList();
        dataTables.add(dataDAO.getTableByName("ConsumersOriginal"));
        dataTables.add(dataDAO.getTableByName("ConsumersMobile"));
        dataTables.add(dataDAO.getTableByName("ConsumersLandlines"));

        long batch = 50000;
        for (DataTable dataTable: dataTables) {
            long currentId = 0;
            long maxId = getTableMaxId(dataTable);
            if (maxId == -1) {
                continue;
            }

            while (currentId < (maxId + batch)) {
                dataDAO.updateConsumersNames(dataTable.getName(), currentId, currentId + batch);

                Logger.info("Updating consumers names... table name: {}, table index: {}, handled records: {}",
                        dataTable.getName(),
                        dataTables.indexOf(dataTable),
                        currentId + batch);

                currentId = currentId + batch;
            }
        }

        return ok();
    }

    public Result generateConsumersMatchingList() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        DataTable table = dataDAO.getTableByName("ConsumersMobile");

        AtomicInteger finished = new AtomicInteger(0);
        ListEntity list = new ListEntity(
                "directory_mobile_matched",
                userDAO.findUserByName("serfeo").getId(),
                100,
                System.currentTimeMillis());

        list.setTableName(table.getName());
        list.setType(table.getType());
        list.setFilePath("/home/makemydata/workspace/directory_mobile.csv");
        list.setRequest("{}");

        listDAO.saveList(list);
        listDAO.updateMatchedById(list.getId(), true);

        BufferedReader reader = new BufferedReader(new FileReader(new File(list.getFilePath())));
        String[] columns = new String[]{"phone"};

        final AtomicLong count = new AtomicLong();
        for (int i = 0; i < 6; i++) {
            Thread thread = new Thread(() -> {
                try {
                    List<String> params = readNextParamsButch(reader);
                    count.set(count.get() + params.size());

                    while (params.size() > 0) {
                        List<List<DataRequest.Entity>> orConditions = new LinkedList();
                        for (String param : params) {
                            String[] paramParts = param.split(",");
                            List<DataRequest.Entity> andCondition = new LinkedList();
                            for (int index = 0; index < columns.length; index++) {
                                String column = columns[index];
                                String value = paramParts[index];

                                andCondition.add(new DataRequest.Entity(column, value, "="));
                                andCondition.add(new DataRequest.Entity("PERSONDATEOFBIRTHDATE", -920647181l, "<"));
                            }

                            orConditions.add(andCondition);
                        }

                        dataDAO.insertMatchedRecords(orConditions, "", list.getTableName(), list.getId());
                        Logger.info("Generating consumers matching list. Table: {}. Handled records: {}",
                                table.getName(),
                                count.get());

                        params = readNextParamsButch(reader);
                        count.set(count.get() + params.size());
                    }

                    finished.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        while (finished.get() != 6) {
            Thread.sleep(15000);
        }

        return ok();
    }

    public Result generateMatchedList() throws Exception {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        DataTable table = dataDAO.getTableByName("ConsumersOriginal");

        AtomicInteger finished = new AtomicInteger(0);
        ListEntity list = new ListEntity(
                "blacklist_matched_" + table.getName(),
                userDAO.findUserByName("serfeo").getId(),
                100,
                System.currentTimeMillis());

        list.setTableName(table.getName());
        list.setType(table.getType());
        list.setFilePath("/home/makemydata/workspace/mattresponder530.csv");
        list.setRequest("{}");

        listDAO.saveList(list);
        listDAO.updateMatchedById(list.getId(), true);

        BufferedReader reader = new BufferedReader(new FileReader(new File(list.getFilePath())));
        String[] columns = new String[]{"phone"};

        final AtomicLong count = new AtomicLong();
        for (int i = 0; i < 6; i++) {
            Thread thread = new Thread(() -> {
                try {
                    List<String> params = readNextParamsButch(reader);
                    count.set(count.get() + params.size());

                    while (params.size() > 0) {
                        List<List<DataRequest.Entity>> orConditions = new LinkedList();
                        for (String param : params) {
                            String[] paramParts = param.split(",");
                            List<DataRequest.Entity> andCondition = new LinkedList();
                            for (int index = 0; index < columns.length; index++) {
                                String column = columns[index];
                                String value = paramParts[index];

                                andCondition.add(new DataRequest.Entity(column, value, "="));
                            }

                            orConditions.add(andCondition);
                        }

                        dataDAO.insertMatchedRecords(orConditions, "", list.getTableName(), list.getId());
                        Logger.info("Generating removeOldListsng list. Table: {}. Handled records: {}",
                                table.getName(),
                                count.get());

                        params = readNextParamsButch(reader);
                        count.set(count.get() + params.size());
                    }

                    finished.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }

        while (finished.get() != 6) {
            Thread.sleep(15000);
        }

        return ok();
    }

    private synchronized List<String> readNextParamsButch(BufferedReader reader) throws Exception {
        List<String> result = new LinkedList();

        String paramsLine;
        while ((paramsLine = reader.readLine()) != null) {
            result.add(paramsLine.replaceAll("\"", ""));

            if (result.size() == 100) {
                return result;
            }
        }

        return result;
    }

    private void updateDNCInTables(List<DataTable> tables, Map<Long, Set<Long>> cleanPhonesMap) {
        long batch = 10000;

        for (DataTable dataTable: tables) {
            long maxId = getTableMaxId(dataTable);
            if (maxId == -1) {
                continue;
            }

            AtomicInteger completed = new AtomicInteger();
            AtomicLong id = new AtomicLong(0);

            for (int i = 0; i < 4; i++) {
                new Thread(() -> {
                    long currentId = id.getAndAdd(batch);
                    List<PhoneEntity> phoneEntities = dataDAO.getPhoneEntityList(dataTable.getName(), currentId, currentId + batch);
                    while (currentId < (maxId + batch)) {
                        Iterator<PhoneEntity> it = phoneEntities.iterator();
                        List<PhoneEntity> dncEntities = filterPhoneEntities(it, cleanPhonesMap);
                        updatePhoneDNC(dataTable.getName(), phoneEntities, dncEntities);

                        Logger.info("Table name: {}, table index: {}, handled records: {}",
                                dataTable.getName(),
                                tables.indexOf(dataTable),
                                currentId + batch);

                        currentId = id.addAndGet(batch);
                        phoneEntities = dataDAO.getPhoneEntityList(dataTable.getName(), currentId, currentId + batch);
                    }

                    completed.incrementAndGet();
                }).start();
            }

            while (completed.get() != 4) {
                try { Thread.sleep(10000); }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private void updatePhoneDNC(String tableName,
                                List<PhoneEntity> phoneEntities,
                                List<PhoneEntity> dncEntities) {
        List<Long> ids = new LinkedList();
        for (PhoneEntity phoneEntity: phoneEntities) {
            ids.add(phoneEntity.getId());
        }

        if (ids.size() > 0) {
            dataDAO.updatePhoneEntityDNC(tableName, ids, false);
        }

        ids.clear();
        for (PhoneEntity phoneEntity: dncEntities) {
            ids.add(phoneEntity.getId());
        }

        /*if (ids.size() > 0) {
            dataDAO.updatePhoneEntityDNC(tableName, ids, true);
        }*/
    }

    private long getTableMaxId(DataTable dataTable) {
        try {
            return dataDAO.getTableMaxId(dataTable.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private List<PhoneEntity> filterPhoneEntities(Iterator<PhoneEntity> it, Map<Long, Set<Long>> cleanPhonesMap) {
        List<PhoneEntity> dncEntities = new LinkedList();
        while (it.hasNext()) {
            PhoneEntity phoneEntity = it.next();
            if (phoneEntity.getPHONE() != null && phoneEntity.getPHONE().length() > 1) {
                String letter = phoneEntity.getPHONE().substring(0, 2);

                Set<Long> phoneSet = cleanPhonesMap.get(parseLong(letter));
                long numPhone = parseLong(phoneEntity.getPHONE());
                if (phoneSet == null || !phoneSet.contains(numPhone)) {
                    dncEntities.add(phoneEntity);
                    it.remove();
                }
            }
        }

        return dncEntities;
    }

    private List<PhoneEntity> filterCleanPhoneEntities(Iterator<PhoneEntity> it, Map<Long, Set<Long>> cleanPhonesMap) {
        List<PhoneEntity> cleanEntities = new LinkedList();
        while (it.hasNext()) {
            PhoneEntity phoneEntity = it.next();
            if (phoneEntity.getPHONE() != null && phoneEntity.getPHONE().length() > 1) {
                String letter = phoneEntity.getPHONE().substring(0, 2);

                Set<Long> phoneSet = cleanPhonesMap.get(parseLong(letter));
                long numPhone = parseLong(phoneEntity.getPHONE());
                if (phoneSet != null && phoneSet.contains(numPhone)) {
                    cleanEntities.add(phoneEntity);
                    it.remove();
                }
            }
        }

        return cleanEntities;
    }

    public static Map<Long, Set<Long>> generateCleanPhonesMap() {
        Map<Long, Set<Long>> result = new HashMap();
        for (long i = 100; i < 1000; i++) {
            result.put(i, new HashSet());
        }

        try {
            FileInputStream inputStream = new FileInputStream("/home/makemydata/workspace/cleanphones.csv");
            Scanner scanner = new Scanner(inputStream, "UTF-8");

            long line = 0;
            while (scanner.hasNextLine()) {
                line++;

                String phone = scanner.nextLine();
                String firstLetter = phone.substring(0, 3);

                Set<Long> phoneSet = result.get(parseLong(firstLetter));
                if (phoneSet != null) {
                    long phoneNum = parseLong(phone);
                    if (phoneNum != -1l) {
                        phoneSet.add(phoneNum);
                    }
                }

                if (line % 100000 == 0) {
                    Logger.info("Handled clear phones lines: {}", line);
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    public static long parseLong(String value) {
        long result = -1;
        try { result = Long.parseLong(value.trim()); }
        catch (Exception e) {/*e.printStackTrace();*/}

        return result;
    }

    public Result convertZipList() throws Exception {
        String input = "/home/heruvim/Development/workspace/ConsumerDataBase/public/static/zip.txt";
        String output = "/home/heruvim/Development/workspace/ConsumerDataBase/public/static/zip.json";

        BufferedReader inputStream = new BufferedReader(new FileReader(input));
        File outputFile = new File(output);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }

        FileWriter filewriter = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter outputStream = new BufferedWriter(filewriter);
        List<ZipCode.ZipCodeData> resultList = new LinkedList();

        String line;
        while ((line = inputStream.readLine()) != null) {
            if (line != null && line.length() > 0 && !line.startsWith("ZIP")) {
                String[] parts = line.split(",");
                resultList.add(new ZipCode.ZipCodeData(
                        parts[0].trim(),
                        Float.parseFloat(parts[1].trim()),
                        Float.parseFloat(parts[2].trim())));
            }
        }
        inputStream.close();

        outputStream.write(new ObjectMapper().writeValueAsString(resultList));
        outputStream.flush();
        outputStream.close();

        return ok();
    }

    public Result convertWebSites() throws Exception {
        PrintWriter printWriter = new PrintWriter(new File("/home/makemydata/workspace/whois_by_websites.csv"));
        generateHeader(printWriter);
        generateWhoIsList(printWriter);
        printWriter.flush();
        printWriter.close();

        return ok();
    }

    private void generateHeader(PrintWriter printWriter) {
        Field[] fields = WhoIs.class.getDeclaredFields();
        for ( int i = 0; i < fields.length; i++ ) {
            Field field = fields[ i ];
            if (WHO_IS_IGNORED_FIELDS.contains(field.getName())) {
                continue;
            }

            if ( "phoneType".equals( field.getName() ) ) {
                printWriter.append( "PHONE TYPE" );
            } else {
                printWriter.append(field.getName().toUpperCase());
            }

            if ( i + 1 < fields.length ) {
                printWriter.append( SEPARATOR );
            }
        }

        printWriter.append(NEW_LINE);
    }

    private void generateWhoIsList(PrintWriter printWriter) throws Exception {
        Field[] fields = WhoIs.class.getDeclaredFields();

        BufferedReader inputStream = new BufferedReader(new FileReader("/home/makemydata/workspace/websites"));
        List<String> websites = readNextWebsitesButch(inputStream);
        List<WhoIs> whoIsList = dataDAO.getWhoIsListByWebSitesList(websites);

        long count = 0;
        long foundCount = whoIsList.size();

        while ( websites.size() > 0 ) {
            for ( int k = 0; k < whoIsList.size(); k++ ) {
                WhoIs whoIsItem = whoIsList.get( k );
                for ( int i = 0; i < fields.length; i++ ) {
                    Field field = fields[ i ];
                    if (WHO_IS_IGNORED_FIELDS.contains(field.getName())) {
                        continue;
                    }

                    Object value = new PropertyDescriptor( field.getName(), WhoIs.class ).getReadMethod().invoke( whoIsItem );
                    if ( field.getName().equals( "phoneType" ) ) {
                        printWriter.append((value != null && !MINUS_ONE.equals( value )) ? ( ZERO.equals( value ) ? "\"landline\"" : "\"mobile\"" ) :  "\"\"");
                    } else {
                        printWriter.append(value != null ? "\"" + handleValue(value.toString()) + "\"" : "");
                    }

                    if ( i + 1 < fields.length ) {
                        printWriter.append(SEPARATOR);
                    }
                }

                printWriter.append( NEW_LINE );
            }

            count = count + 100;
            Logger.info("Websites handled: {}", count);

            websites = readNextWebsitesButch(inputStream);
            whoIsList = dataDAO.getWhoIsListByWebSitesList(websites);

            foundCount = foundCount + whoIsList.size();
            Logger.info("WhoIs records found: {}", foundCount);

        }

        inputStream.close();
    }

    private List<String> readNextWebsitesButch(BufferedReader inputStream) throws Exception {
        List<String> result = new LinkedList();

        String website;
        while ((website = inputStream.readLine()) != null) {
            result.add(website);

            if (result.size() == 100) {
                return result;
            }
        }

        return result;
    }

    private String handleValue( String value ) {
        return value.equals( "-1" ) ? "" : value;
    }

}
