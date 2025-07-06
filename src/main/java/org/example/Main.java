package org.example;
import UI.MainMenu;
import services.SessionServices;
public class Main {
    public static void main(String[] args) {
        SessionServices.startSession();
        MainMenu mainMenu = new MainMenu();
        mainMenu.showAuthenticationMenu();

    }
}

