package models;

import services.db.entity.Comment;
import services.db.entity.User;

import java.util.List;

public class CommentListResponse {

    private int total;
    private List<Comment> comments;

    public CommentListResponse() {}

    public CommentListResponse(int total, List< Comment > comments ) {
        this.total = total;
        this.comments = comments;
    }

    public int getTotal() { return total; }
    public void setTotal( int total ) { this.total = total; }

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) { this.comments = comments; }
}
