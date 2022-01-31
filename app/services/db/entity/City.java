package services.db.entity;

import models.CityData;

public class City {

    private long id;
    private String state;
    private String county;
    private String city;

    public City() {}

    public City( String state, String city, String county ) {
        this.state = state;
        this.city = city;
        this.county = county;
    }

    public static City fromCityData( CityData cityData ) {
        City result = new City();
        result.setCity( cityData.getName() );
        result.setCounty( cityData.getCounty() );
        result.setState( cityData.getState() );

        return result;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
