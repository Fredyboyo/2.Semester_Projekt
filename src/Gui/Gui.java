package Gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Gui extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(initialize(),700,500);

        stage.setScene(scene);
        stage.setTitle("");
        stage.show();
    }

    private Group initialize() {
        Rectangle[] rectangles = new Rectangle[30];
        int rowCount = 8;
        for (int i = 0; i < rectangles.length / rowCount; i++) {
            for (int I = 0; I < rowCount; I++) {
                rectangles[i * rowCount + I] = new Rectangle(50,50);
                rectangles[i * rowCount + I].setX(20 + I * 52);
                rectangles[i * rowCount + I].setY(20 + i * 52);
                rectangles[i * rowCount + I].setFill(Color.GOLD);
            }
        }
        return new Group(rectangles);
    }
}
