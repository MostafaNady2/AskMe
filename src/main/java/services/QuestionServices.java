package services;
import Utils.FileHandler;
import models.Answer;
import models.Question;
import models.ThreadedQeustion;

import java.io.*;
import java.util.*;

public class QuestionServices {
    public static ArrayList<String> questions;
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
        for (String questionLine : questions) {
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

    public static void setQuestions(){
        FileHandler fileHandler = new FileHandler();
        questions = fileHandler.loadFile("./src/main/resources/questions.txt");
    }
    public static void setAnswers(){
        FileHandler fileHandler = new FileHandler();
        answers = fileHandler.loadFile("./src/main/resources/answers.txt");
    }
    public void askQuestion(int senderId , int receiverId, String content, boolean isAnonymous) {
        FileHandler filehandler = new FileHandler();
        Question question = new Question(filehandler.nextQuestionId(),content,isAnonymous,-1);
        filehandler.saveObject(question.toString(),"./src/main/resources/questions.txt");
        filehandler.saveFromTo(senderId,receiverId,question.getId());
        setQuestions();
        UserServices.setMapper();
    }
    public void askThreadedQuestion(int senderId , int receiverId, int parentId , String content,boolean isAnonymous) {
        FileHandler filehandler = new FileHandler();
        ThreadedQeustion threadedQeustion = new ThreadedQeustion(filehandler.nextQuestionId(),content,isAnonymous,-1,parentId);
        filehandler.saveObject(threadedQeustion.toString(),"./src/main/resources/questions.txt");
        filehandler.saveFromTo(senderId,receiverId,threadedQeustion.getId());
        setQuestions();
        UserServices.setMapper();
    }
    public void updateQuestion(int questionId, int targetFieldIndex, String answerId) {
        List<String> updatedLines = new ArrayList<>();
        File file = new File("./src/main/resources/questions.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] fields = line.split(";");

                // Skip malformed lines
                if (fields.length <= targetFieldIndex ) {
                    updatedLines.add(line);
                    continue;
                }

                if (Integer.parseInt(fields[0]) == questionId) {
                    fields[targetFieldIndex] = answerId;
                }

                updatedLines.add(String.join(";", fields));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("questions.txt not found", e);
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String updatedLine : updatedLines) {
                writer.println(updatedLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error writing updated questions.");
        }
        setQuestions();
        UserServices.setMapper();
    }

    public void answerQuestion(int senderId , int receiverId,int questionId, String content) {

        FileHandler filehandler = new FileHandler();
        Answer answer = new Answer(filehandler.nextAnswerId(),content);
        updateQuestion(questionId,3,String.valueOf(answer.getId()));
        filehandler.saveObject(answer.toString(),"./src/main/resources/answers.txt");
        setAnswers();
        setAnswerByQid();
    }
    public void deleteQuestion(int id) {
        // remove from questions.txt
        List<String> newList = new ArrayList<>(QuestionServices.questions);
        File file = new File("./src/main/resources/questions.txt");

        newList.removeIf(line -> {
            String[] array = line.split(";");
            return array.length > 0 && array[0].equals(String.valueOf(id));
        });

        try (PrintWriter pr = new PrintWriter(file)) {
            for (String s : newList) {
                pr.println(s);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        // remove from from_to.txt
        List<String> newFromTo = new ArrayList<>(UserServices.fromTo);
        File fromToFile = new File("./src/main/resources/from_to.txt");

        newFromTo.removeIf(line -> {
            String[] array = line.split(";");
            return array.length > 2 && array[2].equals(String.valueOf(id));
        });

        try (PrintWriter pr = new PrintWriter(fromToFile)) {
            for (String s : newFromTo) {
                pr.println(s);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        // Refresh in-memory data
        setQuestions();
        UserServices.setMapper();
    }
    public void updateAnswer(int answerId, String content) {
        String idStr = String.valueOf(answerId);
        for (int i = 0; i < answers.size(); i++) {
            String[] arr = answers.get(i).split(";");
            if (arr.length > 1 && arr[0].equals(idStr)) {
                arr[1] = content;
                answers.set(i, String.join(";", arr));
                break;
            }
        }

        File file = new File("./src/main/resources/answers.txt");
        try (PrintWriter pr = new PrintWriter(new FileWriter(file), true)){
            for (String s : answers) {
                pr.println(s);
            }
        }catch(IOException e){
            System.out.println("Error writing answers.");
        }
        setAnswers();
        setAnswerByQid();
    }

    public void printFeed() {
        ArrayList<String> fromToMap = UserServices.fromTo;
        if (questions == null || questions.isEmpty() || fromToMap == null || fromToMap.isEmpty()) {
            System.out.println("───────────────────────────────────────────");
            System.out.println("No questions available");
            System.out.println("───────────────────────────────────────────");
            return;
        }
        Map<String, String> qMap = new HashMap<>();
        Map<String,String> isAnonymous = new HashMap<>();
        for (String qLine : questions) {
            String[] qParts = qLine.split(";");
            if (qParts.length > 2) {
                isAnonymous.put(qParts[0], qParts[2]);
            }
            if(qParts.length == 5){
                continue;
            }
            if (qParts.length > 1) {
                qMap.put(qParts[0], qParts[1]);
            }
        }
        System.out.println("───────────────────── Feed ──────────────────────");
        for (String mapping : fromToMap) {
            String[] line = mapping.split(";");
            if (line.length < 3) continue;

            int fromId = Integer.parseInt(line[0]);
            int toId = Integer.parseInt(line[1]);
            String questionId = line[2];


            String fromUsername;
            if(isAnonymous.get(questionId).equals("true")){
                fromUsername = "Anonymous";
            }else{
                fromUsername = UserServices.getUsername(fromId);
            }
            String toUsername = UserServices.getUsername(toId);
            String questionText = qMap.get(questionId);


            if (questionText != null) {
                System.out.println("From : " + fromUsername + "    To : " + toUsername);
                System.out.println("question id [" + questionId + "] | Q : " + questionText);
                System.out.print("    └──  Answer : ");
                String ans=answerByQid.get(questionId);
                System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                if(ans != null){
                    printThread(Integer.parseInt(questionId));
                }
                System.out.println("--------------------------------------------");
            }
        }

        System.out.println("───────────────────────────────────────────");
    }


    public boolean questionExist(int id){
        for(String s : questions){
            if(s.split(";")[0].equals(id+"")){
                return true;
            }
        }
        return false;
    }
    public void printThread(int parentId){
        ArrayList<String> fromToMap = UserServices.fromTo;
        String []arr , l;
        for(String s : questions){
            arr= s.split(";");
            if(arr.length == 5 && Integer.parseInt(arr[4]) == parentId){
                for(String line : fromToMap){
                    l=line.split(";");
                    if(l.length > 2 && l[2].equals(arr[0])){
                        System.out.println("    from : " + UserServices.getUsername(Integer.parseInt(l[0])) + " to : " + UserServices.getUsername(Integer.parseInt(l[1])));
                        System.out.println("    Thread : question id ["+arr[0]+"] " +"| Q : "+ arr[1]);
                        String ans = answerByQid.get(arr[0]);
                        System.out.print("    └──  Answer : ");
                        System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                    }
                }
            }
        }
    }
    public int isAnswered(int id){
        String []arr;
        for(String s : questions){
            arr = s.split(";");
            if(arr.length > 3 && Integer.parseInt(arr[0]) == id && Integer.parseInt(arr[3]) != -1){
                return Integer.parseInt(arr[3]);
            }
        }
        return -1;
    }

}
