package repository;

import exceptions.repository.RepositoryException;
import model.Post;

import java.util.List;

public interface PostRepository {
    void addPost(Post post) throws RepositoryException;

    Post findById(int id) throws RepositoryException;

    boolean deleteById(int id) throws RepositoryException;

    List<Post> getAllPosts() throws RepositoryException;

    List<Post> getPostsByUser(int userId) throws RepositoryException;
}
