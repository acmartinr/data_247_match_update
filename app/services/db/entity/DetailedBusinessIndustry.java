package services.db.entity;

public class DetailedBusinessIndustry {

    private String industry;
    private int sic;

    public DetailedBusinessIndustry() {}

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getSic() {
        return sic;
    }

    public void setSic(int sic) {
        this.sic = sic;
    }
    
}
