package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

public class Facebook {

    private long id;

    @JsonProperty("PHONE")
    private String phone;

    @JsonProperty( "first_name" )
    private String first_name;

    @JsonProperty( "last_name" )
    private String last_name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("CITY")
    private String city;

    @JsonProperty("ST")
    private String ST;

    @JsonProperty("status")
    private String status;

    @JsonProperty("job")
    private String job;



    private Integer phoneType;

    private Integer ST_CODE;

    private Integer AREA_CODE;

    private Boolean dnc;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String lastName) {
        this.last_name = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    private String data;

    private String county;

    public Facebook() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getAREA_CODE() {
        if ( phone == null || phone.length() == 0 ) {
            return null;
        } else {
            try { return Integer.parseInt( phone.substring( 0, 3 ) ); }
            catch ( Exception e ) { return  null; }
        }
    }
    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public Integer getST_CODE() {
        if ( ST != null ) {
            for (int i = 0; i < Application.STATE_CODES.length; i++ ) {
                if ( ST.equals( Application.STATE_CODES[ i ] ) ) {
                    return i;
                }
            }
        }

        return null;
    }
    public void setST_CODE(Integer ST_CODE) { this.ST_CODE = ST_CODE; }


}
