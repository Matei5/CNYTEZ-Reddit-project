package service;

import log.LogManager;
import model.Comment;
import model.Post;
import model.User;
import repository.CommentRepository;
import repository.PostRepository;
import repository.UserRepository;

import java.util.List;

public class UserService {
    private static UserService instance;

    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
        postRepository = PostRepository.getInstance();
        commentRepository = CommentRepository.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    public boolean deleteUser(int userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            LogManager.getInstance().log("Delete user failed! User with id" + userId + " doesn't exist");

            return false;
        }

        userRepository.deleteById(userId);
        LogManager.getInstance().log("Delete user succes! User with id" + userId + " deleted");

        return true;
    }

    public int calculateKarma(int userId) {
        int karma = 0;

        List<Post> posts = postRepository.getPostsByUser(userId);
        for (Post post : posts) {
            karma += post.getScore();
        }

        List<Comment> comments = commentRepository.getCommentsByUser(userId);
        for (Comment comment : comments) {
            karma += comment.getScore();
        }

        return karma;
    }
}
