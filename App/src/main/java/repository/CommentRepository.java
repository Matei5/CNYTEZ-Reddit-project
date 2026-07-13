package repository;

import java.util.List;

import exceptions.repository.RepositoryException;
import model.Comment;

public interface CommentRepository {
    void addComment(Comment comment) throws RepositoryException;

    void removeById(int id) throws RepositoryException;

    List<Comment> getComments() throws RepositoryException;

    Comment findById(int id) throws RepositoryException;

    List<Comment> getCommentsByUser(int userId) throws RepositoryException;
}
