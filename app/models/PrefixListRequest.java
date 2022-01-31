package models;

public class PrefixListRequest {

    private String searchValue;
    private String sortValue;
    private boolean sortDesc;

    private int limit;
    private int offset;
    private int page;

    public PrefixListRequest() {}

    public PrefixListRequest( int offset, int limit, String sortValue, int page ) {
        this.offset = offset;
        this.limit = limit;
        this.sortValue = sortValue;
        this.sortDesc = false;
        this.page = page;
    }

    public String getSearchValue() { return searchValue; }
    public void setSearchValue( String searchValue ) {
        if ( searchValue == null || searchValue.trim().length() == 0 ) {
            searchValue = null;
        } else {
            searchValue = "%" + searchValue + "%";
        }

        this.searchValue = searchValue;
    }

    public String getSortValue() { return sortValue; }
    public void setSortValue( String sortValue ) { this.sortValue = sortValue; }

    public boolean isSortDesc() { return sortDesc; }
    public void setSortDesc(boolean sortDesc) { this.sortDesc = sortDesc; }

    public int getLimit() { return limit; }
    public void setLimit( int limit ) { this.limit = limit; }

    public int getPage() { return page; }
    public void setPage( int page ) { this.page = page; }

    public int getOffset() { return ( page - 1 ) * limit; }
}
