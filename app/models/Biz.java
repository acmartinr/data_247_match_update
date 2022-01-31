package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Biz {

    @JsonProperty("Company Name")
    private String companyName;

    @JsonProperty("Street")
    private String street;

    @JsonProperty("City")
    private String city;

    @JsonProperty("State")
    private String state;

    @JsonProperty("Postal")
    private String postal;

    @JsonProperty("County")
    private String county;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Website")
    private String website;

    @JsonProperty("Contact")
    private String contact;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Direct Phone")
    private String directPhone;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Sales")
    private String sales;

    @JsonProperty("Employees")
    private String employees;

    @JsonProperty("SIC Code")
    private String sicCode;

    @JsonProperty("SIC Description")
    private String sicDescription;

    @JsonProperty("Job Function")
    private String jobFunction;

    @JsonProperty("Job Level")
    private String jobLevel;

    @JsonProperty("Twitter Profile")
    private String twitterProfile;

    @JsonProperty("LinkedIn Profile")
    private String linkedInProfile;

    @JsonProperty("Facebook Profile")
    private String facebookProfile;

    private boolean dnc;

    private int phoneType;

    public Biz() {}

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectPhone() {
        return directPhone;
    }

    public void setDirectPhone(String directPhone) {
        this.directPhone = directPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }

    public String getSicCode() {
        return sicCode;
    }

    public void setSicCode(String sicCode) {
        this.sicCode = sicCode;
    }

    public String getSicDescription() {
        return sicDescription;
    }

    public void setSicDescription(String sicDescription) {
        this.sicDescription = sicDescription;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getTwitterProfile() {
        return twitterProfile;
    }

    public void setTwitterProfile(String twitterProfile) {
        this.twitterProfile = twitterProfile;
    }

    public String getLinkedInProfile() {
        return linkedInProfile;
    }

    public void setLinkedInProfile(String linkedInProfile) {
        this.linkedInProfile = linkedInProfile;
    }

    public String getFacebookProfile() {
        return facebookProfile;
    }

    public void setFacebookProfile(String facebookProfile) {
        this.facebookProfile = facebookProfile;
    }


    public boolean isDnc() {
        return dnc;
    }

    public void setDnc(boolean dnc) {
        this.dnc = dnc;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public void fixPhones() {
        if (getPhone() != null) {
            setPhone(getPhone().
                    replaceAll(" ", "").
                    replaceAll("\\(", "").
                    replaceAll("\\)", "").
                    replaceAll("\\+", "").
                    replaceAll("\\.", "").
                    replaceAll("-", "")
            );
        }

        if (getDirectPhone() != null) {
            setDirectPhone(getDirectPhone().
                    replaceAll(" ", "").
                    replaceAll("\\(", "").
                    replaceAll("\\)", "").
                    replaceAll("\\+", "").
                    replaceAll("\\.", "").
                    replaceAll("-", "")
            );
        }
    }
}
