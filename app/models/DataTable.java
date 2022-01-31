package models;

import java.util.Objects;

public class DataTable {

    private long id;
    private String name;
    private int type;
    private boolean visible;
    private int phoneType;
    private int innerType;

    public static final int ALL = -1;
    public static final int CONSUMERS = 0;
    public static final int BUSINESS = 1;
    public static final int DIRECTORY = 2;
    public static final int CRAIGSLIST = 3;
    public static final int WHOIS = 4;
    public static final int SEARCH_ENGINE = 5;
    public static final int CONSUMERS2 = 6;
    public static final int INSTAGRAM = 7;
    public static final int OPTIN = 8;
    public static final int NEWOPTIN = 20;
    public static final int CONSUMERS3 = 9;
    public static final int AUTO = 10;
    public static final int BLACKLIST = 11;
    public static final int LINKEDIN = 12;
    public static final int BUSINESS_DETAILED = 13;
    public static final int INSTAGRAM2020 = 14;
    public static final int STUDENT = 15;
    public static final int CALLLEADS = 16;
    public static final int HEALTH_BUYER = 17;
    public static final int BUSINESS2 = 18;
    public static final int HEALTH_INSURANCE_LEAD = 19;
    public static final int FACEBOOK = 21;
    public static final int PHILDIRECTORY = 22;
    public static final int EVERYDATA = 23;

    public static final int COMMON = 0;
    public static final int MOBILE = 1;
    public static final int LANDLINE = 2;

    public DataTable() {}

    public DataTable(String name, int type, boolean visible, int phoneType) {
        this.name = name;
        this.type = type;
        this.visible = visible;
        this.phoneType = phoneType;
    }

    public long getId() { return id; }
    public void setId( long id ) { this.id = id; }

    public String getName() { return name; }
    public void setName( String name ) { this.name = name; }

    public int getType() { return type; }
    public void setType( int type ) { this.type = type; }

    public boolean isVisible() { return visible; }
    public void setVisible( boolean visible ) { this.visible = visible; }

    public int getPhoneType() { return phoneType; }
    public void setPhoneType(int phoneType) { this.phoneType = phoneType; }

    public int getInnerType() {
        return innerType;
    }

    public void setInnerType(int innerType) {
        this.innerType = innerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataTable dataTable = (DataTable) o;
        return id == dataTable.id &&
                type == dataTable.type &&
                visible == dataTable.visible &&
                phoneType == dataTable.phoneType &&
                innerType == dataTable.innerType &&
                Objects.equals(name, dataTable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, visible, phoneType, innerType);
    }
}
