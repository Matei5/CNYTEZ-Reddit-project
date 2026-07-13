package repository;

import exceptions.repository.RepositoryException;
import model.Subreddit;

import java.util.List;

public interface SubredditRepository {
    void addSubreddit(Subreddit sub) throws RepositoryException;

    Subreddit getSubredditById(int id) throws RepositoryException;

    Subreddit getSubredditByName(String name) throws RepositoryException;

    List<Subreddit> getAllSubreddits() throws RepositoryException;
}
