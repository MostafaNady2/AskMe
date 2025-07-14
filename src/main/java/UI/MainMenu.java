package UI;

import Utils.FileHandler;
import Utils.IdGenerator;
import Utils.InputValidator;
import models.User;
import repository.UserRepository;
import services.QuestionService;
import services.SessionService;
import services.UserService;

import java.util.ArrayList;
import java.util.Scanner;

import static Utils.Constants.SEPARATOR;
import static Utils.Constants.USERS_FILE;

public class MainMenu {
    private int signChoice;
    private static User currentUser;
    InputValidator validator = new InputValidator();
    UserService userService = new UserService();
    QuestionService questionService = new QuestionService();
    Scanner sc = new Scanner(System.in);

    public void showAuthenticationMenu() {
        System.out.println("[1] Sign Up");
        System.out.println("[2] Sign In");
        Scanner sc = new Scanner(System.in);

        int c = validator.getValidInteger(sc, "Enter your Choice : ");
        sc.nextLine();
        if (c > 2 || c < 1) {
            System.out.println(SEPARATOR);
            System.out.println("Invalid choice.");
            System.out.println(SEPARATOR);
            showAuthenticationMenu();
        } else {
            signChoice = c;
        }
        getUserData(signChoice);
    }

    public void getUserData(int choice) {

        Scanner sc = new Scanner(System.in);
        if (choice == 1) {
            String name = validator.getValidString(sc, "Enter your name : ");
            String email = validator.getValidString(sc, "Enter your email : ");
            String username = validator.getValidString(sc, "Enter your username : ");
            String password = validator.getValidString(sc, "Enter your password : ");
            User user = new User(-1, name, email, username, password);
            System.out.println(SEPARATOR);
            System.out.println("You have successfully logged in");
            System.out.println("Welcome " + username);
            System.out.println(SEPARATOR);
            FileHandler fileHandler = new FileHandler();
            IdGenerator generator = new IdGenerator();
            user.setId(generator.nextUserId());
            fileHandler.saveObject(user.toString(), USERS_FILE);
            currentUser = user;

        } else if (choice == 2) {

            String username = validator.getValidString(sc, "Enter your username : ");
            String password = validator.getValidString(sc, "Enter your password : ");
            User newUser = SessionService.signIn(username, password);
            if (newUser != null) {
                System.out.println(SEPARATOR);
                System.out.println("You have successfully logged in");
                System.out.println("Welcome " + username);
                System.out.println(SEPARATOR);
                currentUser = newUser;
            } else {
                System.out.println(SEPARATOR);
                System.out.println("Invalid username or password.");
                System.out.println(SEPARATOR);
                showAuthenticationMenu();
            }
        } else {
            System.out.println(SEPARATOR);
            System.out.println("Invalid choice.");
            System.out.println(SEPARATOR);
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
        if (choice == 1) {
            userService.printToUser(currentUser);
            System.out.println(SEPARATOR);
        } else if (choice == 2) {
            userService.printFromUser(currentUser);
            System.out.println(SEPARATOR);
        } else if (choice == 3) {
            questionService.printFeed();
        } else if (choice == 4) {
            handleAnswerQuestion();
        } else if (choice == 5) {
            handleDeleteQuestion();
        } else if (choice == 6) {
            handleAskQuestion();
        } else if (choice == 7) {
            userService.listUsers(null);
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
            throw new RuntimeException("Invalid choice.");
        }
    }

    private void handleAnswerQuestion() {
        userService.printToUser(currentUser);
        ArrayList<String> arr = UserService.getQuestionsToMe(currentUser.getId());
        if (arr.isEmpty()) {
            showOptionsMenu();
        }

        int id = validator.getValidInteger(sc, "Enter Question Id : ");
        sc.nextLine();

        String qid = null, uid = null;
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
            if (questionService.isAnswered(Integer.parseInt(qid)) != -1) {
                System.out.println("this question is already answered.");
                System.out.println("Do you want to overwrite the answer?  [ y or n ]");
                boolean res = validator.getYesOrNo(sc, "Do you want to overwrite the answer?  [ y or n ]");
                if (res) {
                    overwrite = true;
                } else {
                    showOptionsMenu();
                    System.out.println(SEPARATOR);
                }
            }
            String answer = validator.getValidString(sc, "Enter your Answer : ");
            if (overwrite) {
                questionService.updateAnswer(questionService.isAnswered(Integer.parseInt(qid)), answer);
            } else {
                questionService.answerQuestion(Integer.parseInt(uid), currentUser.getId(), Integer.parseInt(qid), answer);
            }
        }

    }

    private void handleDeleteQuestion() {
        userService.printFromUser(currentUser);
        ArrayList<Integer> q = UserService.getQuestionByMe(currentUser.getId());
        if (q.isEmpty()) {
            showOptionsMenu();
        }
        int id = validator.getValidInteger(sc, "Enter Question Id to be removed : ");
        sc.nextLine();
        boolean valid = false;
        for (int num : q) {
            if (num == id) {
                questionService.deleteQuestion(id);
                System.out.println("your question has been deleted | question id : " + id);
                System.out.println(SEPARATOR);
                valid = true;
                break;
            }
        }
        if (!valid) {
            System.out.println("Invalid Question Id.");
            showOptionsMenu();
        }
    }

    private void handleAskQuestion() {
        userService.listUsers(currentUser);
        int userId;
        userId = validator.getValidInteger(sc, "Enter user Id : ");
        sc.nextLine();
        for (String line : UserRepository.users) {
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
                } else if (ans.equalsIgnoreCase("n")) {
                    isThreaded = false;
                } else {
                    System.out.println("Invalid answer.");
                    showOptionsMenu();
                }


                String cont;
                int parentId = -1;
                if (isThreaded) {
                    parentId = validator.getValidInteger(sc, "Enter Parent Question Id : ");
                    sc.nextLine();
                    if (!questionService.questionExist(parentId)) {
                        System.out.println("Invalid Question Id.");
                        System.out.println(SEPARATOR);
                        showOptionsMenu();
                    }
                }
                cont = validator.getValidString(sc, "Enter your Question : ");
                if (isThreaded && parentId != -1) {
                    questionService.askThreadedQuestion(currentUser.getId(), userId, parentId, cont, isAnonymous);
                } else {
                    questionService.askQuestion(currentUser.getId(), userId, cont, isAnonymous);
                }
                System.out.println(SEPARATOR);
                break;
            }
        }

    }
}
