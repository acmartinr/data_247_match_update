package models;

import services.db.entity.DetailedBusinessIndustry;

import java.util.List;

public class DetailedBusinessIndustryListResponse {

    private int total;
    private List<DetailedBusinessIndustry> data;

    public DetailedBusinessIndustryListResponse() {}

    public DetailedBusinessIndustryListResponse(Integer count, List<DetailedBusinessIndustry> data) {
        this.total = count;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DetailedBusinessIndustry> getData() {
        return data;
    }

    public void setData(List<DetailedBusinessIndustry> data) {
        this.data = data;
    }

}
