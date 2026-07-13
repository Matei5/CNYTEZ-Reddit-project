import controller.AuthController;
import controller.CommentController;
import controller.PostController;
import controller.SubredditController;
import controller.UserController;
import log.ConsoleLogger;
import log.FileLogger;
import log.LogManager;
import repository.*;
import service.AuthService;
import service.CommentService;
import service.PostService;
import service.SubredditService;
import service.UserService;
import ui.ConsolePrinter;
import ui.ConsoleReader;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        LogManager.getInstance().addLogger(ConsoleLogger.getInstance());
        LogManager.getInstance().addLogger(FileLogger.getInstance());

        UserRepository userRepository = JsonFileUserRepository.getInstance();
        PostRepository postRepository = JsonFilePostRepository.getInstance();
        CommentRepository commentRepository = JsonFileCommentRepository.getInstance();
        SubredditRepository subredditRepository = JsonFileSubredditRepository.getInstance();

        AuthService authService = new AuthService(userRepository);
        PostService postService = new PostService(postRepository, subredditRepository, authService);
        CommentService commentService = new CommentService(commentRepository, postRepository, authService);
        SubredditService subredditService = new SubredditService(subredditRepository, userRepository, authService);
        UserService userService = new UserService(userRepository, postRepository, commentRepository);

        AuthController authController = new AuthController(authService);
        PostController postController = new PostController(
                postService,
                postRepository,
                userRepository,
                subredditRepository,
                authService
        );
        CommentController commentController = new CommentController(
                commentService,
                commentRepository,
                userRepository,
                authService
        );
        SubredditController subredditController = new SubredditController(
                subredditService,
                subredditRepository,
                userRepository
        );
        UserController userController = new UserController(authService, userService);

        Menu menu = new Menu(
                authController,
                postController,
                commentController,
                subredditController,
                userController,
                authService,
                ConsolePrinter.getInstance(),
                ConsoleReader.getInstance()
        );

        menu.start();
    }
}
