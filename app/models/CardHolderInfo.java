package models;

public class CardHolderInfo {

    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;

    public CardHolderInfo() {}

    public String getName() { return name; }
    public void setName( String name ) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity( String city ) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry( String country ) { this.country = country; }

    public String getState() { return state; }
    public void setState( String state ) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip( String zip ) { this.zip = zip; }
}
