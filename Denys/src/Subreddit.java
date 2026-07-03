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

    private List<Integer> postIdList;
    private List<Integer> userIdList;

    public Subreddit(int id, String name, String photo, String banner, int ownerId, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.banner = banner;
        this.ownerId = ownerId;
        this.creationDate = creationDate;

        postIdList = new ArrayList<>();
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

    public List<Integer> getPostIdList() {
        return postIdList;
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
}