package ui;

import model.Comment;
import model.Post;
import model.Subreddit;
import model.User;
import service.AuthService;
import service.CommentService;
import service.PostService;
import service.SubredditService;
import repository.*;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    private AuthService authService;
    private PostService postService;
    private CommentService commentService;
    private SubredditService subredditService;

    public Menu() {
        this.scanner = new Scanner(System.in);

        this.authService = AuthService.getInstance();
        this.postService = PostService.getInstance();
        this.commentService = CommentService.getInstance();
        this.subredditService = SubredditService.getSubredditService();
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
        System.out.println("Welcome to MiniReddit");
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
        System.out.println("Logged in as: " + user.getUsername());
        System.out.println("1. View subreddits");
        System.out.println("2. Create subreddit");
        System.out.println("3. Create post");
        System.out.println("4. View all posts");
        System.out.println("5. View my posts");
        System.out.println("6. View my comments");
        System.out.println("7. Logout");
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
                handleCreatePost();
                return true;

            case "4":
                handleViewPosts();
                return true;

            case "5":
                handleViewMyPosts();
                return true;

            case "6":
                handleViewMyComments();
                return true;

            case "7":
                handleLogout();
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

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = authService.register(name, username, email, password);

        if (success) {
            System.out.println("Account created.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private void handleLogin() {
        System.out.println();
        System.out.println("Login");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = authService.login(username, password);

        if (success) {
            System.out.println("You are now logged in.");
        } else {
            System.out.println("Wrong username or password.");
        }
    }

    private void handleLogout() {
        authService.logout();
        System.out.println("You are now logged out.");
    }

    private void handleListSubreddits() {
        System.out.println();
        System.out.println("Subreddits");

        List<Subreddit> subreddits = SubredditRepository.getSubredditRepository().getAllSubreddits();

        if (subreddits.isEmpty()) {
            System.out.println("No subreddits yet.");
            return;
        }

        for (Subreddit subreddit : subreddits) {
            System.out.println(subreddit.getId() + ". r/" + subreddit.getName());
        }
    }

    private void handleCreateSubreddit() {
        User currentUser = authService.getLoggedInUser();

        System.out.println();
        System.out.println("Create subreddit");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Banner: ");
        String banner = scanner.nextLine();

        System.out.print("Photo (as path): ");
        String photo = scanner.nextLine();

        boolean created = subredditService.createSubreddit(name, photo, banner);

        if (!created) {
            System.out.println("Could not create subreddit.");
            return;
        }

        System.out.println("Subreddit created");
    }

    private void handleCreatePost() {
        User currentUser = authService.getLoggedInUser();

        System.out.println();
        System.out.println("Create post");

        List<Subreddit> subreddits = SubredditRepository.getSubredditRepository().getAllSubreddits();

        if (subreddits.isEmpty()) {
            System.out.println("Create a subreddit first.");
            return;
        }

        handleListSubreddits();

        System.out.print("Subreddit id: ");
        int subredditId = readInt();

        Subreddit subreddit = SubredditRepository.getSubredditRepository().getSubredditById(subredditId);

        if (subreddit == null) {
            System.out.println("Subreddit not found.");
            return;
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Text: ");
        String text = scanner.nextLine();

        System.out.print("Image (as path): ");
        String image = scanner.nextLine();

        postService.createPost(subredditId, title, text, image);
        System.out.println("Post created.");
    }

    private void handleViewPosts() {
        System.out.println();
        System.out.println("All posts");

        List<Post> posts = PostRepository.getInstance().getAllPosts();

        if (posts.isEmpty()) {
            System.out.println("No posts yet.");
            return;
        }

        printPosts(posts);

        System.out.print("Enter post id to open, or 0 to go back: ");
        int postId = readInt();

        if (postId == 0) {
            return;
        }

        Post post = PostRepository.getInstance().findById(postId);

        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        handlePostDetails(post);
    }

    private void handleViewMyPosts() {
        User currentUser = authService.getLoggedInUser();

        System.out.println();
        System.out.println("My posts");

        List<Post> posts = PostRepository.getInstance().getPostsByUser(currentUser.getId());

        if (posts.isEmpty()) {
            System.out.println("You have not written any posts yet.");
            return;
        }

        printPosts(posts);

        System.out.print("Enter post id to open, or 0 to go back: ");
        int postId = readInt();

        if (postId == 0) {
            return;
        }

        Post post = PostRepository.getInstance().findById(postId);

        if (post == null) {
            System.out.println("Post not found.");
            return;
        }

        handlePostDetails(post);
    }

    private void handleViewMyComments() {
        User currentUser = authService.getLoggedInUser();

        System.out.println();
        System.out.println("My comments");

        List<Comment> comments = CommentRepository.getInstance().getCommentsByUser(currentUser.getId());

        if (comments.isEmpty()) {
            System.out.println("You have not written any comments yet.");
            return;
        }

        printComments(comments);

        System.out.print("Enter comment id to delete, or 0 to go back: ");
        int commentId = readInt();

        if (commentId == 0) {
            return;
        }

        Comment comment = CommentRepository.getInstance().findById(commentId);

        if (comment == null) {
            System.out.println("Comment not found.");
            return;
        }

        boolean deleted = commentService.deleteComment(commentId);

        if (deleted) {
            System.out.println("Comment deleted.");
        } else {
            System.out.println("You can only delete your own comments.");
        }
    }

    private void handlePostDetails(Post post) {
        boolean viewingPost = true;

        while (viewingPost) {
            printPostDetails(post);

            System.out.println();
            System.out.println("1. Add comment");
            System.out.println("2. Upvote post");
            System.out.println("3. Downvote post");
            System.out.println("4. Delete post");
            System.out.println("5. Upvote comment");
            System.out.println("6. Downvote comment");
            System.out.println("7. Delete comment");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCreateComment(post);
                    break;

                case "2":
                    postService.votePost(post.getId(), Post.VoteType.UPVOTE);
                    System.out.println("Post upvoted.");
                    break;

                case "3":
                    postService.votePost(post.getId(), Post.VoteType.DOWNVOTE);
                    System.out.println("Post downvoted.");
                    break;

                case "4":
                    boolean deleted = handleDeletePost(post);

                    if (deleted) {
                        viewingPost = false;
                    }

                    break;

                case "5":
                    handleUpvoteComment();
                    break;

                case "6":
                    handleDownvoteComment();
                    break;

                case "7":
                    handleDeleteComment();
                    break;

                case "0":
                    viewingPost = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private void handleCreateComment(Post post) {
        User currentUser = authService.getLoggedInUser();

        System.out.println();
        System.out.println("Add comment");

        System.out.print("Content: ");
        String content = scanner.nextLine();

        Comment comment = commentService.createComment(content, currentUser, post);

        if (comment == null) {
            System.out.println("Could not create comment.");
            return;
        }

        System.out.println("Comment added.");
    }

    private boolean handleDeletePost(Post post) {
        User currentUser = authService.getLoggedInUser();

        boolean deleted = postService.deletePost(post, currentUser);

        if (deleted) {
            System.out.println("Post deleted.");
            return true;
        }

        System.out.println("You can only delete your own posts.");
        return false;
    }

    private void handleUpvoteComment() {
        System.out.print("Comment id: ");
        int commentId = readInt();

        Comment comment = commentService.findById(commentId);

        if (comment == null) {
            System.out.println("Comment not found.");
            return;
        }

        commentService.upvoteComment(comment);
        System.out.println("Comment upvoted.");
    }

    private void handleDownvoteComment() {
        System.out.print("Comment id: ");
        int commentId = readInt();

        Comment comment = commentService.findById(commentId);

        if (comment == null) {
            System.out.println("Comment not found.");
            return;
        }

        commentService.downvoteComment(comment);
        System.out.println("Comment downvoted.");
    }

    private void handleDeleteComment() {
        User currentUser = authService.getLoggedInUser();

        System.out.print("Comment id: ");
        int commentId = readInt();

        Comment comment = commentService.findById(commentId);

        if (comment == null) {
            System.out.println("Comment not found.");
            return;
        }

        boolean deleted = commentService.deleteComment(comment, currentUser);

        if (deleted) {
            System.out.println("Comment deleted.");
        } else {
            System.out.println("You can only delete your own comments.");
        }
    }

    private void printPosts(List<Post> posts) {
        for (Post post : posts) {
            System.out.println();
            System.out.println(post.getId() + ". " + post.getTitle());
            System.out.println("   by " + post.getAuthor().getUsername());
            System.out.println("   r/" + post.getSubreddit().getName());
            System.out.println("   score: " + post.getScore());
            System.out.println("   comments: " + post.getComments().size());
        }
    }

    private void printPostDetails(Post post) {
        System.out.println();
        System.out.println("================================");
        System.out.println(post.getTitle());
        System.out.println("by " + post.getAuthor().getUsername());
        System.out.println("r/" + post.getSubreddit().getName());
        System.out.println("score: " + post.getScore());
        System.out.println("--------------------------------");
        System.out.println(post.getContent());
        System.out.println("--------------------------------");

        if (post.getComments().isEmpty()) {
            System.out.println("No comments yet.");
        } else {
            System.out.println("Comments:");
            printComments(post.getComments());
        }

        System.out.println("================================");
    }

    private void printComments(List<Comment> comments) {
        for (Comment comment : comments) {
            System.out.println();
            System.out.println(comment.getId() + ". " + comment.getAuthor().getUsername());
            System.out.println("   " + comment.getContent());
            System.out.println("   score: " + comment.getScore());

            if (comment.getPost() != null) {
                System.out.println("   on post: " + comment.getPost().getTitle());
            }
        }
    }

    private int readInt() {
        String input = scanner.nextLine();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}