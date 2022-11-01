package Gui;

import Controller.Controller;
import Model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        //gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Group productCompGroup = new Group();
        Group orderLineGroups = new Group();
        int[] Y = {0};

        Order[] order = {null};

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);

        ScrollPane scrollPane = new ScrollPane(productCompGroup);
        scrollPane.setPrefSize(500,300);
        scrollPane.setFocusTraversable(false);
        scrollPane.setBorder(Border.EMPTY);

        ScrollPane orderLinePanes = new ScrollPane(orderLineGroups);
        orderLinePanes.setPrefSize(320,300);
        orderLinePanes.setFocusTraversable(false);

        Button bNewOrder = new Button("+ Order");
        bNewOrder.setMinSize(150,25);

        Button bAdministration = new Button("Administration");
        bAdministration.setMinSize(150,25);

        TextField tfTotalPrice = new TextField();
        tfTotalPrice.setMinSize(150,25);
        tfTotalPrice.setEditable(false);

        Button done = new Button("Done");
        done.setMinSize(150,25);

        gridPane.add(arrangementComboBox,1,1);
        gridPane.add(categoryComboBox,2,1);
        gridPane.add(bNewOrder,3,1);
        gridPane.add(bAdministration,4,1);
        gridPane.add(scrollPane,1,2,2,1);
        gridPane.add(orderLinePanes,3,2,2,1);
        gridPane.add(tfTotalPrice,3,3);
        gridPane.add(done,4,3);

        GridPane.setHalignment(bAdministration, HPos.RIGHT);
        GridPane.setHalignment(done, HPos.RIGHT);

        categoryComboBox.setOnAction(actionEvent -> {
            Arrangement arrangement = arrangementComboBox.getSelectionModel().getSelectedItem();
            ArrayList<ProductComponent> products = categoryComboBox.getSelectionModel().getSelectedItem().getProducts();
            productCompGroup.getChildren().clear();
            for (int i = 0; i < products.size(); i++) {
                double price = 0;
                for (Price price1 : products.get(i).getPrices()) {
                    if (price1.getArrangement() == arrangement) {
                        price = price1.getPrice();
                    }
                }
                ProductComponent[] productComponents = {products.get(i)};
                Button bProductComponent = new Button(products.get(i).getName() + " " + price);
                bProductComponent.setPrefSize(75,75);
                bProductComponent.setTranslateX(83 * i);
                bProductComponent.setTranslateY(0);
                bProductComponent.setOnAction(addEvent -> {
                    if (order[0] == null) {
                        return;
                    }
                    OrderLine orderLine = Controller.createOrderLine(order[0],productComponents[0],1);
                    TextField tfOrderLine = new TextField(orderLine.toString());
                    tfOrderLine.setTranslateY(31 * Y[0]);
                    tfOrderLine.setEditable(false);
                    tfOrderLine.setMinSize(250,30);

                    Button bAppend = new Button("+");
                    bAppend.setTranslateY(31 * Y[0]);
                    bAppend.setTranslateX(250);
                    bAppend.setMinSize(30,30);
                    bAppend.setOnAction(appendEvent -> {
                        orderLine.append();
                        tfOrderLine.setText(orderLine.toString());
                        tfTotalPrice.setText("Total Cost: " + order[0].calculateCollectedCost() + " kr");
                    });

                    Button bDeduct = new Button("-");
                    bDeduct.setTranslateY(31 * Y[0]);
                    bDeduct.setTranslateX(280);
                    bDeduct.setMinSize(30,30);
                    bDeduct.setOnAction(deductEvent -> {
                        orderLine.deduct();
                        tfOrderLine.setText(orderLine.toString());
                        tfTotalPrice.setText("Total Cost: " + order[0].calculateCollectedCost() + " kr");
                    });

                    Y[0]++;

                    tfTotalPrice.setText("Total Cost: " + order[0].calculateCollectedCost() + " kr");
                    orderLineGroups.getChildren().addAll(tfOrderLine,bAppend,bDeduct);
                    bProductComponent.setDisable(true);
                });

                bProductComponent.setOpacity(0);
                productCompGroup.getChildren().add(bProductComponent);
                Fade(bProductComponent,i * 30);
            }
            System.out.println(productCompGroup.getChildren());
        });

        bNewOrder.setOnAction(actionEvent -> {
            Arrangement arrangement = arrangementComboBox.getValue();
            if (arrangement == null) {
                return;
            }
            order[0] = Controller.createOrder(arrangement);
            bNewOrder.setText("Order : " + arrangement);
            bNewOrder.setDisable(true);
            bAdministration.setDisable(true);
        });

        bAdministration.setOnAction(actionEvent -> {});

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
