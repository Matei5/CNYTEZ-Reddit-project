package service;

import exceptions.repository.RepositoryException;
import exceptions.service.ServiceException;
import log.LogManager;
import model.Comment;
import model.Post;
import model.User;
import repository.CommentRepository;
import repository.PostRepository;

public class CommentService {
    private int currentId;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authService = authService;
        this.currentId = 1;
    }

    public boolean createComment(int parentPostID, int parentCommentID,
                              String title, String content,String image) throws ServiceException, RepositoryException {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            throw new ServiceException("Create comment failed! No user was logged in");
        }

        Post post = postRepository.findById(parentPostID);
        if (post == null) {
            throw new ServiceException(
                "Create comment failed! User with id " + loggedUser.getId() +
                " tried to comment on post with id " + parentPostID + " that doesn't exist"
            );
        }

        if (parentCommentID != 0 && commentRepository.findById(parentCommentID) == null) {
            throw new ServiceException(
                "Create comment failed! User with id " + loggedUser.getId() +
                " tried to reply to comment with id " + parentCommentID + " that doesn't exist"
            );
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

    public boolean deleteComment(int id) throws ServiceException, RepositoryException {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            throw new ServiceException("Delete comment failed! No user was logged in");
        }

        Comment comment = commentRepository.findById(id);
        if (comment == null) {
            throw new ServiceException(
                "Delete comment failed! User with id " + loggedUser.getId() +
                " tried to delete comment with id " + id + " that doesn't exist"
            );
        }

        if (comment.getOwnerID() != loggedUser.getId()) {
            throw new ServiceException(
                "Delete comment failed! User with id " + loggedUser.getId() +
                " is not the owner of comment with id " + id
            );
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

    public boolean voteComment(int id, Comment.VoteType voteType) throws ServiceException, RepositoryException {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            throw new ServiceException("Vote comment failed! No user was logged in");
        }

        Comment comment = commentRepository.findById(id);
        if (comment == null) {
            throw new ServiceException(
                "Vote comment failed! User with id " + loggedUser.getId() +
                " tried to vote comment with id " + id + " that doesn't exist"
            );
        }

        comment.vote(loggedUser.getId(), voteType);

        LogManager.getInstance().log(
            "Vote comment success! User with id " + loggedUser.getId() +
            " voted comment with id " + id
        );

        return true;
    }
}
