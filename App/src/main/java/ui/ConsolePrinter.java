package ui;

import model.Comment;
import model.Post;
import model.Subreddit;
import model.User;

import java.util.List;

public class ConsolePrinter {

    private static ConsolePrinter instance;

    private ConsolePrinter() {}

    public static ConsolePrinter getInstance() {
        if (instance == null) {
            instance = new ConsolePrinter();
        }
        return instance;
    }

    public void printGuestMenu() {
        System.out.print("\n=== MiniReddit ===\n" +
                "1. Register    2. Login    0. Exit\n\n" +
                "Choose: ");
    }

    public void printUserMenu(String username) {
        System.out.printf("\n=== MiniReddit ===\n" +
                "Logged in as: %s\n\n" +
                "1. Subreddits\n" +
                "2. Posts\n" +
                "3. Comments\n" +
                "4. Logout\n" +
                "0. Exit\n\n" +
                "Choose: ", username);
    }

    public void printSubredditMenu() {
        System.out.print("\n=== Subreddits ===\n" +
                "1. List subreddits\n" +
                "2. Create subreddit\n" +
                "3. Join subreddit\n" +
                "4. Leave subreddit\n" +
                "0. Back\n\n" +
                "Choose: ");
    }

    public void printPostMenu() {
        System.out.print("\n=== Posts ===\n" +
                "1. Create post       2. List all posts\n" +
                "3. List my posts     4. Edit post\n" +
                "5. Delete post       6. Vote post\n" +
                "0. Back\n\n" +
                "Choose: ");
    }

    public void printCommentMenu() {
        System.out.print("\n=== Comments ===\n" +
                "1. Create comment      2. List comments\n" +
                "3. List my comments    4. Delete comment\n" +
                "0. Back\n\n" +
                "Choose: ");
    }

    public void printVoteMenu() {
        System.out.print("\n1. Upvote    2. Downvote    0. Cancel\n\nChoose: ");
    }

    public void printHeader(String title) {
        System.out.printf("\n=== %s ===\n", title);
    }

    public void printPrompt(String message) {
        System.out.print(message);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printInvalidChoiceMessage() {
        printMessage("Invalid choice.");
    }

    public void printInvalidNumberMessage() {
        printMessage("Please enter a valid number.");
    }

    public void printGoodbyeMessage() {
        System.out.println("\nGoodbye!");
    }

    public void printLoggedInMessage() {
        printMessage("You are now logged in.");
    }

    public void printLoggedOutMessage() {
        printMessage("You are now logged out.");
    }

    public void printPost(Post post, String username, String subredditName, int score, int upvotes, int downvotes) {
        System.out.printf("\nPost #%d\n" +
                "r/%s | by %s\n" +
                "Title: %s\n" +
                "Text: %s\n" +
                "Image: %s\n" +
                "Created: %s\n" +
                "Score: %d (%d up, %d down)\n",
                post.getId(), subredditName, username, post.getTitle(), post.getText(), post.getImage(), post.getCreationDate(), score, upvotes, downvotes);
    }

    public void printSubreddit(Subreddit subreddit, String ownerUsername) {
        System.out.printf("\nSubreddit #%d\n" +
                "Name: r/%s\n" +
                "Owner: %s\n" +
                "Followers: %d\n" +
                "Photo: %s\n" +
                "Banner: %s\n" +
                "Created: %s\n",
                subreddit.getId(), subreddit.getName(), ownerUsername, subreddit.getUserIdList().size(), subreddit.getPhoto(), subreddit.getBanner(), subreddit.getCreationDate());
    }

    public void printComment(Comment comment, String username) {
        System.out.printf("\nComment #%d\n" +
                "Post: %d | Parent comment: %d | By: %s\n" +
                "Title: %s\n" +
                "Text: %s\n" +
                "Image: %s\n" +
                "Replies: %s\n" +
                "Created: %s\n",
                comment.getID(), comment.getParentPostID(), comment.getParentCommentID(), username, comment.getTitle(), comment.getText(), comment.getImage(), comment.getChildCommentIDs(), comment.getCreationDate());
    }

    public void printPosts(List<Post> posts) {
        if (posts.isEmpty()) {
            printMessage("No posts found.");
        }
    }

    public void printComments(List<Comment> comments) {
        if (comments.isEmpty()) {
            printMessage("No comments found.");
        }
    }

    public void printSubreddits(List<Subreddit> subreddits) {
        if (subreddits.isEmpty()) {
            printMessage("No subreddits found.");
        }
    }

    public void printUserProfile(User user, int karma) {
        System.out.printf("\nName: %s\n" +
                "Username: %s\n" +
                "Email: %s\n" +
                "Profile Photo: %s\n" +
                "Karma: %d\n",
                user.getName(), user.getUsername(), user.getEmail(), user.getProfilePhoto(), karma);
    }
}