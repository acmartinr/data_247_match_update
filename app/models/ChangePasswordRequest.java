package models;

public class ChangePasswordRequest {

    private String password;
    private String token;

    public ChangePasswordRequest() {}

    public String getPassword() { return password; }
    public void setPassword( String password ) { this.password = password; }

    public String getToken() { return token; }
    public void setToken( String token ) { this.token = token; }

}
