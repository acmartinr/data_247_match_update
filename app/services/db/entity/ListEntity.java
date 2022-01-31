package services.db.entity;

import java.util.LinkedList;
import java.util.List;

public class ListEntity {

    private int id;
    private int userId;
    private String username;
    private String userEmail;
    private String sentEmail;
    private String reseller;
    private String userPhone;
    private float userBalance;
    private long cnt;
    private long pcnt;
    private long date;
    private String name;
    private String request;
    private int state;
    private int type;
    private String tableName;
    private String filePath;
    private List<String> columns = new LinkedList();
    private String savedColumns;
    private boolean filterDNC;
    private long total;
    private long purchasedTotal;
    private String message;

    public ListEntity() {}

    public ListEntity( String name, int userId, long cnt, long date ) {
        this.name = name;
        this.userId = userId;
        this.cnt = cnt;
        this.date = date;
    }
    public String getSentEmail() {
        return sentEmail;
    }

    public void setSentEmail(String sentEmail) {
        this.sentEmail = sentEmail;
    }

    public int getId() { return id; }
    public void setId( int id ) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId( int userId ) { this.userId = userId; }

    public long getCnt() { return cnt; }
    public void setCnt( long cnt ) { this.cnt = cnt; }

    public long getDate() { return date; }
    public void setDate( long date ) { this.date = date; }

    public String getRequest() { return request; }
    public void setRequest( String request ) { this.request = request; }

    public String getName() { return name; }
    public void setName( String name ) { this.name = name; }

    public int getState() { return state; }
    public void setState( int state ) { this.state = state; }

    public long getPcnt() { return pcnt; }
    public void setPcnt( long pcnt ) { this.pcnt = pcnt; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public String getTableName() {  return tableName; }
    public void setTableName( String tableName ) { this.tableName = tableName; }

    public String getUsername() { return username; }
    public void setUsername( String username ) { this.username = username; }

    public String getSavedColumns() { return savedColumns; }
    public void setSavedColumns(String savedColumns) { this.savedColumns = savedColumns; }

    public List<String> getSavedColumnsList() {
        List<String> result = new LinkedList();

        if (savedColumns != null && savedColumns.length() > 0) {
            String[] parts = savedColumns.split(",");
            for (String part: parts) {
                result.add(part.trim());
            }
        }

        return result;
    }

    public ListEntity copy() {
        ListEntity result = new ListEntity();

        result.setUserId(getUserId());
        result.setCnt(getCnt());
        result.setPcnt(getPcnt());
        result.setDate(getDate());
        result.setName(getName());
        result.setRequest(getRequest());
        result.setState(getState());
        result.setType(getType());
        result.setTableName(getTableName());
        result.setSavedColumns(getSavedColumns());

        return result;
    }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public boolean isFilterDNC() {
        return filterDNC;
    }

    public void setFilterDNC(boolean filterDNC) {
        this.filterDNC = filterDNC;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getReseller() {
        return reseller;
    }

    public void setReseller(String reseller) {
        this.reseller = reseller;
    }

    public float getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(float userBalance) {
        this.userBalance = userBalance;
    }

    public long getPurchasedTotal() {
        return purchasedTotal;
    }

    public void setPurchasedTotal(long purchasedTotal) {
        this.purchasedTotal = purchasedTotal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
