package service;

import exceptions.repository.RepositoryException;
import exceptions.service.ServiceException;
import log.LogManager;
import model.Comment;
import model.Post;
import model.User;
import repository.CommentRepository;
import repository.PostRepository;
import repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public UserService(
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public boolean deleteUser(int userId) throws ServiceException, RepositoryException {
        User user = userRepository.findById(userId);

        if (user == null) {
            throw new ServiceException("Delete user failed! User with id" + userId + " doesn't exist");
        }

        userRepository.deleteById(userId);
        LogManager.getInstance().log("Delete user success! User with id" + userId + " deleted");

        return true;
    }

    public int calculateKarma(int userId) throws RepositoryException {
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
