package models;

import java.util.List;

public class UpdateDataSourceBlockedUsersRequest {

    private int id;
    private List<Integer> userIds;

    public UpdateDataSourceBlockedUsersRequest() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
