package repository;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Subreddit;
import log.LogManager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class JsonFileSubredditRepository implements SubredditRepository {

    private static JsonFileSubredditRepository instance;
    private static final String FILE_PATH = "data/subreddits.json";

    private final List<Subreddit> subredditList;
    private final Gson gson;

    private JsonFileSubredditRepository() {
        gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        subredditList = loadFromFile();
    }

    public static JsonFileSubredditRepository getInstance() {
        if (instance == null) {
            instance = new JsonFileSubredditRepository();
        }

        return instance;
    }

    private List<Subreddit> loadFromFile(){
        File file = new File(FILE_PATH);
        if(!file.exists()){ return new ArrayList<>(); }

        try (Reader reader = new BufferedReader(new FileReader(file))){
            Type listType = new TypeToken<ArrayList<Subreddit>>(){}.getType();
            List<Subreddit> subreddits = gson.fromJson(reader,listType);
            if(subreddits.isEmpty()){ return new ArrayList<>(); }
            else { return subreddits; }
        } catch (IOException e){
            LogManager.getInstance().log("Failed to load subreddits: " + e.getMessage());
            return new ArrayList<>();

        }
    }

    private void saveToFile(){
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (Writer writer = new BufferedWriter(new FileWriter(file))){
            gson.toJson(subredditList, writer);
        } catch (IOException e){
            LogManager.getInstance().log("Failed to save subreddits: " + e.getMessage());
        }
    }

    @Override
    public void addSubreddit(Subreddit subreddit) {
        subredditList.add(subreddit);
        saveToFile();
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

    public void update(Subreddit subreddit) {
        saveToFile();
    }

}
