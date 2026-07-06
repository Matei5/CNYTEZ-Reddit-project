package ui;

import model.Comment;
import model.Post;
import model.Subreddit;
import model.User;
import repository.CommentRepository;
import repository.PostRepository;
import repository.SubredditRepository;
import repository.UserRepository;
import service.AuthService;
import service.CommentService;
import service.PostService;
import service.SubredditService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner;

    private final AuthService authService;
    private final PostService postService;
    private final CommentService commentService;
    private final SubredditService subredditService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;

    public Menu() {
        scanner = new Scanner(System.in);

        authService = AuthService.getInstance();
        postService = PostService.getInstance();
        commentService = CommentService.getInstance();
        subredditService = SubredditService.getSubredditService();

        postRepository = PostRepository.getInstance();
        commentRepository = CommentRepository.getInstance();
        subredditRepository = SubredditRepository.getSubredditRepository();
        userRepository = UserRepository.getInstance();
    }

    public void start() {
        boolean running = true;

        while (running) {
            if (authService.isLoggedIn()) {
                running = showUserMenu();
            } else {
                running = showGuestMenu();
            }
        }

        System.out.println("Goodbye!");
    }

    private boolean showGuestMenu() {
        System.out.println();
        System.out.println("=== MiniReddit ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.print("Choose: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                handleRegister();
                return true;
            case "2":
                handleLogin();
                return true;
            case "0":
                return false;
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }

    private boolean showUserMenu() {
        User user = authService.getLoggedInUser();

        System.out.println();
        System.out.println("=== MiniReddit ===");
        System.out.println("Logged in as: " + user.getUsername());
        System.out.println("1. List subreddits");
        System.out.println("2. Create subreddit");
        System.out.println("3. Join subreddit");
        System.out.println("4. Leave subreddit");
        System.out.println("5. Create post");
        System.out.println("6. List all posts");
        System.out.println("7. List my posts");
        System.out.println("8. Edit post");
        System.out.println("9. Delete post");
        System.out.println("10. Vote post");
        System.out.println("11. Create comment");
        System.out.println("12. List comments");
        System.out.println("13. List my comments");
        System.out.println("14. Delete comment");
        System.out.println("15. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                handleListSubreddits();
                return true;
            case "2":
                handleCreateSubreddit();
                return true;
            case "3":
                handleJoinSubreddit();
                return true;
            case "4":
                handleLeaveSubreddit();
                return true;
            case "5":
                handleCreatePost();
                return true;
            case "6":
                handleListAllPosts();
                return true;
            case "7":
                handleListMyPosts();
                return true;
            case "8":
                handleEditPost();
                return true;
            case "9":
                handleDeletePost();
                return true;
            case "10":
                handleVotePost();
                return true;
            case "11":
                handleCreateComment();
                return true;
            case "12":
                handleListComments();
                return true;
            case "13":
                handleListMyComments();
                return true;
            case "14":
                handleDeleteComment();
                return true;
            case "15":
                authService.logout();
                System.out.println("You are now logged out.");
                return true;
            case "0":
                return false;
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }

    private void handleRegister() {
        System.out.println();
        System.out.println("Register");

        String name = readText("Name: ");
        String username = readText("Username: ");
        String email = readText("Email: ");
        String password = readText("Password: ");

        boolean success = authService.register(name, username, email, password);

        if (success) {
            System.out.println("Account created.");
        } else {
            System.out.println("Could not create account. Username or email already exists.");
        }
    }

    private void handleLogin() {
        System.out.println();
        System.out.println("Login");

        String username = readText("Username: ");
        String password = readText("Password: ");

        boolean success = authService.login(username, password);

        if (success) {
            System.out.println("You are now logged in.");
        } else {
            System.out.println("Wrong username or password.");
        }
    }

    private void handleListSubreddits() {
        System.out.println();
        System.out.println("Subreddits");

        List<Subreddit> subreddits = subredditRepository.getAllSubreddits();

        if (subreddits.isEmpty()) {
            System.out.println("No subreddits yet.");
            return;
        }

        for (Subreddit subreddit : subreddits) {
            printSubreddit(subreddit);
        }
    }

    private void handleCreateSubreddit() {
        System.out.println();
        System.out.println("Create subreddit");

        String name = readText("Name: ");
        String photo = readText("Photo path: ");
        String banner = readText("Banner path: ");

        boolean success = subredditService.createSubreddit(name, photo, banner);

        if (success) {
            System.out.println("Subreddit created.");
        } else {
            System.out.println("Could not create subreddit. Make sure you are logged in and the name is unique.");
        }
    }

    private void handleJoinSubreddit() {
        int subredditId = readInt("Subreddit id: ");
        boolean success = subredditService.joinSubreddit(subredditId);

        if (success) {
            System.out.println("Joined subreddit.");
        } else {
            System.out.println("Could not join subreddit. It may not exist or you may already be a follower.");
        }
    }

    private void handleLeaveSubreddit() {
        int subredditId = readInt("Subreddit id: ");
        boolean success = subredditService.leaveSubreddit(subredditId);

        if (success) {
            System.out.println("Left subreddit.");
        } else {
            System.out.println("Could not leave subreddit. Owners cannot leave their own subreddit before changing owner.");
        }
    }

    private void handleCreatePost() {
        System.out.println();
        System.out.println("Create post");

        int subredditId = readInt("Subreddit id: ");
        if (subredditRepository.getSubredditById(subredditId) == null) {
            System.out.println("Subreddit does not exist.");
            return;
        }

        String title = readText("Title: ");
        String text = readText("Text: ");
        String image = readText("Image path: ");

        postService.createPost(subredditId, title, text, image);
        System.out.println("Post created.");
    }

    private void handleListAllPosts() {
        System.out.println();
        System.out.println("All posts");
        printPosts(postRepository.getAllPosts());
    }

    private void handleListMyPosts() {
        User user = authService.getLoggedInUser();

        System.out.println();
        System.out.println("My posts");
        printPosts(postRepository.getPostsByUser(user.getId()));
    }

    private void handleEditPost() {
        System.out.println();
        System.out.println("Edit post");

        int postId = readInt("Post id: ");
        Post post = postRepository.findById(postId);

        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        if (!isCurrentUserOwner(post.getOwnerId())) {
            System.out.println("You can only edit your own posts.");
            return;
        }

        String title = readText("New title: ");
        String text = readText("New text: ");
        String image = readText("New image path: ");

        boolean success = postService.editPost(postId, title, text, image);

        if (success) {
            System.out.println("Post edited.");
        } else {
            System.out.println("Could not edit post.");
        }
    }

    private void handleDeletePost() {
        System.out.println();
        System.out.println("Delete post");

        int postId = readInt("Post id: ");
        Post post = postRepository.findById(postId);

        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        if (!isCurrentUserOwner(post.getOwnerId())) {
            System.out.println("You can only delete your own posts.");
            return;
        }

        boolean success = postService.deletePost(postId);

        if (success) {
            System.out.println("Post deleted.");
        } else {
            System.out.println("Could not delete post.");
        }
    }

    private void handleVotePost() {
        System.out.println();
        System.out.println("Vote post");

        int postId = readInt("Post id: ");
        System.out.println("1. Upvote");
        System.out.println("2. Downvote");
        String choice = readText("Choose: ");

        Post.VoteType voteType;
        if (choice.equals("1")) {
            voteType = Post.VoteType.UPVOTE;
        } else if (choice.equals("2")) {
            voteType = Post.VoteType.DOWNVOTE;
        } else {
            System.out.println("Invalid vote type.");
            return;
        }

        boolean success = postService.votePost(postId, voteType);

        if (success) {
            System.out.println("Vote saved. If you repeated the same vote, it was removed.");
        } else {
            System.out.println("Post not found.");
        }
    }

    private void handleCreateComment() {
        System.out.println();
        System.out.println("Create comment");

        int postId = readInt("Parent post id: ");
        if (postRepository.findById(postId) == null) {
            System.out.println("Post not found.");
            return;
        }

        int parentCommentId = readInt("Parent comment id (0 if this is a direct post comment): ");
        if (parentCommentId != 0 && commentRepository.findById(parentCommentId) == null) {
            System.out.println("Parent comment not found.");
            return;
        }

        String title = readText("Title: ");
        String text = readText("Text: ");
        String image = readText("Image path: ");

        boolean success = commentService.createComment(postId, parentCommentId, title, text, image);

        if (success) {
            Comment createdComment = getLastComment();
            if (createdComment != null && parentCommentId != 0) {
                Comment parentComment = commentRepository.findById(parentCommentId);
                parentComment.addChildCommentID(createdComment.getID());
            }
            System.out.println("Comment created.");
        } else {
            System.out.println("Could not create comment.");
        }
    }

    private void handleListComments() {
        System.out.println();
        System.out.println("Comments");

        List<Comment> comments = commentRepository.getComments();

        if (comments.isEmpty()) {
            System.out.println("No comments yet.");
            return;
        }

        for (Comment comment : comments) {
            printComment(comment);
        }
    }

    private void handleListMyComments() {
        User user = authService.getLoggedInUser();

        System.out.println();
        System.out.println("My comments");

        List<Comment> comments = commentRepository.getCommentsByUser(user.getId());

        if (comments.isEmpty()) {
            System.out.println("You have not written any comments yet.");
            return;
        }

        for (Comment comment : comments) {
            printComment(comment);
        }
    }

    private void handleDeleteComment() {
        System.out.println();
        System.out.println("Delete comment");

        int commentId = readInt("Comment id: ");
        Comment comment = commentRepository.findById(commentId);

        if (comment == null) {
            System.out.println("Comment not found.");
            return;
        }

        if (!isCurrentUserOwner(comment.getOwnerID())) {
            System.out.println("You can only delete your own comments.");
            return;
        }

        removeCommentFromParent(comment);
        boolean success = commentService.deleteComment(commentId);

        if (success) {
            System.out.println("Comment deleted.");
        } else {
            System.out.println("Could not delete comment.");
        }
    }

    private void printPosts(List<Post> posts) {
        if (posts.isEmpty()) {
            System.out.println("No posts yet.");
            return;
        }

        for (Post post : posts) {
            printPost(post);
        }
    }

    private void printPost(Post post) {
        User owner = userRepository.findById(post.getOwnerId());
        Subreddit subreddit = subredditRepository.getSubredditById(post.getSubredditId());

        String username = owner == null ? "unknown" : owner.getUsername();
        String subredditName = subreddit == null ? "unknown" : subreddit.getName();

        System.out.println();
        System.out.println("Post id: " + post.getId());
        System.out.println("r/" + subredditName + " | by " + username);
        System.out.println("Title: " + post.getTitle());
        System.out.println("Text: " + post.getText());
        System.out.println("Image: " + post.getImage());
        System.out.println("Created: " + post.getCreationDate());
        System.out.println("Score: " + getPostScore(post) + " (" + getPostUpvotes(post) + " up, " + getPostDownvotes(post) + " down)");
    }

    private void printSubreddit(Subreddit subreddit) {
        User owner = userRepository.findById(subreddit.getOwnerId());
        String ownerUsername = owner == null ? "unknown" : owner.getUsername();

        System.out.println();
        System.out.println("Subreddit id: " + subreddit.getId());
        System.out.println("Name: r/" + subreddit.getName());
        System.out.println("Owner: " + ownerUsername);
        System.out.println("Followers: " + subreddit.getUserIdList().size());
        System.out.println("Photo: " + subreddit.getPhoto());
        System.out.println("Banner: " + subreddit.getBanner());
        System.out.println("Created: " + subreddit.getCreationDate());
    }

    private void printComment(Comment comment) {
        User owner = userRepository.findById(comment.getOwnerID());
        String username = owner == null ? "unknown" : owner.getUsername();

        System.out.println();
        System.out.println("Comment id: " + comment.getID());
        System.out.println("Post id: " + comment.getParentPostID());
        System.out.println("Parent comment id: " + comment.getParentCommentID());
        System.out.println("By: " + username);
        System.out.println("Title: " + comment.getTitle());
        System.out.println("Text: " + comment.getText());
        System.out.println("Image: " + comment.getImage());
        System.out.println("Replies: " + comment.getChildCommentIDs());
        System.out.println("Created: " + comment.getCreationDate());
    }

    private boolean isCurrentUserOwner(int ownerId) {
        User currentUser = authService.getLoggedInUser();
        return currentUser != null && currentUser.getId() == ownerId;
    }

    private int getPostScore(Post post) {
        return getPostUpvotes(post) - getPostDownvotes(post);
    }

    private int getPostUpvotes(Post post) {
        int count = 0;

        for (Map.Entry<Integer, Post.VoteType> entry : post.getUserIdVotesMap().entrySet()) {
            if (entry.getValue() == Post.VoteType.UPVOTE) {
                count++;
            }
        }

        return count;
    }

    private int getPostDownvotes(Post post) {
        int count = 0;

        for (Map.Entry<Integer, Post.VoteType> entry : post.getUserIdVotesMap().entrySet()) {
            if (entry.getValue() == Post.VoteType.DOWNVOTE) {
                count++;
            }
        }

        return count;
    }

    private Comment getLastComment() {
        List<Comment> comments = commentRepository.getComments();

        if (comments.isEmpty()) {
            return null;
        }

        return comments.get(comments.size() - 1);
    }

    private void removeCommentFromParent(Comment comment) {
        int parentCommentId = comment.getParentCommentID();

        if (parentCommentId == 0) {
            return;
        }

        Comment parentComment = commentRepository.findById(parentCommentId);
        if (parentComment != null) {
            parentComment.removeChildCommentID(comment.getID());
        }
    }

    private String readText(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    private int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}