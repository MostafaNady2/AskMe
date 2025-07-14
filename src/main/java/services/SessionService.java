package services;
import Utils.FileHandler;
import models.User;
import repository.AnswersRepository;
import repository.FromToRepository;
import repository.QuestionRepository;
import repository.UserRepository;
import static Utils.Constants.*;
import java.io.File;
import java.util.List;

public class SessionService {

    public static User signIn(String username, String password){
        List<String> usernames = UserRepository.users;
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
        UserRepository.setUsers();
        QuestionRepository.setQuestions();
        FromToRepository.setMapper();
        AnswersRepository.setAnswers();
        AnswersRepository.setAnswerByQid();
    }
    public static void startSession(){
        FileHandler.createFiles();
        SessionService.initializeInMemoryDatabase();
        File file = new File(IDS_FILE);
        if (file.exists() && file.length() == 0){
            FileHandler.saveIds(1,1,1);
        }
    }
}
