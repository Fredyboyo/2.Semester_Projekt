package Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gui extends Application {

    Group group = new Group();
    @Override
    public void start(Stage stage) {
        initialize();
        Scene scene = new Scene(group,900,600);

        stage.setScene(scene);
        stage.setTitle("");
        stage.show();
    }

    private void initialize() {

        for (int i = 0; i < 30; i++) {
            Rectangle rectangle = new Rectangle(75,75);
            rectangle.setX(20 + (i % 8) * (rectangle.getWidth() + 3));
            rectangle.setY(20 + (int)(i / 8) * (rectangle.getHeight() + 3));
            rectangle.setFill(Color.GOLD);
            group.getChildren().add(rectangle);
            new Thread(runnable).start();
        }
    }
    private final Runnable runnable = () -> {
        for (int i = 0; i < 30; i++) {
            Platform.runLater(() -> );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
}
