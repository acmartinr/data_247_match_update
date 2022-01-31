package models;

import services.db.entity.ListEntity;

import java.util.List;

public class PagedListResponse {

    private int total;
    private List<ListEntity> lists;

    public PagedListResponse() {}

    public PagedListResponse(int total, List<ListEntity> lists ) {
        this.total = total;
        this.lists = lists;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List<ListEntity> getLists() { return lists; }
    public void setLists(List<ListEntity> lists) { this.lists = lists; }
}
