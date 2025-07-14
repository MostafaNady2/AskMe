package services;
import Utils.FileHandler;
import Utils.IdGenerator;
import models.Answer;
import models.Question;
import models.ThreadedQeustion;
import repository.AnswersRepository;
import repository.FromToRepository;
import repository.QuestionRepository;
import java.io.*;
import java.util.*;

import static Utils.Constants.*;

public class QuestionService {

    public void askQuestion(int senderId , int receiverId, String content, boolean isAnonymous) {
        FileHandler filehandler = new FileHandler();
        IdGenerator generator = new IdGenerator();
        Question question = new Question(generator.nextQuestionId(),content,isAnonymous,-1);
        filehandler.saveObject(question.toString(),QUESTIONS_FILE);
        filehandler.saveFromTo(senderId,receiverId,question.getId());
        QuestionRepository.setQuestions();
        FromToRepository.setMapper();
    }
    public void askThreadedQuestion(int senderId , int receiverId, int parentId , String content,boolean isAnonymous) {
        FileHandler filehandler = new FileHandler();
        IdGenerator generator = new IdGenerator();
        ThreadedQeustion threadedQeustion = new ThreadedQeustion(generator.nextQuestionId(),content,isAnonymous,-1,parentId);
        filehandler.saveObject(threadedQeustion.toString(),QUESTIONS_FILE);
        filehandler.saveFromTo(senderId,receiverId,threadedQeustion.getId());
        QuestionRepository.setQuestions();
        FromToRepository.setMapper();
    }
    public void updateQuestion(int questionId, int targetFieldIndex, String answerId) {
        List<String> updatedLines = new ArrayList<>();
        File file = new File(QUESTIONS_FILE);

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
        QuestionRepository.setQuestions();
        FromToRepository.setMapper();
    }

    public void answerQuestion(int senderId , int receiverId,int questionId, String content) {

        FileHandler filehandler = new FileHandler();
        IdGenerator generator = new IdGenerator();
        Answer answer = new Answer(generator.nextAnswerId(),content);
        updateQuestion(questionId,3,String.valueOf(answer.getId()));
        filehandler.saveObject(answer.toString(),ANSWERS_FILE);
        AnswersRepository.setAnswers();
        AnswersRepository.setAnswerByQid();
    }
    public void deleteQuestion(int id) {
        // remove from questions.txt
        List<String> newList = new ArrayList<>(QuestionRepository.questions);
        File file = new File(QUESTIONS_FILE);

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
        List<String> newFromTo = new ArrayList<>(FromToRepository.fromTo);
        File fromToFile = new File(FROM_TO_FILE);

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
        QuestionRepository.setQuestions();
        FromToRepository.setMapper();
    }
    public void updateAnswer(int answerId, String content) {
        String idStr = String.valueOf(answerId);
        for (int i = 0; i < AnswersRepository.answers.size(); i++) {
            String[] arr = AnswersRepository.answers.get(i).split(";");
            if (arr.length > 1 && arr[0].equals(idStr)) {
                arr[1] = content;
                AnswersRepository.answers.set(i, String.join(";", arr));
                break;
            }
        }

        File file = new File(ANSWERS_FILE);
        try (PrintWriter pr = new PrintWriter(new FileWriter(file), true)){
            for (String s : AnswersRepository.answers) {
                pr.println(s);
            }
        }catch(IOException e){
            System.out.println("Error writing answers.");
        }
        AnswersRepository.setAnswers();
        AnswersRepository.setAnswerByQid();
    }

    public void printFeed() {
        ArrayList<String> fromToMap = FromToRepository.fromTo;
        if (QuestionRepository.questions == null || QuestionRepository.questions.isEmpty() || fromToMap == null || fromToMap.isEmpty()) {
            System.out.println(SEPARATOR);
            System.out.println("No questions available");
            System.out.println(SEPARATOR);
            return;
        }
        Map<String, String> qMap = new HashMap<>();
        Map<String,String> isAnonymous = new HashMap<>();
        for (String qLine : QuestionRepository.questions) {
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
                fromUsername = UserService.getUsername(fromId);
            }
            String toUsername = UserService.getUsername(toId);
            String questionText = qMap.get(questionId);


            if (questionText != null) {
                System.out.println("From : " + fromUsername + "    To : " + toUsername);
                System.out.println("question id [" + questionId + "] | Q : " + questionText);
                System.out.print("    └──  Answer : ");
                String ans=AnswersRepository.answerByQid.get(questionId);
                System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                if(ans != null){
                    printThread(Integer.parseInt(questionId));
                }
                System.out.println("---------------------------------------------------");
            }
        }

        System.out.println(SEPARATOR);
    }


    public boolean questionExist(int id){
        for(String s : QuestionRepository.questions){
            if(s.split(";")[0].equals(id+"")){
                return true;
            }
        }
        return false;
    }
    public void printThread(int parentId){
        ArrayList<String> fromToMap = FromToRepository.fromTo;
        String []arr , l;
        for(String s : QuestionRepository.questions){
            arr= s.split(";");
            if(arr.length == 5 && Integer.parseInt(arr[4]) == parentId){
                for(String line : fromToMap){
                    l=line.split(";");
                    if(l.length > 2 && l[2].equals(arr[0])){
                        System.out.println("    from : " + UserService.getUsername(Integer.parseInt(l[0])) + " to : " + UserService.getUsername(Integer.parseInt(l[1])));
                        System.out.println("    Thread : question id ["+arr[0]+"] " +"| Q : "+ arr[1]);
                        String ans = AnswersRepository.answerByQid.get(arr[0]);
                        System.out.print("    └──  Answer : ");
                        System.out.println(Objects.requireNonNullElse(ans, "Not Answered yet."));
                    }
                }
            }
        }
    }
    public int isAnswered(int id){
        String []arr;
        for(String s : QuestionRepository.questions){
            arr = s.split(";");
            if(arr.length > 3 && Integer.parseInt(arr[0]) == id && Integer.parseInt(arr[3]) != -1){
                return Integer.parseInt(arr[3]);
            }
        }
        return -1;
    }

}
