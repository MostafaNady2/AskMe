package Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    public static int userId ;
    public static int questionId;
    public static int answerId;

    public static void createFiles() {
        File file = new File("./src/main/resources/ids.txt");
        if(file.exists()) {
            return;
        }
        try (PrintWriter out1 = new PrintWriter("./src/main/resources/users.txt");
             PrintWriter out2 = new PrintWriter("./src/main/resources/answers.txt");
             PrintWriter out3 = new PrintWriter("./src/main/resources/questions.txt");
             PrintWriter out4 = new PrintWriter("./src/main/resources/ids.txt");
             PrintWriter out5 = new PrintWriter("./src/main/resources/from_to.txt");){
            System.out.println("          loading system files           ");
            Thread.sleep(1000);

        } catch (FileNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveObject(String data,String path){
        File file = new File(path);
        if (file.exists()) {
            try (PrintWriter pr = new PrintWriter(new FileWriter(file, true), true)) {
                pr.println(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("file not found");
        }
    }

    public ArrayList<String> loadFile(String path) {
        File file = new File(path);
        ArrayList<String> fileData = new ArrayList<>();
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    fileData.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return fileData;
    }

    public void saveFromTo(int from, int to,int id) {
        File file = new File("./src/main/resources/from_to.txt");
        if (file.exists()) {
            try(PrintWriter pr = new PrintWriter(new FileWriter(file,true),true)){
                pr.println(from+";"+to+";"+id+";"+"false");
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("file fromTo not found");
        }
    }

    public static void saveIds(int userId, int questionId, int answerId) {
        File file = new File("./src/main/resources/ids.txt");
        if (file.exists()) {
            try (PrintWriter pr = new PrintWriter(new FileWriter(file), true)) {
                pr.println(userId);
                pr.println(questionId);
                pr.println(answerId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("file not found");
        }
    }

    public void loadIds() {
        File file = new File("./src/main/resources/ids.txt");
        if (file.exists()) {
            try(Scanner sc = new Scanner(file)){
                while (sc.hasNextLine()) {
                    userId = Integer.parseInt(sc.nextLine());
                    if(sc.hasNextLine()){
                        questionId = Integer.parseInt(sc.nextLine());
                    }
                    if(sc.hasNextLine()){
                        answerId = Integer.parseInt(sc.nextLine());
                    }
                }
            }catch (FileNotFoundException | NumberFormatException e){
                System.out.println("file not found");
            }
        } else {
            System.out.println("file not found");
        }
    }
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
