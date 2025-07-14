package repository;

import Utils.FileHandler;

import static Utils.Constants.QUESTIONS_FILE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionRepository {
    public static ArrayList<String> questions;
    public static ArrayList<String> answers;
    public static Map<String, String> answerByQid = new HashMap<>();

    public static void setQuestions(){
        FileHandler fileHandler = new FileHandler();
        questions = fileHandler.loadFile(QUESTIONS_FILE);
    }

}
