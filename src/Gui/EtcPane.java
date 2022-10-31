package Gui;

import Controller.Controller;
import Model.Arrangement;
import Model.Category;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

public class EtcPane extends GridPane {

    public EtcPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);


    }
}
