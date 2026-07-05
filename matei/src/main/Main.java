import repository.CommentRepository;
import repository.PostRepository;
import repository.SubredditRepository;
import repository.UserRepository;

import service.AuthService;
import service.CommentService;
import service.PostService;
import service.SubredditService;

import ui.Menu;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        SubredditRepository subredditRepository = new SubredditRepository();
        PostRepository postRepository = new PostRepository();
        CommentRepository commentRepository = new CommentRepository();

        AuthService authService = new AuthService(userRepository);
        SubredditService subredditService = new SubredditService(subredditRepository);
        PostService postService = new PostService(postRepository);
        CommentService commentService = new CommentService(commentRepository);

        Menu menu = new Menu(
                authService,
                subredditService,
                postService,
                commentService
        );

        menu.start();
    }
}