package ui;

import controller.AuthController;
import controller.CommentController;
import controller.PostController;
import controller.SubredditController;
import repository.CommentRepository;
import repository.PostRepository;
import repository.SubredditRepository;
import repository.UserRepository;
import service.AuthService;
import service.CommentService;
import service.PostService;
import service.SubredditService;

public class Menu {

    private final AuthController authController;
    private final PostController postController;
    private final CommentController commentController;
    private final SubredditController subredditController;
    
    private final AuthService authService;
    private final ConsolePrinter consolePrinter;
    private final ConsoleReader consoleReader;

    public Menu() {
        consolePrinter = ConsolePrinter.getInstance();
        consoleReader = new ConsoleReader(consolePrinter);
        authService = AuthService.getInstance();

        authController = new AuthController(
                consoleReader,
                consolePrinter,
                authService
        );

        postController = new PostController(
                consoleReader,
                consolePrinter,
                PostService.getInstance(),
                PostRepository.getInstance(),
                UserRepository.getInstance(),
                SubredditRepository.getSubredditRepository(),
                authService
        );

        commentController = new CommentController(
                consoleReader,
                consolePrinter,
                CommentService.getInstance(),
                CommentRepository.getInstance(),
                PostRepository.getInstance(),
                UserRepository.getInstance(),
                authService
        );

        subredditController = new SubredditController(
                consoleReader,
                consolePrinter,
                SubredditService.getSubredditService(),
                SubredditRepository.getSubredditRepository(),
                UserRepository.getInstance(),
                authService
        );
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
            case "4" -> {
                authService.logout();
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
                case "0" -> open = false;
                default -> consolePrinter.printInvalidChoiceMessage();
            }
        }
    }
}