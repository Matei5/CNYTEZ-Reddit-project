package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment {
    private int ID;
    private int parentPostID;
    private int parentCommentID;
    private int ownerID;
    private LocalDateTime creationDate;
    private String title;
    private String text;
    private String image;
    private List<Integer> childCommentIDs;

    public enum VoteType {
        UPVOTE,
        DOWNVOTE
    }

    private Map<Integer, VoteType> userIdVoteMap;

    public Comment(int id, int parentPostID, int parentCommentID, int ownerID,
                   String title, String text, String image) {
        this.ID = id;
        this.parentPostID = parentPostID;
        this.parentCommentID = parentCommentID;
        this.ownerID = ownerID;
        this.creationDate = LocalDateTime.now();
        this.userIdVoteMap = new HashMap<Integer, VoteType>();
        this.childCommentIDs = new ArrayList<Integer>();
        this.title = title;
        this.text = text;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public int getParentPostID() {
        return parentPostID;
    }

    public int getParentCommentID() {
        return parentCommentID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public List<Integer> getChildCommentIDs() {
        return childCommentIDs;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setParentPostID(int parentPostID) {
        this.parentPostID = parentPostID;
    }

    public void setParentCommentID(int parentCommentID) {
        this.parentCommentID = parentCommentID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void addChildCommentID(int childCommentID) {
        this.childCommentIDs.add(childCommentID);
    }

    public void removeChildCommentID(int childCommentID) {
        this.childCommentIDs.remove(childCommentID);
    }

    public void vote(int userID, VoteType voteType) {
        if (userIdVoteMap.containsKey(userID)) {
            VoteType existingVote = userIdVoteMap.get(userID);

            if (existingVote == voteType) {
                userIdVoteMap.remove(userID);
            } else {
                userIdVoteMap.put(userID, voteType);
            }
        } else {
            userIdVoteMap.put(userID, voteType);
        }
    }

    public int getUpvotesCount() {
        int count = 0;

        for (VoteType voteType : userIdVoteMap.values()) {
            if (voteType == VoteType.UPVOTE) {
                count++;
            }
        }

        return count;
    }

    public int getDownvotesCount() {
        int count = 0;

        for (VoteType voteType : userIdVoteMap.values()) {
            if (voteType == VoteType.DOWNVOTE) {
                count++;
            }
        }

        return count;
    }

    public int getScore() {
        return getUpvotesCount() - getDownvotesCount();
    }

}
