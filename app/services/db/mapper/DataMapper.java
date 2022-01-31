package services.db.mapper;

import models.*;
import services.db.entity.MatchedResult;
import services.db.entity.Setting;
import services.db.entity.DataSource;
import org.apache.ibatis.annotations.*;
import services.db.entity.*;

import java.util.List;

public interface DataMapper {

    @Insert("INSERT INTO cities(state, county, city) VALUES (#{state}, #{county}, #{city})")
    void insertCity(City city);

    @Insert("INSERT INTO counties(state, county) VALUES (#{state}, #{county})")
    void insertCounty(County county);

    @Select("SELECT * FROM cities WHERE city=#{city} AND state=#{state} LIMIT 1")
    City findCityByNameAndState(@Param("city") String city,
                                @Param("state") String state);

    void insertConsumers(@Param("consumerList") List<Consumer> consumerList,
                         @Param("tableName") String tableName);

    void insertConsumersWithIds(@Param("consumerList") List<Consumer> consumerList,
                                @Param("tableName") String tableName);

    void insertConsumers2(@Param("consumerList") List<Consumer2> consumerList,
                          @Param("tableName") String tableName);

    void insertConsumers2WithIds(@Param("consumerList") List<Consumer2> consumerList,
                                 @Param("tableName") String tableName);

    void insertFacebook(@Param("facebookList") List<Facebook> facebookList,
                         @Param("tableName") String tableName);

    void insertInstagram(@Param("instagramList") List<Instagram> instagramList,
                         @Param("tableName") String tableName);

    void insertInstagram2020(@Param("instagramList") List<Instagram2020> instagramList,
                             @Param("tableName") String tableName);

    void insertOptIn(@Param("optInList") List<OptIn> optInList,
                     @Param("tableName") String tableName);

    void insertNewOptIn(@Param("optInList") List<NewOptIn> optInList,
                        @Param("tableName") String tableName);

    void insertEverydata(@Param("everyDataList") List<Everydata> everyDataList,
                        @Param("tableName") String tableName);

    void insertBusinesses(@Param("businessList") List<Business> businessList,
                          @Param("tableName") String tableName);

    void insertBusinessDetailedList(@Param("businessDetailedList") List<BusinessDetailed> dataList,
                                    @Param("tableName") String tableName);

    void insertStudentList(@Param("studentList") List<Student> dataList,
                           @Param("tableName") String tableName);

    void insertBusinessesWithIds(@Param("businessList") List<Business> businessList,
                                 @Param("tableName") String tableName);

    void insertDirectories(@Param("directoryList") List<Directory> directoryList,
                           @Param("tableName") String tableName);

    void insertZipCodeList(@Param("zipList") List<ZipCode> zipList);

    void insertAreaCodesList(@Param("areaCodeList") List<AreaCode> areaCodeList);

    @Select("SELECT max(city) AS city, #{state} AS state FROM cities WHERE state=#{state} GROUP BY city ORDER BY city")
    List<City> findCitiesByState(@Param("state") String state);

    @Select("SELECT * FROM zipCodes WHERE state=#{state} ORDER BY code")
    List<ZipCode> findZipCodesByState(@Param("state") String state);

    @Select("SELECT * FROM areaCodes WHERE state=#{state} ORDER BY code")
    List<AreaCode> findAreaCodesByState(@Param("state") String state);

    @Select("SELECT * FROM counties WHERE state=#{state} ORDER BY county")
    List<County> findCountiesByState(String state);

    long getRecordsCount(@Param("orConditions") List<List<DataRequest.Entity>> orConditions,
                         @Param("andConditions") List<List<List<DataRequest.Entity>>> andConditions,
                         @Param("lists") List<Integer> lists,
                         @Param("uploaded") List<Integer> uploaded,
                         @Param("tableName") String tableName,
                         @Param("dataType") int dataType,
                         @Param("unique") boolean unique,
                         @Param("isLocalNumbers") boolean isLocalNumbers,
                         @Param("isFbHispanicLName") boolean isFbHispanicLName,
                         @Param("removeCorps") boolean removeCorps,
                         @Param("confirmed") boolean confirmed,
                         @Param("businessMatch") boolean businessMatch,
                         @Param("businessMatch2") boolean businessMatch2,
                         @Param("consumer2018Match") boolean consumer2018Match,
                         @Param("consumer2019Match") boolean consumer2019Match,
                         @Param("whoisMatch") boolean whoisMatch,
                         @Param("healthBuyersMatch") boolean healthBuyersMatch,
                         @Param("healthInsuranceMatch") boolean healthInsuranceMatch,
                         @Param("instagramMatch") boolean instagramMatch,
                         @Param("uniqueEmails") boolean uniqueEmails,
                         @Param("uniqueBusinessName") boolean uniqueBusinessName,
                         @Param("filterDNC") boolean isFilterDNC,
                         @Param("filterEmptyPhone") boolean isFilterEmptyPhone,
                         @Param("filterEmail") boolean isFilterEmail,
                         @Param("filterBusinessEmail") boolean isBusinessFilterEmail,
                         @Param("isBlackListMatch") boolean isBlackListMatch,
                         @Param("isConsumerMatch") boolean isConsumerMatch,
                         @Param("isCraigslistMatch") boolean isCraigslistMatch,
                         @Param("isDirectoryMatch") boolean isDirectoryMatch,
                         @Param("isOptInMatch") boolean isOptinMatch,
                         @Param("isBusinessDetailedMatch") boolean isBusinessDetailedMatch,
                         @Param("isCallLeadsMatch") boolean isCallLeadsMatch,
                         @Param("isFacebookMatch") boolean isFacebookMatch,
                         @Param("isEverydataMatch") boolean isEverydataMatch,
                         @Param("isFilterWebsite") boolean isFilterWebsite);

    long getDetailedRecordsCount(@Param("orConditions") List<List<DataRequest.Entity>> orConditions,
                                 @Param("andConditions") List<List<List<DataRequest.Entity>>> andConditions,
                                 @Param("countOrConditions") List<DataRequest.Entity> countOrConditions,
                                 @Param("countFiled") String countFiled,
                                 @Param("lists") List<Integer> lists,
                                 @Param("uploaded") List<Integer> uploaded,
                                 @Param("tableName") String tableName,
                                 @Param("dataType") int dataType,
                                 @Param("unique") boolean unique,
                                 @Param("uniqueEmails") boolean uniqueEmails,
                                 @Param("uniqueBusinessName") boolean uniqueBusinessName,
                                 @Param("filterDNC") boolean isFilterDNC,
                                 @Param("filterEmptyPhone") boolean isFilterEmptyPhone,
                                 @Param("filterEmail") boolean isFilterEmail,
                                 @Param("filterBusinessEmail") boolean isBusinessFilterEmail,
                                 @Param("isBlackListMatch") boolean isBlackListMatch,
                                 @Param("isConsumerMatch") boolean isConsumerMatch,
                                 @Param("isCraigslistMatch") boolean isCraigslistMatch,
                                 @Param("isDirectoryMatch") boolean isDirectoryMatch,
                                 @Param("isOptInMatch") boolean isOptinMatch,
                                 @Param("isBusinessDetailedMatch") boolean isBusinessDetailedMatch,
                                 @Param("isCallLeadsMatch") boolean isCallLeadsMatch,
                                 @Param("isFilterWebsite") boolean isFilterWebsite);

    void updateListItems(@Param("listId") int id,
                         @Param("orConditions") List<List<DataRequest.Entity>> orConditionList,
                         @Param("andConditions") List<List<List<DataRequest.Entity>>> andConditionsList,
                         @Param("lists") List<Integer> lists,
                         @Param("uploaded") List<Integer> uploaded,
                         @Param("tableName") String tableName,
                         @Param("dataType") int dataType,
                         @Param("unique") boolean unique,
                         @Param("isLocalNumber") boolean isLocalNumber,
                         @Param("isFbHispanicLName") boolean isFbHispanicLName,
                         @Param("removeCorps") boolean removeCorps,
                         @Param("confirmed") boolean confirmed,
                         @Param("businessMatch") boolean businessMatch,
                         @Param("businessMatch2") boolean businessMatch2,
                         @Param("consumer2018Match") boolean consumer2018Match,
                         @Param("consumer2019Match") boolean consumer2019Match,
                         @Param("whoisMatch") boolean whoisMatch,
                         @Param("healthBuyersMatch") boolean healthBuyersMatch,
                         @Param("healthInsuranceMatch") boolean healthInsuranceMatch,
                         @Param("instagramMatch") boolean instagramMatch,
                         @Param("uniqueEmails") boolean uniqueEmails,
                         @Param("uniqueBusinessName") boolean uniqueBusinessName,
                         @Param("filterDNC") boolean isFilterDNC,
                         @Param("filterEmptyPhone") boolean filterEmptyPhone,
                         @Param("filterEmail") boolean isFilterEmail,
                         @Param("filterBusinessEmail") boolean filterBusinessEmail,
                         @Param("isBlackListMatch") boolean isBlackListMatch,
                         @Param("isConsumerMatch") boolean isConsumerMatch,
                         @Param("isCraigslistMatch") boolean isCraigslistMatch,
                         @Param("isDirectoryMatch") boolean isDirectoryMatch,
                         @Param("isOptInMatch") boolean isOptinMatch,
                         @Param("isBusinessDetailedMatch") boolean isBusinessDetailedMatch,
                         @Param("isCallLeadsMatch") boolean isCallLeadsMatch,
                         @Param("isFacebookMatch") boolean isFacebookMatch,
                         @Param("isEverydataMatch") boolean isEverydataMatch,
                         @Param("isFilterWebsite") boolean isFilterWebsite);



    List<SearchEngine> getSearchEngineListByPurchasedList(@Param("listId") int listId,
                                                          @Param("tableName") String tableName,
                                                          @Param("offset") long offset,
                                                          @Param("limit") int limit);

    List<Instagram> getInstagramListByPurchasedList(@Param("listId") int listId,
                                                    @Param("tableName") String tableName,
                                                    @Param("offset") long offset,
                                                    @Param("limit") int limit);

    List<Instagram2020> getInstagram2020ListByPurchasedList(@Param("listId") int listId,
                                                            @Param("tableName") String tableName,
                                                            @Param("offset") long offset,
                                                            @Param("limit") int limit,
                                                            @Param("isNotPurchased") boolean isNotPurchased);

    List<OptIn> getOptInListByPurchasedList(@Param("listId") int listId,
                                            @Param("tableName") String tableName,
                                            @Param("offset") long offset,
                                            @Param("limit") int limit);

    List<NewOptIn> getNewOptInListByPurchasedList(@Param("listId") int listId,
                                                  @Param("tableName") String tableName,
                                                  @Param("offset") long offset,
                                                  @Param("limit") int limit);

    List< Consumer > getConsumerListByPurchasedList(@Param("listId") int listId,
                                                    @Param("tableName") String tableName,
                                                    @Param("offset") long offset,
                                                    @Param("limit") int limit);

    List< HealthInsuranceLead > getHealthInsuranceLeadsByPurchasedList(@Param("listId") int listId,
                                                                       @Param("tableName") String tableName,
                                                                       @Param("offset") long offset,
                                                                       @Param("limit") int limit);

    List< Consumer2 > getConsumer2ListByPurchasedList(@Param("listId") int listId,
                                                      @Param("tableName") String tableName,
                                                      @Param("offset") long offset,
                                                      @Param("limit") int limit,
                                                      @Param("isNotPurchased") boolean isNotPurchased);

    List< Consumer2 > getConsumer3ListByPurchasedList(@Param("listId") int listId,
                                                      @Param("tableName") String tableName,
                                                      @Param("offset") long offset,
                                                      @Param("limit") int limit,
                                                      @Param("isNotPurchased") boolean isNotPurchased);

    List< Consumer > getConsumerListByNonPurchasedList(@Param("listId") int listId,
                                                       @Param("tableName") String tableName,
                                                       @Param("offset") long offset,
                                                       @Param("limit") int limit);

    List< Consumer2 > getConsumer2ListByNonPurchasedList(@Param("listId") int listId,
                                                         @Param("tableName") String tableName,
                                                         @Param("offset") long offset,
                                                         @Param("limit") int limit);

    List< Business > getBusinessListByPurchasedList(@Param("listId") int listId,
                                                    @Param("tableName") String tableName,
                                                    @Param("offset") long offset,
                                                    @Param("limit") int limit);

    List< Facebook > getFacebookListByPurchasedList(@Param("listId") int listId,
                                                    @Param("tableName") String tableName,
                                                    @Param("offset") long offset,
                                                    @Param("limit") int limit);

    List<BusinessDetailed> getBusinessDetailedByPurchasedList(@Param("listId") int listId,
                                                              @Param("tableName") String tableName,
                                                              @Param("offset") long offset,
                                                              @Param("limit") int limit);

    List<Student> getStudentByPurchasedList(@Param("listId") int listId,
                                            @Param("tableName") String tableName,
                                            @Param("offset") long offset,
                                            @Param("limit") int limit);

    List<HealthBuyer> getHealthBuyersByPurchasedList(@Param("listId") int listId,
                                                     @Param("tableName") String tableName,
                                                     @Param("offset") long offset,
                                                     @Param("limit") int limit);

    List<Directory> getDirectoryListByPurchasedList(@Param("listId") int listId,
                                                    @Param("tableName") String tableName,
                                                    @Param("offset") long offset,
                                                    @Param("limit") int limit);
    List<Everydata> getEverydataListByPurchasedList(@Param("listId") int listId,
                                                    @Param("tableName") String tableName,
                                                    @Param("offset") long offset,
                                                    @Param("limit") int limit);

    List< Business > getBusinessListByNonPurchasedList(@Param("listId") int listId,
                                                       @Param("tableName") String tableName,
                                                       @Param("offset") long offset,
                                                       @Param("limit") int limit);

    List<CraigsList> getCraiglistByPurchasedList(@Param("listId") int listId,
                                                 @Param("tableName") String tableName,
                                                 @Param("offset") long offset,
                                                 @Param("limit") int limit);



    @Select("SELECT distinct clean_source FROM ${tableName} where clean_source like concat('%',#{domainSource},'%')")
    List<String> findDomainSources(@Param("domainSource") String domainSource, @Param("tableName") String tableName);

    @Select("SELECT distinct job FROM ${tableName} where job <> ''")
    List<String> findFacebookJobs(@Param("tableName") String tableName);

    @Select("SELECT distinct lastname FROM fb_hispanic_last_name where lastname ilike concat('%',#{strSearch},'%')")
    List<String> findFacebookHLastName(@Param("strSearch") String strSearch);

    @Select("SELECT distinct carrier FROM ${tableName}")
    List<String> findCarrierBrand( @Param("tableName") String tableName);

    @Select("SELECT name FROM industries")
    List< String > findIndustries();

    void createConsumersTable(@Param("tableName") String tableName,
                              @Param("indexName") String indexName);

    void createFacebookTable(@Param("tableName") String tableName,
                              @Param("indexName") String indexName);

    void createConsumersTableIndexes(@Param("tableName") String tableName,
                                     @Param("indexName") String indexName);

    void createFacebookTableIndexes(@Param("tableName") String tableName,
                                     @Param("indexName") String indexName);

    void createBusinessTable(@Param("tableName") String tableName,
                             @Param("indexName") String indexName);

    void createBusinessTableIndexes(@Param("tableName") String tableName,
                                    @Param("indexName") String indexName);

    void createOptIn2TableIndexes(@Param("tableName") String tableName,
                                  @Param("indexName") String indexName);

    void createEverydataTableIndexes(@Param("tableName") String tableName,
                                  @Param("indexName") String indexName);

    void createDirectoryTable(@Param("tableName") String tableName,
                              @Param("indexName") String indexName);

    void createPhilDirectoryTable(@Param("tableName") String tableName,
                              @Param("indexName") String indexName);


    void createStudentTable(@Param("tableName") String tableName,
                            @Param("indexName") String indexName);

    void createCraigslistTable(@Param("tableName") String name,
                               @Param("indexName") String indexName);


    void createBusinessDetailedTable(@Param("tableName") String name,
                                     @Param("indexName") String indexName);

    @Select("SELECT * FROM dataTables WHERE removed = 0 AND type=#{type} AND visible = true ORDER BY name")
    List< DataTable > getVisibleTablesByTypes(@Param("type") int type);

    @Select("SELECT * FROM dataTables WHERE removed = 0 AND type=#{type} AND phoneType=#{phoneType} AND visible = true ORDER BY name")
    List< DataTable > getVisibleTablesByTypeAndPhoneType(@Param("type") int type,
                                                         @Param("phoneType") int phoneType);


    @Update("ALTER TABLE ${oldName} RENAME TO ${newName}")
    void renameTable(@Param("oldName") String oldName,
                     @Param("newName") String newName);

    @Select("SELECT * FROM dataTables WHERE removed = 0 ORDER BY name")
    List< DataTable > getAllTables();

    @Select("SELECT * FROM dataTables WHERE id =#{id}")
    DataTable getTableById(@Param("id") int id);

    @Update("UPDATE dataTables SET removed = 1 WHERE id = #{id}")
    void markTableAsRemoved(@Param("id") long id);

    @Select("SELECT * FROM dataTables WHERE name = #{name} AND removed = 0")
    DataTable getTableByName(@Param("name") String name);

    @Update("UPDATE dataTables SET name = #{name}, type = #{type}, visible = #{visible} WHERE id = #{id}")
    void updateTable(DataTable dataTable);

    @Insert("INSERT INTO dataTables (name, type, visible, phoneType) VALUES (#{name}, #{type}, #{visible}, #{phoneType})")
    void insertTable(DataTable dataTable);

    @Update("UPDATE ${tableName} SET hash = md5(phone) WHERE hash IS NULL")
    void updateBusinessHash(@Param("tableName") String tableName);

    @Update("UPDATE ${tableName} SET hash = md5(phone) WHERE hash IS NULL")
    void updateConsumersHash(@Param("tableName") String tableName);

    @Select("SELECT MAX(id) FROM ${tableName}")
    Long getTableMaxId(@Param("tableName") String tableName);

    @Update("UPDATE ${tableName} SET hash = md5(phone) WHERE hash IS NULL AND id >= #{fromId} AND id <= #{toId}")
    void updateConsumersHashByIdRange(@Param("tableName") String tableName,
                                      @Param("fromId") long fromId,
                                      @Param("toId") long toId);

    @Update("UPDATE ${tableName} SET hash = md5(phone) WHERE hash IS NULL AND id >= #{fromId} AND id <= #{toId}")
    void updateConsumers2HashByIdRange(@Param("tableName") String tableName,
                                       @Param("fromId") long fromId,
                                       @Param("toId") long toId);

    @Update("update consumers2018 as co set county = c.county FROM cities as c WHERE (co.city = c.city OR co.city=UPPER(c.city)) and st_code = #{ST_CODE}")
    void updateCountiesForState(@Param("ST_CODE") int i);

    @Update("update ${tableName} as co set county = c.county FROM cities as c WHERE co.city = c.city and co.${stateColumn} = c.state and co.id <= #{end} and co.id >= #{start} and (co.county = '' or co.county is null);")
    void updateCountiesForIdRange(@Param("tableName") String tableName,
                                  @Param("stateColumn") String stateColumn,
                                  @Param("start") long start,
                                  @Param("end") long end);

    @Update("update ${tableName} as co set county = c.county FROM cities as c WHERE co.city = c.city_upper and co.${stateColumn} = c.state and co.id <= #{end} and co.id >= #{start} and (co.county = '' or co.county is null);")
    void updateCountiesUpperForIdRange(@Param("tableName") String tableName,
                                       @Param("stateColumn") String stateColumn,
                                       @Param("start") long start,
                                       @Param("end") long end);

    @Update("DROP INDEX ${index}")
    void dropIndex(@Param("index") String indexName,
                   @Param("table") String tableName);

    @Update("CREATE INDEX ${index} ON ${table}(${columns});")
    void createIndex(@Param("index") String indexName,
                     @Param("table") String tableName,
                     @Param("columns") String columns);

    @Select("select indexName from pg_indexes where tableName = #{table};")
    List<String> getIndexesFromTable(@Param("table") String tableName);

    @Update("UPDATE ${table} SET age = #{age} WHERE id >= #{start} AND id <= #{end} AND DOB_DATE > #{left} AND DOB_DATE <=#{right}")
    void updateAgeCategoriesForTableAndIdRange(@Param("table") String tableName,
                                               @Param("start") long start,
                                               @Param("end") long end,
                                               @Param("left") long left,
                                               @Param("right") long right,
                                               @Param("age") int i);

    @Select("SELECT MIN(id) FROM ${table} WHERE ${condition}")
    Long getTableMinIdWithCondition(@Param("table") String tableName,
                                    @Param("condition") String condition);

    @Update("UPDATE ${table} as co SET county='' WHERE co.county IS NULL AND co.id <= #{end} and co.id >= #{start};")
    void updateEmptyCountiesForIdRange(@Param("table") String tableName,
                                       @Param("stateColumn") String stateColumn,
                                       @Param("start") long start,
                                       @Param("end") long end);

    @Update("UPDATE ${table} as t set phoneType = p.type FROM phonePrefixes as p WHERE t.PHONE LIKE p.prefix AND t.id <= #{end} and t.id >= #{start};")
    void updatePhoneTypesForIdRange(@Param("table") String tableName,
                                    @Param("start") long start,
                                    @Param("end") long end);

    @Update("UPDATE ${table} as t SET phoneType=-1 WHERE t.phoneType IS NULL AND t.id <= #{end} and t.id >= #{start};")
    void updateEmptyPhoneTypesForIdRange(@Param("table") String tableName,
                                         @Param("start") long start,
                                         @Param("end") long end);

    @Select("SELECT id, PHONE FROM ${table} WHERE id >= #{startIndex} AND id <= #{endIndex}")
    List< PhoneEntity > getPhoneEntityList(@Param("table") String tableName,
                                           @Param("startIndex") long startIndex,
                                           @Param("endIndex") long endIndex);

    @Update("UPDATE ${table} SET phoneType=#{phoneType} WHERE id=#{id}")
    void updatePhoneType(@Param("table") String tableName,
                         @Param("id") long id,
                         @Param("phoneType") Integer phoneType);

    @Update("UPDATE ${table} SET age = 0 WHERE id >= #{start} AND id <= #{end} AND AGE is null")
    void updateUnknownAgeCategoriesForTableAndIdRange(@Param("table") String tableName,
                                                      @Param("start") long start,
                                                      @Param("end") long end);

    @Insert("INSERT INTO ${newTableName} SELECT * FROM ${tableName} WHERE ${tableName}.phoneType=#{phoneType} AND ${tableName}.id >= #{start} AND ${tableName}.id < #{end}")
    void copyDataWithPhoneType(@Param("tableName") String tableName,
                               @Param("newTableName") String newTableName,
                               @Param("phoneType") int phoneType,
                               @Param("start") long start,
                               @Param("end") long end);

    @Select("SELECT * FROM itemTypePrices WHERE dataSource != -1 AND resellerId = 0 ORDER BY dataSource, type")
    List<Price> getAllPrices();

    @Select("SELECT * FROM itemTypePrices WHERE dataSource != -1 AND resellerId = #{resellerId} ORDER BY dataSource, type")
    List<Price> getResellerPrices(@Param("resellerId") int resellerId);

    @Insert("INSERT INTO itemTypePrices(type, price, datasource, resellerId) SELECT type, price, datasource, #{resellerId} FROM itemTypePrices WHERE resellerId = 0")
    void insertResellerPrices(@Param("resellerId") int resellerId);

    @Update("UPDATE itemTypePrices SET price=#{price} WHERE id=#{id}")
    void updatePrice(Price price);

    @Select("SELECT * FROM itemTypePrices WHERE type=#{type} AND dataSource=#{dataSource} AND resellerId=#{resellerId} LIMIT 1")
    Price getPriceByType(@Param("type") int type,
                         @Param("dataSource") int dataSource,
                         @Param("resellerId") int resellerId);

    @Select("SELECT * FROM categories ORDER BY name")
    List<Category> findCategories();

    @Select("SELECT * FROM everydatasources ORDER BY name")
    List<Category> findEverydataCategories();

    @Select("SELECT * FROM studentSources ORDER BY name")
    List<Category> findStudentSources();

    @Select("SELECT * FROM cities ORDER BY id LIMIT #{limit} OFFSET #{offset}")
    List<City> getCityListWithPageOffsetAndLimit(@Param("offset") int offset,
                                                 @Param("limit") int limit);

    @Select("SELECT * FROM areaCodes ORDER BY id LIMIT #{limit} OFFSET #{offset}")
    List<AreaCode> findAreaCodesWithOffsetAndLimit(@Param("offset") int offset,
                                                   @Param("limit") int limit);

    void insertCraigsList(@Param("items") List<CraigsList> data,
                          @Param("tableName") String table);

    @Update("UPDATE ${table} SET ZIP_CODE = ZIP::int WHERE id >= #{start} AND id <= #{end}")
    void updateZipCodesForRange(@Param("table") String tableName,
                                @Param("start") long start,
                                @Param("end") long end);

    @Select("SELECT * FROM dataSources ORDER BY lower(name)")
    List<DataSource> findAllDataSources();

    @Select("select distinct type,name  from datatables where name not like '%Mobile%' and name not like '%Landlines%' and removed = 0")
    List<DataSource> findAllTables();

    List<DataSource> findAllDataSourcesForUser(@Param("userId") int userId,
                                               @Param("resellerId") int resellerId);
    List<String> findAllDataBlockedDatasources(@Param("userId") int userId);


    @Update("UPDATE dataSources SET visible=#{visible} WHERE id=#{id}")
    void updateDataSourceVisibility(@Param("id") Integer id,
                                    @Param("visible") Boolean visible);

    @Update("UPDATE datasourcesblockedusers SET state=#{blockedState} WHERE datasourcesid=#{id} AND state = #{currentState}")
    void updateDataSourceBlockedState(@Param("id") Integer id,
                                      @Param("currentState") Integer currentState,
                                      @Param("blockedState") Integer blockedState);





    @Delete("update dataSourcesBlockedUsers set state = 0 where state = #{state} and datasourcesid = #{datasourceid}")
    void removeBlockedUsersForDataSourceId(@Param("state") int state,@Param("datasourceid") int datasourceid);
//    @Delete("DELETE FROM dataSourcesBlockedUsers WHERE dataSourcesId=#{id}")
//    void removeBlockedUsersForDataSourceId(@Param("id") int id);

    @Select("select id from dataSourcesBlockedUsers where dataSourcesId = #{dataSourcesId} and userid = #{userId} limit 1;")
    Integer checkBlockedDatasource(@Param("dataSourcesId") int dataSourcesId, @Param("userId") int userId);

    void insertBlockedUsers(@Param("id") int id,
                            @Param("userId") int userId,
                            @Param("state") int state);

    void insertBlockedState(@Param("id") int id,
                            @Param("userIds") List<Integer> userIds,
                            @Param("state") int state);

    void updateBlockedState(@Param("id") int id,
                            @Param("userIds") List<Integer> userIds,
                            @Param("state") int state);

    @Select("SELECT * FROM dataSourcesBlockedUsers WHERE dataSourcesId=#{dataSourcesId} AND state =#{state}")
    List<BlockedUser> getBlockedUsersForDataSource(@Param("dataSourcesId") Integer id,@Param("state") Integer state);

    void insertWhoIsList(@Param("items") List<WhoIs> dataList,
                         @Param("tableName") String tableName);

    void createWhoIsTable(@Param("tableName") String name,
                          @Param("indexName") String indexName);

    List<WhoIs> getWhoIsListByPurchasedList(@Param("listId") int listId,
                                            @Param("tableName") String tableName,
                                            @Param("offset") long offset,
                                            @Param("limit") int limit);

    List<WhoIs> getWhoIsListByWebSites(@Param("tableName") String name,
                                       @Param("items") List<String> websites);

    long getMatchingCount_old(@Param("orConditions") List<List<DataRequest.Entity>> orConditions,
                          @Param("tableName") String tableName);

    long getMatchingCount(@Param("columns") List<String> columns,
                          @Param("tableName") String tableName,
                          @Param("datasource") String datasource);

    void insertMatchedRecords(@Param("orConditions") List<List<DataRequest.Entity>> orConditions,
                              @Param("data") String data,
                              @Param("tableName") String tableName,
                              @Param("listId") int listId);

    @Select("SELECT value FROM settings WHERE key=#{key} AND resellerId=#{resellerId} LIMIT 1")
    String getSettingsValueByKey(@Param("key") String key,
                                 @Param("resellerId") int resellerId);

    @Select("SELECT * FROM settings WHERE key=#{key} AND resellerId=#{resellerId} LIMIT 1")
    Setting getSettingByKey(@Param("key") String key,
                            @Param("resellerId") int resellerId);

    @Insert("INSERT INTO settings(key, value, resellerId) VALUES (#{setting.key}, #{setting.value}, #{resellerId})")
    void insertSetting(@Param("setting") Setting setting,
                       @Param("resellerId") int resellerId);

    @Update("UPDATE settings SET value=#{value} WHERE key=#{key} AND resellerId=#{resellerId}")
    void updateSettingValueByKey(@Param("key") String key,
                                 @Param("value") String value,
                                 @Param("resellerId") int resellerId);

    @Select("SELECT phone FROM ${tableName} WHERE phone like #{phone} AND id >= #{leftId} AND id < #{rightId}")
    List<String> getPhoneByTableNameAndPhoneIdRange(@Param("tableName") String name,
                                                    @Param("phone") String phone,
                                                    @Param("leftId") long leftId,
                                                    @Param("rightId") long rightId);

    @Select("SELECT phone FROM ${tableName} WHERE id >= #{leftId} AND id < #{rightId}")
    List<String> getPhoneByTableNameAndIdRange(@Param("tableName") String name,
                                               @Param("leftId") long leftId,
                                               @Param("rightId") long rightId);

    @Select("SELECT * FROM dataTables WHERE phoneType=#{phoneType} AND removed = 0")
    List<DataTable> getTablesByPhoneTypeAndNotRemoved(@Param("phoneType") int phoneType);

    void updatePhoneEntityDNC(@Param("tableName") String tableName,
                              @Param("ids") List<Long> ids,
                              @Param("value") boolean value);

    @Select("SELECT * FROM dataTables WHERE type=#{type} AND removed = 0")
    List<DataTable> getTablesByType(@Param("type") int type);

    @Update("UPDATE ${tableName} SET sic=sic_code::int WHERE sic_code != '' AND sic_code IS NOT NULL AND id >= #{fromId} AND id <= #{toId}")
    void updateBusinessTableSic(@Param("tableName") String name,
                                @Param("fromId") long fromId,
                                @Param("toId") long toId);

    @Insert("INSERT INTO ${dest}(${columns}) SELECT ${columns} FROM ${source} WHERE phoneType=#{phoneType} AND id >= #{leftId} AND id <= #{rightId}")
    void copyData(@Param("source") String sourceTableName,
                  @Param("dest") String destTableName,
                  @Param("phoneType") int phoneType,
                  @Param("leftId") long leftId,
                  @Param("rightId") long rightId,
                  @Param("columns") String columns);

    @Delete("DELETE FROM ${source} WHERE phoneType=#{phoneType} AND id >= #{leftId} AND id <= #{rightId}")
    void removeData(@Param("source") String sourceTableName,
                    @Param("phoneType") int phoneType,
                    @Param("leftId") long leftId,
                    @Param("rightId") long rightId);

    @Select("SELECT column_name FROM information_schema.columns WHERE table_name = #{name}")
    List<String> getTableColumns(@Param("name") String name);

    @Update("UPDATE ${name} SET hash = md5(phone) WHERE id >= #{leftId} AND id <= #{rightId}")
    void updateTableHash(@Param("name") String name,
                         @Param("leftId") long leftId,
                         @Param("rightId") long rightId);

    void updateListNonPurchasedItemsHash(@Param("listId") int id,
                                         @Param("tableName") String tableName);

    void updateListPurchasedItemsHash(@Param("listId") int id,
                                      @Param("tableName") String tableName);

    @Select("SELECT * FROM dataTables WHERE removed = 0")
    List<DataTable> getNonRemovedTables();

    void createSearchEngineTable(@Param("tableName") String name,
                                 @Param("indexName") String indexName);

    void createInstagramTable(@Param("tableName") String name,
                              @Param("indexName") String indexName);

    void createInstagram2020Table(@Param("tableName") String name,
                                  @Param("indexName") String indexName);

    void createOptInTable(@Param("tableName") String name,
                          @Param("indexName") String indexName);

    void createNewOptInTable(@Param("tableName") String name,
                             @Param("indexName") String indexName);

    void createEveridataTable(@Param("tableName") String name,
                             @Param("indexName") String indexName);


    @Update("UPDATE ${tableName} SET hash = md5(phone) WHERE id >= #{leftId} AND id <= #{rightId}")
    void updateSearchEngineListHashByIdRange(@Param("tableName") String tableName,
                                             @Param("leftId") long leftId,
                                             @Param("rightId") long rightId);

    void insertSearchEngineList(@Param("items") List<WhoIs> dataList,
                                @Param("tableName") String tableName);

    @Select("SELECT * FROM dataTables WHERE type=#{type} AND removed=0")
    List<DataTable> getAllTablesByType(@Param("type") int type);

    @Delete("DELETE FROM ${name} WHERE char_length(phone) = 11 AND phone NOT LIKE '1%' AND id >= #{leftId} AND id <= #{rightId}")
    void removeSearchEngineRecordsWithInvalidPhones(@Param("name") String name,
                                                    @Param("leftId") long leftId,
                                                    @Param("rightId") long rightId);

    @Update("UPDATE ${name} SET phone=trim(leading '1' from phone) WHERE char_length(phone) = 11 AND phone LIKE '1%' AND id >= #{leftId} AND id <= #{rightId}")
    void updateSearchEngineRecordsStartsWithOne(@Param("name") String name,
                                                @Param("leftId") long leftId,
                                                @Param("rightId") long rightId);

    @Select("SELECT id, phone FROM ${name} ORDER BY phone LIMIT #{limit} OFFSET #{offset}")
    List<SearchEngine> getSearchEngineListOrderedByPhone(@Param("name") String name,
                                                         @Param("offset") long offset,
                                                         @Param("limit") long limit);

    void bulkDeleteSearchEngineRecords(@Param("name") String name,
                                       @Param("records") List<Integer> recordsForDelete);

    @Select("SELECT * FROM dataTables WHERE phoneType=#{phoneType} AND type=#{type} AND removed=0")
    List<DataTable> getTablesByPhoneTypeAndTypeAndNotRemoved(@Param("phoneType") int phoneType,
                                                             @Param("type") int type);

    List<MatchedResult> getMatchedConsumerBusinessData(@Param("consumersTableName") String consumersTableName,
                                                       @Param("businessTableName") String businessTableName,
                                                       @Param("leftId") long leftId,
                                                       @Param("rightId") long rightId,
                                                       @Param("stateCode") int stateCode);

    @Select("SELECT value FROM settings WHERE key ILIKE #{key} AND resellerId=#{resellerId} ORDER BY id")
    List<String> getSettingsValuesByKey(@Param("key") String key,
                                        @Param("resellerId") int resellerId);

    void removeRecordsWithKeyWord(@Param("tableName") String name,
                                  @Param("value") String value,
                                  @Param("columns") List<String> columns,
                                  @Param("leftId") long leftId,
                                  @Param("rightId") long rightId);

    void createConsumers2Table(@Param("tableName") String name,
                               @Param("indexName") String indexName);

    @Update("UPDATE ${tableName} SET ST_CODE=#{stateCode} WHERE STATE=#{state} AND id >= #{leftId} AND id <= #{rightId}")
    void updateState(@Param("tableName") String name,
                     @Param("leftId") long leftId,
                     @Param("rightId") long rightId,
                     @Param("stateCode") int stateCode,
                     @Param("state") String state);

    @Select("SELECT id, PHONE FROM ${tableName} WHERE id >= #{leftId} AND id <= #{rightId} AND www = 'yelp2019'")
    List<PhoneEntity> getHotFrogPhoneEntityList(@Param("tableName") String name,
                                                @Param("leftId") long leftId,
                                                @Param("rightId") long rightId);

    @Update("UPDATE ${tableName} SET personLastName=trim(personLastName), personFirstName=trim(personFirstName) WHERE id >= #{leftId} AND id <= #{rightId}")
    void trimFields(@Param("tableName") String tableName,
                    @Param("leftId") long leftId,
                    @Param("rightId") long rightId);

    @Select("SELECT id, phone, BUSINESSOWNER FROM ${tableName} WHERE id >= #{leftId} AND id <= #{rightId}")
    List<Consumer2> getConsumers2ListByIdRange(@Param("tableName") String name,
                                               @Param("leftId") long leftId,
                                               @Param("rightId") long rightId);

    @Select("SELECT personfirstname, personlastname, primaryaddress, zipcode FROM ${tableName} WHERE id > #{leftId} AND id <= #{rightId}")
    List<Consumer2> getConsumers2ForOptInListByIdRange(@Param("tableName") String name,
                                                       @Param("leftId") long leftId,
                                                       @Param("rightId") long rightId);

    @Select("SELECT MORTGAGELENDERNAME, PURCHASELENDERNAME, REFINANCELENDERNAME FROM ${tableName} WHERE id >= #{leftId} AND id <= #{rightId}")
    List<Consumer2> getConsumers2LendersListByIdRange(@Param("tableName") String name,
                                                      @Param("leftId") long leftId,
                                                      @Param("rightId") long rightId);

    void updateBusinessOwner(@Param("tableName") String name,
                             @Param("items") List<Consumer2> consumersForUpdate);

    @Select("SELECT * FROM lenders WHERE lender ILIKE #{value} ORDER BY lender LIMIT 30")
    List<Lender> findAllLenders(@Param("value") String value);

    @Insert("INSERT INTO lenders(lender) VALUES(#{lender})")
    void insertLender(@Param("lender") String lender);

    @Update("UPDATE ${tableName} SET phone='' WHERE (phone LIKE '-1%' OR phone IS NULL) AND id >= #{leftId} AND id <= #{rightId}")
    void updateEmptyPhoneValues(@Param("tableName") String tableName,
                                @Param("leftId") long leftId,
                                @Param("rightId") long rightId);

    @Select("SELECT * FROM ${tableName} WHERE id >= #{leftId} AND id <= #{rightId}")
    List<Instagram> getInstagramRecords(@Param("tableName") String tableName,
                                        @Param("leftId") long leftId,
                                        @Param("rightId") long rightId);

    @Update("UPDATE ${tableName} SET state=#{state}, st_code=#{stateCode} WHERE id=#{id}")
    void updateInstagramStateInfo(@Param("tableName") String tableName,
                                  @Param("id") long id,
                                  @Param("state") String state,
                                  @Param("stateCode") Integer stateCode);

    @Update("UPDATE ${tableName} SET sourceCriteria=trim(leading 'www.' from sourceCriteria) WHERE id >= #{leftId} AND id <= #{rightId}")
    void updateSourceCriteria(@Param("tableName") String tableName,
                              @Param("leftId") long leftId,
                              @Param("rightId") long rightId);

    @Insert("INSERT INTO settings (key, value, resellerId) SELECT key, value, #{resellerId} FROM settings WHERE key ILIKE #{key} AND resellerId=0")
    void insertSettings(@Param("key") String key,
                        @Param("resellerId") int resellerId);

    @Update("UPDATE dataSourcesVisibility SET visible=#{visible} WHERE resellerId=#{resellerId} AND dataSourceId = #{dataSourceId}")
    void updateResellerDataSourceVisibility(@Param("resellerId") int resellerId,
                                            @Param("dataSourceId")int dataSourceId,
                                            @Param("visible") boolean visible);

    @Select("SELECT visible FROM dataSourcesVisibility WHERE resellerId=#{resellerId} AND dataSourceId = #{dataSourceId}")
    Boolean getResellerDataSourceVisibility(@Param("resellerId") int resellerId,
                                            @Param("dataSourceId") int dataSourceId);

    @Insert("INSERT INTO dataSourcesVisibility(resellerId, dataSourceId, visible) VALUES(#{resellerId}, #{dataSourceId}, #{visible})")
    void insertResellerDataSourceVisibility(@Param("resellerId") int resellerId,
                                            @Param("dataSourceId") int dataSourceId,
                                            @Param("visible") boolean visible);

    @Select("SELECT dataSourceId AS id, visible FROM dataSourcesVisibility WHERE resellerId = #{resellerId}")
    List<DataSource> findResellerDataSources(@Param("resellerId") int id);

    void createAutoTable(@Param("tableName") String name,
                         @Param("indexName") String indexName);

    void createBlackListTable(@Param("tableName") String name,
                              @Param("indexName") String indexName);

    void createCallLeadsTable(@Param("tableName") String name,
                              @Param("indexName") String indexName);

    void createLinkedInTable(@Param("tableName") String name,
                             @Param("indexName") String indexName);

    void insertAutoList(@Param("items") List<Auto> dataList,
                        @Param("tableName") String tableName);

    void insertBlackList(@Param("items") List<BlackList> dataList,
                         @Param("tableName") String tableName);

    void insertCallLeadList(@Param("items") List<CallLead> dataList,
                            @Param("tableName") String tableName);

    void insertHealthBuyersList(@Param("items") List<HealthBuyer> dataList,
                                @Param("tableName") String tableName);

    void insertHealthInsuranceLeadList(@Param("items") List<HealthInsuranceLead> dataList,
                                       @Param("tableName") String tableName);

    void insertLinkedInList(@Param("items") List<LinkedIn> dataList,
                            @Param("tableName") String tableName);

    List<Auto> getAutoListByPurchasedList(@Param("listId") int listId,
                                          @Param("tableName") String tableName,
                                          @Param("offset") long offset,
                                          @Param("limit") int limit);

    List<BlackList> getBlackListByPurchasedList(@Param("listId") int listId,
                                                @Param("tableName") String tableName,
                                                @Param("offset") long offset,
                                                @Param("limit") int limit);

    List<LinkedIn> getLinkedInByPurchasedList(@Param("listId") int listId,
                                              @Param("tableName") String tableName,
                                              @Param("offset") long offset,
                                              @Param("limit") int limit);

    @Select("SELECT make FROM autoMakes WHERE make ILIKE #{search} ORDER BY make LIMIT #{limit}")
    List<String> findAutoMakesWithSearch(@Param("search") String search,
                                         @Param("limit") int limit);

    @Select("SELECT make FROM autoMakes ORDER BY make LIMIT #{limit}")
    List<String> findAutoMakes(@Param("limit") int limit);

    @Select("SELECT model FROM autoModels WHERE make=#{make} AND model ILIKE #{search} ORDER BY model LIMIT #{limit}")
    List<String> findAutoModelsWithSearch(@Param("make") String make,
                                          @Param("search") String search,
                                          @Param("limit") int limit);

    @Select("SELECT model FROM autoModels WHERE make=#{make} ORDER BY model LIMIT #{limit}")
    List<String> findAutoModels(@Param("make") String make,
                                @Param("limit") int limit);

    @Select("SELECT carrier_name FROM consumer_carrier_names where carrier_name ilike #{carrierName} ")
    List<String> findC2Carriers(@Param("carrierName") String carrierName);

    @Select("SELECT model, make FROM ${tableName} WHERE id >= #{leftId} AND id < #{rightId}")
    List<Auto> getAutoListWithLimitAndOffset(@Param("tableName") String tableName,
                                             @Param("leftId") long leftId,
                                             @Param("rightId") long rightId);

    @Insert("INSERT INTO autoMakes(make) VALUES(#{make})")
    void insertAutoMake(@Param("make") String make);

    @Insert("INSERT INTO autoModels(make, model) VALUES(#{make}, #{model})")
    void insertAutoModel(@Param("make") String make,
                         @Param("model") String model);

    void insertDirectoryUserItems(@Param("listId") int listId,
                                  @Param("companyName") String companyName);

    @Select("SELECT min(id) FROM ${tableName} WHERE www='facebook'")
    Long getDirectoryFacebookMinId(@Param("tableName") String name);

    @Select("SELECT id FROM ${tableName} WHERE id < #{rightId} AND id >= #{leftId}")
    List<Long> getRecordsIds(@Param("tableName") String tableName,
                             @Param("leftId") long leftId,
                             @Param("rightId") long rightId);

    @Select("SELECT itemId FROM purchasedUserListsItems WHERE listId = #{listId} ORDER BY id OFFSET #{offset} LIMIT #{limit}")
    List<Long> getConsumerIdsByPurchasedList(@Param("listId") int id,
                                             @Param("tableName") String tableName,
                                             @Param("offset") long offset,
                                             @Param("limit") int limit);

    @Select("SELECT itemId FROM userListsItems WHERE listId = #{listId} ORDER BY id OFFSET #{offset} LIMIT #{limit}")
    List<Long> getConsumerIdsByNonPurchasedList(@Param("listId") int id,
                                                @Param("tableName") String tableName,
                                                @Param("offset") long offset,
                                                @Param("limit") int limit);

    void insertConsumersRecordsWithIds(@Param("ids") List<Long> consumerIds,
                                       @Param("archiveTableName") String archiveTableName,
                                       @Param("tableName") String tableName);

    long getDetailedRecordsCountByPurchasedList(@Param("listId") int listId,
                                                @Param("tableName") String tableName,
                                                @Param("countOrConditions") List<DataRequest.Entity> countOrConditions,
                                                @Param("countColumn") String countColumn);

    long getDetailedRecordsCountByNonPurchasedList(@Param("listId") int listId,
                                                   @Param("tableName") String tableName,
                                                   @Param("countOrConditions") List<DataRequest.Entity> countOrConditions,
                                                   @Param("countColumn") String countColumn);

    @Select("SELECT * FROM dataSources")
    List<DataSource> getAllDataSources();

    @Select("SELECT * FROM itemTypePrices WHERE dataSource != -1 AND resellerId = #{resellerId} AND dataSource = #{dataSource}")
    List<Price> getResellerPricesByDataSource(@Param("resellerId") int resellerId,
                                              @Param("dataSource") int dataSource);

    @Insert("INSERT INTO itemTypePrices(type, price, datasource, resellerId) SELECT type, price, #{dataSource}, #{resellerId} FROM itemTypePrices WHERE resellerId = 0 AND dataSource = #{dataSource}")
    void insertResellerPricesForDataSource(@Param("resellerId") int resellerId,
                                           @Param("dataSource") int dataSource);

    @Update("UPDATE ${tableName} SET personfirstname=trim(both ' ' from personfirstname), personlastname=trim(both ' ' from personlastname) WHERE id >= #{leftId} AND id <= #{rightId}")
    void updateConsumersNames(@Param("tableName") String tableName,
                              @Param("leftId") long leftId,
                              @Param("rightId") long rightId);

    @Select("SELECT * FROM userItemTypePrices WHERE dataSource != -1 AND userId = #{userId} ORDER BY dataSource, type")
    List<Price> getAllPricesForUser(@Param("userId") int userId);

    @Select("SELECT * FROM userItemTypePrices WHERE id = #{id}")
    Price getUserPriceById(@Param("id") int id);

    @Insert("INSERT INTO userItemTypePrices(type, price, dataSource, userId) VALUES (#{type}, #{price}, #{dataSource}, #{userId})")
    void insertUserPrice(Price price);

    @Update("UPDATE userItemTypePrices SET price=#{price} WHERE id=#{id}")
    void updateUserPrice(Price price);

    @Select("SELECT * FROM userItemTypePrices WHERE type=#{type} AND dataSource=#{dataSource} AND userId=#{userId} LIMIT 1")
    Price getPriceByTypeAndUserId(@Param("type") int type,
                                  @Param("dataSource") int dataSource,
                                  @Param("userId") int userId);

    @Insert("INSERT INTO ${directory}(address,city,company_name,county,industry,phone,state,st_code,www,zip_code,area_code,zip,hash,date,phonetype,dnc) " +
            "SELECT address,city,company_name,county,industry,phone,state,st_code,www,zip_code,area_code,zip,hash,date,phonetype,dnc FROM ${directoryNewSource} WHERE id > #{leftId} AND id <= #{rightId}")
    void copyNewSourcesData(@Param("directoryNewSource") String directoryNewSource,
                            @Param("directory") String directory,
                            @Param("leftId") long left,
                            @Param("rightId") long right);

    @Select("SELECT title FROM titles WHERE title ILIKE #{searchValue} ORDER BY title LIMIT #{limit}")
    List<String> findTitles(StringListRequest request);

    @Select("SELECT count(title) FROM titles WHERE title ILIKE #{searchValue}")
    Integer findTitlesCount(StringListRequest request);

    @Select("SELECT title FROM businessTitles WHERE title ILIKE #{searchValue} ORDER BY title LIMIT #{limit}")
    List<String> findBusinessTitles(StringListRequest request);

    @Select("SELECT count(title) FROM businessTitles WHERE title ILIKE #{searchValue}")
    Integer findBusinessTitlesCount(StringListRequest request);

    @Select("SELECT category FROM instagramCategories WHERE category ILIKE #{searchValue} ORDER BY category LIMIT #{limit}")
    List<String> findInstagramCategories(StringListRequest request);

    @Select("SELECT count(category) FROM instagramCategories WHERE category ILIKE #{searchValue}")
    Integer findInstagramCategoriesCount(StringListRequest request);

    @Select("SELECT source FROM optinSources WHERE source ILIKE #{searchValue} ORDER BY source LIMIT #{limit}")
    List<String> findOptinSources(StringListRequest request);

    @Select("SELECT count(source) FROM optinSources WHERE source ILIKE #{searchValue}")
    Integer findOptinSourcesCount(StringListRequest request);

    @Select("SELECT industry, sic FROM detailedBusinessIndustries WHERE industry ILIKE #{searchValue} ORDER BY industry LIMIT #{limit}")
    List<DetailedBusinessIndustry> findDetailedBusinessIndustries(StringListRequest request);

    @Select("SELECT count(industry) FROM detailedBusinessIndustries WHERE industry ILIKE #{searchValue}")
    Integer findDetailedBusinessIndustriesCount(StringListRequest request);

    List<DetailedBusinessIndustry> findDetailedBusinessIndustriesByValues(StringListRequest request);

    void createHealthBuyerTable(@Param("tableName") String name,
                                @Param("indexName") String indexName);

    @Select("SELECT * FROM tableAccess")
    List<TableAccess> getAllTableAccess();

    void createHealthInsuranceLeadTable(@Param("tableName") String name,
                                        @Param("indexName") String indexName);
}


