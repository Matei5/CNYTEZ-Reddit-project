package service;

import log.LogManager;
import model.Post;
import model.User;
import repository.PostRepository;
import repository.SubredditRepository;

import java.time.LocalDateTime;

public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private int currentId;

    public PostService(PostRepository postRepository, SubredditRepository subredditRepository, AuthService authService) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.authService = authService;
        this.currentId = 1;
    }


    public boolean createPost(int subredditId, String title, String text, String image) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Create post failed! User was not logged in");

            return false;
        }

        if (subredditRepository.getSubredditById(subredditId) == null) {
            LogManager.getInstance().log(
                "Create post failed! User with id " + loggedUser.getId() +
                " tried to create post for subreddit with id " + subredditId + " that doesn't exist"
            );

            return false;
        }

        int ownerId = loggedUser.getId();
        LocalDateTime creationDate = LocalDateTime.now();

        Post post = new Post(currentId, title, text, image, creationDate, subredditId, ownerId);
        postRepository.addPost(post);

        currentId++;

        LogManager.getInstance().log(
            "Create post success! User with id " + loggedUser.getId() +
            " created post with id " + post.getId() + " for subreddit with id " + subredditId
        );

        return true;
    }

    public boolean deletePost(int id) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Delete post failed! No user was logged in");

            return false;
        }

        Post post = postRepository.findById(id);
        if (post == null) {
            LogManager.getInstance().log(
                "Delete post failed! User with id " + loggedUser.getId() +
                " tried to delete post with id " + id + " that doesn't exist"
            );

            return false;
        }

        if (post.getOwnerId() != loggedUser.getId()) {
            LogManager.getInstance().log(
                "Delete post failed! User with id " + loggedUser.getId() +
                " is not the owner of post with id " + id
            );

            return false;
        }

        postRepository.deleteById(id);

        LogManager.getInstance().log(
            "Delete post success! User with id " + loggedUser.getId() + " deleted post with id " + id
        );

        return true;
    }

    public boolean editPost(int id, String newTitle, String newText, String newImage) {
        User loggedUser = authService.getLoggedInUser();
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

        if (post.getOwnerId() != loggedUser.getId()) {
            LogManager.getInstance().log(
                "Edit post failed! User with id " + loggedUser.getId() +
                " is not the owner of post with id " + id
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
        User loggedUser = authService.getLoggedInUser();
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
