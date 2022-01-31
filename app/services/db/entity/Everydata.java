package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Everydata {
    private final static DateFormat dobDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final static DateFormat dobDateFormat2 = new SimpleDateFormat("yyyy");


    public String getStrDob() {
        return strDob;
    }

    public void setStrDob(String strDob) {
        this.strDob = strDob;
    }

    @JsonProperty("dob_date")
    private String strDob;

    private String dob;


    @JsonProperty( "phone" )
    private String PHONE;

    private Long dobDate = 0L;

    @JsonProperty( "city" )
    private String CITY;

    @JsonProperty( "state" )
    private String STATE;


    @JsonProperty("email")
    private String email;


    @JsonProperty("first_name")
    private String firstname;

    public Long getDobDate() {
        if (getStrDob()!= null) {
            try {
                if(getStrDob() != ""){
                    return dobDateFormat.parse(getStrDob()).getTime();
                }else{
                    return -9999999999L;
                }


            }
            catch (Exception e) { /**/ }
        }

        return dobDate;
    }
    public void setDobDate(Long date) {
        this.dobDate = date;
    }

    public String getFirstname() {
        return firstname.replace("'"," ");
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname.replace("'"," ");
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("last_name")
    private String lastname;

    @JsonProperty("source")
    private String source;

    private String COUNTY;

    private Integer ST_CODE;

    private Integer AREA_CODE;

    private Boolean dnc;

    private String data;

    private Integer phoneType;



    public Everydata() {}


    public String getDob()  {
        Timestamp stamp = new Timestamp(Long.parseLong(dob));
        Date date = new Date(stamp.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(dob.equals("-9999999999")){
            return String.valueOf(0);
        }else{
            return String.valueOf(calendar.get(Calendar.YEAR));
        }

    }

    public void setDob(String dob) {
        this.dob = dob;
        try {
            Date date = dobDateFormat.parse(dob);
            this.dobDate = date.getTime();
        } catch (Exception e) {/*e.printStackTrace()*/;}
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
        }else{
            return -1;
        }
        return -1;
       // return null;
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


    public String getEmail() {
        return email.replace("'"," ");
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
