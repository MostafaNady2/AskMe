package repository;

import Utils.FileHandler;

import static Utils.Constants.ANSWERS_FILE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnswersRepository {
    public static ArrayList<String> answers;
    public static Map<String, String> answerByQid = new HashMap<>();

    public static void setAnswerByQid() {
        Map<String, String> answerMap = new HashMap<>();
        for (String answerLine : answers) {
            String[] parts = answerLine.split(";");
            if (parts.length >= 2) {
                answerMap.put(parts[0], parts[1]);
            }
        }
        answerByQid.clear();
        for (String questionLine : QuestionRepository.questions) {
            String[] parts = questionLine.split(";");
            if (parts.length >= 4) {
                String questionId = parts[0];
                String answerId = parts[3];
                String answerContent = answerMap.get(answerId);
                if (answerContent != null) {
                    answerByQid.put(questionId, answerContent);
                }
            }
        }
    }
    public static void setAnswers(){
        FileHandler fileHandler = new FileHandler();
        answers = fileHandler.loadFile(ANSWERS_FILE);
    }
}
