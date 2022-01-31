package services.db.dao;

import com.google.inject.Inject;
import controllers.Application;
import models.Category;
import models.DataRequest;
import models.DataTable;
import models.StringListRequest;
import org.mybatis.guice.transactional.Transactional;
import services.db.entity.*;
import services.db.mapper.DataMapper;

import java.util.*;

public class DataDAO {

    private DataMapper mapper;
    private UserDAO userDAO;

    @Inject
    public DataDAO(DataMapper dataMapper,
                   UserDAO userDAO) {
        this.mapper = dataMapper;
        this.userDAO = userDAO;
    }

    public void saveCity(City city) {
        mapper.insertCity(city);
    }

    public void saveCounty(County county) {
        mapper.insertCounty(county);
    }

    public City findCityByNameAndState(String city, String state) {
        return mapper.findCityByNameAndState(city, state);
    }

    public void insertConsumers(List<Consumer> consumerList, String tableName) {
        if (consumerList.size() > 0) {
            mapper.insertConsumers(consumerList, tableName);
        }
    }

    public void insertConsumers2(List<Consumer2> consumerList, String tableName) {
        if (consumerList.size() > 0) {
            mapper.insertConsumers2(consumerList, tableName);
        }
    }

    public void insertInstagram(List<Instagram> instagramList, String tableName) {
        if (instagramList.size() > 0) {
            mapper.insertInstagram(instagramList, tableName);
        }
    }
    public void insertFacebook(List<Facebook> facebookList, String tableName) {
        if (facebookList.size() > 0) {
            mapper.insertFacebook(facebookList, tableName);
        }
    }
    public void insertInstagram2020(List<Instagram2020> instagramList, String tableName) {
        if (instagramList.size() > 0) {
            mapper.insertInstagram2020(instagramList, tableName);
        }
    }

    public void insertOptIn(List<OptIn> optInList, String tableName) {
        if (optInList.size() > 0) {
            mapper.insertOptIn(optInList, tableName);
        }
    }

    public void insertNewOptIn(List<NewOptIn> optInList, String tableName) {
        if (optInList.size() > 0) {
            mapper.insertNewOptIn(optInList, tableName);
        }
    }
    public void insertEverydata(List<Everydata> everydataList, String tableName) {
        if (everydataList.size() > 0) {
            mapper.insertEverydata(everydataList, tableName);
        }
    }
    public void insertConsumersWithIds(List<Consumer> consumerList, String tableName) {
        mapper.insertConsumersWithIds(consumerList, tableName);
    }

    public void insertConsumers2WithIds(List<Consumer2> consumerList, String tableName) {
        mapper.insertConsumers2WithIds(consumerList, tableName);
    }

    public void insertBusinesses(List<Business> businessList, String tableName) {
        if (businessList.size() > 0) {
            //mapper.insertTestBusinesses(businessList.get(0), tableName);
            mapper.insertBusinesses(businessList, tableName);
        }
    }

    public void insertBusinessDetailedList(List<BusinessDetailed> dataList, String tableName) {
        if (dataList.size() > 0) {
            mapper.insertBusinessDetailedList(dataList, tableName);
        }
    }

    public void insertStudentList(List<Student> dataList, String tableName) {
        if (dataList.size() > 0) {
            mapper.insertStudentList(dataList, tableName);
        }
    }

    public void insertBusinessesWithIds(List<Business> businessList, String tableName) {
        mapper.insertBusinessesWithIds(businessList, tableName);
    }

    public void insertDirectories(List<Directory> directoryList, String tableName) {
        if (directoryList.size() > 0) {
            mapper.insertDirectories(directoryList, tableName);
        }
    }

    public void insertZipCodeList(List<ZipCode> zipList) {
        int index = 0;
        while ( index < zipList.size() ) {
            if ( index + 100 < zipList.size() ) {
                mapper.insertZipCodeList( zipList.subList( index, index + 100 ) );
                index = index + 100;
            } else {
                mapper.insertZipCodeList( zipList.subList( index, zipList.size() ) );
                index = zipList.size();
            }
        }
    }

    public void insertAreaCodesList(List<AreaCode> areaCodeList) {
        int index = 0;
        while ( index < areaCodeList.size() ) {
            if ( index + 100 < areaCodeList.size() ) {
                mapper.insertAreaCodesList( areaCodeList.subList( index, index + 100 ) );
                index = index + 100;
            } else {
                mapper.insertAreaCodesList( areaCodeList.subList( index, areaCodeList.size() ) );
                index = areaCodeList.size();
            }
        }
    }

    public List<City> findCitiesByState(String state) {
        return mapper.findCitiesByState(state);
    }

    public List<ZipCode> findZipCodesByState(String state) {
        return mapper.findZipCodesByState(state);
    }

    public List<AreaCode> findAreaCodesByState(String state) {
        return mapper.findAreaCodesByState(state);
    }

    public List<County> findCountiesByState(String state) {
        return mapper.findCountiesByState(state);
    }

    public long getRecordsCount(List<List<DataRequest.Entity>> orConditions,
                                List<List<List<DataRequest.Entity>>> andConditions,
                                List<Integer> lists,
                                List<Integer> uploaded,
                                String tableName,
                                int dataType,
                                boolean unique,
                                boolean isLocalNumbers,
                                boolean isFbHispanicLName,
                                boolean removeCorps,
                                boolean confirmed,
                                boolean businessMatch,
                                boolean businessMatch2,
                                boolean consumer2018Match,
                                boolean consumer2019Match,
                                boolean whoisMatch,
                                boolean healthBuyersMatch,
                                boolean healthInsuranceMatch,
                                boolean instagramMatch,
                                boolean uniqueEmails,
                                boolean uniqueBusinessName,
                                boolean isFilterDNC,
                                boolean isFilterEmptyPhone,
                                boolean isFilterEmail,
                                boolean isBusinessFilterEmail,
                                boolean isBlackListMatch,
                                boolean isConsumerMatch,
                                boolean isCraigslistMatch,
                                boolean isDirectoryMatch,
                                boolean isOptinMatch,
                                boolean isBusinessDetailedMatch,
                                boolean isCallLeadsMatch,
                                boolean isFacebookMatch,
                                boolean isEverydataMatch,
                                boolean isFilterWebsite) {
        return mapper.getRecordsCount(
                orConditions,
                andConditions,
                lists,
                uploaded,
                tableName,
                dataType,
                unique,
                isLocalNumbers,
                isFbHispanicLName,
                removeCorps,
                confirmed,
                businessMatch,
                businessMatch2,
                consumer2018Match,
                consumer2019Match,
                whoisMatch,
                healthBuyersMatch,
                healthInsuranceMatch,
                instagramMatch,
                uniqueEmails,
                uniqueBusinessName,
                isFilterDNC,
                isFilterEmptyPhone,
                isFilterEmail,
                isBusinessFilterEmail,
                isBlackListMatch,
                isConsumerMatch,
                isCraigslistMatch,
                isDirectoryMatch,
                isOptinMatch,
                isBusinessDetailedMatch,
                isCallLeadsMatch,
                isFacebookMatch,
                isEverydataMatch,
                isFilterWebsite);
    }

    public long getDetailedRecordsCount(List<List<DataRequest.Entity>> orConditions,
                                        List<List<List<DataRequest.Entity>>> andConditions,
                                        List<DataRequest.Entity> countOrConditions,
                                        String countFiled,
                                        List<Integer> lists,
                                        List<Integer> uploaded,
                                        String tableName,
                                        int dataType,
                                        boolean unique,
                                        boolean uniqueEmails,
                                        boolean uniqueBusinessName,
                                        boolean isFilterDNC,
                                        boolean isFilterEmptyPhone,
                                        boolean isFilterEmail,
                                        boolean isBusinessFilterEmail,
                                        boolean isBlackListMatch,
                                        boolean isConsumerMatch,
                                        boolean isCraigslistMatch,
                                        boolean isDirectoryMatch,
                                        boolean isOptinMatch,
                                        boolean isBusinessDetailedMatch,
                                        boolean isCallLeadsMatch,
                                        boolean isFilterWebsite) {
        return mapper.getDetailedRecordsCount(
                orConditions,
                andConditions,
                countOrConditions,
                countFiled,
                lists,
                uploaded,
                tableName,
                dataType,
                unique,
                uniqueEmails,
                uniqueBusinessName,
                isFilterDNC,
                isFilterEmptyPhone,
                isFilterEmail,
                isBusinessFilterEmail,
                isBlackListMatch,
                isConsumerMatch,
                isCraigslistMatch,
                isDirectoryMatch,
                isOptinMatch,
                isBusinessDetailedMatch,
                isCallLeadsMatch,
                isFilterWebsite);
    }

    public void updateListItems(int id,
                                List<List<DataRequest.Entity>> orConditionList,
                                List<List<List<DataRequest.Entity>>> andConditionsList,
                                List<Integer> lists,
                                List<Integer> uploaded,
                                String tableName,
                                int dataType,
                                boolean unique,
                                boolean isLocalNumber,
                                boolean isFbHispanicLName,
                                boolean removeCorps,
                                boolean confirmed,
                                boolean matchBusiness,
                                boolean businessMatch2,
                                boolean consumer2018Match,
                                boolean consumer2019Match,
                                boolean whoisMatch,
                                boolean healthBuyersMatch,
                                boolean healthInsuranceMatch,
                                boolean instagramMatch,
                                boolean uniqueEmails,
                                boolean uniqueBusinessName,
                                boolean isFilterDNC,
                                boolean isFilterEmptyPhone,
                                boolean isFilterEmail,
                                boolean isBusinessFilterEmail,
                                boolean isBlackListMatch,
                                boolean isConsumerMatch,
                                boolean isCraigslistMatch,
                                boolean isDirectoryMatch,
                                boolean isOptinMatch,
                                boolean isBusinessDetailedMatch,
                                boolean isCallLeadsMatch,
                                boolean isFacebookMatch,
                                boolean isEverydataMatch,
                                boolean isFilterWebsite) {
        mapper.updateListItems(
                id,
                orConditionList,
                andConditionsList,
                lists,
                uploaded,
                tableName,
                dataType,
                unique,
                isLocalNumber,
                isFbHispanicLName,
                removeCorps,
                confirmed,
                matchBusiness,
                businessMatch2,
                consumer2018Match,
                consumer2019Match,
                whoisMatch,
                healthBuyersMatch,
                healthInsuranceMatch,
                instagramMatch,
                uniqueEmails,
                uniqueBusinessName,
                isFilterDNC,
                isFilterEmptyPhone,
                isFilterEmail,
                isBusinessFilterEmail,
                isBlackListMatch,
                isConsumerMatch,
                isCraigslistMatch,
                isDirectoryMatch,
                isOptinMatch,
                isBusinessDetailedMatch,
                isCallLeadsMatch,
                isFacebookMatch,
                isEverydataMatch,
                isFilterWebsite);
    }

    public List< Consumer > getConsumerListByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getConsumerListByPurchasedList( listId, tableName, offset, limit );
    }

    public List<HealthInsuranceLead> getHealthInsuranceLeadsByPurchasedList(int listId, String tableName, long offset, int limit) {
        return mapper.getHealthInsuranceLeadsByPurchasedList( listId, tableName, offset, limit );
    }

    public List<Consumer2> getConsumer2ListByPurchasedList( int listId, String tableName, long offset, int limit, boolean isNotPurchased ) {
        try {
            return mapper.getConsumer3ListByPurchasedList(listId, tableName, offset, limit, isNotPurchased);
        } catch (Exception e) { e.printStackTrace(); }

        return mapper.getConsumer2ListByPurchasedList(listId, tableName, offset, limit, isNotPurchased);
    }

    public List<Consumer2> getConsumer2ListByNonPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getConsumer2ListByNonPurchasedList( listId, tableName, offset, limit );
    }

    public List< SearchEngine > getSearchEngineListByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getSearchEngineListByPurchasedList( listId, tableName, offset, limit );
    }

    public List< Consumer > getConsumerListByNonPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getConsumerListByNonPurchasedList( listId, tableName, offset, limit );
    }

    public List< Business > getBusinessListByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getBusinessListByPurchasedList( listId, tableName, offset, limit );
    }
    public List< Facebook > getFacebookListByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getFacebookListByPurchasedList( listId, tableName, offset, limit );
    }

    public List<Directory> getDirectoryListByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getDirectoryListByPurchasedList( listId, tableName, offset, limit );
    }
    public List<Everydata> getEverydataListByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getEverydataListByPurchasedList( listId, tableName, offset, limit );
    }
    public List< Business > getBusinessListByNonPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getBusinessListByNonPurchasedList( listId, tableName, offset, limit );
    }

    public List<CraigsList> getCraiglistByPurchasedList( int listId, String tableName, long offset, int limit ) {
        return mapper.getCraiglistByPurchasedList( listId, tableName, offset, limit );
    }

    public List< String > findIndustries() {
        return mapper.findIndustries();
    }

    public void createConsumersTable(String tableName, boolean createIndexes) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createConsumersTable( tableName, indexName );
        if (createIndexes) {
            mapper.createConsumersTableIndexes(tableName, indexName);
        }
    }
    public void createFacebookTable(String tableName, boolean createIndexes) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createFacebookTable( tableName, indexName );
        if (createIndexes) {
            mapper.createFacebookTableIndexes(tableName, indexName);
        }
    }
    public void createBusinessTable(String tableName, boolean createIndexes) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createBusinessTable( tableName, indexName );
        if (createIndexes) {
            mapper.createBusinessTableIndexes(tableName, indexName);
        }
    }

    public void createDirectoryTable(String tableName) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createDirectoryTable( tableName, indexName );
    }
    public void createPhilDirectoryTable(String tableName) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createPhilDirectoryTable( tableName, indexName );
    }

    public void createStudentTable(String tableName) {
        String indexName = tableName + UUID.randomUUID().toString().substring(0, 5);
        mapper.createStudentTable(tableName, indexName);
    }

    public List< DataTable > getVisibleTablesByType( int type ) {
        return mapper.getVisibleTablesByTypes( type );
    }

    public List< DataTable > getVisibleTablesByTypeAndPhoneType(int type, int phoneType) {
        return mapper.getVisibleTablesByTypeAndPhoneType(type, phoneType);
    }

    public void renameTable( String oldName, String newName ) {
        mapper.renameTable( oldName, newName );
    }

    public List< DataTable > getAllTables() {
        return mapper.getAllTables();
    }

    public DataTable getTableById( int id ) {
        return mapper.getTableById( id );
    }

    public void removeTableById( long id, String name ) {
        mapper.renameTable( name, name + UUID.randomUUID().toString().substring( 0, 8 ) );
        mapper.markTableAsRemoved( id );
    }

    public DataTable getTableByName( String name ) {
        return mapper.getTableByName( name );
    }

    public void updateTable( DataTable dataTable ) {
        mapper.updateTable( dataTable );
    }

    public void insertTable( DataTable dataTable ) {
        mapper.insertTable( dataTable );
    }

    public long getTableMaxId( String tableName ) {
        Long maxId = mapper.getTableMaxId( tableName );
        return maxId != null ? maxId : 0;
    }

    public void updateConsumersHashByIdRange( String tableName, long fromId, long toId ) {
        mapper.updateConsumersHashByIdRange( tableName, fromId, toId );
    }

    public void updateCountiesForIdRange(String tableName, String stateColumn, long start, long end) {
        mapper.updateCountiesForIdRange( tableName, stateColumn, start, end );
        mapper.updateCountiesUpperForIdRange( tableName, stateColumn, start, end );
        //mapper.updateEmptyCountiesForIdRange( tableName, stateColumn, start, end );
    }

    public void dropIndex(String indexName, String tableName) {
        mapper.dropIndex( indexName, tableName );
    }

    public void createIndex(String indexName, String tableName, String columns) {
        mapper.createIndex( indexName, tableName, columns );
    }

    public List<String> getIndexesFromTable(String tableName) {
        return mapper.getIndexesFromTable( tableName );
    }

    public void updateAgeCategoriesForIdRange(String tableName, long start, long end) {
        long currentTime = System.currentTimeMillis();
        long yearInMs = 365l * 24l * 60l * 60l * 1000l;

        for ( int i = 1; i < 8; i++ ) {
            long left;
            long right;

            if ( i == 1 ) {
                right = currentTime - 18 * yearInMs;
                left = currentTime - 25 * yearInMs;
            } else if ( i == 7 ) {
                right = currentTime - 75 * yearInMs;
                left = currentTime - 160 * yearInMs;
            } else {
                int delta = 25 + ( i - 2 ) * 10;

                right = currentTime - delta * yearInMs;
                left = currentTime - ( delta + 10 ) * yearInMs;
            }

            mapper.updateAgeCategoriesForTableAndIdRange( tableName, start, end, left, right, i );
        }

        mapper.updateUnknownAgeCategoriesForTableAndIdRange( tableName, start, end );
    }

    public long getTableMinIdWithCondition( String tableName, String condition ) {
        Long value = mapper.getTableMinIdWithCondition( tableName, condition );
        if ( value == null ) {
            return mapper.getTableMaxId( tableName );
        } else {
            return value;
        }
    }

    public void updatePhoneTypesWithIdRange( String tableName, long start, long end ) {
        mapper.updatePhoneTypesForIdRange( tableName, start, end );
        mapper.updateEmptyPhoneTypesForIdRange( tableName, start, end );
    }

    public List< PhoneEntity > getPhoneEntityList( String tableName, long startIndex, long endIndex ) {
        return mapper.getPhoneEntityList( tableName, startIndex, endIndex );
    }

    @Transactional
    public void updatePhoneEntities( String tableName, List<PhoneEntity> phoneEntities ) {
        for ( PhoneEntity phoneEntity: phoneEntities ) {
            mapper.updatePhoneType( tableName, phoneEntity.getId(), phoneEntity.getPhoneType() );
        }
    }

    public void copyDataWithPhoneType(String tableName, String mobileTableName, int phoneType, long start, long end) {
        mapper.copyDataWithPhoneType(tableName, mobileTableName, phoneType, start, end);
    }

    public List<Price> getAllPrices() {
        return mapper.getAllPrices();
    }

    public void updatePrice(Price price) {
        mapper.updatePrice(price);
    }

    public Price getPriceByType(int type, int dataSource, int resellerId) {
        Price result = mapper.getPriceByType(type, dataSource, resellerId);
        if (result == null) {
            result = mapper.getPriceByType(type, dataSource, 0);
        }

        return result;
    }

    public List<Category> findCategories() {
        return mapper.findCategories();
    }
    public List<Category> findEverydataCategories() {
        return mapper.findEverydataCategories();
    }

    public List<String> findCarrierBrand(String tabeName) {
        return mapper.findCarrierBrand(tabeName);
    }
    public List<String> findFacebookJobs(String tabeName) {
        return mapper.findFacebookJobs(tabeName);
    }
    public List<String> findFacebookHLastName(String strSearch) {
        return mapper.findFacebookHLastName(strSearch);
    }
    public List<String> findDomainSources(String domainSource,String tabeName) {
        return mapper.findDomainSources(domainSource,tabeName);
    }
    public List<Category> findStudentSources() {
        return mapper.findStudentSources();
    }

    public List<City> getCityListWithPageOffsetAndLimit(int offset, int limit) {
        return mapper.getCityListWithPageOffsetAndLimit(offset, limit);
    }

    public void createCraigslistTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createCraigslistTable(name, indexName);
    }

    public List<AreaCode> findAreaCodesWithOffsetAndLimit(int offset, int limit) {
        return mapper.findAreaCodesWithOffsetAndLimit(offset, limit);
    }

    public void insertCraigsList(List<CraigsList> data, String table) {
        if (data.size() > 0) {
            mapper.insertCraigsList(data, table);
        }
    }

    public void updateZipCodesForRange(String tableName, long start, long end) {
        mapper.updateZipCodesForRange(tableName, start, end);
    }

    public List<DataSource> findAllDataSources() {
        return mapper.findAllDataSources();
    }

    public List<DataSource> findAllDataSourcesForUser(User user) {
        List<DataSource> allDataSources = mapper.findAllDataSourcesForUser(user.getId(), user.getResellerId());
        List<DataSource> copyAllDataSources = new ArrayList<>();
        List<String> blockedDatasources = mapper.findAllDataBlockedDatasources(user.getId());
        for(int i =0;i < allDataSources.size();i++){
            if(!blockedDatasources.contains(allDataSources.get(i).getName())) {
                // copyAllDataSources.remove(i);
                copyAllDataSources.add(allDataSources.get(i));
            }
        }
        updateResellerDataSources(user, copyAllDataSources);
        checkResellerDataSources(user, copyAllDataSources);
        return copyAllDataSources;
    }
    public List<DataSource> findAllTables() {
        return mapper.findAllTables();
    }
    private void checkResellerDataSources(User user, List<DataSource> allDataSources) {
        if (user.getResellerId() > 0) {
            User reseller = userDAO.findUserById(user.getResellerId());
            if (reseller != null) {
                List<DataSource> resellersDataSources = findAllDataSourcesForUser(reseller);
                Set<String> resellerDataSourceNames = new HashSet();
                for (DataSource resellerDataSource: resellersDataSources) {
                    resellerDataSourceNames.add(resellerDataSource.getName());
                }

                Iterator<DataSource> it = allDataSources.iterator();
                while (it.hasNext()) {
                    if (!resellerDataSourceNames.contains(it.next().getName())) {
                        it.remove();
                    }
                }
            }
        }
    }

    private void updateResellerDataSources(User user, List<DataSource> allDataSources) {
        if (user.getRole() == User.ROLE_RESELLER) {
            List<DataSource> dataSources = mapper.findResellerDataSources(user.getId());
            for (DataSource outDataSource: allDataSources) {
                for (DataSource inDataSource: dataSources) {
                    if (outDataSource.getId() == inDataSource.getId()) {
                        outDataSource.setVisible(inDataSource.isVisible());
                        break;
                    }
                }
            }
        }
    }

    public void updateDataSourceVisibility(Integer id, Boolean visible) {
        mapper.updateDataSourceVisibility(id, visible);
    }



    @Transactional
    public void updateDataSourceBlockedUsers(int id, List<Integer> userIds) {
        mapper.removeBlockedUsersForDataSourceId(2,id);
        for(int i=0; i< userIds.size();i++){
            mapper.insertBlockedUsers(id, userIds.get(i),2);
        }

        //     if (userIds.size() > 0) {

        //    }
    }
    @Transactional
    public void updateDataSourceBlockedState(int id, List<Integer> userIds) {
        mapper.removeBlockedUsersForDataSourceId(1,id);
        for(int i=0; i< userIds.size();i++){
            mapper.insertBlockedUsers(id, userIds.get(i),1);
        }
        // if (userIds.size() > 0) {
        // mapper.insertBlockedState(id, userIds,1);
        //    }
    }

    public void insertDataSourceBlockedUser(int id, int userId) {
        List<Integer> userIds = new LinkedList();
        userIds.add(userId);

        mapper.insertBlockedUsers(id, 0,2);
    }

    public List<BlockedUser> getBlockedUsersForDataSource(Integer id,Boolean state) {

        if(state){
            return mapper.getBlockedUsersForDataSource(id,1);
        }else{

            return mapper.getBlockedUsersForDataSource(id,2);
        }

    }

    public void blockDirectoryAndCraigslistDataSourcesForUser(int userId) {
        List<DataSource> dataSources = findAllDataSources();
        for (DataSource dataSource: dataSources) {
            if ("directory".equals(dataSource.getName()) ||
                    "craigslist".equals(dataSource.getName())) {
                insertDataSourceBlockedUser(dataSource.getId(), userId);
            }
        }
    }

    public void insertWhoIsList(List<WhoIs> dataList, String tableName) {
        if (dataList.size() > 0) {
            mapper.insertWhoIsList(dataList, tableName);
        }
    }

    public void createWhoIsTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createWhoIsTable(name, indexName);
    }

    public List<WhoIs> getWhoIsListByPurchasedList(int listId, String tableName, long offset, int limit) {
        return mapper.getWhoIsListByPurchasedList( listId, tableName, offset, limit );
    }

    public List<WhoIs> getWhoIsListByWebSitesList(List<String> websites) {
        if (websites.size() > 0) {
            return mapper.getWhoIsListByWebSites("WhoIs2018", websites);
        } else {
            return new LinkedList();
        }
    }

    public long getMatchingCount_old(List<List<DataRequest.Entity>> orConditions, String tableName) {
        return mapper.getMatchingCount_old(orConditions, tableName);
    }
    public long getMatchingCount(List<String> columns,String tableName,String datasource) {
        return mapper.getMatchingCount(columns,tableName,datasource);
    }
    public void insertMatchedRecords(List<List<DataRequest.Entity>> orConditions, String data, String tableName, int listId) {
        mapper.insertMatchedRecords(orConditions, data, tableName, listId);
    }

    public String getSettingsValueByKey(String key, int resellerId) {
        String value = mapper.getSettingsValueByKey(key, resellerId);
        if (value == null) {
            value = mapper.getSettingsValueByKey(key, 0);
        }

        return value;
    }

    public Setting getSettingByKey(String key, int resellerId) {
        Setting setting = mapper.getSettingByKey(key, resellerId);
        if (setting == null) {
            setting = mapper.getSettingByKey(key, 0);
        }

        return setting;
    }

    public void insertSetting(Setting setting, int resellerId) {
        mapper.insertSetting(setting, resellerId);
    }

    public void updateSettingValueByKey(String key, String value, int resellerId) {
        mapper.updateSettingValueByKey(key, value, resellerId);
    }

    public List<String> getPhoneByTableNameAndPhoneIdRange(String name, String phone, long leftId, long rightId) {
        return mapper.getPhoneByTableNameAndPhoneIdRange(name, phone, leftId, rightId);
    }

    public List<DataTable> getTablesByPhoneTypeAndNotRemoved(int phoneType) {
        return mapper.getTablesByPhoneTypeAndNotRemoved(phoneType);
    }

    public List<DataTable> getTablesByPhoneTypeAndTypeAndNotRemoved(int phoneType, int type) {
        return mapper.getTablesByPhoneTypeAndTypeAndNotRemoved(phoneType, type);
    }

    public void updatePhoneEntityDNC(String tableName, List<Long> ids, boolean value) {
        mapper.updatePhoneEntityDNC(tableName, ids, value);
    }

    public List<DataTable> getTablesByType(int type) {
        return mapper.getTablesByType(type);
    }

    public void updateBusinessTableSic(String name, long fromId, long toId) {
        mapper.updateBusinessTableSic(name, fromId, toId);
    }

    public void moveData(String sourceTableName, String destTableName, int phoneType, long leftId, long rightId) {
        List<String> columns = mapper.getTableColumns(sourceTableName.toLowerCase());
        String strColumns = "";
        for (String column: columns) {
            if ("id".equalsIgnoreCase(column)) {
                continue;
            }

            if (strColumns.length() > 0) {
                strColumns = strColumns + ",";
            }

            strColumns = strColumns + column;
        }

        mapper.copyData(sourceTableName, destTableName, phoneType, leftId, rightId, strColumns);
        mapper.removeData(sourceTableName, phoneType, leftId, rightId);
    }

    public void updateTableHash(String name, long leftId, long rightId) {
        mapper.updateTableHash(name, leftId, rightId);
    }

    public void updateListItemsHash(int id, String tableName) {
        mapper.updateListNonPurchasedItemsHash(id, tableName);
        mapper.updateListPurchasedItemsHash(id, tableName);
    }

    public List<DataTable> getNonRemovedTables() {
        return mapper.getNonRemovedTables();
    }

    public void createSearchEngineTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createSearchEngineTable(name, indexName);
    }

    public void updateSearchEngineListHashByIdRange(String tableName, long leftId, long rightId) {
        mapper.updateSearchEngineListHashByIdRange(tableName, leftId, rightId);
    }

    public void insertSearchEngineList(List<WhoIs> dataList, String tableName) {
        mapper.insertSearchEngineList(dataList, tableName);
    }

    public List<DataTable> getAllTablesByType(int type) {
        return mapper.getAllTablesByType(type);
    }

    public void updateSearchEnginePhones(String name, long leftId, long rightId) {
        mapper.removeSearchEngineRecordsWithInvalidPhones(name, leftId, rightId);
        mapper.updateSearchEngineRecordsStartsWithOne(name, leftId, rightId);
    }

    public List<SearchEngine> getSearchEngineListOrderedByPhone(String name, long offset, long limit) {
        return mapper.getSearchEngineListOrderedByPhone(name, offset, limit);
    }

    public void bulkDeleteSearchEngineRecords(String name, List<Integer> recordsForDelete) {
        mapper.bulkDeleteSearchEngineRecords(name, recordsForDelete);
    }

    public List<MatchedResult> getMatchedConsumerBusinessData(DataTable consumersTable, DataTable businessTable,
                                                              long leftId, long rightId, int stateCode) {
        return mapper.getMatchedConsumerBusinessData(
                consumersTable.getName(),
                businessTable.getName(),
                leftId, rightId, stateCode);
    }

    public List<String> getSettingsValuesByKey(String key, int resellerId) {
        List<String> result = mapper.getSettingsValuesByKey(key, resellerId);
        if (result.size() == 0) {
            mapper.insertSettings(key, resellerId);
        } else {
            List<DataSource> dataSources = mapper.getAllDataSources();
            for (DataSource dataSource : dataSources) {
                String matchedKey = "matched_price_" + new Integer(dataSource.getId()).toString();
                List<String> matchedSettings = mapper.getSettingsValuesByKey(matchedKey, resellerId);
                if (matchedSettings.size() == 0) {
                    mapper.insertSettings(matchedKey, resellerId);
                }
            }
        }

        return mapper.getSettingsValuesByKey(key, resellerId);
    }

    public void removeRecordsWithKeyWord(String name, String value, String[] arrayColumns, long leftId, long rightId) {
        List<String> columns = new LinkedList();
        for (String column: arrayColumns) {
            columns.add(column);
        }

        mapper.removeRecordsWithKeyWord(name, value, columns, leftId, rightId);
    }

    public void createConsumers2Table(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createConsumers2Table(name, indexName);
    }

    public void createInstagramTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createInstagramTable(name, indexName);
    }

    public void createInstagram2020Table(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createInstagram2020Table(name, indexName);
    }

    public void createOptInTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createOptInTable(name, indexName);
    }

    public void createNewOptInTable(String tableName, boolean createIndexes) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createNewOptInTable( tableName, indexName );
        if (createIndexes) {
            mapper.createOptIn2TableIndexes(tableName, indexName);
        }
    }
    public void createEverydataTable(String tableName, boolean createIndexes) {
        String indexName = tableName + UUID.randomUUID().toString().substring( 0, 5 );
        mapper.createEveridataTable( tableName, indexName );
        if (createIndexes) {
            mapper.createEverydataTableIndexes(tableName, indexName);
        }
    }
    public void createAutoTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createAutoTable(name, indexName);
    }

    public void createBlackListTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createBlackListTable(name, indexName);
    }

    public void createCallLeadsTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createCallLeadsTable(name, indexName);
    }

    public void createLinkedInTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createLinkedInTable(name, indexName);
    }

    public void createBusinessDetailedTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createBusinessDetailedTable(name, indexName);
    }

    public void updateConsumers2HashByIdRange(String tableName, long leftId, long rightId) {
        mapper.updateConsumers2HashByIdRange(tableName, leftId, rightId);
    }

    public void updateStates(String name, long leftId, long rightId) {
        for (int i = 0; i < Application.STATE_CODES.length; i++) {
            mapper.updateState(name, leftId, rightId, i, Application.STATE_CODES[i]);
        }
    }

    public List<String> getPhoneByTableNameAndIdRange(String name, long leftId, long rightId) {
        return mapper.getPhoneByTableNameAndIdRange(name, leftId, rightId);
    }

    public List<PhoneEntity> getHotFrogPhoneEntityList(String name, long leftId, long rightId) {
        return mapper.getHotFrogPhoneEntityList(name, leftId, rightId);
    }

    public void trimFields(String tableName, long leftId, long rightId) {
        mapper.trimFields(tableName, leftId, rightId);
    }

    public List<Consumer2> getConsumers2ListByIdRange(String name, long leftId, long rightId) {
        return mapper.getConsumers2ListByIdRange(name, leftId, rightId);
    }

    public List<Consumer2> getConsumers2ForOptInListByIdRange(String name, long leftId, long rightId) {
        return mapper.getConsumers2ForOptInListByIdRange(name, leftId, rightId);
    }

    public void updateBusinessOwner(String name, List<Consumer2> consumersForUpdate) {
        if (consumersForUpdate.size() > 0) {
            mapper.updateBusinessOwner(name, consumersForUpdate);
        }
    }

    public List<Lender> findAllLenders(String value) {
        return mapper.findAllLenders("%" + value + "%");
    }

    public void insertLenders(Set<String> lenders) {
        for (String lender: lenders) {
            if (lender != null && lender.length() > 0) {
                mapper.insertLender(lender.trim());
            }
        }
    }

    public List<Consumer2> getConsumers2LendersListByIdRange(String tableName, long left, long right) {
        return mapper.getConsumers2LendersListByIdRange(tableName, left, right);
    }

    public void updateEmptyPhoneValues(String tableName, long leftId, long rightId) {
        mapper.updateEmptyPhoneValues(tableName, leftId, rightId);
    }

    public List<Instagram> getInstagramListByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getInstagramListByPurchasedList(listId, tableName, offset, limit);
    }

    public List<Instagram2020> getInstagram2020ListByPurchasedList(Integer listId, String tableName, long offset, int limit, boolean isNotPurchased) {
        return mapper.getInstagram2020ListByPurchasedList(listId, tableName, offset, limit, isNotPurchased);
    }

    public List<OptIn> getOptInListByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getOptInListByPurchasedList(listId, tableName, offset, limit);
    }
    public List<NewOptIn> getNewOptInListByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getNewOptInListByPurchasedList(listId, tableName, offset, limit);
    }
    public List<Instagram> getInstagramRecords(String tableName, long leftId, long rightId) {
        return mapper.getInstagramRecords(tableName, leftId, rightId);
    }

    @Transactional
    public void updateInstagramStateInfo(String tableName, List<Instagram> instagramRecords) {
        for (Instagram instagram: instagramRecords) {
            mapper.updateInstagramStateInfo(tableName, instagram.getId(), instagram.getState(), instagram.getSt_code());
        }
    }

    public void updateSourceCriteria(String tableName, long leftId, long rightId) {
        mapper.updateSourceCriteria(tableName, leftId, rightId);
    }

    public List<Price> getAllPricesForReseller(int resellerId) {
        List<Price> resellerPrices = mapper.getResellerPrices(resellerId);
        if (resellerPrices.size() == 0) {
            mapper.insertResellerPrices(resellerId);
        } else {
            List<DataSource> dataSources = mapper.getAllDataSources();
            for (DataSource dataSource : dataSources) {
                List<Price> dataSourceResellerPrices = mapper.getResellerPricesByDataSource(resellerId, dataSource.getId());
                if (dataSourceResellerPrices.size() == 0) {
                    mapper.insertResellerPricesForDataSource(resellerId, dataSource.getId());
                }
            }
        }


        return mapper.getResellerPrices(resellerId);
    }

    public void updateResellerDataSourceVisibility(int resellerId, int dataSourceId, boolean visible) {
        if (mapper.getResellerDataSourceVisibility(resellerId, dataSourceId) != null) {
            mapper.updateResellerDataSourceVisibility(resellerId, dataSourceId, visible);
        } else {
            mapper.insertResellerDataSourceVisibility(resellerId, dataSourceId, visible);
        }
    }

    public void insertAutoList(List<Auto> dataList, String tableName) {
        mapper.insertAutoList(dataList, tableName);
    }

    public void insertBlackList(List<BlackList> dataList, String tableName) {
        mapper.insertBlackList(dataList, tableName);
    }

    public void insertCallLeadList(List<CallLead> dataList, String tableName) {
        mapper.insertCallLeadList(dataList, tableName);
    }

    public void insertHealthBuyersList(List<HealthBuyer> dataList, String tableName) {
        mapper.insertHealthBuyersList(dataList, tableName);
    }

    public void insertHealthInsuranceLeadList(List<HealthInsuranceLead> dataList, String tableName) {
        mapper.insertHealthInsuranceLeadList(dataList, tableName);
    }

    public void insertLinkedInList(List<LinkedIn> dataList, String tableName) {
        mapper.insertLinkedInList(dataList, tableName);
    }

    public List<Auto> getAutoListByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getAutoListByPurchasedList(listId, tableName, offset, limit);
    }

    public List<BlackList> getBlackListByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getBlackListByPurchasedList(listId, tableName, offset, limit);
    }

    public List<LinkedIn> getLinkedInByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getLinkedInByPurchasedList(listId, tableName, offset, limit);
    }

    public List<BusinessDetailed> getBusinessDetailedByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getBusinessDetailedByPurchasedList(listId, tableName, offset, limit);
    }

    public List<Student> getStudentByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getStudentByPurchasedList(listId, tableName, offset, limit);
    }

    public List<HealthBuyer> getHealthBuyersByPurchasedList(Integer listId, String tableName, long offset, int limit) {
        return mapper.getHealthBuyersByPurchasedList(listId, tableName, offset, limit);
    }

    public List<String> findAutoMakes(String search, int limit) {
        if (search != null && search.length() > 0) {
            return mapper.findAutoMakesWithSearch("%" + search + "%", limit);
        } else {
            return mapper.findAutoMakes(limit);
        }
    }

    public List<String> findAutoModels(String make, String search, int limit) {
        if (search != null && search.length() > 0) {
            return mapper.findAutoModelsWithSearch(make, "%" + search + "%", limit);
        } else {
            return mapper.findAutoModels(make, limit);
        }
    }
    public List<String> findC2Carriers(String carrierName) {

        return mapper.findC2Carriers("%"+carrierName+"%");

    }
    public List<Auto> getAutoListWithLimitAndOffset(String tableName, long leftId, long rightId) {
        return mapper.getAutoListWithLimitAndOffset(tableName, leftId, rightId);
    }

    public void insertAutoMake(String make) {
        mapper.insertAutoMake(make);
    }

    public void insertAutoModel(String make, Set<String> values) {
        for (String value: values) {
            mapper.insertAutoModel(make, value);
        }
    }

    public void insertDirectoryUserItems(int listId, String companyName) {
        mapper.insertDirectoryUserItems(listId, companyName);
    }

    public long getDirectoryFacebookMinId(String name) {
        Long result = mapper.getDirectoryFacebookMinId(name);
        return result == null ? -1 : result;
    }

    public List<Long> getRecordsIds(String tableName, long leftId, long rightId) {
        return mapper.getRecordsIds(tableName, leftId, rightId);
    }

    public List<Long> getConsumerIdsByPurchasedList(int id, String tableName, long offset, int limit) {
        return mapper.getConsumerIdsByPurchasedList(id, tableName, offset, limit);
    }

    public List<Long> getConsumerIdsByNonPurchasedList(int id, String tableName, long offset, int limit) {
        return mapper.getConsumerIdsByNonPurchasedList(id, tableName, offset, limit);
    }

    public void insertConsumersRecordsWithIds(List<Long> consumerIds, String archiveTableName, String tableName) {
        mapper.insertConsumersRecordsWithIds(consumerIds, archiveTableName, tableName);
    }

    public long getDetailedRecordsCountByPurchasedList(int listId, String tableName,
                                                       List<DataRequest.Entity> countOrConditions,
                                                       String countColumn) {
        return mapper.getDetailedRecordsCountByPurchasedList(listId, tableName, countOrConditions, countColumn);
    }

    public long getDetailedRecordsCountByNonPurchasedList(int listId, String tableName,
                                                          List<DataRequest.Entity> countOrConditions,
                                                          String countColumn) {
        return mapper.getDetailedRecordsCountByNonPurchasedList(listId, tableName, countOrConditions, countColumn);
    }

    public void updateConsumersNames(String tableName, long leftId, long rightId) {
        mapper.updateConsumersNames(tableName, leftId, rightId);
    }

    public List<Price> getAllPricesForUser(int userId) {
        return mapper.getAllPricesForUser(userId);
    }

    public void updateUserPrice(Price price) {
        Price dbPrice = mapper.getPriceByTypeAndUserId(price.getType(), price.getDataSource(), price.getUserId());
        if (dbPrice == null) {
            mapper.insertUserPrice(price);
        } else {
            mapper.updateUserPrice(price);
        }
    }

    public Price getPriceByTypeAndUserId(int phoneType, int type, int userId) {
        return mapper.getPriceByTypeAndUserId(phoneType, type, userId);
    }

    public void copyNewSourcesData(String directoryNewSource, String directory, long left, long right) {
        mapper.copyNewSourcesData(directoryNewSource, directory, left, right);
    }

    public List<String> findTitles(StringListRequest request) {
        return mapper.findTitles(request);
    }

    public Integer findTitlesCount(StringListRequest request) {
        return mapper.findTitlesCount(request);
    }

    public List<String> findBusinessTitles(StringListRequest request) {
        return mapper.findBusinessTitles(request);
    }

    public Integer findBusinessTitlesCount(StringListRequest request) {
        return mapper.findBusinessTitlesCount(request);
    }

    public List<String> findInstagramCategories(StringListRequest request) {
        return mapper.findInstagramCategories(request);
    }

    public Integer findInstagramCategoriesCount(StringListRequest request) {
        Integer value = mapper.findInstagramCategoriesCount(request);
        return value == null ? 0 : value;
    }

    public List<String> findOptinSources(StringListRequest request) {
        return mapper.findOptinSources(request);
    }

    public Integer findOptinSourcesCount(StringListRequest request) {
        Integer value = mapper.findOptinSourcesCount(request);
        return value == null ? 0 : value;
    }

    public List<DetailedBusinessIndustry> findDetailedBusinessIndustries(StringListRequest request) {
        return mapper.findDetailedBusinessIndustries(request);
    }

    public Integer findDetailedBusinessIndustriesCount(StringListRequest request) {
        Integer value = mapper.findDetailedBusinessIndustriesCount(request);
        return value == null ? 0 : value;
    }

    public List<DetailedBusinessIndustry> findDetailedBusinessIndustriesByValues(StringListRequest request) {
        return mapper.findDetailedBusinessIndustriesByValues(request);
    }

    public void createHealthBuyerTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createHealthBuyerTable(name, indexName);
    }

    public List<TableAccess> getAllTableAccess() {
        return mapper.getAllTableAccess();
    }

    public void createHealthInsuranceLeadTable(String name) {
        String indexName = name + UUID.randomUUID().toString().substring(0, 5);
        mapper.createHealthInsuranceLeadTable(name, indexName);
    }
}
