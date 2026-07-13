package ui;

import controller.AuthController;
import controller.CommentController;
import controller.PostController;
import controller.SubredditController;
import controller.UserController;

import log.LogManager;
import service.AuthService;

public class Menu {

    private final AuthController authController;
    private final PostController postController;
    private final CommentController commentController;
    private final SubredditController subredditController;
    private final UserController userController;
    
    private final AuthService authService;
    private final ConsolePrinter consolePrinter;
    private final ConsoleReader consoleReader;

    public Menu(
            AuthController authController,
            PostController postController,
            CommentController commentController,
            SubredditController subredditController,
            UserController userController,
            AuthService authService,
            ConsolePrinter consolePrinter,
            ConsoleReader consoleReader
    ) {
        this.authController = authController;
        this.postController = postController;
        this.commentController = commentController;
        this.subredditController = subredditController;
        this.userController = userController;
        this.authService = authService;
        this.consolePrinter = consolePrinter;
        this.consoleReader = consoleReader;
    }

    public void start() {
        boolean running = true;
        while (running) {
            if (authService.isLoggedIn()) {
                running = runUserMenu();
            } else {
                running = runGuestMenu();
            }
        }
        
        consolePrinter.printGoodbyeMessage();
        consoleReader.close();
    }

    private boolean runGuestMenu() {
        consolePrinter.printGuestMenu();
        String choice = consoleReader.readText();
        
        switch (choice) {
            case "1" -> authController.register();
            case "2" -> authController.login();
            case "0" -> {
                return false;
            }
            default -> consolePrinter.printInvalidChoiceMessage();
        }
        return true;
    }

    private boolean runUserMenu() {
        consolePrinter.printUserMenu(authService.getLoggedInUser().getUsername());
        String choice = consoleReader.readText();
        
        switch (choice) {
            case "1" -> runSubredditMenu();
            case "2" -> runPostMenu();
            case "3" -> runCommentMenu();
            case "4" -> userController.printUserProfile();
            case "5" -> {
                try {
                    authService.logout();
                } catch (Exception e) {
                    LogManager.getInstance().log(e.getMessage());
                }
                consolePrinter.printLoggedOutMessage();
            }
            case "0" -> {
                return false;
            }
            default -> consolePrinter.printInvalidChoiceMessage();
        }
        return true;
    }

    private void runSubredditMenu() {
        boolean open = true;
        while (open && authService.isLoggedIn()) {
            consolePrinter.printSubredditMenu();
            String choice = consoleReader.readText();
            
            switch (choice) {
                case "1" -> subredditController.listSubreddits();
                case "2" -> subredditController.createSubreddit();
                case "3" -> subredditController.joinSubreddit();
                case "4" -> subredditController.leaveSubreddit();
                case "0" -> open = false;
                default -> consolePrinter.printInvalidChoiceMessage();
            }
        }
    }

    private void runPostMenu() {
        boolean open = true;
        while (open && authService.isLoggedIn()) {
            consolePrinter.printPostMenu();
            String choice = consoleReader.readText();
            
            switch (choice) {
                case "1" -> postController.createPost();
                case "2" -> postController.listAllPosts();
                case "3" -> postController.listMyPosts();
                case "4" -> postController.editPost();
                case "5" -> postController.deletePost();
                case "6" -> postController.votePost();
                case "0" -> open = false;
                default -> consolePrinter.printInvalidChoiceMessage();
            }
        }
    }

    private void runCommentMenu() {
        boolean open = true;
        while (open && authService.isLoggedIn()) {
            consolePrinter.printCommentMenu();
            String choice = consoleReader.readText();
            
            switch (choice) {
                case "1" -> commentController.createComment();
                case "2" -> commentController.listComments();
                case "3" -> commentController.listMyComments();
                case "4" -> commentController.deleteComment();
                case "5" -> commentController.voteComment();
                case "0" -> open = false;
                default -> consolePrinter.printInvalidChoiceMessage();
            }
        }
    }
}
