package models;

public class CommentListRequest {

    private String sortValue;
    private boolean sortDesc;

    private int limit;
    private int offset;
    private int page;

    public CommentListRequest() {}

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
