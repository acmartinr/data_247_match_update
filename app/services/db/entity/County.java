package services.db.entity;

public class County {

    private long id;
    private String state;
    private String county;

    public County() {}

    public County(String state, String county) {
        this.state = state;
        this.county = county;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }
}
