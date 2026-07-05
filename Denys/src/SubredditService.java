import java.time.LocalDateTime;

public class SubredditService {
    private int nextSubredditId;

    private SubredditRepository subRepository = SubredditRepository.getSubredditRepository();

    private static SubredditService subService = new SubredditService();

    private SubredditService() {
        nextSubredditId = 1;
    }

    public static SubredditService getSubredditService() {
        return subService;
    }

    public boolean createSubreddit(String name, String photo, String banner) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null)
            return false;

        if (subRepository.getSubredditByName(name) != null)
            return false;
        int ownerId = loggedUser.getId();
        LocalDateTime creationDate = LocalDateTime.now();

        Subreddit sub = new Subreddit(nextSubredditId,name, photo, banner, ownerId, creationDate);
        nextSubredditId++;

        sub.addFollower(loggedUser.getId());

        subRepository.addSubreddit(sub);
        return true;
    }

    public boolean changeOwner(String username, int id) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null)
            return false;

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null)
            return false;

        if (loggedUser.getId() != sub.getOwnerId())
            return false;

        User newOwner = User.findByUsername(username);
        if (newOwner == null)
            return false;

        sub.setOwnerId(newOwner.getId());
        sub.addFollower(newOwner.getId());
        return true;
    }

    public boolean joinSubreddit(int id) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null)
            return false;

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null)
            return false;

        boolean hasJoined = sub.addFollower(loggedUser.getId());
        if (!hasJoined)
            return false;

        return true;
    }

    public boolean leaveSubreddit(int id) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null)
            return false;

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null)
            return false;

        if (loggedUser.getId() == sub.getOwnerId()) // Owner needs to change subreddit ownership before leaving
            return false;

        boolean hasLeft = sub.removeFollower(loggedUser.getId());
        if (!hasLeft)
            return false;
        return true;
    }

    public boolean changePhoto(String photo, int id) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null)
            return false;

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null)
            return false;

        if (loggedUser.getId() != sub.getOwnerId())
            return false;

        sub.setPhoto(photo);
        return true;
    }

    public boolean changeBanner(String banner, int id) {
        User loggedUser = AuthService.getLoggedInUser();
        if (loggedUser == null)
            return false;

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null)
            return false;

        if (loggedUser.getId() != sub.getOwnerId())
            return false;

        sub.setBanner(banner);
        return true;
    }
}
