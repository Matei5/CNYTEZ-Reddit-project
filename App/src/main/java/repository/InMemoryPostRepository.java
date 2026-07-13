package repository;

import model.Post;

import java.util.ArrayList;
import java.util.List;

public class InMemoryPostRepository implements PostRepository {
    private static InMemoryPostRepository instance;
    private final List<Post> posts;

    private InMemoryPostRepository() {
        posts = new ArrayList<>();
    }

    public static InMemoryPostRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryPostRepository();
        }

        return instance;
    }

    @Override
    public void addPost(Post post) {
        posts.add(post);
    }

    @Override
    public Post findById(int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }

    @Override
    public boolean deleteById(int id) {
        return posts.removeIf(post -> post.getId() == id);
    }

    @Override
    public List<Post> getAllPosts() {
        return posts;
    }

    @Override
    public List<Post> getPostsByUser(int userId) {
        List<Post> userPosts = new ArrayList<>();

        for (Post post : posts) {
            if (post.getOwnerId() == userId) {
                userPosts.add(post);
            }
        }

        return userPosts;
    }

    public void update(Post post) {}
}
