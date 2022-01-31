package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Instagram {

    private final static DateFormat fullDateFormat = new SimpleDateFormat("MM.dd.yyyy");

    private long id;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Full Name")
    private String fullname;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Website")
    private String website;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Category")
    private String category;

    @JsonProperty("State")
    private String state;

    @JsonProperty("Postal Code")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private int zipCode;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Street")
    private String street;

    private String county;

    private Integer st_code;

    private Integer phoneType;

    private Boolean dnc;

    private Integer areaCode;

    private long date;

    private String str_date;

    private String data;

    public Instagram() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getSt_code() {
        if (state != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++ ) {
                if (state.equals( Application.STATE_CODES[ i ])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setSt_code(Integer st_code) {
        this.st_code = st_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getAreaCode() {
        if (phone != null && phone.length() >= 3) {
            try {
                return Integer.parseInt(phone.substring(0, 3));
            } catch (Exception e) {e.printStackTrace();}
        }

        return null;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        try {
            Date date = fullDateFormat.parse(str_date);
            this.date = date.getTime();
        } catch (Exception e) {e.printStackTrace();}
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
