package repository;

import model.Comment;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCommentRepository implements CommentRepository {
    private static InMemoryCommentRepository instance;
    private final List<Comment> comments;

    private InMemoryCommentRepository() {
        comments = new ArrayList<Comment>();
    }

    public static InMemoryCommentRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryCommentRepository();
        }
        return instance;
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public void removeById(int id) {
        comments.removeIf(comment -> comment.getID() == id);
    }

    @Override
    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public Comment findById(int id) {
        for (Comment comment : comments) {
            if (comment.getID() == id){
                return comment;
            }
        }

        return null;
    }

    @Override
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
