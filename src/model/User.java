package src.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String username;
    private String profilePhoto;
    private String email;
    private String password;

    private List<Integer> ownPostIds;
    private List<Integer> followedSubredditIds;
    private List<Integer> commentIds;
    private List<Integer> upvotedPostIds;
    private List<Integer> downvotedPostIds;

    public User(int id,
                String name,
                String username,
                String profilePhoto,
                String email,
                String password) {

        this.id = id;
        this.name = name;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.email = email;
        this.password = password;

        ownPostIds = new ArrayList<>();
        followedSubredditIds = new ArrayList<>();
        commentIds = new ArrayList<>();
        upvotedPostIds = new ArrayList<>();
        downvotedPostIds = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public List<Integer> getOwnPostIds() {
        return ownPostIds;
    }

    public List<Integer> getFollowedSubredditIds() {
        return followedSubredditIds;
    }

    public List<Integer> getCommentIds() {
        return commentIds;
    }

    public List<Integer> getUpvotedPostIds() {
        return upvotedPostIds;
    }

    public List<Integer> getDownvotedPostIds() {
        return downvotedPostIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}