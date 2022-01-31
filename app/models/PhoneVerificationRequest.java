package models;

public class PhoneVerificationRequest {

    private int id;
    private String code;

    public PhoneVerificationRequest() {}

    public int getId() { return id; }
    public void setId( int id ) { this.id = id; }

    public String getCode() { return code; }
    public void setCode( String code ) { this.code = code; }
}
