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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
        //gridPane.setGridLinesVisible(true);

        Group productCompGroup = new Group();
        Group orderLineGroups = new Group(new Text());

        Order[] order = {null};
        int[] yHeight = {0};

        ComboBox<Arrangement> cbArrangement = new ComboBox<>();
        cbArrangement.getItems().addAll(Controller.getArrangements());
        cbArrangement.setPrefSize(150,25);

        ComboBox<Category> cbCategory = new ComboBox<>();
        cbCategory.getItems().addAll(Controller.getCategories());
        cbCategory.setPrefSize(150,25);

        ScrollPane spProductComps = new ScrollPane(productCompGroup);
        spProductComps.setPrefSize(300,400);
        spProductComps.setFocusTraversable(false);

        ScrollPane spOrderLines = new ScrollPane(orderLineGroups);
        spOrderLines.setPrefSize(340,300);
        spOrderLines.setFocusTraversable(false);

        Button bNewOrder = new Button("+ Order");
        bNewOrder.setPrefSize(150,25);

        Button bAdministration = new Button("Administration");
        bAdministration.setPrefSize(150,25);

        Label lTotalPrice = new Label("Total Cost :");

        TextField tfTotalPrice = new TextField("* * *");
        tfTotalPrice.setPrefSize(150,25);
        tfTotalPrice.setEditable(false);
        tfTotalPrice.setAlignment(Pos.CENTER);

        Button bDone = new Button("Done");
        bDone.setPrefSize(150,25);

        gridPane.add(cbArrangement,1,1);
        gridPane.add(cbCategory,2,1);
        gridPane.add(bNewOrder,3,1);
        gridPane.add(bAdministration,4,1);
        gridPane.add(spProductComps,1,2,2,1);
        gridPane.add(spOrderLines,3,2,2,1);
        gridPane.add(lTotalPrice,2,3);
        gridPane.add(tfTotalPrice,3,3);
        gridPane.add(bDone,4,3);

        GridPane.setHalignment(bAdministration, HPos.RIGHT);
        GridPane.setHalignment(lTotalPrice, HPos.RIGHT);
        GridPane.setHalignment(bDone, HPos.RIGHT);
        
        cbCategory.setOnAction(actionEvent -> {
            Arrangement arrangement = cbArrangement.getSelectionModel().getSelectedItem();
            ArrayList<ProductComponent> products = cbCategory.getSelectionModel().getSelectedItem().getProducts();
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

                    int y = 30 * yHeight[0] - 10;

                    Label tfOrderLine = new Label("\t" + orderLine);
                    tfOrderLine.setTranslateY(y);
                    tfOrderLine.setPrefSize(150,30);

                    TextField tfPrice = new TextField(orderLine.getCost() + "");
                    tfPrice.setTranslateY(y);
                    tfPrice.setTranslateX(150);
                    tfPrice.setPrefSize(60,30);
                    tfPrice.setAlignment(Pos.CENTER_RIGHT);
                    tfPrice.setOnAction(enterEvent -> {
                        try {
                            orderLine.setCost(Double.parseDouble(tfPrice.getText()));
                            System.out.println(orderLine.getCost());
                            tfTotalPrice.setText(order[0].calculateCollectedCost() + " kr");
                        } catch (NumberFormatException e) {
                            System.out.println("Not a number");
                        }
                        gridPane.requestFocus();
                    });

                    Label tfKr = new Label(" kr");
                    tfKr.setTranslateY(y);
                    tfKr.setTranslateX(210);
                    tfKr.setPrefSize(30,30);

                    Button bAppend = new Button("+");
                    bAppend.setTranslateY(y);
                    bAppend.setTranslateX(240);
                    bAppend.setPrefSize(30,30);
                    bAppend.setOnAction(appendEvent -> {
                        orderLine.append();
                        tfOrderLine.setText("\t" + orderLine);
                        tfPrice.setText(orderLine.getCost()+"");
                        tfTotalPrice.setText(order[0].calculateCollectedCost() + " kr");
                    });

                    Button bDeduct = new Button("-");
                    bDeduct.setTranslateY(y);
                    bDeduct.setTranslateX(272);
                    bDeduct.setPrefSize(30,30);
                    bDeduct.setOnAction(deductEvent -> {
                        orderLine.deduct();
                        tfOrderLine.setText("\t" + orderLine);
                        tfPrice.setText(orderLine.getCost()+"");
                        tfTotalPrice.setText(order[0].calculateCollectedCost() + " kr");
                    });

                    Button bRemove = new Button("X");
                    bRemove.setTranslateY(y);
                    bRemove.setTranslateX(304);
                    bRemove.setPrefSize(30,30);
                    bRemove.setOnAction(removeEvent -> {
                        yHeight[0]--;
                        bProductComponent.setDisable(false);
                        Controller.removerOrderLine(order[0],orderLine);
                        orderLineGroups.getChildren().removeAll(tfOrderLine,tfPrice,tfKr,bAppend,bDeduct,bRemove);
                    });

                    yHeight[0]++;

                    tfTotalPrice.setText(order[0].calculateCollectedCost() + " kr");
                    orderLineGroups.getChildren().addAll(tfOrderLine,tfPrice,tfKr,bAppend,bDeduct,bRemove);
                    Platform.runLater(() -> bProductComponent.setDisable(true));
                });

                bProductComponent.setOpacity(0);
                productCompGroup.getChildren().add(bProductComponent);
                Fade(bProductComponent,i * 30);
            }
            System.out.println(productCompGroup.getChildren());
        });

        bNewOrder.setOnAction(actionEvent -> {
            Arrangement arrangement = cbArrangement.getValue();
            if (arrangement == null) {
                return;
            }
            order[0] = Controller.createOrder(arrangement);
            bNewOrder.setText("Order : " + arrangement);
            bNewOrder.setDisable(true);
            bAdministration.setDisable(true);
        });

        bAdministration.setOnAction(actionEvent -> {});

        if (cbArrangement.getItems().size() > 0)
            Platform.runLater(() -> cbArrangement.setValue(cbArrangement.getItems().get(0)));

        if (cbCategory.getItems().size() > 0)
            Platform.runLater(() -> cbCategory.setValue(cbCategory.getItems().get(0)));

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
