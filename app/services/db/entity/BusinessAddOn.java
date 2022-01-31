package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;

import java.io.IOException;

public class BusinessAddOn {

    private long id;

    @JsonProperty( "Lat" )
    @JsonDeserialize( using = Business.FloatDeserializer.class )
    private Float LATITUDE;

    @JsonProperty( "Long" )
    @JsonDeserialize( using = Business.FloatDeserializer.class )
    private Float LONGITUDE;

    @JsonProperty( "Name" )
    private String COMPANY_NAME;

    @JsonProperty( "Address" )
    private String ADDRESS;

    @JsonProperty( "City" )
    private String CITY;

    @JsonProperty( "State" )
    private String STATE;

    @JsonProperty( "Zip" )
    private String ZIP_CODE;

    @JsonProperty( "County" )
    private String COUNTY;

    @JsonProperty( "Phone" )
    private String PHONE;

    @JsonProperty("Fax")
    private String FAX;

    @JsonProperty("ContactGender")
    private String GENDER;

    @JsonProperty( "ContactName" )
    private String FULL_NAME;

    @JsonProperty( "SIC" )
    private String PRIMARY_SIC;

    @JsonProperty( "TITLE" )
    private String CONTACT_TITLE;

    @JsonProperty( "WWW" )
    private String WEBSITE;

    @JsonProperty( "INDUSTRY" )
    private String industry;

    @JsonProperty( "NumEmployees" )
    private String employees;

    @JsonProperty( "SalesVolume" )
    private String sales;

    @JsonProperty("Email")
    private String email;

    /* mock fields */

    @JsonProperty("TollFreePhone")
    private String tollFreePhone;

    @JsonProperty("MedianIncomeCensusArea")
    private String medianIncomeCensusArea;

    @JsonProperty("MeanHousingCensusArea")
    private String meanHousingCensusArea;

    @JsonProperty("RecordType")
    private String recordType;

    /* end mock fields */

    private boolean dnc;

    private int phoneType;

    public boolean isDnc() {
        return dnc;
    }

    public void setDnc(boolean dnc) {
        this.dnc = dnc;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    BusinessAddOn() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getZIP_CODE() {
        if (ZIP_CODE != null && ZIP_CODE.contains("-")) {
            return ZIP_CODE.split("-")[0];
        } else {
            return ZIP_CODE;
        }
    }

    public void setZIP_CODE(String ZIP_CODE) {
        this.ZIP_CODE = ZIP_CODE;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getFAX() {
        return FAX;
    }

    public void setFAX(String FAX) {
        this.FAX = FAX;
    }

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public String getPRIMARY_SIC() {
        return PRIMARY_SIC;
    }

    public void setPRIMARY_SIC(String PRIMARY_SIC) {
        this.PRIMARY_SIC = PRIMARY_SIC;
    }

    public String getCONTACT_TITLE() {
        return CONTACT_TITLE;
    }

    public void setCONTACT_TITLE(String CONTACT_TITLE) {
        this.CONTACT_TITLE = CONTACT_TITLE;
    }

    public String getWEBSITE() {
        return WEBSITE;
    }

    public void setWEBSITE(String WEBSITE) {
        this.WEBSITE = WEBSITE;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public void setFULL_NAME(String FULL_NAME) {
        this.FULL_NAME = FULL_NAME;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public Float getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(Float LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public Float getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(Float LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

}
