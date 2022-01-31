package models;

import java.util.List;

public class StringListResponse {

    private int total;
    private List<String> data;

    public StringListResponse() {}

    public StringListResponse(int total, List<String> data) {
        this.total = total;
        this.data = data;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List<String> getData() { return data; }
    public void setData(List<String> data) { this.data = data; }

}
