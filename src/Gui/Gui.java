package Gui;

import Controller.Controller;
import Model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Gui extends Application {
    Group root = new Group();

    @Override
    public void start(Stage stage) {
        initContentOrderScene();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("BREW-BREW");
        stage.show();
    }

    private void initContentOrderScene() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Group buttons = new Group();

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150, 25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150, 25);

        ScrollPane scrollPane = new ScrollPane(buttons);
        scrollPane.setPrefSize(500, 300);

        ListView<OrderLine> orderLines = new ListView<>();

        ListView<Order> orders = new ListView<>();
        orders.getItems().addAll(Controller.getOrders());

        Button newOrder = new Button("+ Order");

        Button btnAdministration = new Button("Administration");

        gridPane.add(arrangementComboBox, 1, 1);
        gridPane.add(categoryComboBox, 2, 1);
        gridPane.add(scrollPane, 1, 2, 2, 1);
        //gridPane.add(orderLines,3,1);
        gridPane.add(orderLines, 3, 2);
        gridPane.add(newOrder, 4, 1);
        gridPane.add(orders, 4, 2);
        gridPane.add(btnAdministration, 4, 0);

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
                button.setPrefSize(75, 75);
                button.setTranslateX(83 * i);
                button.setTranslateY(0);
                button.setOnAction(actionEvent -> {
                    Order order = orders.getSelectionModel().getSelectedItem();
                    if (order == null) {
                        return;
                    }
                    OrderLine orderLine = Controller.createOrderLine(order, product[0], 1);
                    orderLines.getItems().add(orderLine);
                });
                button.setOpacity(0);
                buttons.getChildren().add(button);
                Fade(button, i * 30);
            }
            System.out.println(buttons.getChildren());
        });

        orders.setOnMouseClicked(mouseEvent -> {
            Order order = orders.getSelectionModel().getSelectedItem();
            if (order == null) {
                return;
            }
            orderLines.getItems().addAll(order.getOrderLines());
        });

        newOrder.setOnAction(event -> {
            Arrangement arrangement = arrangementComboBox.getValue();
            if (arrangement == null) {
                return;
            }
            Order order = Controller.createOrder(arrangement);
            orders.getItems().add(order);
        });

        root.getChildren().add(gridPane);

        btnAdministration.setOnAction(event -> initContentAdministrationScene());
    }

    private void Fade(Control pane, int delay) {
        double time = 15;
        new Thread(() -> {
            for (int[] i = {-delay}; i[0] < time; i[0]++) {
                Platform.runLater(() -> pane.setOpacity(i[0] / time));
                try {
                    Thread.sleep((long) (1000 / 60.));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initContentAdministrationScene() {
        root.getChildren().clear();
        BorderPane pane = new BorderPane();
        root.getChildren().add(pane);

        TabPane tabPane = new TabPane();
        this.initTabPane(tabPane);
        pane.setCenter(tabPane);
    }

    private void initTabPane(TabPane tabPane) {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab productListTab = new Tab("Prisliste");
        tabPane.getTabs().add(productListTab);
        ProductPane pricePane = new ProductPane();
        productListTab.setContent(pricePane);

        Tab orderListTab = new Tab("Salg");
        tabPane.getTabs().add(orderListTab);
        OrderPane orderPane = new OrderPane();
        orderListTab.setContent(orderPane);

        Tab rentalListTab = new Tab("Udlejninger");
        tabPane.getTabs().add(rentalListTab);
        RentalPane rentalPane = new RentalPane();
        rentalListTab.setContent(rentalPane);

        Tab ticketCouponTab = new Tab("Klip og klippekort?");
        tabPane.getTabs().add(ticketCouponTab);
        ticketCouponTab.setContent(null);

        Tab statsTab = new Tab("Statistik");
        tabPane.getTabs().add(statsTab);
        StatsPane statsPane = new StatsPane();
        statsTab.setContent(statsPane);
    }
}
