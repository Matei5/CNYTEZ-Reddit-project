package service;

import log.LogManager;
import model.Comment;
import model.Post;
import model.User;
import repository.CommentRepository;
import repository.InMemoryCommentRepository;
import repository.InMemoryPostRepository;
import repository.PostRepository;

public class CommentService {
    private static CommentService instance;

    private static int currentId;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentService() {
        this.commentRepository = InMemoryCommentRepository.getInstance();
        this.postRepository = InMemoryPostRepository.getInstance();
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

        Post post = postRepository.findById(parentPostID);
        if (post == null) {
            LogManager.getInstance().log(
                "Create comment failed! User with id " + loggedUser.getId() +
                " tried to comment on post with id " + parentPostID + " that doesn't exist"
            );

            return false;
        }

        if (parentCommentID != 0 && commentRepository.findById(parentCommentID) == null) {
            LogManager.getInstance().log(
                "Create comment failed! User with id " + loggedUser.getId() +
                " tried to reply to comment with id " + parentCommentID + " that doesn't exist"
            );

            return false;
        }

        int ownerId = loggedUser.getId();

        Comment comment = new Comment(currentId++, parentPostID, parentCommentID, ownerId, title, content, image);

        commentRepository.addComment(comment);

        if (parentCommentID != 0) {
            Comment parentComment = commentRepository.findById(parentCommentID);
            parentComment.addChildCommentID(comment.getID());
        }
        LogManager.getInstance().log(
            "Create comment success! User with id " + loggedUser.getId() +
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

        Comment comment = commentRepository.findById(id);
        if (comment == null) {
            LogManager.getInstance().log(
                "Delete comment failed! User with id " + loggedUser.getId() +
                " tried to delete comment with id " + id + " that doesn't exist"
            );

            return false;
        }

        if (comment.getOwnerID() != loggedUser.getId()) {
            LogManager.getInstance().log(
                "Delete comment failed! User with id " + loggedUser.getId() +
                " is not the owner of comment with id " + id
            );

            return false;
        }

        Comment parentComment = commentRepository.findById(comment.getParentCommentID());

        if (parentComment != null) {
            parentComment.removeChildCommentID(comment.getID());
        }

        commentRepository.removeById(id);

        LogManager.getInstance().log(
            "Delete comment success! User with id " + loggedUser.getId() +
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
