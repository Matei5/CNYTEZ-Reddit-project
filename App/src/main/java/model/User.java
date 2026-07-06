package model;

public class User {
    private int id;
    private String name;
    private String username;
    private String profilePhoto;
    private String email;
    private String password;

    public User(int id, String name, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePhoto = "";
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