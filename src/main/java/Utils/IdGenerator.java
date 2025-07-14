package Utils;
import static Utils.FileHandler.loadIds;
import static Utils.FileHandler.saveIds;

public class IdGenerator {
    public static int userId ;
    public static int questionId;
    public static int answerId;

    public int nextUserId() {
        loadIds();
        userId++;
        saveIds(userId, questionId, answerId);
        return userId;
    }

    public int nextQuestionId() {
        loadIds();
        questionId++;
        saveIds(userId, questionId, answerId);
        return questionId;
    }

    public int nextAnswerId() {
        loadIds();
        answerId++;
        saveIds(userId, questionId, answerId);
        return answerId;
    }
}
