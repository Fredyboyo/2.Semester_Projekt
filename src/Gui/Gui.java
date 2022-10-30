package Gui;

import Controller.Controller;
import Model.Arrangement;
import Model.Category;
import Model.Price;
import Model.Product;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gui extends Application {
    private final Group root = new Group();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root,1100,800);
        initContent();

        stage.setScene(scene);
        stage.setTitle("BREWBREW");
        stage.show();
    }

    private void initContent() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        Group products = new Group();
        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);
        arrangementComboBox.setOnAction(event -> {

        });

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);
        categoryComboBox.setOnAction(event -> {
            if (arrangementComboBox.getSelectionModel().getSelectedItem() == null || categoryComboBox.getSelectionModel().getSelectedItem() != null) {
                return;
            }
            Arrangement arrangement = arrangementComboBox.getSelectionModel().getSelectedItem();
            Category category = categoryComboBox.getSelectionModel().getSelectedItem();
            products.getChildren().clear();
            for (Product product : category.getProducts()) {
                double price = 0;
                for (Price price1 : product.getPrices()) {
                    if (price1.getArrangement() == arrangement) {
                        price = price1.getPrice();
                    }
                }
                Button button = new Button(product.getName() + " " + price);
                button.setOnAction(actionEvent -> {

                });
                products.getChildren().add(button);
            }
        });

        ScrollPane scrollPane = new ScrollPane(products);
        scrollPane.setPrefSize(500,500);
        gridPane.add(arrangementComboBox,1,1);
        gridPane.add(categoryComboBox,2,1);
        gridPane.add(scrollPane,1,3,2,1);

        root.getChildren().add(gridPane);
    }

    private void Fade(Pane pane, int delay) {
        double time = 15;
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
