package controller;

import model.Comment;
import model.User;
import repository.CommentRepository;
import repository.PostRepository;
import repository.UserRepository;
import service.AuthService;
import service.CommentService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

import java.util.List;

public class CommentController {

    private final ConsoleReader consoleReader;
    private final ConsolePrinter consolePrinter;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public CommentController(
            ConsoleReader consoleReader,
            ConsolePrinter consolePrinter,
            CommentService commentService,
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            AuthService authService
    ) {
        this.consoleReader = consoleReader;
        this.consolePrinter = consolePrinter;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public void createComment() {
        consolePrinter.printHeader("Create comment");

        int postId = consoleReader.readInt("Parent post id: ");

        if (postRepository.findById(postId) == null) {
            consolePrinter.printMessage("Post not found.");
            return;
        }

        int parentCommentId = consoleReader.readInt(
                "Parent comment id (0 for a direct post comment): "
        );

        if (parentCommentId != 0 && commentRepository.findById(parentCommentId) == null) {
            consolePrinter.printMessage("Parent comment not found.");
            return;
        }

        String title = consoleReader.readText("Title: ");
        String text = consoleReader.readText("Text: ");
        String image = consoleReader.readText("Image path: ");

        boolean success = commentService.createComment(
                postId,
                parentCommentId,
                title,
                text,
                image
        );

        if (!success) {
            consolePrinter.printMessage("Could not create comment.");
            return;
        }

        Comment createdComment = getLastComment();

        if (createdComment != null && parentCommentId != 0) {
            Comment parentComment = commentRepository.findById(parentCommentId);
            if (parentComment != null) {
                parentComment.addChildCommentID(createdComment.getID());
            }
        }

        consolePrinter.printMessage("Comment created.");
    }

    public void listComments() {
        consolePrinter.printHeader("Comments");

        List<Comment> comments = commentRepository.getComments();

        if (comments.isEmpty()) {
            consolePrinter.printMessage("No comments yet.");
            return;
        }

        for (Comment comment : comments) {
            printComment(comment);
        }
    }

    public void listMyComments() {
        User user = authService.getLoggedInUser();

        consolePrinter.printHeader("My comments");

        List<Comment> comments = commentRepository.getCommentsByUser(user.getId());

        if (comments.isEmpty()) {
            consolePrinter.printMessage("You have not written any comments yet.");
            return;
        }

        for (Comment comment : comments) {
            printComment(comment);
        }
    }

    public void deleteComment() {
        consolePrinter.printHeader("Delete comment");

        int commentId = consoleReader.readInt("Comment id: ");
        Comment comment = commentRepository.findById(commentId);

        if (comment == null) {
            consolePrinter.printMessage("Comment not found.");
            return;
        }

        if (!isCurrentUserOwner(comment.getOwnerID())) {
            consolePrinter.printMessage("You can only delete your own comments.");
            return;
        }

        removeCommentFromParent(comment);

        boolean success = commentService.deleteComment(commentId);

        if (success) {
            consolePrinter.printMessage("Comment deleted.");
        } else {
            consolePrinter.printMessage("Could not delete comment.");
        }
    }

    private void printComment(Comment comment) {
        User owner = userRepository.findById(comment.getOwnerID());
        String username = owner == null ? "unknown" : owner.getUsername();
        consolePrinter.printComment(comment, username);
    }

    private boolean isCurrentUserOwner(int ownerId) {
        User currentUser = authService.getLoggedInUser();
        return currentUser != null && currentUser.getId() == ownerId;
    }

    private Comment getLastComment() {
        List<Comment> comments = commentRepository.getComments();
        if (comments.isEmpty()) {
            return null;
        }
        return comments.get(comments.size() - 1);
    }

    private void removeCommentFromParent(Comment comment) {
        int parentCommentId = comment.getParentCommentID();
        if (parentCommentId == 0) {
            return;
        }

        Comment parentComment = commentRepository.findById(parentCommentId);
        if (parentComment != null) {
            parentComment.removeChildCommentID(comment.getID());
        }
    }
}
