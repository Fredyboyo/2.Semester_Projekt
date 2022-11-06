package Gui;

import Controller.Controller;
import Storage.ListStorage;

public class Main {
    public static void main(String[] args) {
        Controller.setStorage(ListStorage.loadStorage());
        Gui.launch(Gui.class);
        ListStorage.saveStorage(Controller.getStorage());
    }
}
