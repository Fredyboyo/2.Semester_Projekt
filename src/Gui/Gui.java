package Gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Gui extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(initialize());

        stage.setScene(scene);
        stage.setTitle("");
        stage.show();
    }

    private GridPane initialize() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));



        return gridPane;
    }
}
