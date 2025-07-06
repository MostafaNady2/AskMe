package UI;

import Utils.FileHandler;
import models.User;
import services.QuestionServices;
import services.SessionServices;
import services.UserServices;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {
    private int signChoice;
    private static User currentUser;

    public void showAuthenticationMenu() {
        System.out.println("[1] Sign Up");
        System.out.println("[2] Sign In");
        System.out.println("Enter your Choice : ");
        Scanner sc = new Scanner(System.in);
        int c = sc.nextInt();
        if (c > 2 || c < 1) {
            System.out.println("───────────────────────────────────────────");
            System.out.println("Invalid choice.");
            System.out.println("───────────────────────────────────────────");
            showAuthenticationMenu();
        } else {
            signChoice = c;
        }
        getUserData(signChoice);
    }

    public void getUserData(int choice) {
        Scanner sc = new Scanner(System.in);
        if (choice == 1) {
            System.out.println("Please enter your name : ");
            String name = sc.nextLine();
            System.out.println("Please enter your email : ");
            String email = sc.nextLine();
            System.out.println("Please enter your username : ");
            String username = sc.nextLine();
            System.out.println("Please enter your password : ");
            String password = sc.nextLine();
            User user = new User(-1, name, email, username, password);
            System.out.println("───────────────────────────────────────────");
            System.out.println("You have successfully logged in");
            System.out.println("Welcome " + username);
            System.out.println("───────────────────────────────────────────");
            FileHandler fileHandler = new FileHandler();
            user.setId(fileHandler.nextUserId());
            fileHandler.saveObject(user.toString(),"./src/main/resources/users.txt");
            currentUser = user;

        } else if (choice == 2) {
            System.out.println("Please enter your username : ");
            String username = sc.nextLine();
            System.out.println("Please enter your password : ");
            String password = sc.nextLine();
            User newUser = SessionServices.signIn(username, password);
            if (newUser != null) {
                System.out.println("───────────────────────────────────────────");
                System.out.println("You have successfully logged in");
                System.out.println("Welcome " + username);
                System.out.println("───────────────────────────────────────────");
                currentUser = newUser;
            } else {
                System.out.println("───────────────────────────────────────────");
                System.out.println("Invalid username or password.");
                System.out.println("───────────────────────────────────────────");
                showAuthenticationMenu();
            }
        } else {
            System.out.println("───────────────────────────────────────────");
            System.out.println("Invalid choice.");
            System.out.println("───────────────────────────────────────────");
            showAuthenticationMenu();
        }
        showOptionsMenu();
    }

    public void showOptionsMenu() {
        System.out.println("  ┌───────────────────────────────────────────┐\n" +
                "  │         === AskMe Main Menu ===           │\n" +
                "  │ [1] Print Questions To Me                 │\n" +
                "  │ [2] Print Questions From Me               │\n" +
                "  │ [3] Print Feed                            │\n" +
                "  │ [4] Answer Question                       │\n" +
                "  │ [5] Delete Question                       │\n" +
                "  │ [6] Ask Question                          │\n" +
                "  │ [7] List System Users                     │\n" +
                "  │ [8] Logout                                │\n" +
                "  └───────────────────────────────────────────┘");

        handleOption(getValidChoice());
    }

    public void handleOption(int choice) {
        UserServices userServices = new UserServices();
        QuestionServices questionServices = new QuestionServices();
        Scanner sc = new Scanner(System.in);
        if (choice == 1) {
            userServices.printToUser(currentUser);
            System.out.println("───────────────────────────────────────────");
        } else if (choice == 2) {
            userServices.printFromUser(currentUser);
        } else if (choice == 3) {
            questionServices.printFeed();
        } else if (choice == 4) {
            userServices.printToUser(currentUser);
            ArrayList<String> arr = UserServices.getQuestionsToMe(currentUser.getId());
            if (arr.isEmpty()) {
//                System.out.println("No Questions Found to be Answered.");
                showOptionsMenu();
            }
            System.out.println("Enter Question Id : ");
            int id = sc.nextInt();
            sc.nextLine();

            String qid = null, uid = null ;
            String[] parts;
            for (String line : arr) {
                parts = line.split(";");
                if (parts.length > 1 && parts[1].equals(String.valueOf(id))) {
                    uid = parts[0];
                    qid = parts[1];
                    break;
                }
            }
            boolean overwrite = false;
            if (qid == null || uid == null) {
                System.out.println("Invalid Question Id.");
                showOptionsMenu();
            } else {
                // ckeck if the question is answered or not
                if(questionServices.isAnswered(Integer.parseInt(qid)) != -1){
                    System.out.println("this question is already answered.");
                    System.out.println("Do you want to overwrite the answer?  [ y or n ]");
                    String res = sc.nextLine();
                    if(res.equalsIgnoreCase("y")){
                        overwrite = true;
                    } else if (res.equalsIgnoreCase("n")) {
                        overwrite = false;
                        showOptionsMenu();
                    }else {
                        System.out.println("Invalid choice.");
                        System.out.println("───────────────────────────────────────────");
                    }
                }
                System.out.println("Enter your Answer : ");
                String answer = sc.nextLine();
                if(overwrite){

                    questionServices.updateAnswer(questionServices.isAnswered(Integer.parseInt(qid)), answer);
                }else {
                    questionServices.answerQuestion(Integer.parseInt(uid), currentUser.getId(), Integer.parseInt(qid), answer);
                }
            }

        } else if (choice == 5) {
            userServices.printFromUser(currentUser);
            ArrayList<Integer> q = UserServices.getQuestionByMe(currentUser.getId());
            if (q.isEmpty()) {
                showOptionsMenu();
            }
            System.out.println("Enter Question Id to be removed : ");
            int id = sc.nextInt();
            sc.nextLine();
            boolean valid = false;
            for (int num : q) {
                if (num == id) {
                    questionServices.deleteQuestion(id);
                    System.out.println("your question has been deleted | question id : " + id);
                    System.out.println("───────────────────────────────────────────");
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Invalid Question Id.");
                showOptionsMenu();
            }
        } else if (choice == 6) {
            userServices.listUsers(currentUser);
            System.out.println("Enter user Id : ");
            int userId;
            userId = sc.nextInt();
            sc.nextLine();
            for (String line : UserServices.users) {
                if (Integer.parseInt(line.split(";")[0]) == userId && userId != currentUser.getId()) {
                    boolean isAnonymous = false;
                    System.out.println("This question is Anonymous ?   [y or n]");
                    String ans = sc.nextLine();
                    if (ans.equalsIgnoreCase("y")) {
                        isAnonymous = true;
                    } else if (ans.equalsIgnoreCase("n")) {
                        isAnonymous = false;
                    } else {
                        System.out.println("Invalid answer.");
                        showOptionsMenu();
                    }


                    boolean isThreaded = false;
                    System.out.println("This Question is Threaded ?   [y or n]");
                    ans = sc.nextLine();
                    if (ans.equalsIgnoreCase("y")) {
                        isThreaded = true;
                    }else if (ans.equalsIgnoreCase("n")) {
                        isThreaded = false;
                    }else {
                        System.out.println("Invalid answer.");
                        showOptionsMenu();
                    }


                    String cont;
                    int parentId = -1;
                    if (isThreaded) {
                        System.out.println("Enter Parent Question Id : ");
                        parentId = sc.nextInt();
                        sc.nextLine();
                        if (!questionServices.questionExist(parentId)) {
                            System.out.println("Invalid Question Id.");
                            System.out.println("───────────────────────────────────────────");
                            showOptionsMenu();
                        }
                    }
                    System.out.println("Enter your Question : ");
                    cont = sc.nextLine();
                    if (isThreaded && parentId != -1) {
                        questionServices.askThreadedQuestion(currentUser.getId(), userId, parentId, cont, isAnonymous);
                    } else {
                        questionServices.askQuestion(currentUser.getId(), userId, cont, isAnonymous);
                    }
                    System.out.println("───────────────────────────────────────────");
                    break;
                }
            }

        } else if (choice == 7) {
            userServices.listUsers(null);
        } else if (choice == 8) {
            currentUser = null;
            showAuthenticationMenu();
        }
        showOptionsMenu();
    }

    public int getValidChoice() {
        System.out.println("Enter your option [1 : 8] :");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice > 0 && choice < 9) {
            return choice;
        } else {
            return -1;
        }
    }
}
