package services.db.entity;

public class ZipCode {

    private long id;
    private String state;
    private Integer code;

    public ZipCode() {}

    public ZipCode( String state, Integer code ) {
        this.state = state;
        this.code = code;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public static class ZipCodeData {

        private String zipCode;
        private float latitude;
        private float longitude;

        public ZipCodeData() {}

        public ZipCodeData( String zipCode, float latitude, float longitude ) {
            this.zipCode = zipCode;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }

        public float getLatitude() { return latitude; }
        public void setLatitude(float latitude) { this.latitude = latitude; }

        public float getLongitude() { return longitude; }
        public void setLongitude(float longitude) { this.longitude = longitude; }
    }
}
