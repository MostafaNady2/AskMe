package org.example;
import UI.MainMenu;
import services.SessionService;
public class Main {
    public static void main(String[] args) {
        SessionService.startSession();
        MainMenu mainMenu = new MainMenu();
        mainMenu.showAuthenticationMenu();

    }
}

