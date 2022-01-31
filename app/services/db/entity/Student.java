package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

public class Student {

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    private Integer st_code;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("ip")
    private String ip;

    private Integer zipCode;

    private Integer areaCode;

    private String fileName;

    private Boolean dnc;

    private Integer phoneType;

    private String county;

    private String data;

    private long date;

    private String source;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state != null) {
            this.state = state.toUpperCase();
        }
    }

    public Integer getSt_code() {
        if (state != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++) {
                if (state.equalsIgnoreCase( Application.STATE_CODES[i])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setSt_code(int st_code) {
        this.st_code = st_code;
    }

    public Integer getZipCode() {
        if (zip != null) {
            try { return Integer.parseInt(zip.split( "-" )[ 0 ]); }
            catch ( Exception e ) { /*noop*/ }
        }

        return null;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getAreaCode() {
        if (getPhone() == null || getPhone().length() == 0) {
            return null;
        } else {
            try { return Integer.parseInt(getPhone().substring(0, 3)); }
            catch ( Exception e ) { return  null; }
        }
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSt_code(Integer st_code) {
        this.st_code = st_code;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
