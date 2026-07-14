package repository;

import java.util.List;

import model.Comment;

public interface CommentRepository {
    void addComment(Comment comment);

    void removeById(int id);

    List<Comment> getComments();

    Comment findById(int id);

    List<Comment> getCommentsByUser(int userId);

    void update(Comment comment);
}
