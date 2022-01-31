package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;
import models.Biz;

import java.io.IOException;

public class Business {

    private long id;

    @JsonProperty( "ADDRESS" )
    private String ADDRESS;

    @JsonProperty( "ANNUAL SALES" )
    private String ANNUAL_SALES;

    @JsonProperty( "CITY" )
    private String CITY;

    @JsonProperty( "COMPANY NAME" )
    private String COMPANY_NAME;

    @JsonProperty( "CONTACT NAME" )
    private String CONTACT_NAME;

    @JsonProperty( "COUNTY" )
    private String COUNTY;

    @JsonProperty( "EMPLOYEE COUNT" )
    private String EMPLOYEE;

    @JsonProperty( "Fax" )
    private String FAX;

    @JsonProperty( "GENDER" )
    private String GENDER;

    @JsonProperty( "INDUSTRY" )
    private String INDUSTRY;

    @JsonProperty( "LATITUDE" )
    @JsonDeserialize( using = FloatDeserializer.class )
    private Float LATITUDE;

    @JsonProperty( "LONGITUDE" )
    @JsonDeserialize( using = FloatDeserializer.class )
    private Float LONGITUDE;

    @JsonProperty( "PHONE" )
    private String PHONE;

    @JsonProperty( "SIC CODE" )
    private String SIC_CODE;

    @JsonProperty( "STATE" )
    private String STATE;

    private Integer ST_CODE;

    @JsonProperty( "TITLE" )
    private String TITLE;

    @JsonProperty( "WWW" )
    private String WWW;

    private Integer ZIP_CODE;

    @JsonProperty( "ZIP" )
    private String ZIP;

    private Integer AREA_CODE;

    private Integer phoneType;

    private Boolean dnc;

    private Integer SIC;

    private String data;

    // unsed fields section
    @JsonProperty("EMPLOYEE CODE")
    private String employeeCode;

    @JsonProperty("SALES CODE")
    private String salesCode;

    @JsonProperty("MeanHousingCensusArea")
    private String meanHousingCensusArea;

    @JsonProperty("RecordType")
    private String recordType;

    private String email1;

    private String email2;

    private String email3;

    private String email4;

    private String email5;

    private String email6;

    private String email7;

    private String email8;

    private String email9;

    private String email10;

    // end unused fields section

    public Business() {}

    public static String updateSales(String sales) {
        if ("$100 mil to less than $25".equalsIgnoreCase(sales)) {
            return "$100 to 500 million";
        } else if ("100 to 500 million".equalsIgnoreCase(sales)) {
            return "$100 to 500 million";
        } else if ("$100 TO 500 MILLION".equalsIgnoreCase(sales)) {
            return "$100 to 500 million";
        } else if ("$10 mil to less than $25".equalsIgnoreCase(sales)) {
            return "$10 to 20 million";
        } else if ("$10 to 20 million".equalsIgnoreCase(sales)) {
            return "$10 to 20 million";
        } else if ("10 to 20 million".equalsIgnoreCase(sales)) {
            return "$10 to 20 million";
        } else if ("$10 TO 20 MILLION".equalsIgnoreCase(sales)) {
            return "$10 to 20 million";
        } else if ("$1 bil and above".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("$1 mil to less than $5 mi".equalsIgnoreCase(sales)) {
            return "$2.5 to 5 million";
        } else if ("1 to 2.5 million".equalsIgnoreCase(sales)) {
            return "$1 to 2.5 million";
        } else if ("$1 TO 2.5 MILLION".equalsIgnoreCase(sales)) {
            return "$1 to 2.5 million";
        } else if ("1 to 49".equalsIgnoreCase(sales)) {
            return "less than $500,000";
        } else if ("$20 to 50 million".equalsIgnoreCase(sales)) {
            return "$20 to 50 million";
        } else if ("20 to 50 million".equalsIgnoreCase(sales)) {
            return "$20 to 50 million";
        } else if ("$20 TO 50 MILLION".equalsIgnoreCase(sales)) {
            return "$20 to 50 million";
        } else if ("$250 mil to less than $50".equalsIgnoreCase(sales)) {
            return "$100 to 500 million";
        } else if ("$25 mil to less than $50".equalsIgnoreCase(sales)) {
            return "$20 to 50 million";
        } else if ("2.5 to 5 million".equalsIgnoreCase(sales)) {
            return "$2.5 to 5 million";
        } else if ("$2.5 TO 5 MILLION".equalsIgnoreCase(sales)) {
            return "$2.5 to 5 million";
        } else if ("$500,000 TO $1 MILLION".equalsIgnoreCase(sales)) {
            return "$500,000 to $1 million";
        } else if ("500k to 1 million".equalsIgnoreCase(sales)) {
            return "$500,000 to $1 million";
        } else if ("500 million to 1 billion".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("$500 MILLION TO $1 BILLION".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("$500 mil to less than $1".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("$50 mil to less than $100".equalsIgnoreCase(sales)) {
            return "$50 to 100 million";
        } else if ("$50 to 100 million".equalsIgnoreCase(sales)) {
            return "$50 to 100 million";
        } else if ("50 to 100 million".equalsIgnoreCase(sales)) {
            return "$50 to 100 million";
        } else if ("$50 TO 100 MILLION".equalsIgnoreCase(sales)) {
            return "$50 to 100 million";
        } else if ("50 to 99".equalsIgnoreCase(sales)) {
            return "less than $500,000";
        } else if ("$5 mil to less than $10 m".equalsIgnoreCase(sales)) {
            return "$5 to 10 million";
        } else if ("$5 to 10 million".equalsIgnoreCase(sales)) {
            return "$5 to 10 million";
        } else if ("5 to 10 million".equalsIgnoreCase(sales)) {
            return "$5 to 10 million";
        } else if ("$5 TO 10 MILLION".equalsIgnoreCase(sales)) {
            return "$5 to 10 million";
        } else if ("Less than $1 mil".equalsIgnoreCase(sales)) {
            return "$500,000 to $1 million";
        } else if ("LESS THAN $500,000".equalsIgnoreCase(sales)) {
            return "less than $500,000";
        } else if ("Less than $5 million".equalsIgnoreCase(sales)) {
            return "$2.5 to 5 million";
        } else if ("Over $100 million".equalsIgnoreCase(sales)) {
            return "$100 to 500 million";
        } else if ("Over 1 billion".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("Over 1 Billion".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("OVER $1 BILLION".equalsIgnoreCase(sales)) {
            return "$500 million to $1 billion";
        } else if ("Under 500k".equalsIgnoreCase(sales)) {
            return "less than $500,000";
        }


        return sales.toLowerCase();
    }

    public static String updateEmployees(String employees) {
        if ("10000 +".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("100,000 and above".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("10000 and Over".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("1000 to 4999".equalsIgnoreCase(employees)) {
            return "1,000 to 4,999";
        } else if ("1,000 TO 4,999".equalsIgnoreCase(employees)) {
            return "1,000 to 4,999";
        } else if ("1,000 to less than 5,000".equalsIgnoreCase(employees)) {
            return "1,000 to 4,999";
        } else if ("100 to 249".equalsIgnoreCase(employees)) {
            return "100 to 249";
        } else if ("100 to 499".equalsIgnoreCase(employees)) {
            return "250 to 499";
        } else if ("100 to less than 500".equalsIgnoreCase(employees)) {
            return "250 to 499";
        } else if ("10 to 19".equalsIgnoreCase(employees)) {
            return "10 to 19";
        } else if ("1 to 10".equalsIgnoreCase(employees)) {
            return "5 to 9";
        } else if ("1 to 4".equalsIgnoreCase(employees)) {
            return "1 to 4";
        } else if ("1 to 49".equalsIgnoreCase(employees)) {
            return "20 to 49";
        } else if ("1 to 9".equalsIgnoreCase(employees)) {
            return "5 to 9";
        } else if ("20 to 49".equalsIgnoreCase(employees)) {
            return "20 to 49";
        } else if ("25,000 to less than 100,0".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("250 to 499".equalsIgnoreCase(employees)) {
            return "250 to 499";
        } else if ("25 to less than 100".equalsIgnoreCase(employees)) {
            return "50 to 99";
        } else if ("5000 to 9999".equalsIgnoreCase(employees)) {
            return "5,000 to 9,999";
        } else if ("5,000 TO 9,999".equalsIgnoreCase(employees)) {
            return "5,000 to 9,999";
        } else if ("5,000 to less than 25,000".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("500 to 999".equalsIgnoreCase(employees)) {
            return "500 to 999";
        } else if ("500 to less than 1,000".equalsIgnoreCase(employees)) {
            return "500 to 999";
        } else if ("50 to 99".equalsIgnoreCase(employees)) {
            return "50 to 99";
        } else if ("5 to 9".equalsIgnoreCase(employees)) {
            return "5 to 9";
        } else if ("Less than 25".equalsIgnoreCase(employees)) {
            return "10 to 19";
        } else if ("Over 10000".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("OVER 10,000".equalsIgnoreCase(employees)) {
            return "over 10,000";
        } else if ("Over 500".equalsIgnoreCase(employees)) {
            return "500 to 999";
        }

        return employees.toLowerCase();
    }

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

    public String getANNUAL_SALES() {
        return ANNUAL_SALES != null ? ANNUAL_SALES.toLowerCase() : null;
    }

    public void setANNUAL_SALES(String ANNUAL_SALES) {
        this.ANNUAL_SALES = ANNUAL_SALES.toLowerCase();
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getCONTACT_NAME() {
        return CONTACT_NAME;
    }

    public void setCONTACT_NAME(String CONTACT_NAME) {
        this.CONTACT_NAME = CONTACT_NAME;
    }

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public String getEMPLOYEE() {
        return EMPLOYEE != null ? EMPLOYEE.toLowerCase() : null;
    }

    public void setEMPLOYEE(String EMPLOYEE) {
        this.EMPLOYEE = EMPLOYEE.toLowerCase();
    }

    public String getFAX() {
        return FAX;
    }

    public void setFAX(String FAX) {
        this.FAX = FAX;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getINDUSTRY() {
        return INDUSTRY;
    }

    public void setINDUSTRY(String INDUSTRY) {
        this.INDUSTRY = INDUSTRY;
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

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getSIC_CODE() {
        return SIC_CODE;
    }

    public void setSIC_CODE(String SIC_CODE) {
        this.SIC_CODE = SIC_CODE;
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

    public String getTITLE() {
        return TITLE != null ? TITLE.toLowerCase() : null;
    }

    public void setTITLE(String TITLE) {
        if (TITLE != null) {
            this.TITLE = TITLE.toLowerCase();
        }
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

    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }

    public Integer getSIC() {
        if (SIC == null) {
            try { SIC = Integer.parseInt(getSIC_CODE()); }
            catch (Exception e) {/*e.printStackTrace();*/}
        }

        return SIC;
    }

    public void setSIC(Integer SIC) {
        this.SIC = SIC;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getEmail4() {
        return email4;
    }

    public void setEmail4(String email4) {
        this.email4 = email4;
    }

    public String getEmail5() {
        return email5;
    }

    public void setEmail5(String email5) {
        this.email5 = email5;
    }

    public String getEmail6() {
        return email6;
    }

    public void setEmail6(String email6) {
        this.email6 = email6;
    }

    public String getEmail7() {
        return email7;
    }

    public void setEmail7(String email7) {
        this.email7 = email7;
    }

    public String getEmail8() {
        return email8;
    }

    public void setEmail8(String email8) {
        this.email8 = email8;
    }

    public String getEmail9() {
        return email9;
    }

    public void setEmail9(String email9) {
        this.email9 = email9;
    }

    public String getEmail10() {
        return email10;
    }

    public void setEmail10(String email10) {
        this.email10 = email10;
    }

    public static class FloatDeserializer extends JsonDeserializer {

        public FloatDeserializer() {}

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString( "" );
            if ( value.length() == 0 ) {
                return -1.0f;
            } else {
                try { return Float.parseFloat( value ); }
                catch( Exception e ) { /*e.printStackTrace();*/ }

                return -1.0f;
            }
        }
    }
}
