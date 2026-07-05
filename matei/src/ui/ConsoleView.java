package ui;

import model.Comment;
import model.Post;
import model.Subreddit;
import model.User;

import java.util.List;

public class ConsoleView {
    public void ShowGuestMenu(){
        System.out.println("=".repeat(40));
        System.out.println("Welcome to Cnytez's Reddit!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("Choose an option (0-2): ");
    }


}
