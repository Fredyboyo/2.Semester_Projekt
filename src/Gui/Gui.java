package Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gui extends Application {

    Group root = new Group();
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root,1100,800);
        initialize();

        stage.setScene(scene);
        stage.setTitle("BREWBREW");
        stage.show();
    }

    private final Group rectangles = new Group();
    private int rowSize = 5;
    private final int size = 39;

    private void initialize() {
        for (int i = 0; i < size; i++) {
            int finalI = i;
            Pane rectangle = new Pane();
            rectangle.setDisable(true);
            rectangle.setMinSize(120,120);
            rectangle.setTranslateX((i % rowSize) * 123);
            rectangle.setTranslateY((i / rowSize) * 123);
            rectangle.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(20),Insets.EMPTY)));
            rectangle.setOnMouseClicked(mouseEvent -> {
                System.out.println(finalI);
            });
            Fade(rectangle,i * 5 + 45);
            rectangles.getChildren().add(rectangle);
        }

        ScrollPane scrollPane = new ScrollPane(rectangles);
        scrollPane.setTranslateX(25);
        scrollPane.setTranslateY(25);
        scrollPane.setPrefSize(123 * rowSize, root.getScene().getHeight()-45);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent;\n -fx-background-color: transparent");

        Pane add = new Pane();
        add.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(20),Insets.EMPTY)));
        add.setMinSize(120,120);
        add.setTranslateX((size % rowSize) * 123);
        add.setTranslateY((size / rowSize) * 123);

        ToggleButton button = new ToggleButton("EDIT Products");
        button.setFocusTraversable(false);
        //button.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10), Insets.EMPTY)));
        button.setPrefSize(200,50);
        button.setTranslateX(50 + 123 * rowSize);
        button.setTranslateY(root.getScene().getHeight()-100);
        button.setFont(Font.font(24));
        button.setOnAction(actionEvent -> {
            ToggleButton toggleButton = (ToggleButton) actionEvent.getSource();
            ArrayList<Pane> rects = new ArrayList<>();

            try {
                for (Object o : rectangles.getChildren().toArray()) {
                    if (o.getClass() == Pane.class && o != add) {
                        rects.add((Pane) o);
                    }
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

            if (toggleButton.isSelected()) {
                for (Pane rectangle : rects) {
                    rectangle.setDisable(false);
                    rectangle.setBackground(new Background(new BackgroundFill(Color.GOLDENROD, new CornerRadii(20),Insets.EMPTY)));
                }
                rectangles.getChildren().add(add);
            } else {
                for (Pane rectangle : rects) {
                    rectangle.setDisable(true);
                    rectangle.setBackground(new Background(new BackgroundFill(Color.GOLD, new CornerRadii(20),Insets.EMPTY)));
                }
                rectangles.getChildren().remove(add);
            }
            update();
        });

        root.getChildren().addAll(scrollPane,button);
    }

    private void update() {

    }

    private void Fade(Pane pane, int delay) {
        double time = 30;
        new Thread(() -> {
            for (int[] i = {-delay}; i[0] < time; i[0]++) {
                Platform.runLater(() -> pane.setOpacity(i[0]/time));
                try {
                    Thread.sleep((long) (1000 / 60.));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
