package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HealthInsuranceLead {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private long id;

    @JsonProperty( "ADDRESS" )
    private String ADDRESS;

    @JsonProperty( "CITY" )
    private String CITY;

    @JsonProperty( "FIRSTNAME" )
    private String FIRSTNAME;

    @JsonProperty( "LASTNAME" )
    private String LASTNAME;

    @JsonProperty( "COUNTY" )
    private String COUNTY;

    @JsonProperty( "EMAIL" )
    private String EMAIL;

    @JsonProperty( "GENDER" )
    private String GENDER;

    @JsonProperty( "DATE" )
    private String STR_DATE;

    @JsonProperty( "TIME" )
    private String STR_TIME;

    @JsonProperty( "IP" )
    private String IP;

    @JsonProperty( "PHONE" )
    private String PHONE;

    @JsonProperty( "AGE" )
    private String AGE;

    private Long DOB_DATE;

    private Long date;

    @JsonProperty( "STATE" )
    private String STATE;

    private Integer ST_CODE;

    private Integer ZIP_CODE;

    @JsonProperty( "ZIPCODE" )
    private String ZIPCODE;

    private Integer AREA_CODE;

    private Integer phoneType;

    private Boolean dnc;

    private String data;

    // unsed fields section
    @JsonProperty( "FullHash" )
    private String FullHash;

    @JsonProperty( "FULLHASH" )
    private String FULLHASH;
    // end unused fields section

    public HealthInsuranceLead() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public Integer getST_CODE() {
        if ( STATE != null ) {
            for (int i = 0; i < Application.STATE_CODES.length; i++ ) {
                if ( STATE.equals( Application.STATE_CODES[ i ] ) ) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setST_CODE(Integer ST_CODE) {
        this.ST_CODE = ST_CODE;
    }

    public Integer getZIP_CODE() {
        if ( ZIPCODE != null ) {
            try { return Integer.parseInt( ZIPCODE.split( "-" )[ 0 ] ); }
            catch ( Exception e ) { /*noop*/ }
        }

        return null;
    }

    public void setZIP_CODE(Integer ZIP_CODE) {
        this.ZIP_CODE = ZIP_CODE;
    }

    public Integer getAREA_CODE() {
        if ( PHONE == null || PHONE.length() == 0 ) {
            return null;
        } else {
            try { return Integer.parseInt( PHONE.substring( 0, 3 ) ); }
            catch ( Exception e ) { return  null; }
        }
    }

    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
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

    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        this.FIRSTNAME = FIRSTNAME;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public void setLASTNAME(String LASTNAME) {
        this.LASTNAME = LASTNAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getSTR_DATE() {
        return STR_DATE;
    }

    public void setSTR_DATE(String STR_DATE) {
        this.STR_DATE = STR_DATE;
    }

    public String getSTR_TIME() {
        return STR_TIME;
    }

    public void setSTR_TIME(String STR_TIME) {
        this.STR_TIME = STR_TIME;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public Long getDOB_DATE() {
        if (getAGE() != null) {
            try {
                return System.currentTimeMillis() - Long.parseLong(getAGE()) * 365 * 24 * 60 * 60 * 1000;
            } catch (Exception e) { /**/ }
        }

        return DOB_DATE;
    }

    public void setDOB_DATE(Long DOB_DATE) {
        this.DOB_DATE = DOB_DATE;
    }

    public String getZIPCODE() {
        return ZIPCODE;
    }

    public void setZIPCODE(String ZIPCODE) {
        this.ZIPCODE = ZIPCODE;
    }

    public String getFullHash() {
        return FullHash;
    }

    public void setFullHash(String fullHash) {
        FullHash = fullHash;
    }

    public String getFULLHASH() {
        return FULLHASH;
    }

    public void setFULLHASH(String FULLHASH) {
        this.FULLHASH = FULLHASH;
    }

    public Long getDate() {
        if (getSTR_DATE()!= null) {
            try { return dateFormat.parse(getSTR_DATE() + " " +  getSTR_TIME()).getTime(); }
            catch (Exception e) { /**/ }
        }

        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
