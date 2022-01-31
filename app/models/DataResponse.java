package models;

public class DataResponse {

    private long count;
    private String textNote;

    public DataResponse() {}

    public DataResponse(long count,String textNote) {
        this.count = count;
        this.textNote = textNote;
    }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
    public String getTextNote() {
        return textNote;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }
}
