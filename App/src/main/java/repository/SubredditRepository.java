package repository;

import model.Subreddit;

import java.util.ArrayList;
import java.util.List;

public class SubredditRepository {
    private List<Subreddit> subredditList;

    private static SubredditRepository subRepository = new SubredditRepository();

    private SubredditRepository() {
        subredditList = new ArrayList<>();
    }

    public static SubredditRepository getSubredditRepository() {
        return subRepository;
    }

    public void addSubreddit(Subreddit sub) {
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
            if (sub.getName().equals(name))
                return sub;
        }
        return null;
    }

    public List<Subreddit> getAllSubreddits() {
        return subredditList;
    }
}