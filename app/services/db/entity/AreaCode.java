package services.db.entity;

public class AreaCode {

    private long id;
    private String state;
    private Integer code;

    public AreaCode() {}

    public AreaCode( String state, Integer code ) {
        this.state = state;
        this.code = code;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
}
