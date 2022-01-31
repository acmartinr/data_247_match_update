package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import controllers.Application;

public class BlackList {

    @JsonProperty("phone")
    private String phone;

    private Integer areaCode;

    private Integer ST_CODE;

    private String state;

    private Integer phoneType;

    private Boolean dnc;

    public BlackList() {}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAreaCode() {
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

    public Integer getST_CODE() {
        if (state != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++) {
                if (state.equals(Application.STATE_CODES[i])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setST_CODE(Integer ST_CODE) {
        this.ST_CODE = ST_CODE;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
}
