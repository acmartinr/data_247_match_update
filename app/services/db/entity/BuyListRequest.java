package services.db.entity;

public class BuyListRequest {

    private int userId;
    private int listId;
    private int newListId;
    private long total;

    public BuyListRequest() {}

    public int getUserId() { return userId; }
    public void setUserId( int userId ) { this.userId = userId; }

    public int getListId() { return listId; }
    public void setListId( int listId ) { this.listId = listId; }

    public long getTotal() { return total; }
    public void setTotal( long total ) { this.total = total; }

    public int getNewListId() { return newListId; }
    public void setNewListId(int newListId) { this.newListId = newListId; }
}
