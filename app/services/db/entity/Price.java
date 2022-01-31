package services.db.entity;

public class Price {

    private int id;
    private int type;
    private int dataSource;
    private float price;
    private int resellerId;
    private int userId;

    public Price() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDataSource() {
        return dataSource;
    }

    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    public int getResellerId() {
        return resellerId;
    }

    public void setResellerId(int resellerId) {
        this.resellerId = resellerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
