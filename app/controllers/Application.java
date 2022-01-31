package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.*;
import org.apache.commons.lang.WordUtils;
import org.mybatis.guice.transactional.Transactional;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.RangeResults;
import play.mvc.Result;
import services.EmailService;
import services.db.dao.DataDAO;
import services.db.dao.ListDAO;
import services.db.dao.PrefixesDAO;
import services.db.dao.UserDAO;
import services.db.entity.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class Application extends Controller {

    private final DateFormat timeFormat;
    private final DataDAO dataDAO;
    private final UserDAO userDAO;
    private final ListDAO listDAO;
    private final PrefixesDAO prefixesDAO;
    private final EmailService emailService;

    private WSClient wsClient;

    private String importMessage;
    private String updateMessage;

    //private DateFormat whoIsDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private DateFormat whoIsDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat craigsDateFormat = new SimpleDateFormat("dd-MM-yy");
    private DateFormat commonDateFormat = new SimpleDateFormat("MM.dd.yyyy");

    public static String[] STATE_CODES = new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "DC", "FL", "GA", "GU", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "PR", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VI", "VA", "WA", "WV", "WI", "WY"};
    public static Map<String, String> STATE_TO_NAME = new HashMap() {
        {
            put("AL", "Alabama");
            put("AK", "Alaska");
            put("AS", "American Samoa");
            put("AZ", "Arizona");
            put("AR", "Arkansas");
            put("CA", "California");
            put("CO", "Colorado");
            put("CT", "Connecticut");
            put("DE", "Delaware");
            put("DC", "District Of Columbia");
            put("FM", "Federated States Of Micronesia");
            put("FL", "Florida");
            put("GA", "Georgia");
            put("GU", "Guam");
            put("HI", "Hawaii");
            put("ID", "Idaho");
            put("IL", "Illinois");
            put("IN", "Indiana");
            put("IA", "Iowa");
            put("KS", "Kansas");
            put("KY", "Kentucky");
            put("LA", "Louisiana");
            put("ME", "Maine");
            put("MH", "Marshall Islands");
            put("MD", "Maryland");
            put("MA", "Massachusetts");
            put("MI", "Michigan");
            put("MN", "Minnesota");
            put("MS", "Mississippi");
            put("MO", "Missouri");
            put("MT", "Montana");
            put("NE", "Nebraska");
            put("NV", "Nevada");
            put("NH", "New Hampshire");
            put("NJ", "New Jersey");
            put("NM", "New Mexico");
            put("NY", "New York");
            put("NC", "North Carolina");
            put("ND", "North Dakota");
            put("MP", "Northern Mariana Islands");
            put("OH", "Ohio");
            put("OK", "Oklahoma");
            put("OR", "Oregon");
            put("PW", "Palau");
            put("PA", "Pennsylvania");
            put("PR", "Puerto Rico");
            put("RI", "Rhode Island");
            put("SC", "South Carolina");
            put("SD", "South Dakota");
            put("TN", "Tennessee");
            put("TX", "Texas");
            put("UT", "Utah");
            put("VT", "Vermont");
            put("VI", "Virgin Islands");
            put("VA", "Virginia");
            put("WA", "Washington");
            put("WV", "West Virginia");
            put("WI", "Wisconsin");
            put("WY", "Wyoming");
        }
    };
    public Map<String, String> NAME_TO_STATE = invertStateToName();

    public Map<String, String> invertStateToName(){
        Map<String,String> inverted = new HashMap<String,String>();
        Map<String,String> stateToName = new HashMap<String,String>();
        stateToName.putAll(STATE_TO_NAME);
        for(Map.Entry<String,String> entry: stateToName.entrySet()){
            inverted.put(entry.getValue(),entry.getKey());
        }
        return inverted;
    }

    public static Map<String, String[]> STATES_BY_TIMEZONE = new HashMap(){
        {
            put("HAST", new String[]{"HI"});
            put("AKST", new String[]{"AK"});
            put("PST", new String[]{"CA","ID","NV","OR","WA"});
            put("MST", new String[]{"AZ","CO","ID","KS","MT","NE","NM","ND","SD","TX","UT","WY"});
            put("CST", new String[]{"AL","AR", "FL","IL","IN","IA","KS","KY","LA","MI","MN","MS","MO","NE","ND","OK","SD","TN","TX","WI"});
            put("EST", new String[]{"CT","DE","FL","GA","IN","KY","ME","MD","MA","MI","NH","NY","NJ","NC","OH","PA","RI","SC","TN","VT","VA","DC","WV"});
        }
    };

    public static Map<Integer, String> AREA_CODE_TO_STATE_MAP = new HashMap() {
        {
            put(201, "NJ");
            put(202, "DC");
            put(203, "CT");
            put(205, "AL");
            put(206, "WA");
            put(207, "ME");
            put(208, "ID");
            put(209, "CA");
            put(210, "TX");
            put(212, "NY");
            put(213, "CA");
            put(214, "TX");
            put(215, "PA");
            put(216, "OH");
            put(217, "IL");
            put(218, "MN");
            put(219, "IN");
            put(225, "LA");
            put(228, "MS");
            put(229, "GA");
            put(470, "GA");
            put(231, "MI");
            put(239, "FL");
            put(248, "MI");
            put(251, "AL");
            put(252, "NC");
            put(253, "WA");
            put(254, "TX");
            put(256, "AL");
            put(260, "IN");
            put(262, "WI");
            put(269, "MI");
            put(270, "KY");
            put(276, "VA");
            put(281, "TX");
            put(301, "MD");
            put(302, "DE");
            put(303, "CO");
            put(304, "WV");
            put(681, "WV");
            put(305, "FL");
            put(307, "WY");
            put(308, "NE");
            put(309, "IL");
            put(310, "CA");
            put(312, "IL");
            put(313, "MI");
            put(314, "MO");
            put(315, "NY");
            put(316, "KS");
            put(317, "IN");
            put(318, "LA");
            put(319, "IA");
            put(320, "MN");
            put(321, "FL");
            put(323, "CA");
            put(325, "TX");
            put(330, "OH");
            put(334, "AL");
            put(336, "NC");
            put(337, "LA");
            put(340, "VI");
            put(352, "FL");
            put(360, "WA");
            put(361, "TX");
            put(386, "FL");
            put(401, "RI");
            put(402, "NE");
            put(531, "NE");
            put(404, "GA");
            put(405, "OK");
            put(406, "MT");
            put(407, "FL");
            put(408, "CA");
            put(409, "TX");
            put(410, "MD");
            put(240, "MD");
            put(443, "MD");
            put(667, "MD");
            put(412, "PA");
            put(413, "MA");
            put(414, "WI");
            put(415, "CA");
            put(417, "MO");
            put(419, "OH");
            put(423, "TN");
            put(425, "WA");
            put(432, "TX");
            put(434, "VA");
            put(435, "UT");
            put(440, "OH");
            put(478, "GA");
            put(479, "AR");
            put(480, "AZ");
            put(501, "AR");
            put(502, "KY");
            put(503, "OR");
            put(504, "LA");
            put(505, "NM");
            put(507, "MN");
            put(508, "MA");
            put(509, "WA");
            put(510, "CA");
            put(512, "TX");
            put(513, "OH");
            put(515, "IA");
            put(516, "NY");
            put(517, "MI");
            put(947, "MI");
            put(518, "NY");
            put(520, "AZ");
            put(530, "CA");
            put(540, "VA");
            put(541, "OR");
            put(458, "OR");
            put(971, "OR");
            put(559, "CA");
            put(561, "FL");
            put(562, "CA");
            put(563, "IA");
            put(570, "PA");
            put(267, "PA");
            put(272, "PA");
            put(484, "PA");
            put(878, "PA");
            put(573, "MO");
            put(574, "IN");
            put(575, "NM");
            put(580, "OK");
            put(585, "NY");
            put(586, "MI");
            put(601, "MS");
            put(769, "MS");
            put(602, "AZ");
            put(603, "NH");
            put(605, "SD");
            put(606, "KY");
            put(607, "NY");
            put(608, "WI");
            put(609, "NJ");
            put(551, "NJ");
            put(848, "NJ");
            put(862, "NJ");
            put(610, "PA");
            put(612, "MN");
            put(614, "OH");
            put(615, "TN");
            put(616, "MI");
            put(617, "MA");
            put(618, "IL");
            put(619, "CA");
            put(620, "KS");
            put(623, "AZ");
            put(626, "CA");
            put(630, "IL");
            put(631, "NY");
            put(636, "MO");
            put(641, "IA");
            put(650, "CA");
            put(651, "MN");
            put(660, "MO");
            put(661, "CA");
            put(662, "MS");
            put(678, "GA");
            put(762, "GA");
            put(701, "ND");
            put(702, "NV");
            put(775, "NV");
            put(703, "VA");
            put(704, "NC");
            put(980, "NC");
            put(984, "NC");
            put(706, "GA");
            put(707, "CA");
            put(708, "IL");
            put(224, "IL");
            put(331, "IL");
            put(779, "IL");
            put(872, "IL");
            put(712, "IA");
            put(713, "TX");
            put(346, "TX");
            put(430, "TX");
            put(469, "TX");
            put(682, "TX");
            put(737, "TX");
            put(714, "CA");
            put(715, "WI");
            put(716, "NY");
            put(347, "NY");
            put(646, "NY");
            put(717, "PA");
            put(718, "NY");
            put(719, "CO");
            put(724, "PA");
            put(727, "FL");
            put(731, "TN");
            put(732, "NJ");
            put(734, "MI");
            put(740, "OH");
            put(757, "VA");
            put(760, "CA");
            put(763, "MN");
            put(765, "IN");
            put(770, "GA");
            put(772, "FL");
            put(773, "IL");
            put(775, "NV");
            put(781, "MA");
            put(339, "MA");
            put(351, "MA");
            put(774, "MA");
            put(857, "MA");
            put(785, "KS");
            put(787, "PR");
            put(801, "UT");
            put(385, "UT");
            put(802, "VT");
            put(803, "SC");
            put(804, "VA");
            put(571, "VA");
            put(805, "CA");
            put(806, "TX");
            put(808, "HI");
            put(810, "MI");
            put(812, "IN");
            put(813, "FL");
            put(814, "PA");
            put(815, "IL");
            put(816, "MO");
            put(817, "TX");
            put(818, "CA");
            put(828, "NC");
            put(830, "TX");
            put(831, "CA");
            put(843, "SC");
            put(845, "NY");
            put(847, "IL");
            put(850, "FL");
            put(856, "NJ");
            put(858, "CA");
            put(859, "KY");
            put(860, "CT");
            put(863, "FL");
            put(864, "SC");
            put(865, "TN");
            put(870, "AR");
            put(901, "TN");
            put(903, "TX");
            put(904, "FL");
            put(906, "MI");
            put(907, "AK");
            put(908, "NJ");
            put(909, "CA");
            put(910, "NC");
            put(912, "GA");
            put(913, "KS");
            put(914, "NY");
            put(747, "CA");
            put(669, "CA");
            put(657, "CA");
            put(442, "CA");
            put(424, "CA");
            put(917, "NY");
            put(929, "NY");
            put(915, "TX");
            put(916, "CA");
            put(918, "OK");
            put(539, "OK");
            put(919, "NC");
            put(920, "WI");
            put(534, "WI");
            put(925, "CA");
            put(928, "AZ");
            put(931, "TN");
            put(936, "TX");
            put(937, "OH");
            put(234, "OH");
            put(567, "OH");
            put(939, "PR");
            put(940, "TX");
            put(941, "FL");
            put(754, "FL");
            put(786, "FL");
            put(949, "CA");
            put(951, "CA");
            put(952, "MN");
            put(954, "FL");
            put(956, "TX");
            put(970, "CO");
            put(972, "TX");
            put(973, "NJ");
            put(978, "MA");
            put(979, "TX");
            put(985, "LA");
            put(989, "MI");
        }
    };

    private String dataURL = "http://api.sba.gov/geodata/city_links_for_state_of/";

    private AtomicInteger sslFlag = new AtomicInteger(1);

    @Inject
    public Application(DataDAO dataDAO, UserDAO userDAO,
                       ListDAO listDAO, PrefixesDAO prefixesDAO,
                       WSClient wsClient, EmailService emailService) {
        this.dataDAO = dataDAO;
        this.wsClient = wsClient;
        this.userDAO = userDAO;
        this.listDAO = listDAO;
        this.prefixesDAO = prefixesDAO;
        this.emailService = emailService;

        initUsersStatus();

        timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private void initUsersStatus() {
        List<User> users = userDAO.findUsersWithEmptyStatus();
        for (User user : users) {
            if (user.getVerified() == 0) {
                userDAO.updateStatusById(user.getId(), User.Status.NOT_VERIFIED);
            } else {
                userDAO.updateStatusById(user.getId(), User.Status.ACTIVE);
            }
        }
    }

    public Result getSupportPhoneNumber(int userId) {
        return ok(Json.toJson(Response.OK(userDAO.getSupportPhoneNumber(userId))));
    }

    public Result getUserDetails(int userId) {
        User user = userDAO.findUserById(userId);
        user.setPassword("");

        return ok(Json.toJson(Response.OK(user)));
    }

    public Result updateListsHash() {
        List<ListEntity> lists = listDAO.getAllConsumersLists();
        for (ListEntity list : lists) {
            listDAO.updateHashForConsumersListItems(list);
        }

        lists = listDAO.getAllBusinessLists();
        for (ListEntity list : lists) {
            listDAO.updateHashForBusinessListItems(list);
        }

        return ok();
    }

    public Result configureSSL(Integer flag) {
        sslFlag.set(flag);
        return ok();
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result cv() {
        return ok(views.html.cv.render());
    }

    public Result invoice(Integer invoiceNumber) {
        return ok(views.html.invoice.render(invoiceNumber));
    }

    public Result invitation(int resellerNumber) {
        return ok(views.html.invitation.render(resellerNumber));
    }

    public Result getConsumersImportMessage() {
        return ok(Json.toJson(Response.OK(importMessage)));
    }

    public Result getBusinessImportMessage() {
        return ok(Json.toJson(Response.OK(importMessage)));
    }

    public Result message() {
        return ok(Json.toJson(Response.OK(updateMessage)));
    }

    public Result updateCounties(String tableName) {
        String stateColumn = "st";
        DataTable table = dataDAO.getTableByName(tableName);
        if (table.getType() != DataTable.CONSUMERS) {
            stateColumn = "state";
        }

        long maxId = dataDAO.getTableMaxId(tableName);
        long batch = 1000;
        long start = dataDAO.getTableMinIdWithCondition(tableName, "county IS NULL");
        long end = start + batch;

        String indexName = getTableIndexName(tableName, "county_id_indx");
        try {
            dataDAO.dropIndex(indexName, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (start < maxId) {
            dataDAO.updateCountiesForIdRange(tableName, stateColumn, start, end);
            updateMessage = String.format("Updated counties records: %d", start - 1);
            start = start + batch;
            end = end + batch;
        }

        String columns = "COUNTY, id";
        try {
            dataDAO.createIndex(indexName, tableName, columns);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateMessage = "";

        return ok(Json.toJson(Response.OK()));
    }

    private String getTableIndexName(String tableName, String index) {
        List<String> indexes = dataDAO.getIndexesFromTable(tableName);
        for (String dbIndex : indexes) {
            if (dbIndex.contains(index)) {
                return dbIndex;
            }
        }

        return "";
    }

    public Result updateAgeCategories(String tableName) {
        long maxId = dataDAO.getTableMaxId(tableName);
        long batch = 1000;
        long start = dataDAO.getTableMinIdWithCondition(tableName, "age IS NULL");
        long end = start + batch;

        String indexName = getTableIndexName(tableName, "age_id_indx");
        if (indexName.isEmpty()) {
            indexName = tableName + UUID.randomUUID().toString().substring(0, 5) + "_age_id_indx";
        }
        try {
            dataDAO.dropIndex(indexName, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (start < maxId) {
            dataDAO.updateAgeCategoriesForIdRange(tableName, start, end);
            updateMessage = String.format("Updated age categories records: %d", start - 1);
            start = start + batch;
            end = end + batch;
        }

        String columns = "AGE, id";
        try {
            dataDAO.createIndex(indexName, tableName, columns);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateMessage = "";

        return ok(Json.toJson(Response.OK()));
    }

    public Result updateZipCodes(String tableName) {
        long maxId = dataDAO.getTableMaxId(tableName);
        long batch = 1000;
        long start = dataDAO.getTableMinIdWithCondition(tableName, "ZIP_CODE IS NULL");
        long end = start + batch;

        while (start < maxId) {
            dataDAO.updateZipCodesForRange(tableName, start, end);
            updateMessage = String.format("Updated zip codes records: %d", start - 1);
            start = start + batch;
            end = end + batch;
        }

        updateMessage = "";

        return ok(Json.toJson(Response.OK()));
    }

    public Result createMobileUsersData(String tableName) {
        return createDataTableWithType(tableName, DataTable.MOBILE);
    }

    public Result createLandLineUsersData(String tableName) {
        return createDataTableWithType(tableName, DataTable.LANDLINE);
    }

    public Result createDataTableWithType(String tableName, int phoneType) {
        try {
            DataTable table = dataDAO.getTableByName(tableName);
            if (table != null) {
                createTableAndCopyData(tableName, table.getType(), phoneType);
            }

            updateMessage = null;
            return ok(Json.toJson(Response.OK()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ok(Json.toJson(Response.ERROR()));
    }

    private void createTableAndCopyData(String tableName, int type, int phoneType) {
        String newTableName = createTableWithPhoneType(tableName, type, phoneType);

        final AtomicLong start = new AtomicLong(0);
        final AtomicInteger finished = new AtomicInteger(0);

        final long end = dataDAO.getTableMaxId(tableName);
        long batch = 10000;

        int threadsCount = 4;

        for (int i = 0; i < threadsCount; i++) {
            new Thread(() -> {
                long startIndex = start.getAndAdd(batch);
                while (startIndex < end) {
                    int dataPhoneType = 0;
                    if (phoneType == DataTable.MOBILE) {
                        dataPhoneType = 1;
                    }

                    dataDAO.copyDataWithPhoneType(tableName, newTableName, dataPhoneType, startIndex, startIndex + batch);
                    updateMessage = String.format("Copied records: %d", startIndex + batch);

                    startIndex = start.getAndAdd(batch);
                }

                int finishedThreads = finished.incrementAndGet();
                if (finishedThreads == threadsCount) {
                    updateMessage = null;
                }
            }).start();
        }
    }

    public Result updatePhoneTypes(String tableName) {
        long batch = 10000;
        final AtomicLong start = new AtomicLong(0); //dataDAO.getTableMinIdWithCondition(tableName, "phoneType IS NULL");

        int index = 0;
        List<DataTable> tables = dataDAO.getAllTables();
        for (DataTable table: tables) {
            if (table.getName().equals(tableName)) {
                index = tables.indexOf(table);
            }
        }
        int finalIndex = index;

        String indexName = getTableIndexName(tableName, "phone_type_id_indx");
        if (indexName.isEmpty()) {
            indexName = tableName + UUID.randomUUID().toString().substring(0, 5) + "_phone_type_id_indx";
        }

        try {
            //dataDAO.dropIndex(indexName, tableName);
        } catch (Exception e) { /*e.printStackTrace();*/ }

        //Map<String, Boolean> mobilePrefixesMap = new HashMap();
        //Map<String, Boolean> landLinePrefixesMap = new HashMap();
        //updatePhonePrefixesData(mobilePrefixesMap, landLinePrefixesMap);

        Map<Long, Set<Long>> landLinePhones = getLandLinePhones(3);
        long maxId = dataDAO.getTableMaxId(tableName);

        AtomicInteger completed = new AtomicInteger();
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                long currentStart = start.getAndAdd(batch);
                List<PhoneEntity> phoneEntities = dataDAO.getPhoneEntityList(tableName, currentStart, currentStart + batch);
                while (currentStart < maxId + batch) {
                    Iterator<PhoneEntity> it = phoneEntities.iterator();
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

                            /*for (int delta = 0; delta < 2; delta++) {
                                String phonePrefix = phoneEntity.getPHONE().substring(delta, delta + 6);

                                Boolean isMobile = mobilePrefixesMap.get(phonePrefix);
                                if (isMobile != null) {
                                    phoneEntity.setPhoneType(isMobile ? 1 : 0);
                                    break;
                                } else {
                                    phoneEntity.setPhoneType(-1);
                                }
                            }*/
                            }

                            if (oldPhoneType != null && oldPhoneType.equals(phoneEntity.getPhoneType())) {
                                it.remove();
                            }
                        } else {
                            it.remove();
                        }
                    }

                    if (phoneEntities.size() > 0) {
                        dataDAO.updatePhoneEntities(tableName, phoneEntities);
                    }

                    String message = String.format("Updated phone type records: %d", currentStart + batch - 1);
                    Logger.info(String.format("Table: %s (%d/%d). %s", tableName, finalIndex, tables.size(), message));
                    updateMessage = message;

                    currentStart = start.addAndGet(batch);
                    phoneEntities = dataDAO.getPhoneEntityList(tableName, currentStart, currentStart + batch);
                }

                completed.incrementAndGet();
            }).start();
        }

        while (completed.get() != 4) {
            try { Thread.sleep(10000); }
            catch (Exception e) {e.printStackTrace();}
        }

        updateMessage = "";
        landLinePhones.clear();

        return ok(Json.toJson(Response.OK()));
    }

    public static Map<Long, Set<Long>> getLandLinePhones(int digits) {
        Map<Long, Set<Long>> result = new HashMap();

        try {
            FileInputStream inputStream = new FileInputStream("/home/makemydata/workspace/landlinephones.csv");
            Scanner scanner = new Scanner(inputStream, "UTF-8");

            for (int i = (int)Math.pow(10, digits - 1); i < (int)Math.pow(10, digits); i++) {
                result.put(new Long(i), new HashSet());
            }

            long line = 0;
            while (scanner.hasNextLine()) {
                line++;

                String phone = scanner.nextLine();
                if (phone == null || phone.length() < digits) {
                    continue;
                }

                String firstLetter = phone.substring(0, digits);
                Long code = -1l;
                try {code = Long.parseLong(firstLetter);}
                catch (Exception e) {e.printStackTrace();}
                if (code == -1) {continue;}

                Set<Long> phoneSet = result.get(code);
                if (phoneSet != null) {
                    long numPhone = -1;
                    try { numPhone = Long.parseLong(phone); }
                    catch (Exception e) {e.printStackTrace();}
                    if (numPhone == -1) {continue;}

                    phoneSet.add(numPhone);
                }

                if (line % 100000 == 0) {
                    // Logger.info("Handled land line phones lines: {}", line);
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    private Map<Long, Set<Long>> getDataSourceUniquePhones(int digits, String tableName) {
        Map<Long, Set<Long>> result = new HashMap();
        for (int i = (int)Math.pow(10, digits - 1); i < (int)Math.pow(10, digits); i++) {
            result.put(new Long(i), new HashSet());
        }

        //List<DataTable> tables = dataDAO.getVisibleTablesByTypeAndPhoneType(type, DataTable.COMMON);
        List<DataTable> tables = new LinkedList();
        tables.add(dataDAO.getTableByName(tableName));

        for (DataTable table: tables) {
            long maxId = getTableMaxId(table);
            if (maxId == -1) {
                continue;
            }

            long id = 0;
            long batch = 100000;
            while (id < (maxId - batch)) {
                List<String> phones = dataDAO.getPhoneByTableNameAndIdRange(table.getName(), id, id + batch);
                for (String phone: phones) {
                    if (phone == null || phone.length() < digits) {
                        continue;
                    }

                    String firstLetters = phone.substring(0, digits);
                    Long code = -1l;
                    try {code = Long.parseLong(firstLetters);}
                    catch (Exception e) {e.printStackTrace();}
                    if (code == -1) {continue;}

                    Set<Long> phoneSet = result.get(code);
                    if (phoneSet != null) {
                        long numPhone = -1;
                        try { numPhone = Long.parseLong(phone); }
                        catch (Exception e) {e.printStackTrace();}
                        if (numPhone == -1) {continue;}

                        phoneSet.add(numPhone);
                    }
                }

                id = id + batch;
                Logger.info("Getting unique phones. Table name: {}, table index: {}, handled records: {}",
                        table.getName(),
                        tables.indexOf(table),
                        id);
            }
        }

        return result;
    }

    private long getTableMaxId(DataTable dataTable) {
        try {
            return dataDAO.getTableMaxId(dataTable.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /*private void updatePhonePrefixesData(Map<String, Boolean> mobilePhonePrefixes,
                                         Map<String, Boolean> landLinePhonePrefixes) {
        int offset = 0;
        int page = 1;

        List<PhonePrefix> prefixes = prefixesDAO.getPrefixList(new PrefixListRequest(offset, 10000, "id", page));
        offset = offset + prefixes.size();
        page++;

        while (prefixes == null || prefixes.size() > 0) {
            for (PhonePrefix prefix : prefixes) {
                if (prefix.getType() == 1) {
                    mobilePhonePrefixes.put(prefix.getPrefix().substring(0, 6), true);
                } else if (prefix.getType() == 0) {
                    landLinePhonePrefixes.put(prefix.getPrefix().substring(0, 6), true);
                }
            }

            prefixes = prefixesDAO.getPrefixList(new PrefixListRequest(offset, 10000, "id", page));
            offset = offset + prefixes.size();
            page++;
        }
    }*/

    /*private int checkPhoneType(String phone,
                               Map<String, Boolean> mobilePrefixesMap,
                               Map<String, Boolean> landLinePrefixesMap) {
        int result = DataTable.COMMON;
        if (phone == null || phone.length() == 0) {
            return result;
        }

        for (int delta = 0; delta < 2; delta++) {
            if (phone.length() >= delta + 6) {
                String phonePrefix = phone.substring(delta, delta + 6);

                Boolean isMobile = mobilePrefixesMap.get(phonePrefix);
                if (isMobile != null) {
                    result = DataTable.MOBILE;
                    break;
                }

                Boolean isLandLine = landLinePrefixesMap.get(phonePrefix);
                if (isLandLine != null) {
                    result = DataTable.LANDLINE;
                    break;
                }
            }
        }

        return result;
    }*/

    private int checkPhoneType(String phone, Map<Long, Set<Long>> landLinePhones) {
        if (phone != null && phone.length() > 3) {
            String startChars = phone.substring(0, 3);

            Long code = -1l;
            try {code = Long.parseLong(startChars);}
            catch (Exception e) {
                Logger.info("Wrong phone: " + phone);
                /*e.printStackTrace();*/
            }

            if (code == -1) {return DataTable.COMMON;}

            Set<Long> landLinePhoneSet = landLinePhones.get(code);
            if (landLinePhoneSet != null) {
                Long numPhone = -1l;

                try {numPhone = Long.parseLong(phone);}
                catch (Exception e) {/*e.printStackTrace();*/}

                if (numPhone == -1) {return DataTable.COMMON;}

                if (landLinePhoneSet.contains(numPhone)) {
                    return DataTable.LANDLINE;
                } else {
                    return DataTable.MOBILE;
                }
            }
        }

        return DataTable.COMMON;
    }

    public static boolean isPhoneUnique(String phone, Map<Long, Set<Long>> uniquePhones, int charsCount) {
        if (phone != null && phone.length() > charsCount) {
            String startChars = phone.substring(0, charsCount);

            Long code = -1l;
            try {code = Long.parseLong(startChars);}
            catch (Exception e) {
                e.printStackTrace();
            }

            if (code == -1) {return false;}

            Set<Long> uniquePhonesSet = uniquePhones.get(code);
            if (uniquePhonesSet == null) {
                uniquePhonesSet = new HashSet();
                uniquePhones.put(code, uniquePhonesSet);
            }

            Long numPhone = -1l;
            try {numPhone = Long.parseLong(phone);}
            catch (Exception e) {
                e.printStackTrace();
            }

            if (numPhone == -1) {return false;}

            if (uniquePhonesSet.contains(numPhone)) {
                return false;
            } else {
                uniquePhonesSet.add(numPhone);
                return true;
            }
        }

        return false;
    }

    public Result getTables(int type) {
        List<DataTable> results = new LinkedList();

        results.addAll(dataDAO.getVisibleTablesByTypeAndPhoneType(type, DataTable.COMMON));
        results.addAll(dataDAO.getVisibleTablesByTypeAndPhoneType(type, DataTable.LANDLINE));
        results.addAll(dataDAO.getVisibleTablesByTypeAndPhoneType(type, DataTable.MOBILE));

        User user = getCurrentUser();
        if (user == null || user.getAdmin() == 0) {
            Iterator<DataTable> it = results.iterator();
            while (it.hasNext()) {
                DataTable dataTable = it.next();

                if (dataTable.getInnerType() > 0) {
                    it.remove();
                }
            }
        }

        List<DataTable> removed = new LinkedList();
        Iterator<DataTable> it = results.iterator();
        while (it.hasNext()) {
            DataTable dataTable = it.next();
            if (dataTable.getName().toLowerCase().contains("old") ||
                    dataTable.getName().toLowerCase().contains("new")) {
                removed.add(dataTable);
                it.remove();
            }
        }

        it = results.iterator();
        while (it.hasNext()) {
            DataTable dataTable = it.next();
            if (dataTable.getName().toLowerCase().contains("inner")) {
                removed.add(dataTable);
                it.remove();
            }
        }

        results.addAll(removed);

        if (user != null) {
            Map<Integer, Set<Integer>> tableAccess = generateTableAccessMap();
            Set<Integer> tableIds = tableAccess.get(user.getId());
            if (tableIds != null) {
                for (Integer tableId : tableIds) {
                    DataTable table = dataDAO.getTableById(tableId);
                    if (table != null && table.getType() == type && !results.contains(table)) {
                        results.add(table);
                    }
                }
            }
        }

        return ok(Json.toJson(Response.OK(results)));
    }

    private Map<Integer, Set<Integer>> generateTableAccessMap() {
        List<TableAccess> tableAccesses = dataDAO.getAllTableAccess();

        Map<Integer, Set<Integer>> results = new HashMap();
        for (TableAccess tableAccess: tableAccesses) {
            if (!results.containsKey(tableAccess.getUserId())) {
                results.put(tableAccess.getUserId(), new HashSet());
            }

            results.get(tableAccess.getUserId()).add(tableAccess.getTableId());
        }

        return results;
    }

    public Result getAllTables() {
        return ok(Json.toJson(Response.OK(dataDAO.getAllTables())));
    }

    public Result getAllTablesByType(int type) {
        return ok(Json.toJson(Response.OK(dataDAO.getAllTablesByType(type))));
    }

    public Result removeTable(int id) {
        DataTable table = dataDAO.getTableById(id);
        if (table != null) {
            int count = listDAO.findListCountByTableName(table.getName());
            if (count > 0) {
                return ok(Json.toJson(Response.ERROR()));
            } else {
                dataDAO.removeTableById(table.getId(), table.getName());
                return ok(Json.toJson(Response.OK()));
            }
        } else {
            return ok(Json.toJson(Response.ERROR()));
        }
    }

    @Transactional
    public Result saveTable() throws Exception {
        DataTable dataTable = Json.fromJson(request().body().asJson(), DataTable.class);
        dataTable.setName(dataTable.getName().replace(" ", "_").replace(".", "_"));

        DataTable dbDataTable = dataDAO.getTableByName(dataTable.getName());
        if (dbDataTable != null && dbDataTable.getId() != dataTable.getId()) {
            return ok(Json.toJson(Response.ERROR()));
        } else {
            if (dataTable.getId() > 0) {
                dbDataTable = dataDAO.getTableById((int) dataTable.getId());
                dataDAO.updateTable(dataTable);

                if (!dbDataTable.getName().equals(dataTable.getName())) {
                    dataDAO.renameTable(dbDataTable.getName(), dataTable.getName());
                    listDAO.updateTableNames(dbDataTable.getName(), dataTable.getName());
                }
            } else {
                dataDAO.insertTable(dataTable);
                switch (dataTable.getType()) {
                    case DataTable.CONSUMERS:
                        dataDAO.createConsumersTable(dataTable.getName(), true);
                        break;
                    case DataTable.BUSINESS:
                    case DataTable.BUSINESS2:
                        dataDAO.createBusinessTable(dataTable.getName(), true);
                        break;
                    case DataTable.DIRECTORY:
                        dataDAO.createDirectoryTable(dataTable.getName());
                        break;
                    case DataTable.PHILDIRECTORY:
                        dataDAO.createPhilDirectoryTable(dataTable.getName());
                        break;
                    case DataTable.CRAIGSLIST:
                        dataDAO.createCraigslistTable(dataTable.getName());
                        break;
                    case DataTable.WHOIS:
                        dataDAO.createWhoIsTable(dataTable.getName());
                        break;
                    case DataTable.SEARCH_ENGINE:
                        dataDAO.createSearchEngineTable(dataTable.getName());
                        break;
                    case DataTable.CONSUMERS2:
                    case DataTable.CONSUMERS3:
                        dataDAO.createConsumers2Table(dataTable.getName());
                        break;
                    case DataTable.INSTAGRAM:
                        dataDAO.createInstagramTable(dataTable.getName());
                        break;
                    case DataTable.INSTAGRAM2020:
                        dataDAO.createInstagram2020Table(dataTable.getName());
                        break;
                    case DataTable.OPTIN:
                        dataDAO.createOptInTable(dataTable.getName());
                        break;
                    case DataTable.NEWOPTIN:
                        dataDAO.createNewOptInTable(dataTable.getName(),true);
                        break;
                    case DataTable.EVERYDATA:
                        dataDAO.createEverydataTable(dataTable.getName(),true);
                        break;
                    case DataTable.AUTO:
                        dataDAO.createAutoTable(dataTable.getName());
                        break;
                    case DataTable.BLACKLIST:
                        dataDAO.createBlackListTable(dataTable.getName());
                        break;
                    case DataTable.CALLLEADS:
                        dataDAO.createCallLeadsTable(dataTable.getName());
                        break;
                    case DataTable.LINKEDIN:
                        dataDAO.createLinkedInTable(dataTable.getName());
                        break;
                    case DataTable.BUSINESS_DETAILED:
                        dataDAO.createBusinessDetailedTable(dataTable.getName());
                        break;
                    case DataTable.STUDENT:
                        dataDAO.createStudentTable(dataTable.getName());
                        break;
                    case DataTable.HEALTH_BUYER:
                        dataDAO.createHealthBuyerTable(dataTable.getName());
                        break;
                    case DataTable.HEALTH_INSURANCE_LEAD:
                        dataDAO.createHealthInsuranceLeadTable(dataTable.getName());
                        break;
                    case DataTable.FACEBOOK:
                        dataDAO.createFacebookTable(dataTable.getName(),true);
                        break;
                }
            }

            return ok(Json.toJson(Response.OK()));
        }
    }

    public Result importOptInAddon() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        ImportDataRequest importDataRequest = Json.fromJson(request().body().asJson(), ImportDataRequest.class);
        Map<Long, Set<Long>> optinUniquePhones = getDataSourceUniquePhones(3, "OptInOriginal");

        return importData(importDataRequest.getPath(),
                importDataRequest.getTableName(),
                ' ',
                importDataRequest.getSkipRecords(),
                DataTable.OPTIN,
                OptIn.class,
                true,
                new IUpdateDataItemHandler() {
                    @Override
                    public <T> boolean updateItem(T item,
                                                  List<T> mobileDataList,
                                                  List<T> landLineDataList,
                                                  Map<Long, Set<Long>> landLinePhones,
                                                  Map<Long, Set<Long>> cleanPhones,
                                                  Map<String, String> areaCodeToStateMap,
                                                  Map<String, String> cityToStateMap,
                                                  Map<String, String> stateNameToAbbreviation,
                                                  Map<String, String> cityToCountyMap,
                                                  Set<String> stateCodes,
                                                  Map<Integer, List<Long>> ageCategoriesRanges,
                                                  String fileName) {
                        OptIn optIn = (OptIn)item;
                        if (isBanned(optIn.getPhone(), null)) {
                            return false;
                        }

                        if ("email".equalsIgnoreCase(optIn.getEmail())) {
                            return false;
                        }

                        if (optIn.getPhone() == null) {
                            return false;
                        }

                        String repairedPhone = checkAndRepairPhone(optIn.getPhone());
                        if (repairedPhone == null) {
                            return false;
                        } else {
                            optIn.setPhone(repairedPhone);
                        }

                        if (!isPhoneUnique(optIn.getPhone(), optinUniquePhones, 3)) {
                            return false;
                        }

                        if (optIn.getCounty() == null || optIn.getCounty().length() == 0) {
                            optIn.setCounty(getCountyByCity(optIn.getCity(), optIn.getCounty(), cityToCountyMap));
                        }

                        optIn.setStrDate(optIn.getStrDate());

                        int phoneType = checkPhoneType(
                                optIn.getPhone(), landLinePhones);
                        optIn.setPhoneType(convertPhoneType(phoneType));
                        if (DataTable.MOBILE == phoneType) {
                            mobileDataList.add(item);
                        } else if (DataTable.LANDLINE == phoneType){
                            landLineDataList.add(item);
                        }

                        boolean isClearPhone = checkIfClearPhone(optIn.getPhone(), cleanPhones);
                        optIn.setDnc(!isClearPhone);

                        return true;
                    }
                },
                new IWriteDataItemsHandler() {
                    @Override
                    public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                        dataDAO.insertOptIn((List<OptIn>)dataList, tableName);
                        dataList.clear();
                    }
                });
    }

    public Result importBusinessAddon() {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        ImportDataRequest importDataRequest = Json.fromJson(request().body().asJson(), ImportDataRequest.class);
        Map<Long, Set<Long>> businessUniquePhones = getDataSourceUniquePhones(2, "BusinessOriginal");

        return importData(importDataRequest.getPath(),
                importDataRequest.getTableName(),
                ',',
                importDataRequest.getSkipRecords(),
                DataTable.BUSINESS,
                Business.class,
                true,
                new IUpdateDataItemHandler() {
                    @Override
                    public <T> boolean updateItem(T item,
                                                  List<T> mobileDataList,
                                                  List<T> landLineDataList,
                                                  Map<Long, Set<Long>> landLinePhones,
                                                  Map<Long, Set<Long>> cleanPhones,
                                                  Map<String, String> areaCodeToStateMap,
                                                  Map<String, String> cityToStateMap,
                                                  Map<String, String> stateNameToAbbreviation,
                                                  Map<String, String> cityToCountyMap,
                                                  Set<String> stateCodes,
                                                  Map<Integer, List<Long>> ageCategoriesRanges,
                                                  String fileName) {
                        Business business = (Business) item;

                        String repairedPhone = checkAndRepairPhone(business.getPHONE());
                        if (repairedPhone == null) {
                            return false;
                        } else {
                            business.setPHONE(repairedPhone);
                        }

                        if (business.getPHONE() == null) {
                            return false;
                        }

                        /*if (!isPhoneUnique(business.getPHONE(), businessUniquePhones, 2)) {
                            return false;
                        }*/

                        if (business.getCOUNTY() != null && business.getCOUNTY().length() == 0) {
                            business.setCOUNTY(getCountyByCity(business.getCITY(), business.getCOUNTY(), cityToCountyMap));
                        }

                        if (business.getCOUNTY() != null && business.getCOUNTY().length() > 1) {
                            String county = business.getCOUNTY().toLowerCase();
                            business.setCOUNTY(WordUtils.capitalize(county));
                        }

                        if (business.getCITY() != null && business.getCITY().length() > 1) {
                            String city = business.getCITY().toLowerCase();
                            business.setCITY(WordUtils.capitalize(city));
                        }

                        business.setANNUAL_SALES(Business.updateSales(business.getANNUAL_SALES()));
                        business.setEMPLOYEE(Business.updateEmployees(business.getEMPLOYEE()));

                        boolean isClearPhone = checkIfClearPhone(business.getPHONE(), cleanPhones);
                        business.setDnc(!isClearPhone);

                        int phoneType = checkPhoneType(
                                business.getPHONE(), landLinePhones);
                        business.setPhoneType(convertPhoneType(phoneType));

                        if (DataTable.MOBILE == phoneType) {
                            mobileDataList.add(item);
                        } else if (DataTable.LANDLINE == phoneType){
                            landLineDataList.add(item);
                        }

                        return true;
                    }
                },
                new IWriteDataItemsHandler() {
                    @Override
                    public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                        if (dataList.size() > 0) {
                            writeBusinessToDatabase((List<Business>)dataList, tableName);
                            dataList.clear();
                        }
                    }
                });
    }

    public Result importData(int type, String path, String tableName, long skipRecords) {
        switch (type) {
            case DataTable.CONSUMERS:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.CONSUMERS,
                        Consumer.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Consumer consumer = (Consumer)item;
                                consumer.setAge(getAgeCategory(consumer.getDOB_DATE(), ageCategoriesRanges));
                                consumer.setCOUNTY(getCountyByCity(consumer.getCITY(), consumer.getCOUNTY(), cityToCountyMap));

                                int phoneType = checkPhoneType(
                                        consumer.getPHONE(), landLinePhones);
                                consumer.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                writeConsumersToDatabase((List<Consumer>) dataList, tableName);
                                dataDAO.updateConsumersHashByIdRange(tableName, leftId, rightId);
                            }
                        });
            case DataTable.BUSINESS:
            case DataTable.BUSINESS2:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        type,
                        Business.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Business business = (Business) item;
                                if (isBanned(business.getPHONE(), business.getCOMPANY_NAME())) {
                                    return false;
                                }

                                business.setANNUAL_SALES(Business.updateSales(business.getANNUAL_SALES()));
                                business.setEMPLOYEE(Business.updateEmployees(business.getEMPLOYEE()));

                                boolean isClearPhone = checkIfClearPhone(business.getPHONE(), cleanPhones);
                                business.setDnc(!isClearPhone);

                                if (business.getCOUNTY() != null && business.getCOUNTY().length() == 0) {
                                    business.setCOUNTY(getCountyByCity(business.getCITY(), business.getCOUNTY(), cityToCountyMap));
                                }

                                if (business.getCOUNTY() != null && business.getCOUNTY().length() > 1) {
                                    String county = business.getCOUNTY().toLowerCase();
                                    business.setCOUNTY(WordUtils.capitalize(county));
                                }

                                if (business.getCITY() != null && business.getCITY().length() > 1) {
                                    String city = business.getCITY().toLowerCase();
                                    business.setCITY(WordUtils.capitalize(city));
                                }

                                int phoneType = checkPhoneType(
                                        business.getPHONE(), landLinePhones);
                                business.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                writeBusinessToDatabase((List<Business>) dataList, tableName);
                            }
                        });
            case DataTable.DIRECTORY:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.DIRECTORY,
                        Directory.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Directory directory = (Directory) item;

                                String repairedPhone = checkAndRepairPhone(directory.getPHONE());
                                if (repairedPhone == null) {
                                    return false;
                                } else {
                                    directory.setPHONE(repairedPhone);
                                }

                                if (isBanned(directory.getPHONE(), directory.getCOMPANY_NAME())) {
                                    return false;
                                }

                                if (!stateCodes.contains(directory.getSTATE()) && directory.getAREA_CODE() != null) {
                                    String state = Application.AREA_CODE_TO_STATE_MAP.get(directory.getAREA_CODE());
                                    if (state != null) {
                                        directory.setSTATE(state);
                                    } else {
                                        return false;
                                    }
                                }

                                directory.setCOUNTY(getCountyByCity(directory.getCITY(), directory.getCOUNTY(), cityToCountyMap));

                                String source = fileName.split("_")[0];
                                directory.setWWW(source);

                                try {
                                    Date date = commonDateFormat.parse(fileName.split("_")[1].split(".csv")[0]);
                                    directory.setTIMESTAMP(date.getTime());
                                } catch (Exception e) { e.printStackTrace(); }

                                int phoneType = checkPhoneType(
                                        directory.getPHONE(), landLinePhones);
                                directory.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(directory.getPHONE(), cleanPhones);
                                directory.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                writeDirectoryToDatabase((List<Directory>) dataList, tableName);
                            }
                        });
            case DataTable.PHILDIRECTORY:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.PHILDIRECTORY,
                        Directory.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Directory directory = (Directory) item;

                                String repairedPhone = checkAndRepairPhone(directory.getPHONE());
                                if (repairedPhone == null) {
                                    return false;
                                } else {
                                    directory.setPHONE(repairedPhone);
                                }

                                if (isBanned(directory.getPHONE(), directory.getCOMPANY_NAME())) {
                                    return false;
                                }

                                if (!stateCodes.contains(directory.getSTATE()) && directory.getAREA_CODE() != null) {
                                    String state = Application.AREA_CODE_TO_STATE_MAP.get(directory.getAREA_CODE());
                                    if (state != null) {
                                        directory.setSTATE(state);
                                    } else {
                                        return false;
                                    }
                                }

                                directory.setCOUNTY(getCountyByCity(directory.getCITY(), directory.getCOUNTY(), cityToCountyMap));

                                String source = fileName.split("_")[0];
                                directory.setWWW(source);

                                try {
                                    Date date = commonDateFormat.parse(fileName.split("_")[1].split(".csv")[0]);
                                    directory.setTIMESTAMP(date.getTime());
                                } catch (Exception e) { e.printStackTrace(); }

                                int phoneType = checkPhoneType(
                                        directory.getPHONE(), landLinePhones);
                                directory.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(directory.getPHONE(), cleanPhones);
                                directory.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                writeDirectoryToDatabase((List<Directory>) dataList, tableName);
                            }
                        });
            case DataTable.CRAIGSLIST:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.CRAIGSLIST,
                        CraigsList.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                CraigsList craigItem = (CraigsList) item;
                                if (isBanned(craigItem.getPHONE(), null)) {
                                    return false;
                                }

                                if (craigItem.getPHONE() != null && craigItem.getPHONE().length() > 0) {
                                    String repairedPhone = checkAndRepairPhone(craigItem.getPHONE());
                                    if (repairedPhone == null) {
                                        return false;
                                    } else {
                                        craigItem.setPHONE(repairedPhone);
                                    }
                                }

                                if (craigItem.getPHONE() != null) {
                                    try {
                                        Integer areaCode = Integer.parseInt(craigItem.getPHONE().substring(0, 3));
                                        craigItem.setSTATE(Application.AREA_CODE_TO_STATE_MAP.get(areaCode));
                                    } catch (Exception e) {e.printStackTrace();}
                                }

                                int phoneType = checkPhoneType(
                                        craigItem.getPHONE(), landLinePhones);
                                craigItem.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(craigItem.getPHONE(), cleanPhones);
                                craigItem.setDnc(!isClearPhone);

                                try {
                                    craigItem.setDate(craigsDateFormat.parse(fileName.split("\\.")[0]).getTime());
                                } catch (Exception e) { e.printStackTrace(); }

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long maxId, long l) {
                                writeCraigslistToDatabase((List<CraigsList>) dataList, tableName);
                            }
                        });
            case DataTable.WHOIS:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.WHOIS,
                        WhoIs.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                WhoIs whoIs = (WhoIs) item;
                                if (isBanned(whoIs.getPhone(), whoIs.getBusiness())) {
                                    return false;
                                }

                                whoIs.setPhone(checkAndRepairPhone(whoIs.getPhone()));
                                if (whoIs.getPhone() == null) {
                                    return false;
                                }

                                if (!stateCodes.contains(whoIs.getState())) {
                                    String fixedState = tryToFixState(
                                            whoIs.getState(), whoIs.getCity(),
                                            cityToStateMap, stateCodes, stateNameToAbbreviation);
                                    if (fixedState != null) {
                                        whoIs.setState(fixedState);
                                    } else {
                                        return false;
                                    }
                                }

                                whoIs.setCOUNTY(getCountyByCity(whoIs.getCity(), whoIs.getCOUNTY(), cityToCountyMap));
                                try {
                                    whoIs.setDate(whoIsDateFormat.parse(whoIs.getCreateStrDate()).getTime());
                                } catch (Exception e) { e.printStackTrace(); }

                                int phoneType = checkPhoneType(
                                        whoIs.getPhone(), landLinePhones);
                                whoIs.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(whoIs.getPhone(), cleanPhones);
                                whoIs.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long maxId, long l) {
                                writeWhoIsToDatabase((List<WhoIs>) dataList, tableName);
                            }
                        });
            case DataTable.SEARCH_ENGINE:
                return importData(path,
                        tableName,
                        ';',
                        skipRecords,
                        DataTable.SEARCH_ENGINE,
                        SearchEngine.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                SearchEngine searchEngine = (SearchEngine)item;
                                if (isBanned(searchEngine.getPhone(), null)) {
                                    return false;
                                }

                                if (searchEngine.getPhone() == null ||
                                        searchEngine.getPhone().length() < 10 ||
                                        searchEngine.getPhone().length() > 11) {
                                    return false;
                                }

                                if (searchEngine.getPhone() != null) {
                                    searchEngine.setSTATE(areaCodeToStateMap.get(searchEngine.getPhone().substring(0, 3)));
                                }

                                int phoneType = checkPhoneType(
                                        searchEngine.getPhone(), landLinePhones);
                                searchEngine.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long maxId, long l) {
                                writeSearchEngineToDatabase((List<WhoIs>) dataList, tableName);
                                dataDAO.updateSearchEngineListHashByIdRange(tableName, maxId, maxId + 100);
                            }
                        });
            case DataTable.CONSUMERS2:
            case DataTable.CONSUMERS3:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        type,
                        Consumer2.class,
                        false,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Consumer2 consumer = (Consumer2)item;
                                if (isBanned(consumer.getPHONE(), null)) {
                                    return false;
                                }

                                if (consumer.getADDRESSID().equalsIgnoreCase("ADDRESSID")) {
                                    return false;
                                }

                                if (consumer.getPERSONFIRSTNAME() != null) {
                                    consumer.setPERSONFIRSTNAME(consumer.getPERSONFIRSTNAME().trim());
                                }

                                if (consumer.getPERSONLASTNAME() != null) {
                                    consumer.setPERSONLASTNAME(consumer.getPERSONLASTNAME().trim());
                                }

                                consumer.setPERSONAGE(getAgeCategory(consumer.getPERSONDATEOFBIRTHDATE(), ageCategoriesRanges));
                                consumer.updatePhone();

                                if (!stateCodes.contains(consumer.getSTATE())) {
                                    String fixedState = tryToFixState(
                                            consumer.getSTATE(), consumer.getCITYNAME(),
                                            cityToStateMap, stateCodes, stateNameToAbbreviation);
                                    if (fixedState != null) {
                                        consumer.setSTATE(fixedState);
                                    }
                                }

                                int phoneType = checkPhoneType(
                                        consumer.getPHONE(), landLinePhones);
                                consumer.setPHONETYPE(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(consumer.getPHONE(), cleanPhones);
                                consumer.setDNC(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                writeConsumers2ToDatabase((List<Consumer2>) dataList, tableName);
                            }
                        });
            case DataTable.INSTAGRAM:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.INSTAGRAM,
                        Instagram.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Instagram instagram = (Instagram)item;
                                if (isBanned(instagram.getPhone(), null)) {
                                    return false;
                                }

                                instagram.setStr_date(fileName.split("\\.csv")[0]);

                                if (!stateCodes.contains(instagram.getState())) {
                                    String fixedState = tryToFixState(
                                            instagram.getState(), instagram.getCity(),
                                            cityToStateMap, stateCodes, stateNameToAbbreviation);
                                    if (fixedState != null) {
                                        instagram.setState(fixedState);
                                    }
                                }

                                instagram.setCounty(getCountyByCity(instagram.getCity(), instagram.getCounty(), cityToCountyMap));

                                int phoneType = checkPhoneType(
                                        instagram.getPhone(), landLinePhones);
                                instagram.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(instagram.getPhone(), cleanPhones);
                                instagram.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertInstagram((List<Instagram>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.FACEBOOK:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.FACEBOOK,
                        Facebook.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Facebook facebook = (Facebook)item;

                                if (isBanned(facebook.getPhone(), null)) {
                                    return false;
                                }

                                facebook.setST(NAME_TO_STATE.get(facebook.getST().replaceFirst(" ","")));

                                if (!stateCodes.contains(facebook.getST())) {
                                    String fixedState = tryToFixState(
                                            facebook.getST(), facebook.getCity(),
                                            cityToStateMap, stateCodes, stateNameToAbbreviation);
                                    if (fixedState != null) {
                                        facebook.setST(fixedState);
                                    }
                                }

                                facebook.setCounty(getCountyByCity(facebook.getCity(), facebook.getCounty(), cityToCountyMap));

                                int phoneType = checkPhoneType(
                                        facebook.getPhone(), landLinePhones);
                                facebook.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(facebook.getPhone(), cleanPhones);
                                facebook.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertFacebook((List<Facebook>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.INSTAGRAM2020:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.INSTAGRAM2020,
                        Instagram2020.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Instagram2020 instagram = (Instagram2020)item;
                                if (isBanned(instagram.getPhone(), null)) {
                                    return false;
                                }

                                if (!stateCodes.contains(instagram.getState())) {
                                    String fixedState = tryToFixState(
                                            instagram.getState(), instagram.getCity(),
                                            cityToStateMap, stateCodes, stateNameToAbbreviation);
                                    if (fixedState != null) {
                                        instagram.setState(fixedState);
                                    }
                                }

                                instagram.setCounty(getCountyByCity(instagram.getCity(), instagram.getCounty(), cityToCountyMap));

                                try {
                                    instagram.setDate(commonDateFormat.parse(fileName.split(".csv")[0]).getTime());
                                } catch (Exception e) { e.printStackTrace(); }

                                int phoneType = checkPhoneType(
                                        instagram.getPhone(), landLinePhones);
                                instagram.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(instagram.getPhone(), cleanPhones);
                                instagram.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertInstagram2020((List<Instagram2020>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.OPTIN:
                return importData(path,
                        tableName,
                        ' ',
                        skipRecords,
                        DataTable.OPTIN,
                        OptIn.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                OptIn optIn = (OptIn)item;
                                if (isBanned(optIn.getPhone(), null)) {
                                    return false;
                                }

                                if ("email".equalsIgnoreCase(optIn.getEmail())) {
                                    return false;
                                }

                                optIn.setCounty(getCountyByCity(optIn.getCity(), optIn.getCounty(), cityToCountyMap));
                                optIn.setStrDate(optIn.getStrDate());

                                String repairedPhone = checkAndRepairPhone(optIn.getPhone());
                                if (repairedPhone == null) {
                                    return false;
                                } else {
                                    optIn.setPhone(repairedPhone);
                                }

                                int phoneType = checkPhoneType(
                                        optIn.getPhone(), landLinePhones);
                                optIn.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(optIn.getPhone(), cleanPhones);
                                optIn.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertOptIn((List<OptIn>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.NEWOPTIN:
                return importData(path,
                        tableName,
                        ' ',
                        skipRecords,
                        DataTable.NEWOPTIN,
                        NewOptIn.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                NewOptIn optIn = (NewOptIn)item;
                                if (isBanned(String.valueOf(optIn.getPhone()), null)) {
                                    return false;
                                }

                                if ("email".equalsIgnoreCase(optIn.getEmail())) {
                                    return false;
                                }

                                optIn.setCounty(getCountyByCity(optIn.getCity(), optIn.getCounty(), cityToCountyMap));

                                //  optIn.setCounty(getCountyByCity(optIn.getCity(), optIn.getCounty(), cityToCountyMap));
                                // optIn.setStrDate(optIn.getStrDate());

                                String repairedPhone = checkAndRepairPhone(optIn.getPhone());
                                if (repairedPhone == null) {
                                    return false;
                                } else {
                                    optIn.setPhone(repairedPhone);
                                }

                                int phoneType = checkPhoneType(
                                        optIn.getPhone(), landLinePhones);
                                optIn.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(optIn.getPhone(), cleanPhones);
                                optIn.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertNewOptIn((List<NewOptIn>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.EVERYDATA:
                return importData(path,
                        tableName,
                        ' ',
                        skipRecords,
                        DataTable.EVERYDATA,
                        Everydata.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Everydata everydata = (Everydata)item;
                                if (isBanned(String.valueOf(everydata.getPHONE()), null)) {
                                    return false;
                                }

                                everydata.setCOUNTY(getCountyByCity(everydata.getCITY(), everydata.getCOUNTY(), cityToCountyMap));

                                String repairedPhone = checkAndRepairPhone(everydata.getPHONE());
                                if (repairedPhone == null) {
                                    return false;
                                } else {
                                    everydata.setPHONE(repairedPhone);
                                }

                                int phoneType = checkPhoneType(
                                        everydata.getPHONE(), landLinePhones);
                                everydata.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(everydata.getPHONE(), cleanPhones);
                                everydata.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertEverydata((List<Everydata>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.AUTO:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.AUTO,
                        Auto.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Auto auto = (Auto)item;
                                if (isBanned(auto.getPHONE(), null)) {
                                    return false;
                                }

                                auto.setCOUNTY(getCountyByCity(auto.getCITY(), auto.getCOUNTY(), cityToCountyMap));
                                auto.setStrDate(fileName.split("\\.csv")[0]);

                                String fixedState = tryToFixState(
                                        auto.getSTATE(), auto.getCITY(),
                                        cityToStateMap, stateCodes, stateNameToAbbreviation);
                                if (fixedState != null) {
                                    auto.setSTATE(fixedState);
                                }

                                int phoneType = checkPhoneType(
                                        auto.getPHONE(), landLinePhones);
                                auto.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(auto.getPHONE(), cleanPhones);
                                auto.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertAutoList((List<Auto>)dataList, tableName);
                                dataList.clear();
                            }
                        });

            case DataTable.BLACKLIST:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.BLACKLIST,
                        BlackList.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                BlackList blackList = (BlackList)item;

                                if (blackList.getAreaCode() != null) {
                                    blackList.setState(Application.AREA_CODE_TO_STATE_MAP.get(blackList.getAreaCode()));
                                }

                                int phoneType = checkPhoneType(blackList.getPhone(), landLinePhones);
                                blackList.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(blackList.getPhone(), cleanPhones);
                                blackList.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertBlackList((List<BlackList>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.CALLLEADS:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.CALLLEADS,
                        CallLead.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                CallLead callLead = (CallLead) item;

                                String repairedPhone = checkAndRepairPhone(callLead.getPhone());
                                if (repairedPhone == null) {
                                    return false;
                                } else {
                                    callLead.setPhone(repairedPhone);
                                }

                                if (callLead.getAreaCode() != null) {
                                    callLead.setState(Application.AREA_CODE_TO_STATE_MAP.get(callLead.getAreaCode()));
                                }

                                int phoneType = checkPhoneType(callLead.getPhone(), landLinePhones);
                                callLead.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(callLead.getPhone(), cleanPhones);
                                callLead.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertCallLeadList((List<CallLead>)dataList, tableName);
                                dataList.clear();
                            }
                        });

            case DataTable.LINKEDIN:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.LINKEDIN,
                        LinkedIn.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                LinkedIn linkedIn = (LinkedIn)item;
                                if (isBanned(linkedIn.getPhone(), null)) {
                                    return false;
                                }

                                if (linkedIn.getPhone() != null && linkedIn.getPhone().length() > 0) {
                                    String repairedPhone = checkAndRepairPhone(linkedIn.getPhone());
                                    if (repairedPhone == null) {
                                        return false;
                                    } else {
                                        linkedIn.setPhone(repairedPhone);
                                    }
                                }

                                linkedIn.setCounty(getCountyByCity(linkedIn.getCity(), linkedIn.getCounty(), cityToCountyMap));

                                String fixedState = tryToFixState(
                                        linkedIn.getState(), linkedIn.getCity(),
                                        cityToStateMap, stateCodes, stateNameToAbbreviation);
                                if (fixedState != null) {
                                    linkedIn.setState(fixedState);
                                }

                                int phoneType = checkPhoneType(
                                        linkedIn.getPhone(), landLinePhones);
                                linkedIn.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(linkedIn.getPhone(), cleanPhones);
                                linkedIn.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertLinkedInList((List<LinkedIn>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.BUSINESS_DETAILED:
                return importData(path,
                        tableName,
                        '|',
                        skipRecords,
                        DataTable.BUSINESS_DETAILED,
                        BusinessDetailed.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                BusinessDetailed businessDetailed = (BusinessDetailed)item;

                                if (businessDetailed.getPhone() != null && businessDetailed.getPhone().length() > 0) {
                                    String repairedPhone = checkAndRepairPhone(businessDetailed.getPhone());
                                    if (repairedPhone == null) {
                                        return false;
                                    } else {
                                        businessDetailed.setPhone(repairedPhone);
                                    }
                                }

                                if (isBanned(businessDetailed.getPhone(), null)) {
                                    return false;
                                }

                                businessDetailed.setCounty(getCountyByCity(businessDetailed.getCity(), businessDetailed.getCounty(), cityToCountyMap));

                                String fixedState = tryToFixState(
                                        businessDetailed.getState(), businessDetailed.getCity(),
                                        cityToStateMap, stateCodes, stateNameToAbbreviation);
                                if (fixedState != null) {
                                    businessDetailed.setState(fixedState);
                                }

                                int phoneType = checkPhoneType(
                                        businessDetailed.getPhone(), landLinePhones);
                                businessDetailed.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(businessDetailed.getPhone(), cleanPhones);
                                businessDetailed.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertBusinessDetailedList((List<BusinessDetailed>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.STUDENT:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.STUDENT,
                        Student.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                Student student = (Student)item;

                                if (student.getPhone() != null && student.getPhone().length() > 0) {
                                    String repairedPhone = checkAndRepairPhone(student.getPhone());
                                    if (repairedPhone == null) {
                                        return false;
                                    } else {
                                        student.setPhone(repairedPhone);
                                    }
                                }

                                if (student.getAreaCode() != null && (student.getState() == null || student.getState().length() == 0)) {
                                    student.setState(Application.AREA_CODE_TO_STATE_MAP.get(student.getAreaCode()));
                                }

                                if (isBanned(student.getPhone(), null)) {
                                    return false;
                                }

                                student.setCounty(getCountyByCity(student.getCity(), student.getCounty(), cityToCountyMap));
                                student.setFileName(fileName);

                                student.setSource(fileName.split("_")[0]);

                                try {
                                    Date date = commonDateFormat.parse(fileName.split("_")[1].split("\\.csv")[0]);
                                    student.setDate(date.getTime());
                                } catch (Exception e) { e.printStackTrace(); }

                                int phoneType = checkPhoneType(
                                        student.getPhone(), landLinePhones);
                                student.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(student.getPhone(), cleanPhones);
                                student.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertStudentList((List<Student>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.HEALTH_BUYER:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.HEALTH_BUYER,
                        HealthBuyer.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                HealthBuyer healthBuyer = (HealthBuyer)item;

                                if (healthBuyer.getPhone() != null && healthBuyer.getPhone().length() > 0) {
                                    String repairedPhone = checkAndRepairPhone(healthBuyer.getPhone());
                                    if (repairedPhone == null) {
                                        return false;
                                    } else {
                                        healthBuyer.setPhone(repairedPhone);
                                    }
                                }

                                if (isBanned(healthBuyer.getPhone(), null)) {
                                    return false;
                                }

                                healthBuyer.setCounty(getCountyByCity(healthBuyer.getCity(), healthBuyer.getCounty(), cityToCountyMap));

                                int phoneType = checkPhoneType(
                                        healthBuyer.getPhone(), landLinePhones);
                                healthBuyer.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(healthBuyer.getPhone(), cleanPhones);
                                healthBuyer.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertHealthBuyersList((List<HealthBuyer>)dataList, tableName);
                                dataList.clear();
                            }
                        });
            case DataTable.HEALTH_INSURANCE_LEAD:
                return importData(path,
                        tableName,
                        ',',
                        skipRecords,
                        DataTable.HEALTH_INSURANCE_LEAD,
                        HealthInsuranceLead.class,
                        true,
                        new IUpdateDataItemHandler() {
                            @Override
                            public <T> boolean updateItem(T item,
                                                          List<T> mobileDataList,
                                                          List<T> landLineDataList,
                                                          Map<Long, Set<Long>> landLinePhones,
                                                          Map<Long, Set<Long>> cleanPhones,
                                                          Map<String, String> areaCodeToStateMap,
                                                          Map<String, String> cityToStateMap,
                                                          Map<String, String> stateNameToAbbreviation,
                                                          Map<String, String> cityToCountyMap,
                                                          Set<String> stateCodes,
                                                          Map<Integer, List<Long>> ageCategoriesRanges,
                                                          String fileName) {
                                HealthInsuranceLead healthInsuranceLead = (HealthInsuranceLead)item;

                                if (healthInsuranceLead.getPHONE() != null && healthInsuranceLead.getPHONE().length() > 0) {
                                    String repairedPhone = checkAndRepairPhone(healthInsuranceLead.getPHONE());
                                    if (repairedPhone == null) {
                                        return false;
                                    } else {
                                        healthInsuranceLead.setPHONE(repairedPhone);
                                    }
                                }

                                if (isBanned(healthInsuranceLead.getPHONE(), null)) {
                                    return false;
                                }

                                healthInsuranceLead.setCOUNTY(getCountyByCity(
                                        healthInsuranceLead.getCITY(), healthInsuranceLead.getCOUNTY(), cityToCountyMap));

                                int phoneType = checkPhoneType(
                                        healthInsuranceLead.getPHONE(), landLinePhones);
                                healthInsuranceLead.setPhoneType(convertPhoneType(phoneType));
                                if (DataTable.MOBILE == phoneType) {
                                    mobileDataList.add(item);
                                } else if (DataTable.LANDLINE == phoneType){
                                    landLineDataList.add(item);
                                }

                                boolean isClearPhone = checkIfClearPhone(healthInsuranceLead.getPHONE(), cleanPhones);
                                healthInsuranceLead.setDnc(!isClearPhone);

                                return true;
                            }
                        },
                        new IWriteDataItemsHandler() {
                            @Override
                            public <T> void writeDataItems(List<T> dataList, String tableName, long leftId, long rightId) {
                                dataDAO.insertHealthInsuranceLeadList((List<HealthInsuranceLead>)dataList, tableName);
                                dataList.clear();
                            }
                        });
        }

        return notFound();
    }

    private boolean isBanned(String phone, String companyName) {
        if ("7602693107".equals(phone)) {
            return true;
        }

        if ("Anton A Ewing JD".equalsIgnoreCase(companyName)) {
            return true;
        }

        return false;
    }

    private boolean checkIfClearPhone(String phone, Map<Long, Set<Long>> cleanPhonesMap) {
        if (phone != null && phone.length() > 3) {
            String letter = phone.substring(0, 3);

            Set<Long> phoneSet = cleanPhonesMap.get(DataOperations.parseLong(letter));
            long numPhone = DataOperations.parseLong(phone);
            if (phoneSet != null && phoneSet.contains(numPhone)) {
                return true;
            }
        }

        return false;
    }

    private String checkAndRepairPhone(String phone) {
        phone = phone.
                replaceAll(" ", "").
                replaceAll("\\(", "").
                replaceAll("\\)", "").
                replaceAll("\\+", "").
                replaceAll("\\.", "").
                replaceAll("-", "").
                replaceAll(":", "");

        if (phone != null && phone.length() == 10 && !phone.startsWith("1")) {
            return phone;
        }

        if (phone != null && phone.length() == 11 && phone.startsWith("1")) {
            return phone.substring(1, 11);
        }

        return null;
    }

    private void writeSearchEngineToDatabase(List<WhoIs> dataList, String tableName) {
        dataDAO.insertSearchEngineList(dataList, tableName);
        dataList.clear();
    }

    private Integer convertPhoneType(int phoneType) {
        if (phoneType == DataTable.MOBILE) {
            return 1;
        } else if (phoneType == DataTable.LANDLINE) {
            return 0;
        } else {
            return -1;
        }
    }

    private void writeWhoIsToDatabase(List<WhoIs> dataList, String tableName) {
        dataDAO.insertWhoIsList(dataList, tableName);
        dataList.clear();
    }

    private String getCountyByCity(String city, String county, Map<String, String> cityToCountyMap) {

        Logger.info(cityToCountyMap.size()+"");

        if ((county == null || county.length() == 0) && city != null) {
            city = city.toUpperCase();
            if (cityToCountyMap.containsKey(city)) {
                return cityToCountyMap.get(city);
            }
        }

        return null;
    }

    private Map<String, String> getAreaCodeToStateMap() {
        Map<String, String> result = new HashMap();

        int offset = 0;

        List<AreaCode> areaCodes = dataDAO.findAreaCodesWithOffsetAndLimit(offset, 10000);
        offset = offset + areaCodes.size();

        while (areaCodes == null || areaCodes.size() > 0) {
            for (AreaCode areaCode : areaCodes) {
                result.put(String.valueOf(areaCode.getCode()), areaCode.getState());
            }

            areaCodes = dataDAO.findAreaCodesWithOffsetAndLimit(offset, 10000);
            offset = offset + areaCodes.size();
        }

        return result;
    }

    private int getAgeCategory(Long dobDate, Map<Integer, List<Long>> ageCategoriesRanges) {
        if (dobDate == null){ return -1; }

        Iterator<Map.Entry<Integer, List<Long>>> it = ageCategoriesRanges.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, List<Long>> entry = it.next();

            Integer category = entry.getKey();
            List<Long> ranges = entry.getValue();

            if (dobDate >= ranges.get(0) && dobDate <= ranges.get(1)) {
                return category;
            }
        }

        return -1;
    }

    private Map<Integer, List<Long>> getAgeCategoriesRanges() {
        Map<Integer, List<Long>> result = new HashMap();

        long currentTime = System.currentTimeMillis();
        long yearInMs = 365l * 24l * 60l * 60l * 1000l;

        for (int i = 1; i < 8; i++) {
            long left;
            long right;

            if (i == 1) {
                right = currentTime - 18 * yearInMs;
                left = currentTime - 25 * yearInMs;
            } else if (i == 7) {
                right = currentTime - 75 * yearInMs;
                left = currentTime - 160 * yearInMs;
            } else {
                int delta = 25 + (i - 2) * 10;

                right = currentTime - delta * yearInMs;
                left = currentTime - (delta + 10) * yearInMs;
            }

            List<Long> ranges = new LinkedList();
            ranges.add(left);
            ranges.add(right);

            result.put(i, ranges);
        }

        return result;
    }

    private String createTableWithPhoneType(String tableName, int type, int phoneType) {
        String tableNameWithPhoneType;
        tableName = tableName.replace("Original", "");
        if (DataTable.MOBILE == phoneType) {
            tableNameWithPhoneType = tableName + "Mobile";
        } else {
            tableNameWithPhoneType = tableName + "Landlines";
        }
        DataTable table = dataDAO.getTableByName(tableNameWithPhoneType);
        if (table == null) {
            switch (type) {
                case DataTable.CONSUMERS:
                    dataDAO.createConsumersTable(tableNameWithPhoneType, true);
                    break;
                case DataTable.BUSINESS:
                case DataTable.BUSINESS2:
                    dataDAO.createBusinessTable(tableNameWithPhoneType, true);
                    break;
                case DataTable.DIRECTORY:
                    dataDAO.createDirectoryTable(tableNameWithPhoneType);
                    break;
                case DataTable.PHILDIRECTORY:
                    dataDAO.createPhilDirectoryTable(tableNameWithPhoneType);
                    break;
                case DataTable.CRAIGSLIST:
                    dataDAO.createCraigslistTable(tableNameWithPhoneType);
                    break;
                case DataTable.WHOIS:
                    dataDAO.createWhoIsTable(tableNameWithPhoneType);
                    break;
                case DataTable.SEARCH_ENGINE:
                    dataDAO.createSearchEngineTable(tableNameWithPhoneType);
                    break;
                case DataTable.CONSUMERS2:
                case DataTable.CONSUMERS3:
                    dataDAO.createConsumers2Table(tableNameWithPhoneType);
                    break;
                case DataTable.HEALTH_BUYER:
                    dataDAO.createHealthBuyerTable(tableNameWithPhoneType);
                    break;
                case DataTable.INSTAGRAM:
                    dataDAO.createInstagramTable(tableNameWithPhoneType);
                    break;
                case DataTable.INSTAGRAM2020:
                    dataDAO.createInstagram2020Table(tableNameWithPhoneType);
                    break;
                case DataTable.OPTIN:
                    dataDAO.createOptInTable(tableNameWithPhoneType);
                    break;
                case DataTable.NEWOPTIN:
                    dataDAO.createNewOptInTable(tableNameWithPhoneType,true);
                    break;
                case DataTable.EVERYDATA:
                    dataDAO.createEverydataTable(tableNameWithPhoneType,true);
                    break;
                case DataTable.AUTO:
                    dataDAO.createAutoTable(tableNameWithPhoneType);
                    break;
                case DataTable.BLACKLIST:
                    dataDAO.createBlackListTable(tableNameWithPhoneType);
                    break;
                case DataTable.CALLLEADS:
                    dataDAO.createCallLeadsTable(tableNameWithPhoneType);
                    break;
                case DataTable.LINKEDIN:
                    dataDAO.createLinkedInTable(tableNameWithPhoneType);
                    break;
                case DataTable.BUSINESS_DETAILED:
                    dataDAO.createBusinessDetailedTable(tableNameWithPhoneType);
                    break;
                case DataTable.STUDENT:
                    dataDAO.createStudentTable(tableNameWithPhoneType);
                    break;
                case DataTable.HEALTH_INSURANCE_LEAD:
                    dataDAO.createHealthInsuranceLeadTable(tableNameWithPhoneType);
                    break;
                case DataTable.FACEBOOK:
                    dataDAO.createFacebookTable(tableNameWithPhoneType,true);
                    break;

            }
            dataDAO.insertTable(new DataTable(tableNameWithPhoneType, type, true, phoneType));
        }

        return tableNameWithPhoneType;
    }

    private CsvSchema updateSchemaAccordingToObject(CsvSchema schema, String header, char separator) {
        try {
            String splitSeparator = String.valueOf(separator);
            if (separator == ' ') {
                splitSeparator = "\" \"";
            }

            String[] columns = header.split(splitSeparator);
            List<String> fileColumnsList = new LinkedList();
            for (String column : columns) {
                fileColumnsList.add(column
                        .replaceAll(" ", "")
                        .replaceAll("-", "")
                        .replaceAll("\\(", "")
                        .replaceAll("\\)", "")
                        .replaceAll("/", "")
                        .replaceAll("-", "").trim().toUpperCase());
            }

            Set<String> fileColumnsSet = new HashSet();
            Set<String> filteredColumnsSet = new HashSet();

            fileColumnsSet.addAll(fileColumnsList);

            List<CsvSchema.Column> columnsList = new LinkedList();
            Iterator<CsvSchema.Column> columnsIterator = schema.rebuild().getColumns();
            while (columnsIterator.hasNext()) {
                columnsList.add(columnsIterator.next());
            }

            List<CsvSchema.Column> filteredColumnsList = new LinkedList();
            for (String fileColumn : fileColumnsList) {
                for (CsvSchema.Column column : columnsList) {
                    String columnName = column.getName()
                            .replaceAll(" ", "")
                            .replaceAll("-", "")
                            .replaceAll("\\(", "")
                            .replaceAll("\\)", "")
                            .replaceAll("/", "")
                            .replaceAll("-", "").trim().toUpperCase();
                    if (fileColumn.equalsIgnoreCase(columnName)) {
                        filteredColumnsSet.add(columnName);
                        filteredColumnsList.add(column);
                        break;
                    }
                }
            }

            filteredColumnsList.size();
            fileColumnsSet.removeAll(filteredColumnsSet);
            fileColumnsSet.size();

            return schema.rebuild().clearColumns().addColumns(filteredColumnsList).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return schema;
    }

    private void writeConsumersToDatabase(List<Consumer> consumerList, String tableName) {
        dataDAO.insertConsumers(consumerList, tableName);
        consumerList.clear();
    }

    private void writeConsumers2ToDatabase(List<Consumer2> consumerList, String tableName) {
        dataDAO.insertConsumers2(consumerList, tableName);
        consumerList.clear();
    }

    private Map<String, String> getCityToCountyMap() {
        Map<String, String> result = new HashMap();

        int offset = 0;
        List<City> cities = dataDAO.getCityListWithPageOffsetAndLimit(offset, 1000);
        offset = offset + cities.size();

        while (cities == null || cities.size() > 0) {
            for (City city : cities) {
                result.put(city.getCity().toUpperCase(), city.getCounty());
            }

            cities = dataDAO.getCityListWithPageOffsetAndLimit(offset, 1000);
            offset = offset + cities.size();
        }

        return result;
    }

    private Map<String, String> getStateNameToAbbreviationMap() {
        Map<String, String> result = new HashMap();
        for (Map.Entry<String, String> entry : STATE_TO_NAME.entrySet()) {
            result.put(entry.getValue().toUpperCase(), entry.getKey());
        }

        return result;
    }

    private String tryToFixState(String state,
                                 String city,
                                 Map<String, String> cityToStateMap,
                                 Set<String> stateCodes,
                                 Map<String, String> stateNameToAbbreviation) {
        if (state == null) {
            return null;
        }


        if (state != null) {
            state = state.toUpperCase();
        }

        if (stateCodes.contains(state)) {
            return state;
        }

        String[] parts = state.split(" ");
        if (parts.length > 1) {
            String part = parts[0];
            if (stateCodes.contains(part)) {
                return part;
            }
        }

        if (stateNameToAbbreviation.containsKey(state)) {
            return stateNameToAbbreviation.get(state);
        }

        if (city != null) {
            city = city.toUpperCase();
            if (cityToStateMap.containsKey(city)) {
                return cityToStateMap.get(city);
            }
        }

        return null;
    }

    private Map<String, String> getCityToStateMap() {
        Map<String, String> result = new HashMap();

        int offset = 0;
        List<City> cities = dataDAO.getCityListWithPageOffsetAndLimit(offset, 1000);
        offset = offset + cities.size();

        while (cities == null || cities.size() > 0) {
            for (City city : cities) {
                result.put(city.getCity().toUpperCase(), city.getState());
            }

            cities = dataDAO.getCityListWithPageOffsetAndLimit(offset, 1000);
            offset = offset + cities.size();
        }

        return result;
    }

    private void writeBusinessToDatabase(List<Business> businessList, String table) {
        dataDAO.insertBusinesses(businessList, table);
        businessList.clear();
    }

    private void writeCraigslistToDatabase(List<CraigsList> data, String table) {
        dataDAO.insertCraigsList(data, table);
        data.clear();
    }

    private void writeDirectoryToDatabase(List<Directory> directoryList, String table) {
        dataDAO.insertDirectories(directoryList, table);
        directoryList.clear();
    }

    public Result handleBiz() throws Exception {
        File inFile = new File("/Users/serfeo/Downloads/USA 1st qrtr 2019 Biz List.csv");
        File outFile = new File("/Users/serfeo/Downloads/biz_phones.csv");

        FileWriter filewriter = new FileWriter(outFile.getAbsoluteFile());
        BufferedWriter writer = new BufferedWriter(filewriter);

        long total = 0;

        MappingIterator<Biz> iterator = getReader(Biz.class, inFile, ',', -1, true).readValues(inFile);
        while (hasNext(iterator)) {
            Biz value = iterator.next();
            value.fixPhones();

            String directPhone = checkAndRepairPhone(value.getDirectPhone());
            if (directPhone != null) {
                writer.write(directPhone);
                writer.newLine();
            }

            String phone = checkAndRepairPhone(value.getPhone());
            if (phone != null) {
                writer.write(phone);
                writer.newLine();
            }

            total = total + 1;
            if (total % 10000 == 0) {
                Logger.info("Handling biz phones... Handled: {}", total);
            }
        }

        writer.flush();
        writer.close();

        return ok();
    }

    private String generateBizLine(Biz value) {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                value.getCompanyName(),
                value.getStreet(),
                value.getCity(),
                value.getState(),
                value.getPostal(),
                value.getCounty(),
                value.getPhone(),
                value.getWebsite(),
                value.getContact(),
                value.getTitle(),
                value.getDirectPhone(),
                value.getEmail(),
                value.getSales(),
                value.getEmployees(),
                value.getSicCode(),
                value.getSicDescription(),
                value.getJobFunction(),
                value.getJobLevel(),
                value.getTwitterProfile(),
                value.getLinkedInProfile(),
                value.getFacebookProfile());
    }

    public <T> Result importData(String path,
                                 String tName,
                                 char separator,
                                 long skipRecords,
                                 int type,
                                 Class<T> clazz,
                                 boolean containsHeader,
                                 IUpdateDataItemHandler updateDataItemHandler,
                                 IWriteDataItemsHandler writeDataItemsHandler) {
        final String tableName = tName.replace(" ", "_").replace(".", "_");
        String mobileTableName = createTableWithPhoneType(tableName, type, DataTable.MOBILE);
        String landLineTableName = createTableWithPhoneType(tableName, type, DataTable.LANDLINE);

        Map<Long, Set<Long>> landLinePhones = getLandLinePhones(3);
        Map<Long, Set<Long>> cleanPhonesMap = DataOperations.generateCleanPhonesMap();
        Map<String, String> areaCodeToStateMap = getAreaCodeToStateMap();
        Map<String, String> cityToStateMap = getCityToStateMap();
        Map<String, String> stateNameToAbbreviation = getStateNameToAbbreviationMap();
        Map<String, String> cityToCountyMap = getCityToCountyMap();

        Map<Integer, List<Long>> ageCategoriesRanges = getAgeCategoriesRanges();

        Set<String> stateCodes = new HashSet();
        for (String state : STATE_CODES) {
            stateCodes.add(state);
        }

        try {
            File file = new File(path);
            if (file.exists()) {
                File[] files;
                if (file.isDirectory()) {
                    files = file.listFiles();
                } else {
                    files = new File[]{file};
                }

                long batch = 100;

                int filesSize = files.length;
                AtomicInteger completed = new AtomicInteger();
                AtomicLong total = new AtomicLong();

                for (File csvFile : files) {
                    if (csvFile.getName().endsWith("csv") || csvFile.getName().endsWith("mer")) {
                        new Thread(() -> {
                            try {
                                List<T> dataList = new LinkedList();
                                List<T> mobileDataList = new LinkedList();
                                List<T> landLineDataList = new LinkedList();

                                long count = 0;
                                long innerSkipRecords = skipRecords;
                                long ignored = 0;

                                boolean comma = true;
                                try {
                                    String header = getHeaderLine(csvFile);
                                    comma = header.contains(",");
                                } catch (Exception e) {e.printStackTrace();}

                                char finalSeparator = ' ' == separator ? (comma ? ',' : ';') : separator;
                                MappingIterator<T> iterator = getReader(clazz, csvFile, finalSeparator, type, containsHeader).readValues(csvFile);
                                while (hasNext(iterator)) {
                                    try {
                                        T item;
                                        try {
                                            item = iterator.next();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            // Logger.info("Record ignored: line {}", count);

                                            total.incrementAndGet();
                                            count++;
                                            ignored++;

                                            continue;
                                        }

                                        if (innerSkipRecords > 0) {
                                            if ((skipRecords - innerSkipRecords) % 10000 == 0) {
                                                importMessage = String.format("Table: %s, file: %s, skipping in progress, skipped total: %d", tableName, csvFile.getName(), skipRecords - innerSkipRecords);
                                            }
                                            innerSkipRecords--;
                                            continue;
                                        }

                                        List<T> items = new LinkedList();
                                        if (item instanceof Auto) {
                                            items.addAll(((Auto)item).getAllItems());
                                        } else {
                                            items.add(item);
                                        }

                                        for (T loopItem: items) {
                                            boolean result = updateDataItemHandler.updateItem(
                                                    loopItem, mobileDataList, landLineDataList,
                                                    landLinePhones,
                                                    cleanPhonesMap,
                                                    areaCodeToStateMap,
                                                    cityToStateMap, stateNameToAbbreviation,
                                                    cityToCountyMap, stateCodes,
                                                    ageCategoriesRanges,
                                                    csvFile.getName());
                                            if (!result) {
                                                //Logger.info("Object ignored: line {}", count + ignored);
                                                ignored++;
                                                continue;
                                            }

                                            dataList.add(loopItem);
                                            total.incrementAndGet();

                                            count++;
                                        }

                                        items.clear();
                                    } catch (Exception e) {
                                        Logger.error("Import error", e);
                                        importMessage = null;

                                        completed.incrementAndGet();
                                        return;
                                        //return ok(Json.toJson(Response.ERROR(String.valueOf(skipRecords + count + 2))));
                                    }

                                    try {
                                        if (dataList.size() > batch) {
                                            writeDataItemsHandler.writeDataItems(dataList, tableName, -1, -1);
                                            importMessage = String.format("Table: %s, file: %s, import in progress, inserted total: %d", tableName, csvFile.getName(), count);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        dataList.clear();
                                    }

                                    if (count % 10000 == 0 && importMessage != null) {
                                        Logger.info(importMessage);
                                    }

                                    try {
                                        if (mobileDataList.size() > batch) {
                                            writeDataItemsHandler.writeDataItems(mobileDataList, mobileTableName, -1, -1);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        mobileDataList.clear();
                                    }

                                    try {
                                        if (landLineDataList.size() > batch) {
                                            writeDataItemsHandler.writeDataItems(landLineDataList, landLineTableName, -1, -1);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        landLineDataList.clear();
                                    }
                                }

                                if (dataList.size() > 0) {
                                    writeDataItemsHandler.writeDataItems(dataList, tableName, -1, -1);
                                }

                                if (mobileDataList.size() > 0) {
                                    writeDataItemsHandler.writeDataItems(mobileDataList, mobileTableName, -1, -1);
                                }

                                if (landLineDataList.size() > 0) {
                                    writeDataItemsHandler.writeDataItems(landLineDataList, landLineTableName, -1, -1);
                                }

                                importMessage = String.format(
                                        "Table: %s, file: %s, import finished, inserted total: %d, ignored: %d",
                                        tableName,
                                        csvFile.getName(),
                                        count,
                                        ignored);

                                //     Logger.info(importMessage);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Logger.error(this.getClass().getName(), e);
                            }

                            completed.incrementAndGet();
                        }).start();
                    }
                }

                while(completed.get() != filesSize) {
                    Thread.sleep(10000);
                }

                importMessage = null;

                areaCodeToStateMap.clear();
                landLinePhones.clear();

                return ok(Json.toJson(Response.OK(total.get())));
            }

        } catch (Exception e) {
            Logger.error("Import error", e);
            e.printStackTrace();
        }

        importMessage = null;
        return ok(Json.toJson(Response.ERROR()));
    }

    private <T> ObjectReader getReader(Class<T> clazz,
                                       File csvFile,
                                       char separator,
                                       int type,
                                       boolean containsHeader) throws Exception{
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(clazz).
                withColumnSeparator(separator);
        ObjectReader reader = mapper.readerFor(clazz);

        if (containsHeader) {
            schema = schema.withHeader();
            schema = updateSchemaAccordingToObject(schema, getHeaderLine(csvFile), separator);
            schema = schema.withQuoteChar('"');
            reader = reader.with(schema.
                    withColumnSeparator(separator));
        } else if (type == DataTable.CONSUMERS3 || type == DataTable.CONSUMERS2) {
            schema = schema.withHeader();
            schema = updateSchemaAccordingToObject(schema, getHeaderLine(csvFile), separator);

            schema = schema.withQuoteChar('"');
            reader = reader.with(schema.
                    withColumnSeparator(separator));
        } else if (type == DataTable.WHOIS) {
            schema = updateSchemaAccordingToObject(schema, getHeaderLine(csvFile), separator);
            schema = schema.withQuoteChar('"');

            reader = reader.with(schema.
                    withColumnSeparator(separator));
        } else if (type == DataTable.CRAIGSLIST) {
            schema = updateSchemaAccordingToObject(schema, getHeaderLineForCraigsList(), separator);
            schema = schema.withQuoteChar('"');

            reader = reader.with(schema.
                    withColumnSeparator(separator));
        } else if (type == DataTable.DIRECTORY) {
            schema = updateSchemaAccordingToObject(schema, getHeaderLineForDirectoryList(), separator);
            schema = schema.withQuoteChar('"');

            reader = reader.with(schema.
                    withColumnSeparator(separator));
        } else if (type == DataTable.BUSINESS || type == DataTable.BUSINESS2) {
            schema = updateSchemaAccordingToObject(schema, getHeaderLineForBusinessList(), separator);
            schema = schema.withQuoteChar('"');

            reader = reader.with(schema.
                    withColumnSeparator(separator));
        }

        return reader;
    }

    private String getHeaderLineForBusinessList() {
        return "ADDRESS,ANNUAL SALES,CITY,COMPANY NAME,CONTACT NAME,COUNTY,EMPLOYEE CODE,EMPLOYEE COUNT,FAX,GENDER,INDUSTRY,LATITUDE,LONGITUDE,PHONE,SALES CODE,SIC CODE,STATE,TITLE,WWW,ZIP";
    }

    private String getConsumersHeaderLine() {
        return "AddressID,IndividualId,personfirstname,personmiddleinitial,personlastname,PersonSurnameSuffix,persontitleofrespect,housenumber,predirection,streetname,streetsuffix,postdirection,unitdesignator,unitdesignatornumber,primaryaddress,secondaryaddress,state,ZipCode,Zip_4,del_point_check_digit,msa,countycode,countyname,citynameabbr,cityname,carrier_route,censustract,censusblock,latitude,longitude,timezone,Xaxis,Yaxis,Zaxis,dpv_code,NumberOfSources,dwellingtype,secondaryaddresspresent,livingunitid,RDID,areacode,Phone,estimatedincomecode,homeownerprobabilitymodel,lengthofresidence,lengthofresidencecode,numberofpersonsinlivingunit,presenceofchildren,NumberOfChildren,ChildrenAge00_02,ChildrenAge00_02Male,ChildrenAge00_02Female,ChildrenAge00_02Unknown,ChildrenAge03_05,ChildrenAge03_05Male,ChildrenAge03_05Female,ChildrenAge03_05Unknown,ChildrenAge06_10,ChildrenAge06_10Male,ChildrenAge06_10Female,ChildrenAge06_10Unknown,ChildrenAge11_15,ChildrenAge11_15Male,ChildrenAge11_15Female,ChildrenAge11_15Unknown,ChildrenAge16_17,ChildrenAge16_17Male,ChildrenAge16_17Female,ChildrenAge16_17Unknown,persongender,persondateofbirthyear,persondateofbirthmonth,persondateofbirthday,personexactage,personagecode,Males_18_24,Females_18_24,Unknowngender_18_24,Males_25_34,Females_25_34,Unknowngender_25_34,Males_35_44,Females_35_44,Unknowngender_35_44,Males_45_54,Females_45_54,Unknowngender_45_54,Males_55_64,Females_55_64,Unknowngender_55_64,Males_65_74,Females_65_74,Unknowngender_65_74,Males_75_Plus,Females_75_Plus,Unknowngender_75_Plus,personmaritalstatus,InferredAge,occupationgroup,personoccupation,ethniccode,languagecode,ethnicgroup,religioncode,hispaniccountrycode,personeducation,businessowner,EthnicConfidenceCode,InferredHouseholdRank,NumberOfAdults,GenerationsInHousehold,PresenceOfCreditCard,presenceofgoldorplatinumcreditcard,PresenceOfPremiumCreditCard,PresenceOfUpscaleRetailCard,PresenceOfBankCard,GasDeptRetailCardHolder,americanexpresscard,CreditRating,investment,investmentstocksecurities,Networth,NumberOfLinesOfCredit,Credit_RangeOfNewCredit,AmericanExpressGoldPremium,DiscoverGoldPremium,DiscoverRegular,GasolineOrRetailCardGoldPremium,GASOLINE OR RETAIL CARD REGULAR,MastercardGoldPremium,MastercardRegular,VisaGoldPremium,VisaRegular,CREDIT CARD INDICATOR,BANK CARD HOLDER,GAS/DEPARTMENT/RETAIL CARD HOLDER,TravelAndEntertainmentCardHolder,CreditCardholderUnknownType,PREMIUM CARD HOLDER,UPSCALE (DEPARTMENT STORE) CARD HOLDER,CreditCardUser,CreditCardNewIssue,BANK CARD - PRESENCE IN HOUSEHOLD,Investing_Active,InvestmentsPersonal,InvestmentsRealEstate,InvestingFinanceGrouping,InvestmentsForeign,InvestmentEstimatedResidentialPropertiesOwned,AssimilationCodes,valuehunter,opportunityseekers,newsandfinancial,automotivebuff,bookreader,MembershipClub,computerowner,cookingenthusiast,do_it_yourselfers,exerciseenthusiast,Gardener,golfenthusiasts,homedecoratingenthusiast,outdoorenthusiast,outdoorsportslover,photography,traveler,pets,cats,dogs,mailresponder,RespondedtoCatalog,sweepstakes,religiousmagazine,malemerchbuyer,femalemerchbuyer,crafts_hobbmerchbuyer,gardening_farmingbuyer,bookbuyer,collect_specialfoodsbuyer,religiouscontributor,politicalcontributor,health_institutioncontributor,charitable,generalcontributor,donatestoenvironmentalcauses,donatesbymail,veteraninhousehold,HeavyBusinessTravelers,hightechleader,Smoker,MailOrderBuyer,OnlinePurchasingIndicator,ApparelWomens,ApparelWomensPetite,ApparelWomensPlusSizes,YoungWomensApparel,ApparelMens,ApparelMensBigAndTall,YoungMensApparel,ApparelChildrens,HealthAndBeauty,BeautyCosmetics,Jewelry,Luggage,COMMUNITY INVOLVEMENT - CAUSES SUPPORTED FINANCIALLY,AnimalWelfareCharitableDonation,ArtsOrCulturalCharitableDonation,ChildrensCharitableDonation,ENVIRONMENT OR WILDLIFE  CHARITABLE DONATION,EnvironmentalIssuesCharitableDonation,InternationalAidCharitableDonation,PoliticalCharitableDonation,PoliticalConservativeCharitableDonation,PoliticalLiberalCharitableDonation,VeteransCharitableDonation,CharitableDonations_Other,CommunityCharities,Parenting,SingleParent,ChildrensApparelInfantsAndToddlers,ChildrensLearningAndActivityToys,ChildrensProductsGeneralBabyCare,ChildrensProductsGeneralBackToSchool,ChildrensProductsGeneral,YoungAdultInHousehold,SeniorAdultInHousehold,ChildrensInterests,Grandchildren,ChristianFamilies,Equestrian,OtherPetOwner,CareerImprovement,WorkingWoman,AfricanAmericanProfessionals,SohoIndicator,Career,BooksAndMagazinesMagazines,BooksAndMusicBooks,BooksAndMusicBooksAudio,ReadingGeneral,READING - RELIGIOUS / INSPIRATIONAL,ReadingScienceFiction,ReadingMagazines,ReadingAudioBooks,ReadingGrouping,HistoryMilitary,CurrentAffairsPolitics,ReligiousInspirational,ScienceSpace,Magazines,EducationOnline,Gaming,ComputingHomeOfficeGeneral,DVDsVideos,ElectronicsandComputingTVVideoMovieWatcher,ElectronicsComputingAndHomeOffice,HighEndAppliances,IntendToPurchaseHDTVSatelliteDish,MusicHomeStereo,MusicPlayer,MusicCollector,MusicAvidListener,MovieCollector,TVCable,GamesVideoGames,TVSatelliteDish,COMPUTERS,GamesComputerGames,ConsumerElectronics,MovieMusicGrouping,ElectronicsComputersGrouping,Telecommunications,ArtsAndAntiquesAntiques,ArtsAndAntiquesArt,TheaterPerformingArts,Arts,Musicalinstruments,CollectiblesGeneral,CollectiblesStamps,CollectiblesCoins,CollectiblesArts,CollectiblesAntiques,CollectorAvid,CollectiblesandAntiquesGrouping,CollectiblesSportsMemorabilia,MilitaryMemorabiliaWeaponry,LifestylesInterestsandPassionsCollectibles,Autowork,SewingKnittingNeedlework,Woodworking,Aviation,HousePlants,Crafts,HomeandGarden,GARDENING ,GARDENING2,HomeImprovementGrouping,PhotographyAndVideoEquipment,HomeFurnishingsDecorating,HomeImprovement,IntendtoPurchaseHomeImprovement,FoodWines,CookingGeneral,COOKING - GOURMET,FoodsNatural,CookingFoodGrouping,GamesBoardGamesPuzzles,GamingCasino,TravelGrouping,TRAVEL ,TravelDomestic,TravelInternational,TravelCruiseVacations,HomeLiving,DIYLiving,SportyLiving,UpscaleLiving,CulturalArtisticLiving,Highbrow,HIGH-TECH LIVING,CommonLiving,ProfessionalLiving,BroaderLiving,ExerciseHealthGrouping,ExerciseRunningJogging,ExerciseWalking,ExerciseAerobic,SpectatorSportsAutoMotorcycleRacing,SpectatorSportsTVSports,SpectatorSportsFootball,SpectatorSportsBaseball,SpectatorSportsBasketball,SpectatorSportsHockey,SpectatorSportsSoccer,Tennis,Snowskiing,Motorcycling,Nascar,BoatingSailing,ScubaDiving,SportsandLeisure,Hunting,Fishing,CampingHiking,HuntingShooting,SportsGrouping,OutdoorsGrouping,HealthMedical,DietingWeightLoss,SelfImprovement,AutomotiveAutoPartsAndAccessories,RDI,homeswimmingpoolindicator,airconditioning,homeheatindicator,homepurchaseprice,homepurchasepricecode,homepurchasedateyear,homepurchasedatemonth,homepurchasedateday,homeyearbuilt,estimatedcurrenthomevaluecode,mortgageamountinthousands,mortgageamountinthousandscode,mortgagelendername,mortgagelendernameavailable,mortgagerate,mortgageratetype,mortgageloantype,transactiontype,deeddateofrefinanceyear,deeddateofrefinancemonth,deeddateofrefinanceday,refinanceamountinthousands,refinanceamountinthousandscode,refinancelendername,refinancelendernameavailable,refinanceratetype,refinanceloantype,CensusMedianHomeValue,CensusMedianHouseholdIncome,CRA_IncomeClassificationCode,MostRecentMortgageAmount2nd,Purchase2ndMortgageAmount,MostRecentMortgageDate2nd,PurchaseMortgageDate,MostRecentMortgage2ndLoanTypeCode,Purchase2ndMortgageLoanTypeCode,MostRecentLenderCode,MostRecent2ndLenderCode,PurchaseLenderCode,MostRecentLenderName2nd,PURCHASE LENDER NAME,MostRecentMortgage2ndInterestRateType,Purchase2ndMortgageInterestRateType,MostRecentMortgageInterestRate,MostRecentMortgage2ndInterestRate,Purchase2ndMortgageInterestRate,Sewer,Water,LoanToValue,PassProspectorValueHomeValueMortgageFile,EMAILFLAG,NCOA_Effective_date,DONOTCALL,NAME,EMAIL,URL,IP,DATE";
    }

    private String getHeaderLineForDirectoryList() {
        return "Source URL;Name;City;State;Postal code;Street;Mock1;Clear Phone;Mock2;Mock3;Mock4;Categories;Mock5";
    }

    private String getHeaderLineForCraigsList() {
        return "MOCK\tLINK\tSUBJECT\tPHONE";
    }

    private <T> boolean hasNext(MappingIterator<T> iterator) {
        try { return iterator.hasNext(); }
        catch (Exception e) { e.printStackTrace(); }

        return true;
    }

    private String getHeaderLineForWhoIs() {
        return "domain_name,create_date,expiry_date,domain_registrar_name,registrant_name,registrant_company,registrant_address,registrant_city,registrant_state,registrant_zip,registrant_country,registrant_email,registrant_phone";
    }

    private String getHeaderLine(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine().replace("\"", "");
        reader.close();

        return line;
    }

    public Result importCountiesAndCitiesData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        for (String state : STATE_CODES) {
            Set<String> counties = new HashSet();
            WSResponse response = wsClient.url(dataURL + state + ".json").get().toCompletableFuture().get();

            try {
                List<CityData> cityDataList = mapper.readValue(response.asJson().toString(), new TypeReference<List<CityData>>() {
                });
                for (CityData cityData : cityDataList) {
                    City city = City.fromCityData(cityData);
                    dataDAO.saveCity(city);

                    counties.add(city.getCounty());
                }

                for (String county : counties) {
                    dataDAO.saveCounty(new County(state, county));
                }

                Logger.info("State: {} finished", state);
                Thread.sleep(5000);
            } catch (Exception e) {
                Logger.error("State: {} error", state);
                Logger.error(e.getMessage());
            }
        }

        return ok();
    }

    public Result getApprovedComments() {
        return  ok(Json.toJson(Response.OK(userDAO.getApprovedComments())));
    }

    public Result sendFeedback() {
        FeedbackRequest feedbackRequest = Json.fromJson(request().body().asJson(), FeedbackRequest.class);

        User user = getResellerByHostName();
        if (user == null) {
            user = userDAO.findUserByEmail(feedbackRequest.getEmail());
            if (user != null) {
                user = userDAO.findUserById(user.getResellerId());
            }
        }

        emailService.sendFeedbackEmail(feedbackRequest, user);
        return ok(Json.toJson(Response.OK()));
    }

    private User getResellerByHostName() {
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
                            return user;
                        }
                    }
                }
            }
        }

        return null;
    }

    public Result sendVideo(String fileName) {
        Config config = ConfigFactory.load();
        String videoFolder = config.getString("video.folder");

        File videoFile = new File(videoFolder, fileName);
        if (videoFile.exists()) {
            return RangeResults.ofFile(videoFile);
        }

        return notFound();
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

    interface IUpdateDataItemHandler {

        <T> boolean updateItem(T item,
                               List<T> mobileDataList,
                               List<T> landLineDataList,
                               Map<Long, Set<Long>> landLinePhones,
                               Map<Long, Set<Long>> cleanPhones,
                               Map<String, String> areaCodeToStateMap,
                               Map<String, String> cityToStateMap,
                               Map<String, String> stateNameToAbbreviation,
                               Map<String, String> cityToCountyMap,
                               Set<String> stateCodes,
                               Map<Integer, List<Long>> ageCategoriesRanges,
                               String fileName);
    }

    interface IWriteDataItemsHandler {

        <T> void writeDataItems(List<T> dataList,
                                String tableName,
                                long leftId,
                                long rightId);
    }

}