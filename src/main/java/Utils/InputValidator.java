package Utils;

import java.util.Scanner;

public class InputValidator {
    public int getValidInteger(Scanner scanner , String prompt){
        while (true){
            try{
                System.out.println(prompt);
                return  scanner.nextInt();
            }catch (Exception e){
                System.out.println("please enter a number");
                scanner.nextLine();
            }
        }
    }
    public String getValidString(Scanner scanner, String prompt){
        System.out.println(prompt);
        String input = scanner.nextLine();
        while (input.trim().isEmpty()){
            System.out.println("input is empty");
            input = scanner.nextLine();
        }
        return input;
    }
    public boolean getYesOrNo(Scanner scanner, String prompt){
        System.out.println(prompt);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y") ||  input.equalsIgnoreCase("yes")){
            return true;
        }else if (input.equalsIgnoreCase("n")  || input.equalsIgnoreCase("no")){
            return false;
        }else {
            System.out.println("Please enter 'y' or 'n'.");
            throw new IllegalArgumentException("please enter 'y' or 'n'.");
        }
    }
}
