package services.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import controllers.Data;
import models.DataRequest;
import models.DataTable;
import models.PagedListsRequest;
import org.mybatis.guice.transactional.Transactional;
import services.db.entity.BuyListRequest;
import services.db.entity.ListEntity;
import services.db.entity.UploadedListItem;
import services.db.mapper.ListMapper;

import java.util.List;

public class ListDAO {

    private final ListMapper mapper;
    private final DataDAO dataDAO;

    private final ObjectMapper objectMapper;

    @Inject
    public ListDAO(ListMapper mapper,
                   DataDAO dataDAO) {
        this.mapper = mapper;
        this.dataDAO = dataDAO;
        this.objectMapper = new ObjectMapper();
    }

    public List<ListEntity> getPurchasedListsByUserId(int userId, int dataType) {
        return mapper.findListsByUserIdAndState(userId, 2, dataType);
    }

    public List<ListEntity> getNonPurchasedListsByUserId(int userId) {
        return mapper.findListsByUserIdAndState(userId, 0, DataTable.ALL);
    }

    public void saveListWithoutItems(ListEntity listEntity) {
        mapper.saveList(listEntity);
    }

    public void savePurchaseList(ListEntity listEntity) {
        mapper.savePurchaseList(listEntity);
    }
    public void saveSentEmailList(long date,String name,String emailout,String emailin) {
        mapper.saveSentEmailList(date,name,emailout,emailin);
    }

    @Transactional
    public void saveList(ListEntity list) {
        mapper.saveList(list);

        try {
            DataRequest dataRequest = objectMapper.readValue(list.getRequest(), DataRequest.class);
            List<List<List<DataRequest.Entity>>> andConditions = Data.initAndConditions(dataRequest);
            if (dataRequest.getDataType() == DataTable.CONSUMERS) {
                andConditions.addAll(Data.getConsumersAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.CONSUMER_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.BUSINESS || dataRequest.getDataType() == DataTable.BUSINESS2) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.BUSINESS_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.DIRECTORY) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                andConditions.addAll(Data.getRestrictedSourcesConditionsList());
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.DIRECTORY_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.PHILDIRECTORY) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                andConditions.addAll(Data.getRestrictedSourcesConditionsList());
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.DIRECTORY_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.EVERYDATA) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.EVERYDATA_KEYWORD_COLUMNS);
            }else if (dataRequest.getDataType() == DataTable.CRAIGSLIST) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.CRAIGSLIST_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.WHOIS) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                Data.addWhoisDateCondition(andConditions);

                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.WHOIS_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.SEARCH_ENGINE) {
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.SEARCH_ENGINE_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.CONSUMERS2 || dataRequest.getDataType() == DataTable.CONSUMERS3) {
                andConditions.addAll(Data.getConsumers2AndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.CONSUMER_2_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.INSTAGRAM || dataRequest.getDataType() == DataTable.INSTAGRAM2020) {
                andConditions.addAll(Data.getInstagramAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.INSTAGRAM_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.OPTIN) {
                andConditions.addAll(Data.getOptInAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.OPTIN_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.NEWOPTIN) {
                andConditions.addAll(Data.getNewOptInAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.OPTIN_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.FACEBOOK) {
                andConditions.addAll(Data.getFacebookAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.FACEBOOK_KEYWORD_COLUMNS);

            } else if (dataRequest.getDataType() == DataTable.AUTO) {
                andConditions.addAll(Data.getAutoAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.AUTO_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.BLACKLIST) {
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.BLACKLIST_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.CALLLEADS) {
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.BLACKLIST_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.HEALTH_BUYER) {
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.HEALTH_BUYER_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.LINKEDIN) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.LINKEDIN_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.BUSINESS_DETAILED) {
                andConditions.addAll(Data.getBusinessAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.BUSINESS_DETAILED_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.STUDENT) {
                andConditions.addAll(Data.getStudentAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.STUDENT_KEYWORD_COLUMNS);
            } else if (dataRequest.getDataType() == DataTable.HEALTH_INSURANCE_LEAD) {
                andConditions.addAll(Data.getHealthInsuranceLeadAndConditionsList(dataRequest));
                Data.addKeywordConditions(andConditions, dataRequest.getKeywords(),
                        dataRequest.getKeywordsColumns(), Data.HEALTH_INSURANCE_KEYWORD_COLUMNS);
            }

            List<Integer> lists = dataRequest.getSelectedLists();
            dataDAO.updateListItems(
                    list.getId(),
                    Data.getOrConditionList(dataRequest),
                    andConditions,
                    lists,
                    dataRequest.getUploadedLists(),
                    dataRequest.getTableName(),
                    dataRequest.getDataType(),
                    dataRequest.isUnique(),
                    dataRequest.isLocalNumbers(),
                    dataRequest.isFbHispanicLName(),
                    dataRequest.isRemoveCorps(),
                    dataRequest.isConfirmed(),
                    dataRequest.isBusinessMatch(),
                    dataRequest.isBusinessMatch2(),
                    dataRequest.isConsumerMatch2018(),
                    dataRequest.isConsumerMatch2019(),
                    dataRequest.isWhoisMatch(),
                    dataRequest.isHealthBuyersMatch(),
                    dataRequest.isHealthInsuranceMatch(),
                    dataRequest.isInstagramMatch(),
                    dataRequest.isUniqueEmails(),
                    dataRequest.isUniqueBusinessName(),
                    dataRequest.isFilterDNC(),
                    dataRequest.isFilterEmptyPhone(),
                    dataRequest.isFilterEmail(),
                    dataRequest.isFilterBusinessEmail(),
                    dataRequest.isBlackListMatch(),
                    dataRequest.isConsumerMatch(),
                    dataRequest.isCraigslistMatch(),
                    dataRequest.isDirectoryMatch(),
                    dataRequest.isOptinMatch(),
                    dataRequest.isBusinessDetailedMatch(),
                    dataRequest.isCallLeadsMatch(),
                    dataRequest.isFacebookMatch(),
                    dataRequest.isEverydataMatch(),
                    dataRequest.isFilterWebsite());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteListById(int id) {
        mapper.deleteListById(id);
    }

    public void updateList(ListEntity list) {
        mapper.updateList(list);
    }

    public void updateListUserId(ListEntity list) {
        mapper.updateListUserId(list);
    }

    public ListEntity findListById(int listId) {
        return mapper.findListById(listId);
    }

    public void updateCountById(int listId, long cnt) {
        mapper.updateCountById(listId, cnt);
    }

    public void updatePurchasedCountById(int listId, long cnt) {
        mapper.updatePurchasedCountById(listId, cnt);
    }

    public void updateStatusById(int listId, int status) {
        mapper.updateStatusById(listId, status);
    }

    public void insertBoughtListItems(BuyListRequest request) {
        mapper.insertBoughtListItems(request);
    }


    public void deleteBoughtListItems(BuyListRequest request) {
        mapper.deleteBoughtListItems(request);
    }

    public int findListCountByTableName(String name) {
        return mapper.findListCountByTableName(name);
    }

    public void updateTableNames(String oldName, String newName) {
        mapper.updateListsTableNames(oldName, newName);
    }

    public List<ListEntity> getAllConsumersLists() {
        return mapper.getAllConsumersLists();
    }

    public List<ListEntity> getAllBusinessLists() {
        return mapper.getAllBusinessLists();
    }

    public void updateHashForConsumersListItems(ListEntity list) {
        mapper.updateHashForConsumersListItems(list);
        mapper.updateHashForPurchasedConsumersListItems(list);
    }

    public void updateHashForBusinessListItems(ListEntity list) {
        mapper.updateHashForBusinessListItems(list);
        mapper.updateHashForPurchasedBusinessListItems(list);
    }
    public List<ListEntity> getAllUploadedLists() {
        return mapper.getAllUploadedLists();
    }

    public List<ListEntity> getUploadedListsByUserId(int userId) {
        return mapper.getUploadedListsByUserId(userId);
    }

    public void updateNameOfUploadedList(ListEntity listEntity) {
        mapper.updateNameOfUploadedList(listEntity);
    }

    public void deleteUploadedList(int listId) {
        mapper.deleteUploadedList(listId);
    }

    public void insertUploadedList(ListEntity listEntity) {
        mapper.insertUploadedList(listEntity);
    }

    public void updateCountOfUploadedListById(long count, int listId) {
        mapper.updateCountOfUploadedListById(count, listId);
    }

    public void insertUploadedListItems(List<UploadedListItem> items) {
        mapper.insertUploadedListItems(items);
        items.clear();
    }
    public void createGenericTempTable(String name, String indexName,List<String> items){
        mapper.createGenericTempTable(name,indexName,items);
    }
    public void insertUploadedTmpFileItems(String name,List<List<String>> rows,List<String> columns){
        mapper.insertUploadedTmpFileItems(name,rows,columns);
    }
    public void insertMatchFiles(String name,int count,long date){
        mapper.insertMatchFiles(name,count,date);
    }
    public List<ListEntity> findListWithNameTemplateAndTableName(String template, String tableName) {
        return mapper.findListWithNameTemplateAndTableName(template, tableName);
    }

    public void updateDateByListId(long date, int id) {
        mapper.updateDateByListId(date, id);
    }

    public void updateListTableName(ListEntity listEntity) {
        mapper.updateListTableName(listEntity);
    }

    public void updateMatchedById(int id, boolean value) {
        mapper.updateMatchedById(id, value);
    }

    public boolean isListMatched(int listId) {
        return mapper.isListMatched(listId);
    }

    public List<ListEntity> getPurchasedLists(PagedListsRequest request) {
        return mapper.getPagedLists(request, 2);
    }

    public List<ListEntity> getPagedPurchasedListsLogs(PagedListsRequest request) {
        return mapper.getPagedPurchasedListsLogs(request, 2);
    }
    public List<ListEntity> getPagedSentListsLogs(PagedListsRequest request) {
        return mapper.getPagedSentListsLogs(request, 2);
    }

    public List<ListEntity> getNonPurchasedLists(PagedListsRequest request) {
        return mapper.getPagedLists(request, 0);
    }

    public int getPurchasedListsCount(PagedListsRequest request) {
        return mapper.getPagedListsCount(request, 2);
    }

    public int getPagedPurchaseListsLogsCount(PagedListsRequest request) {
        return mapper.getPagedPurchaseListsLogsCount(request, 2);
    }
    public List<ListEntity> getMatchTables() {
        return mapper.getMatchTables();
    }
    public int getPagedSentListsCount(PagedListsRequest request) {
        return mapper.getPagedSentListsCount(request, 2);
    }

    public int getNonPurchasedListsCount(PagedListsRequest request) {
        return mapper.getPagedListsCount(request, 0);
    }

    public List<ListEntity> getAllLists() {
        return mapper.getAllLists();
    }

    public ListEntity findUploadedListById(int listId) {
        return mapper.findUploadedListById(listId);
    }

    public List<ListEntity> getOldLists(long date) {
        return mapper.getOldLists(date);
    }

    public List<ListEntity> findPurchasedLists(String name) {
        return mapper.findPurchasedLists(name + "-part%");
    }

    public List<String> getUploadedListsPhones(int listId, int offset, int limit) {
        return mapper.getUploadedListsPhones(listId, offset, limit);
    }

    public void copyPhonesToUploadedList(Integer listId, int uploadedListId, String tableName) {
        mapper.copyPhonesToUploadedList(listId, uploadedListId, tableName);
    }

    public ListEntity findListByName(String name) {
        return mapper.findListByName(name);
    }

    public List<ListEntity> getAllBusinessListsWithTableName(String tableName) {
        return mapper.getAllBusinessListsWithTableName(tableName);
    }

    public List<ListEntity> getAllConsumersListsWithTableName(String tableName) {
        return mapper.getAllConsumersListsWithTableName(tableName);
    }
}
