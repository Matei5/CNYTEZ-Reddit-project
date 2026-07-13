package repository;

import model.Post;
import model.User;

import java.util.List;

public interface PostRepository {
    void addPost(Post post);

    Post findById(int id);

    boolean deleteById(int id);

    List<Post> getAllPosts();

    List<Post> getPostsByUser(int userId);

    void update(Post post);
}
