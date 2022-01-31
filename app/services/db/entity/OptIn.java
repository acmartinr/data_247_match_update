package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OptIn {

    private final static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final static DateFormat dobDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    @JsonProperty("email")
    private String email;

    @JsonProperty("fname")
    private String firstName;

    @JsonProperty("lname")
    private String lastName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    private Integer st_code;

    @JsonProperty("zip")
    private String zip;
    private Integer zipCode;

    private Integer areaCode;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("joindate")
    private String strDate;

    private Long date;

    @JsonProperty("url")
    private String source;

    private String sourceCriteria;

    @JsonProperty("gender")
    private String strGender;

    private Integer genderCode;

    @JsonProperty("dob")
    private String strDob;

    private Long dobDate;

    private Boolean dnc;
    private Integer phoneType;

    private String county;

    private String data;

    // mock fields
    @JsonProperty("address2")
    private String mock1;

    @JsonProperty("mobile")
    private String mock2;

    public OptIn() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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
        return phone != null ?
                phone.replace(" ", "").replace("-", "").replace("(", "").replace(")", "") :
                null;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;

        try {
            Date date = dateFormat.parse(strDate.split(" ")[0]);
            this.date = date.getTime();
        } catch (Exception e) {/*e.printStackTrace();*/}
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceCriteria() {
        if (source != null) {
            return source.
                    replace("https://", "").
                    replace("http://", "").
                    split("/")[0];
        } else {
            return null;
        }
    }

    public void setSourceCriteria(String sourceCriteria) {
        this.sourceCriteria = sourceCriteria;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrGender(String strGender) {
        this.strGender = strGender;

        if ("MALE".equalsIgnoreCase(strGender)) {
            this.genderCode = 0;
        } else {
            this.genderCode = 1;
        }
    }

    public Integer getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(Integer genderCode) {
        this.genderCode = genderCode;
    }

    public String getStrDob() {
        return strDob;
    }

    public void setStrDob(String strDob) {
        this.strDob = strDob;

        try {
            Date date = dobDateFormat.parse(strDob);
            this.dobDate = date.getTime();
        } catch (Exception e) {/*e.printStackTrace()*/;}
    }

    public Long getDobDate() {
        return dobDate;
    }

    public void setDobDate(Long dobDate) {
        this.dobDate = dobDate;
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
}
