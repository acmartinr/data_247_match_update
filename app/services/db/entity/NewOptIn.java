package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NewOptIn {
    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("address")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("st")
    private String state;
    @JsonProperty("zip5")
    private int zip5;
    @JsonProperty("zip4")
    private int zip4;
    @JsonProperty("cell")
    private String phone;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("NXX")
    private String NXX;
    @JsonProperty("CellPart")
    private String CellPart;
    @JsonProperty("Carrier")
    private String Carrier;
    @JsonProperty("datime")
    private String STR_DATE;
    @JsonProperty("source")
    private String source;
    private Integer phoneType;
    private Boolean dnc;
    private String data;
    private Integer st_code;
    private String CLEAN_SOURCE;


    private Long date;
    private boolean confirmedEmail;

    private String county;

    public String getSTR_DATE() {
        return STR_DATE;
    }
    public void setSTR_DATE(String STR_DATE) {
        this.STR_DATE = STR_DATE;
    }

    public Long getDate() {
        if (getSTR_DATE()!= null) {
            try { return dateFormat.parse(getSTR_DATE()).getTime();}
            catch (Exception e) { /**/ }
        }

        return date;
    }
    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isConfirmedEmail() {
        return confirmedEmail;
    }

    public void setConfirmedEmail(boolean confirmedEmail) {
        this.confirmedEmail = confirmedEmail;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    //private long CLEAN_CELL;
    public NewOptIn() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
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

    public void setState(String st) {
        this.state = st;
    }

    public int getZip5() {
        return zip5;
    }

    public void setZip5(int zip5) {
        this.zip5 = zip5;
    }

    public int getZip4() {
        return zip4;
    }

    public void setZip4(int zip4) {
        this.zip4 = zip4;
    }

    public String getPhone() {
        return phone;
    }
    public static boolean isNumber(String str){
        try{
            Integer.parseInt(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
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


/*
    public void setCLEAN_CELL(long clean_cell) {
        this.CLEAN_CELL = clean_cell;
    }
    public long getCLEAN_CELL(){
        String cleanPhone = "";
        if(getCell() != null && this.CLEAN_CELL == 0) {
            char charArr[] = getCell().toCharArray();
            for (int i = 0; i < charArr.length; i++) {
                if (!isNumber(String.valueOf(charArr[i]))) {

                } else if (isNumber(String.valueOf(charArr[i]))) {
                    cleanPhone = cleanPhone + charArr[i];
                }
            }
            if (!cleanPhone.isEmpty()) {
                this.CLEAN_CELL = Long.parseLong(cleanPhone);
                return Long.parseLong(cleanPhone);
            } else {
                return 0;
            }
        }
        else {
            return this.CLEAN_CELL;
        }
    }

 */


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public String getNXX() {
        return NXX;
    }

    public void setNXX(String NXX) {
        this.NXX = NXX;
    }

    public String getCellPart() {
        return CellPart;
    }

    public void setCellPart(String cellPart) {
        this.CellPart = CellPart;
    }

    public String getCarrier() {
        return Carrier;
    }

    public void setCarrier(String carrier) {
        this.Carrier = carrier;
    }

    public String getSource() {
        return source;
    }

    public void setCLEAN_SOURCE(String CLEAN_SOURCE){
        this.CLEAN_SOURCE = CLEAN_SOURCE;
    }
    public String getCLEAN_SOURCE() {
        if(getSource() != null) {
            Character v;
            String dom = "";
            int step = 0;

            for (int i = 0, l = getSource().length(); i < l; i++) {
                v = getSource().charAt(i);
                if (step == 0) {
                    //First, skip 0 to 5 characters ending in ':' (ex: 'https://')
                    if (i > 5) {
                        i = -1;
                        step = 1;
                    } else if (v == ':') {
                        i += 2;
                        step = 1;
                    }
                } else if (step == 1) {
                    //Skip 0 or 4 characters 'www.'
                    //(Note: Doesn't work with www.com, but that domain isn't claimed anyway.)
                    if (v == 'w' && getSource().charAt(i + 1) == 'w' && getSource().charAt(i + 2) == 'w' && getSource().charAt(i + 3) == '.')
                        i += 4;
                    dom += getSource().charAt(i);
                    step = 2;
                } else if (step == 2) {
                    //Stop at subpages, queries, and hashes.
                    if (v == '/' || v == '?' || v == '#') break;
                    dom += v;
                }
            }
            return dom;
        }else{
            return this.CLEAN_SOURCE;
        }
    }
    public void setSource(String source) {
        this.source = source;
    }

    public int getAREA_CODE() {
        try {
            return Integer.parseInt(String.valueOf(getPhone()).substring(0, 3));
        } catch (Exception e) {/**/}

        return 0;
    }

}
