package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LinkedIn {

    public static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("title")
    private String title;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("fax")
    private String fax;

    @JsonProperty("website")
    private String website;

    @JsonProperty("company_website")
    private String companyWebsite;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("linkedinID")
    private String linkedInId;

    @JsonProperty("Date and Time")
    private String strDate;

    private long date;

    private int zipCode;

    private String county;

    private Integer phoneType;

    private Integer ST_CODE;

    private Integer AREA_CODE;

    private Boolean dnc;

    public LinkedIn() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website != null && website.length() > 0 ? website : companyWebsite;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLinkedInId() {
        return linkedInId;
    }

    public void setLinkedInId(String linkedInId) {
        this.linkedInId = linkedInId;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
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

    public long getDate() {
        if (getStrDate() != null && getStrDate().length() > 0) {
            try { return dateFormat.parse(getStrDate()).getTime(); }
            catch (Exception e) { e.printStackTrace(); }
        }

        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Integer getST_CODE() {
        if (state != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++) {
                if (state.equals( Application.STATE_CODES[ i ])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setST_CODE(Integer ST_CODE) {
        this.ST_CODE = ST_CODE;
    }

    public Integer getAREA_CODE() {
        if (phone == null || phone.length() == 0) {
            return null;
        } else {
            try { return Integer.parseInt(phone.substring(0, 3)); }
            catch ( Exception e ) { return  null; }
        }
    }

    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }
}
