package models;

public class UploadFileRequest {

    public int listId;
    public String name;
    public int userId;
    public String filePath;
    public int column;

    public UploadFileRequest() {}

    public int getListId() { return listId; }
    public void setListId(int listId) { this.listId = listId; }

    public String getName() { return name; }
    public void setName( String name ) { this.name = name; }

    public void setUserId( int userId ) { this.userId = userId; }
    public int getUserId() { return userId; }

    public String getFilePath() { return filePath; }
    public void setFilePath( String filePath ) { this.filePath = filePath; }

    public int getColumn() { return column; }
    public void setColumn( int column ) { this.column = column; }
}
