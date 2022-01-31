package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PaymentRequest {

    private int userId;
    private String token;
    private float amount;
    private String type = "stripe";
    private boolean mobile;

    public PaymentRequest() {}

    public int getUserId() { return userId; }
    public void setUserId( int userId ) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken( String token ) { this.token = token; }

    public float getAmount() { return amount; }
    public void setAmount( float amount ) { this.amount = amount; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }
}
