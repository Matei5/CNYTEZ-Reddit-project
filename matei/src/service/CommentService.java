package service;

import model.Comment;
import repository.CommentRepository;

public class CommentService {
    private static CommentService instance;

    private static int currentId;
    private CommentRepository commentRepository;

    public CommentService() {
        this.repo = CommentRepository.getInstance();
        currentId = 1;
    }

    public static CommentService getInstance() {
        if (instance == null) {
            instance = new CommentService();
        }
        return instance;
    }

    public boolean createComment(int parentPostID, int parentCommentID, int ownerId,
                              String title, String content,String image) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null) {
            return false;
        }

        int ownerId = loggedUser.getId();

        Comment comment = new Comment(currentId++, parentPostID, parentCommentID, ownerId, title, content, image);

        commentRepository.addComment(comment);
        return true;
    }
}
