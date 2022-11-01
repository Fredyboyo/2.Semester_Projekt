package Gui;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class RentalPane extends GridPane {

    public RentalPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);
    }
}