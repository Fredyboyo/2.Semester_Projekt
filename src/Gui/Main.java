package Gui;

import Controller.Controller;

public class Main {
    public static void main(String[] args) {
        Gui.launch(Gui.class);
        Controller.saveTheProducts();
    }
}
