package repository;

import Utils.FileHandler;

import static Utils.Constants.USERS_FILE;

import java.util.ArrayList;

public class UserRepository {
    public static ArrayList<String> users;
    public static void setUsers() {
        FileHandler fileHandler = new FileHandler();
        users = fileHandler.loadFile(USERS_FILE);
    }

}
