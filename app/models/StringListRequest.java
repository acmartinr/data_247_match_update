package models;

import java.util.List;

public class StringListRequest {

    private List<Integer> values;
    private String searchValue;
    private int limit;
    private int page;
    private int offset;

    public StringListRequest() {}

    public String getSearchValue() { return searchValue; }
    public void setSearchValue( String searchValue ) {
        if ( searchValue == null || searchValue.trim().length() == 0 ) {
            searchValue = "%%";
        } else {
            searchValue = "%" + searchValue + "%";
        }

        this.searchValue = searchValue;
    }

    public int getLimit() { return limit; }
    public void setLimit( int limit ) { this.limit = limit; }

    public int getPage() { return page; }
    public void setPage( int page ) { this.page = page; }

    public int getOffset() { return ( page - 1 ) * limit; }

    public List<Integer> getValues() { return values; }
    public void setValues(List<Integer> values) { this.values = values; }
}
