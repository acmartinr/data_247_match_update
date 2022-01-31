package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

public class WhoIs {

    private long id;

    @JsonProperty( "domain_name" )
    private String website;

    @JsonProperty( "registrant_email" )
    private String email;

    @JsonProperty( "registrant_name" )
    private String name;

    @JsonProperty( "registrant_company" )
    private String business;

    @JsonProperty( "registrant_address" )
    private String address;

    @JsonProperty( "registrant_city" )
    private String city;

    @JsonProperty( "registrant_state" )
    private String state;

    @JsonProperty( "registrant_zip" )
    private String ZIP;

    @JsonProperty( "registrant_country" )
    private String country;

    @JsonProperty( "registrant_phone" )
    private String phone;

    @JsonProperty( "create_date" )
    private String createStrDate;

    @JsonProperty( "expiry_date" )
    private String expiryStrDate;

    @JsonProperty("domain_registrar_name")
    private String domainRegistrantName;

    private String COUNTY;

    private Integer ZIP_CODE;

    private Integer AREA_CODE;

    private Integer phoneType;

    private Integer ST_CODE;

    private long date;

    private String hash;

    private Boolean dnc;

    private String data;

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
    public void setST_CODE(Integer ST_CODE) { this.ST_CODE = ST_CODE; }

    public Integer getZIP_CODE() {
        if (ZIP_CODE != null && ZIP_CODE > 0) {
            return ZIP_CODE;
        } else if (ZIP != null) {
            try {
                return Integer.parseInt(ZIP.split("-")[ 0 ] );
            } catch ( Exception e ) { /*noop*/ }
        }

        return null;
    }
    public void setZIP_CODE(Integer ZIP_CODE) { this.ZIP_CODE = ZIP_CODE; }

    public Integer getAREA_CODE() {
        if (phone == null || phone.length() == 0) {
            return null;
        } else {
            try {
                return Integer.parseInt(phone.substring(0, 3));
            } catch (Exception e) { return  null; }
        }
    }

    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZIP() {
        return ZIP;
    }

    public void setZIP(String ZIP) {
        this.ZIP = ZIP;
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

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public String getCreateStrDate() {
        return createStrDate;
    }

    public void setCreateStrDate(String createStrDate) {
        this.createStrDate = createStrDate;
    }

    public String getExpiryStrDate() {
        return expiryStrDate;
    }

    public void setExpiryStrDate(String expiryStrDate) {
        this.expiryStrDate = expiryStrDate;
    }

    public String getDomainRegistrantName() {
        return domainRegistrantName;
    }

    public void setDomainRegistrantName(String domainRegistrantName) {
        this.domainRegistrantName = domainRegistrantName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
