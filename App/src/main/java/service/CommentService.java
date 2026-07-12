package service;

import log.LogManager;
import model.Comment;
import model.User;
import repository.CommentRepository;
import repository.InMemoryCommentRepository;

public class CommentService {
    private static CommentService instance;

    private static int currentId;
    private CommentRepository commentRepository;

    public CommentService() {
        this.commentRepository = InMemoryCommentRepository.getInstance();
        currentId = 1;
    }

    public static CommentService getInstance() {
        if (instance == null) {
            instance = new CommentService();
        }
        return instance;
    }

    public boolean createComment(int parentPostID, int parentCommentID,
                              String title, String content,String image) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Create comment failed! No user was logged in");

            return false;
        }

        int ownerId = loggedUser.getId();

        Comment comment = new Comment(currentId++, parentPostID, parentCommentID, ownerId, title, content, image);

        commentRepository.addComment(comment);
        LogManager.getInstance().log(
            "Create comment success! User with id " + loggedUser +
            " created comment with id " + comment.getID()
        );

        return true;
    }

    public boolean deleteComment(int id) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Delete comment failed! No user was logged in");

            return false;
        }

        commentRepository.removeById(id);

        LogManager.getInstance().log(
            "Create comment success! User with id " + loggedUser +
            " deleted comment with id " + id
        );

        return true;
    }

    public boolean voteComment(int id, Comment.VoteType voteType) {
        User loggedUser = AuthService.getInstance().getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Vote comment failed! No user was logged in");

            return false;
        }

        Comment comment = commentRepository.findById(id);
        if (comment == null) {
            LogManager.getInstance().log(
                "Vote comment failed! User with id " + loggedUser.getId() +
                " tried to vote comment with id " + id + " that doesn't exist"
            );

            return false;
        }

        comment.vote(loggedUser.getId(), voteType);

        LogManager.getInstance().log(
            "Vote comment success! User with id " + loggedUser.getId() +
            " voted comment with id " + id
        );

        return true;
    }
}
