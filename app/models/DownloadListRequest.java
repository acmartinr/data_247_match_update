package models;

import java.util.LinkedList;
import java.util.List;

public class DownloadListRequest {

    private Integer listId;
    private Integer userId;
    private List< String > columns = new LinkedList();
    private String code;



    private String emailAddress;
    private boolean notPurchased;

    public DownloadListRequest() {}

    public DownloadListRequest(int listId, int userId, String[] columns, String code) {
        this.listId = listId;
        this.userId = userId;

        for (String column: columns) {
            this.columns.add(column);
        }

        this.code = code;
    }

    public Integer getListId() { return listId; }
    public void setListId( Integer listId ) { this.listId = listId; }

    public Integer getUserId() { return userId; }
    public void setUserId( Integer userId ) { this.userId = userId; }
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddreess) {
        this.emailAddress = emailAddreess;
    }
    public List<String> getColumns() {
        List<String> orderedColumns = new LinkedList();

        for (int i = 0; i < columns.size(); i++) {
            String column = columns.get(i);

            if ("phone".equalsIgnoreCase(column)) {
                orderedColumns.add(0, column);
                continue;
            }

            if ("phoneType".equalsIgnoreCase(column)) {
                orderedColumns.add(1, column);
                continue;
            }

            orderedColumns.add(column);
        }

        return orderedColumns;
    }

    public void setColumns( List< String > columns ) { this.columns = columns; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public boolean isNotPurchased() {
        return notPurchased;
    }

    public void setNotPurchased(boolean notPurchased) {
        this.notPurchased = notPurchased;
    }
}
