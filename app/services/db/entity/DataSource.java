package services.db.entity;

public class DataSource {

    private int id;
    private String name;
    private String title;
    private boolean visible;

    public DataSource() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getTitle() {
        if ("consumers2".equalsIgnoreCase(name)) {
            return "consumers";
        } else if ("blacklist".equalsIgnoreCase(name)) {
            return "responder";
        } else if ("business detailed".equalsIgnoreCase(name)) {
            return "detailed business";
        } else if ("instagram2020".equals(name)) {
            return "instagram";
        } else if ("instagram".equals(name)) {
            return "instagram2019";
        } else if ("callleads".equals(name)) {
            return "call leads";
        } else if ("healthbuyers".equals(name)) {
            return "health buyers";
        } else if ("healthinsuranceleads".equals(name)) {
            return "health insurance leads";
        } else {
            return name;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
