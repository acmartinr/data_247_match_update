package models;

public class UserListRequest {

    private String searchValue;
    private String sortValue;
    private boolean sortDesc;
    private int resellerId;

    private int limit;
    private int offset;
    private int page;

    public UserListRequest() {}

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

    public int getResellerId() { return resellerId; }
    public void setResellerId(int resellerId) { this.resellerId = resellerId; }
}
