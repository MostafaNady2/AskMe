package Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static Utils.Constants.*;

public class FileHandler {

    public static void createFiles() {
        File file = new File("./src/main/resources/ids.txt");
        if(file.exists()) {
            return;
        }
        try (PrintWriter out1 = new PrintWriter(USERS_FILE);
             PrintWriter out2 = new PrintWriter(ANSWERS_FILE);
             PrintWriter out3 = new PrintWriter(QUESTIONS_FILE);
             PrintWriter out4 = new PrintWriter(IDS_FILE);
             PrintWriter out5 = new PrintWriter(FROM_TO_FILE)){
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
        File file = new File(FROM_TO_FILE);
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
        File file = new File(IDS_FILE);
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

    public static void loadIds() {
        File file = new File(IDS_FILE);
        if (file.exists()) {
            try(Scanner sc = new Scanner(file)){
                while (sc.hasNextLine()) {
                    IdGenerator.userId = Integer.parseInt(sc.nextLine());
                    if(sc.hasNextLine()){
                        IdGenerator.questionId = Integer.parseInt(sc.nextLine());
                    }
                    if(sc.hasNextLine()){
                        IdGenerator.answerId = Integer.parseInt(sc.nextLine());
                    }
                }
            }catch (FileNotFoundException | NumberFormatException e){
                System.out.println("file not found");
            }
        } else {
            System.out.println("file not found");
        }
    }
}
