package models;

import services.db.entity.User;

import java.util.List;

public class UserListResponse {

    private int total;
    private List< User > users;

    public UserListResponse() {}

    public UserListResponse( int total, List< User > users ) {
        this.total = total;
        this.users = users;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List< User > getUsers() { return users; }
    public void setUsers( List< User > users ) { this.users = users; }

}
