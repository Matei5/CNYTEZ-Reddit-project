package ui;

import service.AuthService;
import service.SubredditService;
import service.PostService;
import service.CommentService;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    private AuthService authService;
    private SubredditService subredditService;
    private PostService postService;
    private CommentService commentService;

    public Menu(AuthService authSerrvice, SubredditService subredditService,
                PostService postService, CommentService commentService){
        this.scanner = new Scanner(System.in);
        this.authService = authService;
        this.subredditService = subredditService;
        this.postService = postService;
        this.commentService = commentService;
    }

    public void start() {
        boolean running = true;

        while(running){
            if (authService.isLoggedIn()) {
                running = showUserMenu();
            } else {
                running = showGuestMenu();
            }
        }

        System.out.println("Goodbye! You're always welcome back!");
    }

    private void showGuestMenu() {
    }

    private void showUserMenu() {
    }

    private void handleRegister() {
    }

    private void handleLogin() {
    }

    private void handleLogout() {
    }

    private void handleCreateSubreddit() {
    }

    private void handleListSubreddits() {
    }

    private void handleCreatePost() {
    }

    private void handleViewPosts() {
    }

    private void handleViewPostDetails() {
    }

    private void handleCreateComment() {
    }

    private void handleDeleteComment() {
    }

    private void handleDeletePost() {
    }

}
