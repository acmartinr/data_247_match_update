package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

public class SearchEngine {

    private int id;

    @JsonProperty("Link")
    private String website;

    @JsonProperty("Phone")
    private String phone;

    private String STATE;

    private Integer ST_CODE;

    private Integer AREA_CODE;

    private int phoneType;

    private boolean dnc;

    private String data;

    public SearchEngine() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getWebsite() {
        if (website != null && website.length() > 1000) {
            return website.substring(0, 1000);
        } else {
            return website;
        }
    }
    public void setWebsite(String website) { this.website = website; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getPhoneType() { return phoneType; }
    public void setPhoneType(int phoneType) { this.phoneType = phoneType; }

    public boolean isDnc() { return dnc; }
    public void setDnc(boolean dnc) { this.dnc = dnc; }

    public String getSTATE() { return STATE; }
    public void setSTATE(String STATE) { this.STATE = STATE; }

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
        if ( phone == null || phone.length() == 0 ) {
            return null;
        } else {
            try { return Integer.parseInt( phone.substring( 0, 3 ) ); }
            catch ( Exception e ) { return  null; }
        }
    }
    public void setAREA_CODE(Integer AREA_CODE) {
        this.AREA_CODE = AREA_CODE;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
