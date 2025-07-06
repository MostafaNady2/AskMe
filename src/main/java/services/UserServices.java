package services;
import Utils.FileHandler;
import models.User;
import java.util.*;
import static services.QuestionServices.answerByQid;

public class UserServices {

    public static ArrayList<String> users;
    public static ArrayList<String> fromTo;

    public static void setUsers() {
        FileHandler fileHandler = new FileHandler();
        users = fileHandler.loadFile("./src/main/resources/users.txt");
    }

    public static void setMapper() {
        FileHandler fileHandler = new FileHandler();
        fromTo = fileHandler.loadFile("./src/main/resources/from_to.txt");
    }

    public void listUsers(User exceptMe) {
        setUsers();
        setMapper();

        if (users == null || users.isEmpty()) {
            System.out.println("No users available");
            return;
        }

        System.out.println("────────────────────── System Users ─────────────────────");

        for (String userLine : UserServices.users) {
            String[] line = userLine.split(";");
            if (line.length < 4) continue;

            int userId = Integer.parseInt(line[0]);
            String username = line[3];

            if ( exceptMe != null && userId == exceptMe.getId()) {
                continue;
            }

            System.out.println("username : " + username + " | user id : " + userId);
        }

        System.out.println("───────────────────────────────────────────");
    }

    public void printToUser(User user) {

        if (user == null || users.isEmpty() ||
                QuestionServices.questions == null || QuestionServices.questions.isEmpty() ||
                UserServices.fromTo == null || UserServices.fromTo.isEmpty()) {
            System.out.println("───────────────────────────────────────────");
            System.out.println("No Questions Asked");
            System.out.println("───────────────────────────────────────────");
            return;
        }
        boolean found = false;

        int targetUserId = user.getId();
        ArrayList<String> map = UserServices.fromTo;
        ArrayList<String> questions = QuestionServices.questions;

        // Map questionId -> questionText
        Map<String, String> questionMap = new HashMap<>();
        Map<String,String> isAnonymous = new HashMap<>();
        for (String qLine : questions) {
            String[] qParts = qLine.split(";");
            if (qParts.length > 1) {
                questionMap.put(qParts[0], qParts[1]);
            }
            if(qParts.length > 2){
                isAnonymous.put(qParts[0], qParts[2]);
            }
        }

        for (String mapping : map) {
            String[] parts = mapping.split(";");
            if (parts.length < 3) continue;

            int fromId = Integer.parseInt(parts[0]);
            int toId = Integer.parseInt(parts[1]);
            String questionId = parts[2];

            if (toId == targetUserId) {
                String fromUsername;
                if(isAnonymous.get(questionId).equals("true")){
                    fromUsername = "Anonymous";
                }else{
                    fromUsername = UserServices.getUsername(fromId);
                }
                String questionText = questionMap.get(questionId);

                if (questionText != null) {
                    found = true;
                    System.out.println("From: " + fromUsername + " | Question Id : [" + questionId + "]" + " | Question: " + questionText);
                    System.out.print("    └──  Answer : " );
                    String ans= answerByQid.get(questionId);
                    System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                }
            }
        }
        if (!found) {
            System.out.println("───────────────────────────────────────────");
            System.out.println("No Questions Asked to you");
            System.out.println("───────────────────────────────────────────");
        }
    }

    public void printFromUser(User user) {
        if (user == null ||
                QuestionServices.questions == null || QuestionServices.questions.isEmpty() ||
                UserServices.fromTo == null || UserServices.fromTo.isEmpty()) {
            System.out.println("───────────────────────────────────────────");
            System.out.println("No Questions Asked");
            System.out.println("───────────────────────────────────────────");
            return;
        }
        boolean found = false;

        ArrayList<String> map = UserServices.fromTo;
        ArrayList<String> questions = QuestionServices.questions;

        // Build a map for faster lookup
        Map<String, String> questionMap = new HashMap<>();
        for (String qLine : questions) {
            String[] qParts = qLine.split(";");
            if (qParts.length > 1) {
                questionMap.put(qParts[0], qParts[1]);
            }
        }

        for (String entry : map) {
            String[] parts = entry.split(";");
            if (parts.length < 3) {
                continue;
            }

            int fromId = Integer.parseInt(parts[0]);
            int toId = Integer.parseInt(parts[1]);
            String questionId = parts[2];

            if (fromId == user.getId()) {
                String toUsername = UserServices.getUsername(toId);
                String questionText = questionMap.get(questionId);

                if (questionText != null) {
                    found = true;
                    System.out.println("To : " + toUsername + " | Question Id : [" + questionId + "]" + " | Question : " + questionText);
                    System.out.print("    └──  Answer : " );
                    String ans= answerByQid.get(questionId);
                    System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                }
            }
        }
        if (!found) {
            System.out.println("───────────────────────────────────────────");
            System.out.println("No Questions Asked by you");
        }
        System.out.println("───────────────────────────────────────────");
    }


    public static String getUsername(int id) {
        ArrayList<String> username = users;
        String[] array;
        for (String username1 : username) {
            array = username1.split(";");
            if (Integer.parseInt(array[0]) == id) {
                return array[3];
            }
        }
        return null;
    }

    public static ArrayList<String> getQuestionsToMe(int id) { // get questions sent to me
        ArrayList<String> friends = new ArrayList<>();
        String[] array;
        for (String username : UserServices.fromTo) {
            array = username.split(";");
            if (array.length > 1 && array[1].equals(id + "")) {
                friends.add(array[0] + ";" + array[2]);
            }
        }
        return friends;
    }

    public static ArrayList<Integer> getQuestionByMe(int id) {
        ArrayList<Integer> questions = new ArrayList<>();
        String[] array;
        for (String username1 : UserServices.fromTo) {
            array = username1.split(";");
            if (array[0].equals(id + "")) {
                questions.add(Integer.parseInt(array[2]));
            }
        }
        return questions;
    }
}
