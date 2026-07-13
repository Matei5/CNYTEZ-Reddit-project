package controller;

import exceptions.repository.RepositoryException;
import log.LogManager;
import model.Comment;
import model.User;
import repository.CommentRepository;
import repository.UserRepository;
import service.AuthService;
import service.CommentService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

import java.util.ArrayList;
import java.util.List;

public class CommentController {
    private final ConsoleReader consoleReader;
    private final ConsolePrinter consolePrinter;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public CommentController(
            CommentService commentService,
            CommentRepository commentRepository,
            UserRepository userRepository,
            AuthService authService
    ) {
        this.consoleReader = ConsoleReader.getInstance();
        this.consolePrinter = ConsolePrinter.getInstance();
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public void createComment() {
        consolePrinter.printHeader("Create comment");

        int postId = consoleReader.readInt("Parent post id: ");
        int parentCommentId = consoleReader.readInt(
                "Parent comment id (0 for a direct post comment): "
        );

        String title = consoleReader.readText("Title: ");
        String text = consoleReader.readText("Text: ");
        String image = consoleReader.readText("Image path: ");

        boolean success = false;

        try {
            success = commentService.createComment(
                    postId,
                    parentCommentId,
                    title,
                    text,
                    image
            );
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Comment created.");
        } else {
            consolePrinter.printMessage("Could not create comment. Make sure the ids are correct");
        }
    }

    public void listComments() {
        consolePrinter.printHeader("Comments");

        List<Comment> comments = new ArrayList<>();

        try {
            comments = commentRepository.getComments();
        } catch (RepositoryException e) {
            LogManager.getInstance().log(e.getMessage());
        }

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
        List<Comment> comments = new ArrayList<>();

        try {
            comments = commentRepository.getCommentsByUser(user.getId());
        } catch (RepositoryException e) {
            LogManager.getInstance().log(e.getMessage());
        }

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

        boolean success = false;

        try {
            success = commentService.deleteComment(commentId);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Comment deleted.");
        } else {
            consolePrinter.printMessage("Could not delete comment. It may not exist or you are not the owner");
        }
    }

    public void voteComment() {
        consolePrinter.printHeader("Vote comment");

        int commentId = consoleReader.readInt("Comment id: ");

        consolePrinter.printVoteMenu();
        String choice = consoleReader.readText();
        Comment.VoteType voteType;

        switch (choice) {
            case "1" -> voteType = Comment.VoteType.UPVOTE;
            case "2" -> voteType = Comment.VoteType.DOWNVOTE;
            case "0" -> {
                return;
            }
            default -> {
                consolePrinter.printInvalidChoiceMessage();
                return;
            }
        }

        boolean success = false;

        try {
            success = commentService.voteComment(commentId, voteType);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Vote saved. Repeating the same vote removes it.");
        } else {
            consolePrinter.printMessage("Could not save vote.");
        }
    }

    private void printComment(Comment comment) {
        User owner = null;
        try {
            owner = userRepository.findById(comment.getOwnerID());
        } catch (RepositoryException e) {
            LogManager.getInstance().log(e.getMessage());
        }
        String username = owner == null ? "unknown" : owner.getUsername();
        consolePrinter.printComment(comment, username);
    }
}
