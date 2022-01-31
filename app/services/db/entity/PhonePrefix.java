package services.db.entity;

public class PhonePrefix {

    private long id;
    private String prefix;
    private int type;

    public PhonePrefix() {}

    public PhonePrefix( String prefix, int type ) {
        this.prefix = prefix + "%";
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
