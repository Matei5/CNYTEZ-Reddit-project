package controller;

import log.LogManager;
import model.Post;
import model.Subreddit;
import model.User;
import repository.PostRepository;
import repository.SubredditRepository;
import repository.UserRepository;
import service.AuthService;
import service.PostService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

import java.util.List;

public class PostController {
    private final ConsoleReader consoleReader;
    private final ConsolePrinter consolePrinter;
    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    public PostController(
            PostService postService,
            PostRepository postRepository,
            UserRepository userRepository,
            SubredditRepository subredditRepository,
            AuthService authService
    ) {
        this.consoleReader = ConsoleReader.getInstance();
        this.consolePrinter = ConsolePrinter.getInstance();
        this.postService = postService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
        this.authService = authService;
    }

    public void createPost() {
        consolePrinter.printHeader("Create post");

        int subredditId = consoleReader.readInt("Subreddit id: ");
        String title = consoleReader.readText("Title: ");
        String text = consoleReader.readText("Text: ");
        String image = consoleReader.readText("Image path: ");

        boolean success = false;

        try {
            success = postService.createPost(subredditId, title, text, image);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Post created.");
        } else {
            consolePrinter.printMessage("Post could not be created! Make sure the subreddit exists");
        }
    }

    public void listAllPosts() {
        consolePrinter.printHeader("All posts");
        printPosts(postRepository.getAllPosts());
    }

    public void listMyPosts() {
        User user = authService.getLoggedInUser();
        consolePrinter.printHeader("My posts");
        printPosts(postRepository.getPostsByUser(user.getId()));
    }

    public void editPost() {
        consolePrinter.printHeader("Edit post");

        int postId = consoleReader.readInt("Post id: ");

        String title = consoleReader.readText("New title: ");
        String text = consoleReader.readText("New text: ");
        String image = consoleReader.readText("New image path: ");

        boolean success = false;

        try {
            success = postService.editPost(postId, title, text, image);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Post edited.");
        } else {
            consolePrinter.printMessage("Could not edit post. It may not exist or you are not the owner");
        }
    }

    public void deletePost() {
        consolePrinter.printHeader("Delete post");

        int postId = consoleReader.readInt("Post id: ");

        boolean success = false;

        try {
            success = postService.deletePost(postId);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Post deleted.");
        } else {
            consolePrinter.printMessage("Could not delete post. It may not exist or you are not the owner");
        }
    }

    public void votePost() {
        consolePrinter.printHeader("Vote post");

        int postId = consoleReader.readInt("Post id: ");
        consolePrinter.printVoteMenu();
        String choice = consoleReader.readText();
        Post.VoteType voteType;

        switch (choice) {
            case "1" -> voteType = Post.VoteType.UPVOTE;
            case "2" -> voteType = Post.VoteType.DOWNVOTE;
            case "0" -> {
                return;
            }
            default -> {
                consolePrinter.printInvalidChoiceMessage();
                return;
            }
        }

        boolean success = false;

        try {
            success = postService.votePost(postId, voteType);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Vote saved. Repeating the same vote removes it.");
        } else {
            consolePrinter.printMessage("Could not save vote.");
        }
    }

    private void printPosts(List<Post> posts) {
        if (posts.isEmpty()) {
            consolePrinter.printMessage("No posts yet.");
            return;
        }

        for (Post post : posts) {
            User owner = userRepository.findById(post.getOwnerId());
            Subreddit subreddit = subredditRepository.getSubredditById(post.getSubredditId());

            String username = owner == null ? "unknown" : owner.getUsername();
            String subredditName = subreddit == null ? "unknown" : subreddit.getName();

            consolePrinter.printPost(
                    post,
                    username,
                    subredditName,
                    post.getScore(),
                    post.getUpvotesCount(),
                    post.getDownvotesCount()
            );
        }
    }
}
