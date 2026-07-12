package service;

import log.LogManager;
import model.Post;
import model.User;
import repository.PostRepository;

import java.time.LocalDateTime;

public class PostService {
    private static PostService instance;

    private PostRepository postRepository;
    private int currentId;

    public PostService() {
        postRepository = PostRepository.getInstance();
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

        LogManager.getInstance().log(
            "Create post success! User with id " + loggedUser.getId() +
            " created post with id " + post.getId() + " for subreddit with id " + subredditId
        );
    }

    public boolean deletePost(int id) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Delete post failed! No user was logged in");

            return false;
        }

        boolean deleted = postRepository.deleteById(id);

        if (!deleted) {
            LogManager.getInstance().log(
                "Create post failed! User with id " + loggedUser.getId() +
                " tried to delete post with id " + id + " that doesn't exist"
            );
        } else {
            LogManager.getInstance().log(
                "Create post success! User with id " + loggedUser.getId() + " deleted post with id " + id
            );
        }

        return deleted;
    }

    public boolean editPost(int id, String newTitle, String newText, String newImage) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Edit post failed! No user was logged in");

            return false;
        }

        Post post = postRepository.findById(id);
        if (post == null) {
            LogManager.getInstance().log(
                "Edit post failed! User with id " + loggedUser.getId() +
                " tried to edit post with id " + id + " that doesn't exist"
            );

            return false;
        }

        post.setTitle(newTitle);
        post.setText(newText);
        post.setImage(newImage);

        LogManager.getInstance().log(
            "Edit post success! User with id " + loggedUser.getId() + " edited post with id " + id
        );

        return true;
    }

    public boolean votePost(int id, Post.VoteType voteType) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Vote post failed! No user was logged in");

            return false;
        }

        int userId = loggedUser.getId();

        Post post = postRepository.findById(id);
        if (post == null) {
            LogManager.getInstance().log(
                "Vote post failed! User with id " + loggedUser.getId() +
                " tried to vote post with id " + id + " that doesn't exist"
            );

            return false;
        }

        post.vote(userId, voteType);

        LogManager.getInstance().log(
            "Vote post success! User with id " + loggedUser.getId() +
            " voted post with id " + id
        );

        return true;
    }
}
