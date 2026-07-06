package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Subreddit {
    private int id;
    private String name;
    private String photo;
    private String banner;
    private int ownerId;
    private LocalDateTime creationDate;

    private List<Integer> userIdList;

    public Subreddit(int id, String name, String photo, String banner, int ownerId, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.banner = banner;
        this.ownerId = ownerId;
        this.creationDate = creationDate;

        userIdList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBanner() {
        return banner;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public List<Integer> getUserIdList() {
        return userIdList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean addFollower(int userId) {
        if (!userIdList.contains(userId)) {
            this.userIdList.add(userId);
            return true;
        }
        return false;
    }

    public boolean removeFollower(int userId) {
        int followerIndex = this.userIdList.indexOf(userId);
        if (followerIndex != -1) {
            userIdList.remove(followerIndex);
            return true;
        }
        return false;
    }
}