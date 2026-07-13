package repository;

import model.Subreddit;

import java.util.List;

public interface SubredditRepository {
    void addSubreddit(Subreddit sub);

    Subreddit getSubredditById(int id);

    Subreddit getSubredditByName(String name);

    List<Subreddit> getAllSubreddits();

    void update(Subreddit sub);
}
