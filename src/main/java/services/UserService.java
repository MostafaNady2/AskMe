package services;
import models.User;
import repository.AnswersRepository;
import repository.FromToRepository;
import repository.QuestionRepository;
import repository.UserRepository;
import java.util.*;

import static Utils.Constants.SEPARATOR;


public class UserService {

    public void listUsers(User exceptMe) {
        UserRepository.setUsers();
        FromToRepository.setMapper();

        if (UserRepository.users == null || UserRepository.users.isEmpty()) {
            System.out.println("No users available");
            return;
        }

        System.out.println("────────────────────── System Users ─────────────────────");

        for (String userLine : UserRepository.users) {
            String[] line = userLine.split(";");
            if (line.length < 4) continue;

            int userId = Integer.parseInt(line[0]);
            String username = line[3];

            if ( exceptMe != null && userId == exceptMe.getId()) {
                continue;
            }

            System.out.println("username : " + username + " | user id : " + userId);
        }

        System.out.println(SEPARATOR);
    }

    public void printToUser(User user) {

        if (user == null || UserRepository.users.isEmpty() ||
                QuestionRepository.questions == null || QuestionRepository.questions.isEmpty() ||
                FromToRepository.fromTo == null || FromToRepository.fromTo.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No Questions Asked");
            System.out.println(SEPARATOR);
            return;
        }
        boolean found = false;

        int targetUserId = user.getId();
        ArrayList<String> map = FromToRepository.fromTo;
        ArrayList<String> questions = QuestionRepository.questions;

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
                    fromUsername = UserService.getUsername(fromId);
                }
                String questionText = questionMap.get(questionId);

                if (questionText != null) {
                    found = true;
                    System.out.println("From: " + fromUsername + " | Question Id : [" + questionId + "]" + " | Question: " + questionText);
                    System.out.print("    └──  Answer : " );
                    String ans= AnswersRepository.answerByQid.get(questionId);
                    System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                }
            }
        }
        if (!found) {
            System.out.println(SEPARATOR);
            System.out.println("No Questions Asked to you");
            System.out.println(SEPARATOR);
        }
    }

    public void printFromUser(User user) {
        if (user == null ||
                QuestionRepository.questions == null || QuestionRepository.questions.isEmpty() ||
                FromToRepository.fromTo == null || FromToRepository.fromTo.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No Questions Asked");
            return;
        }
        boolean found = false;

        ArrayList<String> map = FromToRepository.fromTo;
        ArrayList<String> questions = QuestionRepository.questions;

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
                String toUsername = UserService.getUsername(toId);
                String questionText = questionMap.get(questionId);

                if (questionText != null) {
                    found = true;
                    System.out.println("To : " + toUsername + " | Question Id : [" + questionId + "]" + " | Question : " + questionText);
                    System.out.print("    └──  Answer : " );
                    String ans= AnswersRepository.answerByQid.get(questionId);
                    System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                }
            }
        }
        if (!found) {
            System.out.println(SEPARATOR);
            System.out.println("No Questions Asked by you");
        }

    }


    public static String getUsername(int id) {
        ArrayList<String> username = UserRepository.users;
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
        for (String username : FromToRepository.fromTo) {
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
        for (String username1 : FromToRepository.fromTo) {
            array = username1.split(";");
            if (array[0].equals(id + "")) {
                questions.add(Integer.parseInt(array[2]));
            }
        }
        return questions;
    }
}
