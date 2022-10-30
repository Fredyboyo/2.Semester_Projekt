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
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gui extends Application {
    private final Group root = new Group();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(root);
        initContent();

        stage.setScene(scene);
        stage.setTitle("BREW-BREW");
        stage.show();
    }

    private void initContent() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Group buttons = new Group();

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);
        categoryComboBox.setOnAction(event -> {
            Arrangement arrangement = arrangementComboBox.getSelectionModel().getSelectedItem();
            ArrayList<Product> products = categoryComboBox.getSelectionModel().getSelectedItem().getProducts();
            buttons.getChildren().clear();
            for (int i = 0; i < products.size(); i++) {
                double price = 0;
                for (Price price1 : products.get(i).getPrices()) {
                    if (price1.getArrangement() == arrangement) {
                        price = price1.getPrice();
                    }
                }
                Button button = new Button(products.get(i).getName() + " " + price);

                button.setPrefSize(75,75);
                button.setTranslateX(83 * i);
                button.setTranslateY(0);
                button.setOnAction(actionEvent -> {});
                button.setOpacity(0);
                buttons.getChildren().add(button);
                Fade(button,i * 30);
            }
            System.out.println(buttons.getChildren());
        });

        ScrollPane scrollPane = new ScrollPane(buttons);
        scrollPane.setPrefSize(500,500);

        gridPane.add(arrangementComboBox,1,1);
        gridPane.add(categoryComboBox,2,1);
        gridPane.add(scrollPane,1,3,2,1);

        root.getChildren().add(gridPane);
    }

    private void Fade(Control pane, int delay) {
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
