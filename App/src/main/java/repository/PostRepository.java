package repository;

import model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private static PostRepository instance;
    private List<Post> posts;

    private PostRepository() {
        posts = new ArrayList<>();
    }

    public static PostRepository getInstance() {
        if (instance == null) {
            instance = new PostRepository();
        }

        return instance;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public Post findById(int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }

    public boolean deleteById(int id) {
        return posts.removeIf(post -> post.getId() == id);
    }

    public List<Post> getAllPosts() {
        return posts;
    }

    public List<Post> getPostsByUser(int userId) {
        List<Post> userPosts = new ArrayList<>();

        for (Post post : posts) {
            if (post.getOwnerId() == userId) {
                userPosts.add(post);
            }
        }

        return userPosts;
    }
}
