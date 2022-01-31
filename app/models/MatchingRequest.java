package models;

import java.util.LinkedList;
import java.util.List;

public class MatchingRequest {

    private String filePath;
    private String tableName;
    private int userId;

    private String selectedTable;
    private List<String> columns = new LinkedList();
    private String saveFields = "";
    private boolean filterDNC;

    public MatchingRequest() {}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getSaveFields() {
        return saveFields;
    }

    public void setSaveFields(String saveFields) {
        this.saveFields = saveFields;
    }

    public boolean isFilterDNC() {
        return filterDNC;
    }

    public void setFilterDNC(boolean filterDNC) {
        this.filterDNC = filterDNC;
    }


    public String getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

}
