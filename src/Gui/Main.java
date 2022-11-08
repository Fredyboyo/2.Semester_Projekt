package Gui;

import Controller.Controller;
import Controller.Storage;
import Storage.ListStorage;

public class Main {
    public static void main(String[] args) throws Exception {
        Storage storage = ListStorage.loadStorage();
        if (storage == null) {
            storage = new ListStorage();
        }
        Controller.setStorage(storage);

        if (storage == null) {

        }
        Gui.launch(Gui.class);
        ListStorage.saveStorage(storage);
    }
}
