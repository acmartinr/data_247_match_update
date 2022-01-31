package services.db.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment {

    private int id;
    private long date;
    private String strDate;
    private int userId;
    private String username;
    private String email;
    private Type type;
    private float amount;
    private boolean allUsers;
    private Boolean manual;
    private String reseller;

    private static final DateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss" );

    public Payment() {}

    public Payment(int userId, Type type, float amount, boolean manual) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.manual = manual;

        setDate(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public void setId( int id ) { this.id = id; }

    public long getDate() { return date; }
    public void setDate( long date ) {
        this.date = date;
        this.strDate = dateFormat.format( new Date( date ) );
    }

    public String getStrDate() { return strDate; }
    public void setStrDate( String strDate ) { this.strDate = strDate; }

    public int getUserId() { return userId; }
    public void setUserId( int userId ) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername( String username ) { this.username = username; }

    public Type getType() { return type; }
    public void setType( Type type ) { this.type = type; }

    public float getAmount() { return amount; }
    public void setAmount( float amount ) { this.amount = amount; }

    public boolean isAllUsers() { return allUsers; }
    public void setAllUsers(boolean allUsers) { this.allUsers = allUsers; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getManual() { return manual; }
    public void setManual(Boolean manual) { this.manual = manual; }

    public String getReseller() {
        return reseller;
    }

    public void setReseller(String reseller) {
        this.reseller = reseller;
    }

    public enum Type {
        ADD_FUNDS, SPEND
    }
}
