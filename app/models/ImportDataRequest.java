package models;

public class ImportDataRequest {

    private String path;
    private String tableName;
    private long skipRecords;
    private boolean directPhone;

    public ImportDataRequest() {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getSkipRecords() {
        return skipRecords;
    }

    public void setSkipRecords(long skipRecords) {
        this.skipRecords = skipRecords;
    }

    public boolean isDirectPhone() {
        return directPhone;
    }

    public void setDirectPhone(boolean directPhone) {
        this.directPhone = directPhone;
    }
}
