package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CraigsList {

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private long id;

    @JsonProperty( "phone" )
    private String PHONE;

    @JsonProperty( "title" )
    private String INDUSTRY;

    @JsonProperty("url")
    private String website;

    @JsonProperty("SCRAPER_DATE")
    private String strDate;

    @JsonProperty("MOCK")
    private String mock;

    private long date;

    private String STATE;

    private Integer phoneType;

    private Integer ST_CODE;

    private Integer AREA_CODE;

    private Boolean dnc;

    private String data;

    public CraigsList() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getINDUSTRY() {
        return INDUSTRY;
    }

    public void setINDUSTRY(String INDUSTRY) {
        this.INDUSTRY = INDUSTRY;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public long getDate() {
        /*if (getStrDate() != null && getStrDate().length() > 0) {
            try { return dateFormat.parse(getStrDate()).getTime(); }
            catch (Exception e) { e.printStackTrace(); }
        }*/

        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMock() {
        return mock;
    }

    public void setMock(String mock) {
        this.mock = mock;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
