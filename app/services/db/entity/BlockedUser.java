package services.db.entity;

public class BlockedUser {

    private int id;
    private int dataSourcesId;
    private int userId;

    public BlockedUser() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDataSourcesId() {
        return dataSourcesId;
    }

    public void setDataSourcesId(int dataSourcesId) {
        this.dataSourcesId = dataSourcesId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
