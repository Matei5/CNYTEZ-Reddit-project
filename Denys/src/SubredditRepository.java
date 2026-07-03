import java.util.ArrayList;
import java.util.List;

public class SubredditRepository {
    private List<Subreddit> subredditList;

    public SubredditRepository() {
        subredditList = new ArrayList<>();
    }

    public void AddSubreddit(Subreddit sub) {
        subredditList.add(sub);
    }

    public Subreddit getSubredditById(int id) {
        for (Subreddit sub : subredditList) {
            if (sub.getId() == id)
                return sub;
        }
        return null;
    }

    public Subreddit getSubredditByName(String name) {
        for (Subreddit sub : subredditList) {
            if (sub.getName() == name)
                return sub;
        }
        return null;
    }

    public List<Subreddit> getAllSubreddits() {
        return subredditList;
    }
}