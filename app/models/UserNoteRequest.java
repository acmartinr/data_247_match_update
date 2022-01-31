package models;

public class UserNoteRequest {

    private int userId;
    private String note;
    private int noteStatus;

    public UserNoteRequest() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(int noteStatus) {
        this.noteStatus = noteStatus;
    }
}
