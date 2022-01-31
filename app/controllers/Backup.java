package controllers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.DataTable;
import models.Response;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.db.dao.DataDAO;
import services.db.dao.ListDAO;
import services.db.entity.Business;
import services.db.entity.Consumer;
import services.db.entity.Consumer2;
import services.db.entity.ListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class Backup extends Controller {

    private ListDAO listDAO;
    private DataDAO dataDAO;
    private Date backupDate;

    private DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");

    @Inject
    public Backup(ListDAO listDAO,
                  DataDAO dataDAO) {
        this.listDAO = listDAO;
        this.dataDAO = dataDAO;
    }

    public Result backupListsData(String tableName) {
        if (!"58d4b9ac-1b31-4b48-915e-bccb37b4e695".equals(request().getQueryString("uuid"))) return notFound();

        List<ListEntity> businessLists = listDAO.getAllBusinessListsWithTableName(tableName);
        List<ListEntity> consumersLists = listDAO.getAllConsumersListsWithTableName(tableName);

        int totalCount = businessLists.size() + consumersLists.size();

        Map<String, Set<Long>> recordsIds = new HashMap();
        ListEntity listEntity = null;

        for (int i = 0; i < businessLists.size(); i++) {
            try {
                listEntity = businessLists.get(i);
                backupListData(listEntity, i, totalCount, recordsIds);
            } catch (Exception e) {
                e.printStackTrace();

                return ok(Json.toJson(Response.ERROR(
                        String.format("Making backup of list [%s] failed", listEntity.getName()),
                        null)));
            }
        }

        for (int i = 0; i < consumersLists.size(); i++) {
            try {
                listEntity = consumersLists.get(i);
                backupListData(
                        listEntity, businessLists.size() + i, totalCount, recordsIds);
            } catch (Exception e) {
                e.printStackTrace();

                return ok(Json.toJson(Response.ERROR(
                        String.format("Making backup of list [%s] failed", listEntity.getName()),
                        null)));
            }
        }

        recordsIds.clear();
        backupDate = null;
        Logger.info("Making backup for table " + tableName + " is finished");

        return ok(Json.toJson(Response.OK()));
    }

    private void backupListData(ListEntity listEntity,
                                int listIndex,
                                int totalCount,
                                Map<String, Set<Long>> recordsIds) {
        DataTable table = dataDAO.getTableByName(listEntity.getTableName());
        if (table != null) {
            DataTable archivedTable = createArchivedTable(table);
            if (recordsIds.get(listEntity.getTableName()) == null ||
                    recordsIds.get(listEntity.getTableName()).size() == 0) {
                updateRecordsIds(archivedTable.getName(), listEntity.getTableName(), recordsIds);
            }

            backupPurchasedItems(
                    archivedTable, listEntity, totalCount, listIndex, recordsIds);
            backupNonPurchasedItems(
                    archivedTable, listEntity, totalCount, listIndex, recordsIds);

            listEntity.setTableName(archivedTable.getName());
            listDAO.updateListTableName(listEntity);
        }
    }

    private void updateRecordsIds(String archivedName, String tableName, Map<String, Set<Long>> recordsIds) {
        Set<Long> ids = new HashSet();
        recordsIds.put(tableName, ids);

        long batch = 10000;
        long maxId = dataDAO.getTableMaxId(archivedName);
        long currentId = 0;

        while (currentId < maxId + batch) {
            ids.addAll(dataDAO.getRecordsIds(archivedName, currentId, currentId + batch));
            currentId = currentId + batch;
        }
    }

    private void backupNonPurchasedItems(DataTable archivedTable,
                                         ListEntity listEntity,
                                         int totalCount,
                                         int listIndex,
                                         Map<String, Set<Long>> recordsIds) {
        Set<Long> setRecordsIds = recordsIds.get(listEntity.getTableName());
        if (setRecordsIds == null) {
            setRecordsIds = new HashSet();
            recordsIds.put(listEntity.getTableName(), setRecordsIds);
        }

        switch (archivedTable.getType()) {
            case DataTable.CONSUMERS2:
            case DataTable.CONSUMERS3:
                backupConsumersItems(archivedTable, listEntity, totalCount, listIndex, setRecordsIds, false);
                break;
            case DataTable.BUSINESS:
                backupBusinessItems(archivedTable, listEntity, totalCount, listIndex, setRecordsIds, false);
                break;
        }
    }

    private void backupBusinessItems(DataTable archivedTable,
                                     ListEntity listEntity,
                                     int totalCount,
                                     int listIndex,
                                     Set<Long> recordsIds,
                                     boolean purchased) {
        String base = String.format("Making backup for %d from %d lists: ", listIndex + 1, totalCount);

        long offset = 0;
        int limit = 100;

        List<Business> businessList = getBusinessList(listEntity, purchased, offset, limit);
        updateBackupMessage(base, offset);

        int writtenCount = 0;

        while (businessList.size() > 0) {
            Iterator<Business> it = businessList.iterator();
            while (it.hasNext()) {
                Business business = it.next();
                if (recordsIds.contains(business.getId())) {
                    //System.err.println("RECORD IGNORED");
                    it.remove();
                } else {
                    recordsIds.add(business.getId());
                }
            }

            if (businessList.size() > 0) {
                dataDAO.insertBusinessesWithIds(businessList, archivedTable.getName());
                writtenCount = writtenCount + businessList.size();
                //System.err.println("COUNT: " + writtenCount);
            }

            offset = offset + limit;
            businessList = getBusinessList(listEntity, purchased, offset, limit);
            updateBackupMessage(base, offset);
        }
    }

    private void backupConsumersItems(DataTable archivedTable,
                                      ListEntity listEntity,
                                      int totalCount,
                                      int listIndex,
                                      Set<Long> recordsIds,
                                      boolean purchased) {
        String base = String.format("Making backup for %d [%s] from %d lists: ", listIndex + 1, listEntity.getName(), totalCount);

        long offset = 0;
        int limit = 10000;

        int writtenCount = 0;

        List<Long> consumerIds = getConsumersIds(listEntity, purchased, offset, limit);
        updateBackupMessage(base, offset);

        while (consumerIds.size() > 0) {
            consumerIds.removeAll(recordsIds);

            if (consumerIds.size() > 0) {
                dataDAO.insertConsumersRecordsWithIds(consumerIds, archivedTable.getName(), listEntity.getTableName());
                recordsIds.addAll(consumerIds);

                writtenCount = writtenCount + consumerIds.size();
            }

            offset = offset + limit;
            consumerIds = getConsumersIds(listEntity, purchased, offset, limit);
            updateBackupMessage(base, offset);
        }
    }

    private List<Long> getConsumersIds(ListEntity listEntity, boolean purchased, long offset, int limit) {
        if (purchased) {
            return dataDAO.getConsumerIdsByPurchasedList(
                    listEntity.getId(), listEntity.getTableName(), offset, limit);
        } else {
            return dataDAO.getConsumerIdsByNonPurchasedList(
                    listEntity.getId(), listEntity.getTableName(), offset, limit);
        }
    }

    private void updateBackupMessage(String base, long offset) {
        String backupListsDataMessage = String.format("%s inserted %d records", base, offset);
        if (offset % 1000 == 0) {
            Logger.info(backupListsDataMessage);
        }
    }

    private List<Consumer2> getConsumersList(ListEntity listEntity, boolean purchased, long offset, int limit) {
        if (purchased) {
            return dataDAO.getConsumer2ListByPurchasedList(
                    listEntity.getId(), listEntity.getTableName(), offset, limit, false);
        } else {
            return dataDAO.getConsumer2ListByNonPurchasedList(
                    listEntity.getId(), listEntity.getTableName(), offset, limit);
        }
    }

    private List<Business> getBusinessList(ListEntity listEntity, boolean purchased, long offset, int limit) {
        if (purchased) {
            return dataDAO.getBusinessListByPurchasedList(
                    listEntity.getId(), listEntity.getTableName(), offset, limit);
        } else {
            return dataDAO.getBusinessListByNonPurchasedList(
                    listEntity.getId(), listEntity.getTableName(), offset, limit);
        }
    }

    private void backupPurchasedItems(DataTable archivedTable,
                                      ListEntity listEntity,
                                      int totalCount,
                                      int listIndex,
                                      Map<String, Set<Long>> recordsIds) {
        Set<Long> setRecordsIds = recordsIds.get(listEntity.getTableName());
        if (setRecordsIds == null) {
            setRecordsIds = new HashSet();
            recordsIds.put(listEntity.getTableName(), setRecordsIds);
        }

        switch (archivedTable.getType()) {
            case DataTable.CONSUMERS2:
            case DataTable.CONSUMERS3:
                backupConsumersItems(archivedTable, listEntity, totalCount, listIndex, setRecordsIds, true);
                break;
            case DataTable.BUSINESS:
                backupBusinessItems(archivedTable, listEntity, totalCount, listIndex, setRecordsIds, true);
                break;
        }
    }

    private DataTable createArchivedTable(DataTable table) {
        if (backupDate == null) {
            backupDate = new Date();
        }

        /*String archivedTableName = String.format("%s_archived_%s",
                table.getName(),
                dateFormat.format(backupDate));*/

        String archivedTableName = String.format("%s_archived_08_25_2019",
                table.getName());

        DataTable archivedTable = dataDAO.getTableByName(archivedTableName);
        if (archivedTable == null) {
            switch (table.getType()) {
                case DataTable.CONSUMERS:
                    dataDAO.createConsumersTable(archivedTableName, false);
                    break;
                case DataTable.CONSUMERS2:
                case DataTable.CONSUMERS3:
                    dataDAO.createConsumers2Table(archivedTableName);
                    break;
                case DataTable.BUSINESS:
                    dataDAO.createBusinessTable(archivedTableName, false);
                    break;
            }

            dataDAO.insertTable(new DataTable(
                    archivedTableName,
                    table.getType(),
                    false,
                    table.getPhoneType()));
        }

        if (archivedTable == null) {
            archivedTable = dataDAO.getTableByName(archivedTableName);
        }

        return archivedTable;
    }
}
