package services.db.mapper;

import models.PagedListsRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import services.db.entity.BuyListRequest;
import services.db.entity.ListEntity;
import services.db.entity.UploadedListItem;

import java.util.List;

public interface ListMapper {

    List< ListEntity > findListsByUserIdAndState( @Param( "userId" ) int userId,
                                                  @Param( "state" ) int state,
                                                  @Param( "dataType" ) int dataType );

    void saveList( ListEntity list );
    void savePurchaseList( ListEntity list );
    void saveSentEmailList( @Param( "date" ) long date ,@Param( "name" ) String name,@Param( "emailout" ) String sentEmail,@Param( "emailin" ) String userEmail);

    @Delete( "DELETE FROM lists WHERE id = #{id}" )
    void deleteListById( @Param( "id" ) int id );

    @Update( "UPDATE lists SET name = #{name} WHERE id = #{id}" )
    void updateList( ListEntity list );

    @Update( "UPDATE lists SET userId = #{userId} WHERE id = #{id}" )
    void updateListUserId(ListEntity list);

    @Select( "SELECT * FROM lists WHERE id = #{listId}" )
    ListEntity findListById( @Param( "listId" ) int listId );

    @Update( "UPDATE lists SET cnt = #{cnt} WHERE id = #{listId}" )
    void updateCountById( @Param( "listId" ) int listId,
                          @Param( "cnt" ) long cnt );

    @Update( "UPDATE lists SET pcnt = #{pcnt} WHERE id = #{listId}" )
    void updatePurchasedCountById( @Param( "listId" ) int listId,
                                   @Param( "pcnt" ) long cnt );

    @Update( "UPDATE lists SET state = #{status} WHERE id = #{listId}" )
    void updateStatusById( @Param( "listId" ) int listId,
                           @Param( "status" ) int status );

    void insertBoughtListItems( BuyListRequest request );

    void deleteBoughtListItems( BuyListRequest request );

    @Select( "SELECT count(id) FROM lists WHERE tableName = #{name}" )
    int findListCountByTableName( @Param( "name" ) String name );

    @Update( "UPDATE lists SET tableName = #{newName} WHERE tableName = #{oldName}" )
    void updateListsTableNames( @Param( "oldName" ) String oldName,
                                @Param( "newName" ) String newName );


    @Select( "SELECT * FROM lists WHERE tableName = 'ConsumersOld'" )
    List<ListEntity> getAllConsumersLists();

    @Select( "SELECT * FROM lists WHERE type = 1" )
    List<ListEntity> getAllBusinessLists();

    @Select( "SELECT * FROM lists WHERE tableName = #{tableName}" )
    List<ListEntity> getAllBusinessListsWithTableName(@Param("tableName") String tableName);

    @Select( "SELECT * FROM lists WHERE tableName = #{tableName}" )
    List<ListEntity> getAllConsumersListsWithTableName(@Param("tableName") String tableName);

    void updateHashForConsumersListItems( ListEntity list );

    void updateHashForBusinessListItems( ListEntity list );

    void updateHashForPurchasedConsumersListItems( ListEntity list );

    void updateHashForPurchasedBusinessListItems( ListEntity list );

    List< ListEntity > getAllUploadedLists();

    List< ListEntity > getUploadedListsByUserId( @Param( "userId" ) int userId );

    @Update( "UPDATE uploadedLists SET name = #{name} WHERE id = #{id}" )
    void updateNameOfUploadedList( ListEntity listEntity );

    @Delete( "DELETE FROM uploadedLists WHERE id = #{listId}" )
    void deleteUploadedList( @Param( "listId" ) int listId );

    void insertUploadedList( ListEntity listEntity );

    @Update( "UPDATE uploadedLists SET cnt = #{count} WHERE id = #{listId}" )
    void updateCountOfUploadedListById( @Param( "count" ) long count,
                                        @Param( "listId" ) int listId );

    void insertUploadedListItems( @Param( "items" ) List< UploadedListItem > items );

    @Select( "SELECT * FROM lists WHERE name like #{template} AND tableName=#{tableName}" )
    List<ListEntity> findListWithNameTemplateAndTableName(@Param("template") String template,
                                                          @Param("tableName") String tableName);



    void createGenericTempTable(@Param("tableName") String tableName,
                                @Param("indexName") String indexName,
                                @Param( "columns" ) List<String> items
    );
    void insertUploadedTmpFileItems(@Param("tableName") String tableName,
                                    @Param( "rows" )List<List<String>> rows,
                                    @Param( "columns" )List<String> columns);

    void insertMatchFiles(@Param("tableName") String tableName,@Param("count") int count,@Param("date") long date);

    @Update("UPDATE lists SET date = #{date} WHERE id = #{id}")
    void updateDateByListId(@Param("date") long date,
                            @Param("id") int id);


    @Update( "UPDATE lists SET tableName = #{tableName} WHERE id = #{id}" )
    void updateListTableName(ListEntity listEntity);

    @Update("UPDATE lists SET matched=#{value} WHERE id=#{id}")
    void updateMatchedById(@Param("id") int id,
                           @Param("value") boolean value);

    @Select("SELECT matched FROM lists WHERE id=#{id}")
    boolean isListMatched(@Param("id") int listId);

    List<ListEntity> getPagedLists(@Param("request") PagedListsRequest request,
                                   @Param("state") int state);
    List<ListEntity> getPagedPurchasedListsLogs(@Param("request") PagedListsRequest request,
                                                @Param("state") int state);

    List<ListEntity> getPagedSentListsLogs(@Param("request") PagedListsRequest request,
                                           @Param("state") int state);


    int getPagedListsCount(@Param("request") PagedListsRequest request,
                           @Param("state") int state);
    List<ListEntity> getMatchTables();

    int getPagedPurchaseListsLogsCount(@Param("request") PagedListsRequest request,
                                       @Param("state") int state);
    int getPagedSentListsCount(@Param("request") PagedListsRequest request,
                               @Param("state") int state);


    @Select("SELECT * FROM lists")
    List<ListEntity> getAllLists();

    @Select("SELECT * FROM uploadedLists WHERE id=#{listId}")
    ListEntity findUploadedListById(@Param("listId") int listId);

    @Select("SELECT * FROM lists WHERE pcnt = 0 AND date < #{date}")
    List<ListEntity> getOldLists(long date);

    @Select("SELECT * FROM lists WHERE name like #{name}")
    List<ListEntity> findPurchasedLists(@Param("name") String name);

    @Select("SELECT phone FROM uploadedListsItems WHERE listId=#{listId} OFFSET #{offset} LIMIT #{limit}")
    List<String> getUploadedListsPhones(@Param("listId") int listId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    void copyPhonesToUploadedList(@Param("listId") Integer listId,
                                  @Param("uploadedListId") int uploadedListId,
                                  @Param("tableName") String tableName);

    @Select("SELECT * FROM lists WHERE name = #{name}")
    ListEntity findListByName(@Param("name") String name);
}
