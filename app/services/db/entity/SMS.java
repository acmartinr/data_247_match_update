package services.db.entity;

public class SMS {

    private int id;
    private int userId;
    private String phone;
    private String message;

    public SMS() {}

    public SMS( int userId, String phone, String message ) {
        this.userId = userId;
        this.phone = correctPhone( phone );
        this.message = message;
    }

    private String correctPhone( String phone ) {
        if ( phone.length() == 10 ) {
            return "1" + phone;
        } else {
            return phone;
        }
    }

    public int getId() { return id; }
    public void setId( int id ) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId( int userId ) { this.userId = userId; }

    public String getPhone() { return phone; }
    public void setPhone( String phone ) { this.phone = phone; }

    public String getMessage() { return message; }
    public void setMessage( String message ) { this.message = message; }
}
