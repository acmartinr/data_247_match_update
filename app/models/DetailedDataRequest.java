package models;

public class DetailedDataRequest {

    private DataRequest searchRequest;
    private DetailedRequest request;
    private Integer listId;

    public DetailedDataRequest() {}

    public DataRequest getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(DataRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    public DetailedRequest getRequest() {
        return request;
    }

    public void setRequest(DetailedRequest request) {
        this.request = request;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }
}
