package services.db.entity;

public class TableAccess {

    private int userId;
    private int tableId;

    public TableAccess() {}

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
