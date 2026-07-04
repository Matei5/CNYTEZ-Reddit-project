import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Post {
    private int id;

    private String title, text, image;
    private LocalDateTime creationDate;

    private int subredditId, ownerId;
    private List<Integer> commentIdList;

    public enum VoteType {
        UPVOTE,
        DOWNVOTE
    }

    private Map<Integer, VoteType> userIdVotesMap;

    public Post(int id, String title, String text, String image, LocalDateTime creationDate, int subredditId, int ownerId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image = image;
        this.creationDate = creationDate;
        this.subredditId = subredditId;
        this.ownerId = ownerId;

        commentIdList = new ArrayList<>();
        userIdVotesMap = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getSubredditId() {
        return subredditId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public List<Integer> getCommentIdList() {
        return commentIdList;
    }

    public Map<Integer, VoteType> getUserIdVotesMap() {
        return userIdVotesMap;
    }
}
