package repository;

import model.Subreddit;

import java.util.ArrayList;
import java.util.List;

public class InMemorySubredditRepository implements SubredditRepository {
    private static InMemorySubredditRepository instance;
    private final List<Subreddit> subredditList;

    private InMemorySubredditRepository() {
        subredditList = new ArrayList<>();
    }

    public static InMemorySubredditRepository getInstance() {
        if (instance == null) {
            instance = new InMemorySubredditRepository();
        }

        return instance;
    }

    @Override
    public void addSubreddit(Subreddit sub) {
        subredditList.add(sub);
    }

    @Override
    public Subreddit getSubredditById(int id) {
        for (Subreddit sub : subredditList) {
            if (sub.getId() == id)
                return sub;
        }
        return null;
    }

    @Override
    public Subreddit getSubredditByName(String name) {
        for (Subreddit sub : subredditList) {
            if (sub.getName().equals(name))
                return sub;
        }
        return null;
    }

    @Override
    public List<Subreddit> getAllSubreddits() {
        return subredditList;
    }
}
