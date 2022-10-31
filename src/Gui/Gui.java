package Gui;

import Controller.Controller;
import Model.*;
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
        Order[] order = {null};

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);

        ScrollPane scrollPane = new ScrollPane(buttons);
        scrollPane.setPrefSize(500,300);

        ListView<OrderLine> orderLines = new ListView<>();

        Button newOrder = new Button("+ Order");

        gridPane.add(arrangementComboBox,1,1);
        gridPane.add(categoryComboBox,2,1);
        gridPane.add(scrollPane,1,2,2,1);
        gridPane.add(orderLines,3,2);
        gridPane.add(newOrder,3,1);

        categoryComboBox.setOnAction(event -> {
            Arrangement arrangement = arrangementComboBox.getSelectionModel().getSelectedItem();
            ArrayList<ProductComponent> products = categoryComboBox.getSelectionModel().getSelectedItem().getProducts();
            buttons.getChildren().clear();
            for (int i = 0; i < products.size(); i++) {
                double price = 0;
                for (Price price1 : products.get(i).getPrices()) {
                    if (price1.getArrangement() == arrangement) {
                        price = price1.getPrice();
                    }
                }
                ProductComponent[] product = {products.get(i)};
                Button button = new Button(products.get(i).getName() + " " + price);
                button.setPrefSize(75,75);
                button.setTranslateX(83 * i);
                button.setTranslateY(0);
                button.setOnAction(actionEvent -> {
                    if (order[0] == null) {
                        return;
                    }
                    OrderLine orderLine = Controller.createOrderLine(order[0],product[0],1);
                    orderLines.getItems().add(orderLine);
                });
                button.setOpacity(0);
                buttons.getChildren().add(button);
                Fade(button,i * 30);
            }
            System.out.println(buttons.getChildren());
        });

        newOrder.setOnAction(event -> {
            Arrangement arrangement = arrangementComboBox.getValue();
            if (arrangement == null) {
                return;
            }
            order[0] = Controller.createOrder(arrangement);
        });

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
