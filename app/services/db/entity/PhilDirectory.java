package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PhilDirectory {

    private final static DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
    private final static DateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long startOfYearTimestamp = 0l;

    {{
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        startOfYearTimestamp = calendar.getTimeInMillis();
    }}

    @JsonProperty( "phone" )
    private String PHONE;

    @JsonProperty( "company" )
    private String COMPANY_NAME;

    @JsonProperty( "address" )
    private String ADDRESS;

    @JsonProperty( "street" )
    private String STREET;

    @JsonProperty( "city" )
    private String CITY;

    @JsonProperty( "state" )
    private String STATE;

    @JsonProperty( "zip" )
    private String ZIP;

    @JsonProperty( "category" )
    private String INDUSTRY;

    @JsonProperty( "site" )
    private String WWW;

    @JsonProperty("email")
    private String email;

    private String STR_DATE;

    private Long TIMESTAMP;

    private String COUNTY;

    private Integer ST_CODE;

    private Integer ZIP_CODE;

    private Integer AREA_CODE;

    private Boolean dnc;

    private String data;

    private Integer phoneType;

    @JsonProperty("name")
    private String contact_name;

    @JsonProperty("website")
    private String websites;

    // mock fields

    @JsonProperty("place_id")
    private String mock1;

    @JsonProperty("street1")
    private String mock2;

    @JsonProperty("clear_phone")
    private String mock3;

    @JsonProperty("international_phone")
    private String mock4;

    @JsonProperty("updated_at")
    private String mock5;

    @JsonProperty("source_url")
    private String mock6;

    @JsonProperty("lat")
    private String mock7;

    @JsonProperty("lng")
    private String mock8;

    @JsonProperty("rating")
    private String mock9;

    @JsonProperty("reviews")
    private String mock10;

    @JsonProperty("created_at")
    private String mock11;

    public String getMock1() {
        return mock1;
    }

    public void setMock1(String mock1) {
        this.mock1 = mock1;
    }

    public String getMock2() {
        return mock2;
    }

    public void setMock2(String mock2) {
        this.mock2 = mock2;
    }

    public String getMock3() {
        return mock3;
    }

    public void setMock3(String mock3) {
        this.mock3 = mock3;
    }

    public String getMock4() {
        return mock4;
    }

    public void setMock4(String mock4) {
        this.mock4 = mock4;
    }

    public String getMock5() {
        return mock5;
    }

    public void setMock5(String mock5) {
        this.mock5 = mock5;
    }

    public String getMock6() {
        return mock6;
    }

    public void setMock6(String mock6) {
        this.mock6 = mock6;
    }

    public String getMock7() {
        return mock7;
    }

    public void setMock7(String mock7) {
        this.mock7 = mock7;
    }

    public String getMock8() {
        return mock8;
    }

    public void setMock8(String mock8) {
        this.mock8 = mock8;
    }

    public String getMock9() {
        return mock9;
    }

    public void setMock9(String mock9) {
        this.mock9 = mock9;
    }

    public String getMock10() {
        return mock10;
    }

    public void setMock10(String mock10) {
        this.mock10 = mock10;
    }

    public String getMock11() {
        return mock11;
    }

    public void setMock11(String mock11) {
        this.mock11 = mock11;
    }

    public PhilDirectory() {}

    public String getADDRESS() {
        if (STREET != null) {
            ADDRESS = STREET;
        }

        if (ADDRESS != null && ADDRESS.length() > 200) {
            return ADDRESS.substring(0, 200);
        } else {
            return ADDRESS;
        }
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

    public String getCOMPANY_NAME() {
        if (COMPANY_NAME != null && COMPANY_NAME.length() > 200) {
            return COMPANY_NAME.substring(0, 200);
        } else {
            return COMPANY_NAME;
        }
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public String getINDUSTRY() {
        if (INDUSTRY != null && INDUSTRY.length() > 400) {
            return INDUSTRY.substring(0, 400);
        } else {
            return INDUSTRY;
        }
    }

    public void setINDUSTRY(String INDUSTRY) {
        if (INDUSTRY != null && !INDUSTRY.equalsIgnoreCase("\\N")) {
            this.INDUSTRY = INDUSTRY;
        }
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

    public String getWWW() {
        return WWW;
    }

    public void setWWW(String WWW) {
        this.WWW = WWW;
    }

    public Integer getZIP_CODE() {
        if ( ZIP != null ) {
            try { return Integer.parseInt( ZIP.split( "-" )[ 0 ] ); }
            catch ( Exception e ) { /*noop*/ }
        }

        return null;
    }

    public void setZIP_CODE(Integer ZIP_CODE) {
        this.ZIP_CODE = ZIP_CODE;
    }

    public String getZIP() {
        return ZIP;
    }

    public void setZIP(String ZIP) {
        this.ZIP = ZIP;
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

    public String getSTR_DATE() {
        return STR_DATE;
    }

    public void setSTR_DATE(String STR_DATE) {
        this.STR_DATE = STR_DATE;

        /*try {
            Date date = dateFormat.parse(STR_DATE);
            this.TIMESTAMP = startOfYearTimestamp + date.getTime();
        } catch (Exception e) {e.printStackTrace();}*/

        try {
            Date date = fullDateFormat.parse(STR_DATE);
            this.TIMESTAMP = date.getTime();
        } catch (Exception e) {e.printStackTrace();}
    }

    public Long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(Long TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public void fixPhone() {
        if (getPHONE() != null) {
            setPHONE(getPHONE().
                    replaceAll(" ", "").
                    replaceAll("\\(", "").
                    replaceAll("\\)", "").
                    replaceAll("\\+", "").
                    replaceAll("-", "")
            );
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getWebsites() {
        if (websites != null && websites.length() > 400) {
            return websites.substring(0, 400);
        } else {
            return websites;
        }
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

    public void setSTREET(String STREET) {
        this.STREET = STREET;
    }

    public String getSTREET() {
        return STREET;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
