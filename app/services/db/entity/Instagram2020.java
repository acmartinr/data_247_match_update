package services.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import controllers.Application;

public class Instagram2020 {

    private long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("name")
    private String fullname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("website")
    private String website;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("business_type")
    private String category;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zipcode")
    @JsonDeserialize(using = Consumer.IntegerDeserializer.class)
    private int zipCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("publications_count")
    private int publicationsCount;

    @JsonProperty("followers_count")
    private int followersCount;

    @JsonProperty("subscriptions_count")
    private int subscriptionsCount;

    @JsonProperty("runs_ads")
    private int runsAds;

    @JsonProperty("feed_ads_count")
    private int feedAdsCount;

    @JsonProperty("story_ads_count")
    private int storyAdsCount;

    @JsonProperty("biography")
    private String biography;

    private String county;

    private Integer st_code;

    private Integer phoneType;

    private Boolean dnc;

    private Integer areaCode;

    private long date;

    private String data;

    public Instagram2020() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getSt_code() {
        if (state != null) {
            for (int i = 0; i < Application.STATE_CODES.length; i++ ) {
                if (state.equals( Application.STATE_CODES[ i ])) {
                    return i;
                }
            }
        }

        return null;
    }

    public void setSt_code(Integer st_code) {
        this.st_code = st_code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getAreaCode() {
        if (phone != null && phone.length() >= 3) {
            try {
                return Integer.parseInt(phone.substring(0, 3));
            } catch (Exception e) {e.printStackTrace();}
        }

        return null;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPublicationsCount() {
        return publicationsCount;
    }

    public void setPublicationsCount(int publicationsCount) {
        this.publicationsCount = publicationsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getSubscriptionsCount() {
        return subscriptionsCount;
    }

    public void setSubscriptionsCount(int subscriptionsCount) {
        this.subscriptionsCount = subscriptionsCount;
    }

    public int getRunsAds() {
        return runsAds;
    }

    public void setRunsAds(int runsAds) {
        this.runsAds = runsAds;
    }

    public int getFeedAdsCount() {
        return feedAdsCount;
    }

    public void setFeedAdsCount(int feedAdsCount) {
        this.feedAdsCount = feedAdsCount;
    }

    public int getStoryAdsCount() {
        return storyAdsCount;
    }

    public void setStoryAdsCount(int storyAdsCount) {
        this.storyAdsCount = storyAdsCount;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
