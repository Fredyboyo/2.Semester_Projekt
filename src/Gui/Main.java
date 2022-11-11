package Gui;

import Controller.Controller;
import Controller.Storage;
import Storage.ListStorage;

public class Main {
    public static void main(String[] args) {
        Storage storage = ListStorage.loadStorage();
        if (storage == null) {
            storage = new ListStorage();
            Controller.setStorage(storage);
            Controller.init();
        } else {
            Controller.setStorage(storage);
        }
        Gui.launch(Gui.class);
        ListStorage.saveStorage(Controller.getStorage());
    }
}
