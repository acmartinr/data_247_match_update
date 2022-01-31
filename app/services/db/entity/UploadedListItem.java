package services.db.entity;

public class UploadedListItem {

    public long id;
    public int listId;
    public String phone;

    public UploadedListItem() {}

    public UploadedListItem( int listId, String phone ) {
        this.listId = listId;
        this.phone = phone;
    }

    public long getId() { return id; }
    public void setId( long id ) { this.id = id; }

    public int getListId() { return listId; }
    public void setListId( int listId ) { this.listId = listId; }

    public String getPhone() { return phone; }
    public void setPhone( String phone ) { this.phone = phone; }

}
