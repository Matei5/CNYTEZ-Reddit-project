package service;

import model.Post;
import model.User;
import repository.InMemoryPostRepository;
import repository.PostRepository;

import java.time.LocalDateTime;

public class PostService {
    private static PostService instance;

    private PostRepository postRepository;
    private int currentId;

    public PostService() {
        postRepository = InMemoryPostRepository.getInstance();
        currentId = 1;
    }

    public static PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }

        return instance;
    }

    public void createPost(int subredditId, String title, String text, String image) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        int ownerId = loggedUser.getId();
        LocalDateTime creationDate = LocalDateTime.now();

        Post post = new Post(currentId, title, text, image, creationDate, subredditId, ownerId);
        postRepository.addPost(post);

        currentId++;
    }

    public boolean deletePost(int id) {
        return postRepository.deleteById(id);
    }

    public boolean editPost(int id, String newTitle, String newText, String newImage) {
        Post post = postRepository.findById(id);
        if (post == null) {
            return false;
        }

        post.setTitle(newTitle);
        post.setText(newText);
        post.setImage(newImage);

        return true;
    }

    public boolean votePost(int id, Post.VoteType voteType) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        int userId = loggedUser.getId();

        Post post = postRepository.findById(id);
        if (post == null) {
            return false;
        }

        post.vote(userId, voteType);

        return true;
    }
}
