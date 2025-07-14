package repository;

import Utils.FileHandler;

import static Utils.Constants.FROM_TO_FILE;

import java.util.ArrayList;

public class FromToRepository {

    public static ArrayList<String> fromTo;

    public static void setMapper() {
        FileHandler fileHandler = new FileHandler();
        fromTo = fileHandler.loadFile(FROM_TO_FILE);
    }
}
