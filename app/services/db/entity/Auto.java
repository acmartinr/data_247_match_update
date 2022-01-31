package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Auto {

    private static final DateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

    @JsonProperty("FIRST NAME")
    private String FIRST_NAME;

    @JsonProperty("LAST NAME")
    private String LAST_NAME;

    @JsonProperty("ADDRESS")
    private String ADDRESS;

    @JsonProperty("CITY")
    private String CITY;

    @JsonProperty("STATE")
    private String STATE;

    @JsonProperty("ZIP")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer ZIP_CODE;

    @JsonProperty("VIN")
    private String VIN;

    @JsonProperty("YEAR")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private Integer YEAR;

    @JsonProperty("MAKE")
    private String MAKE;

    @JsonProperty("MODEL")
    private String MODEL;

    @JsonProperty("PHONE1")
    private String PHONE1;

    @JsonProperty("PHONE2")
    private String PHONE2;

    @JsonProperty("PHONE")
    private String PHONE;

    private String COUNTY;

    private Long date;

    private Integer ST_CODE;

    private Integer areaCode;

    private Boolean dnc;

    private Integer phoneType;

    private String data;

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
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

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public Integer getYEAR() {
        return YEAR;
    }

    public void setYEAR(Integer YEAR) {
        this.YEAR = YEAR;
    }

    public String getMAKE() {
        return MAKE;
    }

    public void setMAKE(String MAKE) {
        this.MAKE = MAKE;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getPHONE1() {
        return PHONE1;
    }

    public void setPHONE1(String PHONE1) {
        this.PHONE1 = PHONE1;
    }

    public String getPHONE2() {
        return PHONE2;
    }

    public void setPHONE2(String PHONE2) {
        this.PHONE2 = PHONE2;
    }

    public String getPHONE() {
        if (PHONE != null) {
            return PHONE;
        } else if (PHONE1 != null) {
            return PHONE1;
        } else if (PHONE2 != null) {
            return PHONE2;
        } else {
            return null;
        }
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setStrDate(String date) {
        try {
            this.date = dateFormat.parse(date).getTime();
        } catch (Exception e) {e.printStackTrace();}
    }

    public Integer getST_CODE() {
        if (STATE != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++) {
                if (STATE.equals( Application.STATE_CODES[i])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setST_CODE(Integer ST_CODE) {
        this.ST_CODE = ST_CODE;
    }

    public Integer getAreaCode() {
        String phone = getPHONE();
        if (phone == null || phone.length() == 0) {
            return null;
        } else {
            try { return Integer.parseInt(phone.substring(0, 3)); }
            catch (Exception e) { return  null; }
        }
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAGRID16() {
        return AGRID16;
    }

    public void setAGRID16(String AGRID16) {
        this.AGRID16 = AGRID16;
    }

    public String getMIDDLE_NAME() {
        return MIDDLE_NAME;
    }

    public void setMIDDLE_NAME(String MIDDLE_NAME) {
        this.MIDDLE_NAME = MIDDLE_NAME;
    }

    public String getNAME_SUFFIX() {
        return NAME_SUFFIX;
    }

    public void setNAME_SUFFIX(String NAME_SUFFIX) {
        this.NAME_SUFFIX = NAME_SUFFIX;
    }

    public String getFULL_NAME() {
        return FULL_NAME;
    }

    public void setFULL_NAME(String FULL_NAME) {
        this.FULL_NAME = FULL_NAME;
    }

    public String getZIP4() {
        return ZIP4;
    }

    public void setZIP4(String ZIP4) {
        this.ZIP4 = ZIP4;
    }

    public String getMOCK1() {
        return MOCK1;
    }

    public void setMOCK1(String MOCK1) {
        this.MOCK1 = MOCK1;
    }

    public String getMOCK2() {
        return MOCK2;
    }

    public void setMOCK2(String MOCK2) {
        this.MOCK2 = MOCK2;
    }

    public String getMOCK3() {
        return MOCK3;
    }

    public void setMOCK3(String MOCK3) {
        this.MOCK3 = MOCK3;
    }

    // MOCK FIELDS
    @JsonProperty("ID")
    private String ID;

    @JsonProperty("AGRID16")
    private String AGRID16;

    @JsonProperty("MIDDLE NAME")
    private String MIDDLE_NAME;

    @JsonProperty("NAME SUFFIX")
    private String NAME_SUFFIX;

    @JsonProperty("FULL NAME")
    private String FULL_NAME;

    @JsonProperty("ZIP4")
    private String ZIP4;

    @JsonProperty("DATE")
    private String MOCK3;

    @JsonProperty("MOCK1")
    private String MOCK1;

    @JsonProperty("MOCK2")
    private String MOCK2;
    // END MOCK FIELDS

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public Integer getZIP_CODE() {
        return ZIP_CODE;
    }

    public void setZIP_CODE(Integer ZIP_CODE) {
        this.ZIP_CODE = ZIP_CODE;
    }

    public <T> Collection<? extends T> getAllItems() {
        List<Auto> results = new LinkedList();

        if (getPHONE1() != null && getPHONE2() != null &&
                getPHONE1().length() > 0 && getPHONE2().length() > 0 &&
                !getPHONE1().equals(getPHONE2())) {
            Auto firstItem = new Auto();
            copyDataTo(firstItem, getPHONE1());
            results.add(firstItem);

            Auto secondItem = new Auto();
            copyDataTo(secondItem, getPHONE2());
            results.add(secondItem);
        } else {
            results.add(this);
        }

        return (Collection<? extends T>)results;
    }

    private void copyDataTo(Auto item, String phone) {
        item.setFIRST_NAME(FIRST_NAME);
        item.setLAST_NAME(LAST_NAME);
        item.setADDRESS(ADDRESS);
        item.setCITY(CITY);
        item.setSTATE(STATE);
        item.setZIP_CODE(ZIP_CODE);
        item.setVIN(VIN);
        item.setYEAR(YEAR);
        item.setMAKE(MAKE);
        item.setMODEL(MODEL);
        item.setPHONE(phone);
        item.setDate(date);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
