package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityData {

    @JsonProperty( "county_name" )
    private String county;

    private String name;

    @JsonProperty( "state_abbreviation" )
    private String state;

    public CityData() {}

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
