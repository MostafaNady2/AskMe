package services;
import Utils.FileHandler;
import models.User;

import java.io.File;
import java.util.List;

public class SessionServices {

    public static User signIn(String username, String password){
        List<String> usernames = UserServices.users;
        String[] split;
        for (String s : usernames){
            split = s.split(";");
            if(split[3].equals(username) && split[4].equals(password)){
                return new User(Integer.parseInt(split[0]),split[1],split[2],split[3],split[4]);
            }
        }
        return null;
    }
    public static void initializeInMemoryDatabase(){
        UserServices.setUsers();
        QuestionServices.setQuestions();
        UserServices.setMapper();
        QuestionServices.setAnswers();
        QuestionServices.setAnswerByQid();
    }
    public static void startSession(){
        FileHandler.createFiles();
        SessionServices.initializeInMemoryDatabase();
        File file = new File("./src/main/resources/ids.txt");
        if (file.exists() && file.length() == 0){
            FileHandler.saveIds(1,1,1);
        }
    }
}
