package models;

import services.db.entity.PhonePrefix;

import java.util.List;

public class PrefixListResponse {

    private int total;
    private List<PhonePrefix> prefixes;

    public PrefixListResponse() {}

    public PrefixListResponse(int total, List< PhonePrefix > prefixes ) {
        this.total = total;
        this.prefixes = prefixes;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List< PhonePrefix > getPrefixes() { return prefixes; }
    public void setPrefixes( List< PhonePrefix > prefixes ) { this.prefixes = prefixes; }
}
