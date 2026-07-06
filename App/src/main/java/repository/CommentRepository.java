package repository;

import java.util.ArrayList;
import java.util.List;

import model.Comment;
import model.Post;

public class CommentRepository {
    private static CommentRepository instance;
    private List<Comment> comments;

    private CommentRepository() {
        comments = new ArrayList<Comment>();
    }

    public static CommentRepository getInstance() {
        if (instance == null) {
            instance = new CommentRepository();
        }
        return instance;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeById(int id) {
        comments.removeIf(comment -> comment.getID() == id);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Comment findById(int id) {
        for (Comment comment : comments) {
            if (comment.getID() == id){
                return comment;
            }
        }

        return null;
    }

    public List<Comment> getCommentsByUser(int userId) {
        List<Comment> userComments = new ArrayList<>();

        for (Comment comment : comments) {
            if (comment.getOwnerID() == userId) {
                userComments.add(comment);
            }
        }

        return userComments;
    }
}
