package Gui.Shop;

import Controller.Controller;
import Gui.Gui;
import Gui.Observer;
import Model.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopWindow extends Stage implements Observer {
    private final Gui gui;

    public ShopWindow(Gui gui) {
        this.gui = gui;
    }

    private Order selectedOrder = null;

    private final ComboBox<Arrangement> cbArrangement = new ComboBox<>();
    private final ComboBox<String> cbCategory = new ComboBox<>();

    private final Button bNewRental = new Button("+ Udlejning");
    private final Button bNewOrder = new Button("+ Ordre");
    private final Button bAdministration = new Button("Administration");
    private final Button bCancel = new Button("Annuller");
    private final Button bDone = new Button("Færdig");
    private final Button bFinishRental = new Button("Fjern");
    private final ToggleButton tbShowRentals = new ToggleButton("Vis udlejningsordre");

    private final GridPane gProductDisplay = new GridPane();
    private final GridPane gOrderLineDisplay = new GridPane();
    private final TextField tfTotalPrice = new TextField();
    private final TextField tfTotalClips = new TextField();
    private final ScrollPane spOrderLines = new ScrollPane(gOrderLineDisplay);
    private final ScrollPane spProductComps = new ScrollPane(gProductDisplay);
    private final ListView<Order> lvOpenOrder = new ListView<>();

    private final HashMap<String, ArrayList<ToggleButton>> hmCategoryProducts = new HashMap<>();
    private final String all = "* Alle";
    private final GridPane root = this.initContentOrderScene();

    public GridPane initContentOrderScene() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        cbArrangement.setPrefSize(150,25);
        cbArrangement.setFocusTraversable(false);

        cbCategory.setPrefSize(150,25);
        cbCategory.setFocusTraversable(false);
        cbCategory.setDisable(true);

        gOrderLineDisplay.setHgap(2);
        gOrderLineDisplay.setPadding(new Insets(2));

        gProductDisplay.setHgap(2);
        gProductDisplay.setVgap(2);
        gProductDisplay.setPadding(new Insets(2));

        spProductComps.setPrefSize(385,500);
        spProductComps.setFocusTraversable(false);

        spOrderLines.setPrefSize(725,300);
        spOrderLines.setFocusTraversable(false);

        lvOpenOrder.setPrefSize(725,300);
        lvOpenOrder.setFocusTraversable(false);

        Label lTotalPrice = new Label("Samlet beløb :");
        Label lKr = new Label("Kr.");

        Label lTotalClips = new Label("Totale antal klip :");
        Label lClips = new Label("Klip");

        bNewRental.     setPrefSize(125,25);
        bNewOrder.      setPrefSize(125,25);
        bAdministration.setPrefSize(100,25);
        bDone.          setPrefSize(100,25);
        bCancel.        setPrefSize(100,25);
        bFinishRental.  setPrefSize(100,25);
        tbShowRentals.  setPrefSize(150,25);

        bDone.setDisable(true);
        bCancel.setDisable(true);

        tfTotalPrice.setPrefSize(125,25);
        tfTotalPrice.setEditable(false);
        tfTotalPrice.setAlignment(Pos.CENTER);
        tfTotalPrice.setFocusTraversable(false);

        tfTotalClips.setPrefSize(125,25);
        tfTotalClips.setEditable(false);
        tfTotalClips.setAlignment(Pos.CENTER);
        tfTotalClips.setFocusTraversable(false);

        Label lArrangement = new Label("Salgsiturationer:");
        Label lCategory = new Label("Kategorier:");
        Label lCreateOrder = new Label("Opret Ordre:");
        Label lCreateRental = new Label("Opret Udlejning:");

        gridPane.add(lArrangement,0,0);
        gridPane.add(lCategory,1,0);
        gridPane.add(lCreateOrder,2,0);
        gridPane.add(lCreateRental,3,0);

        gridPane.add(cbArrangement,0,1);
        gridPane.add(cbCategory,1,1);
        gridPane.add(bNewOrder,2,1);
        gridPane.add(bNewRental,3,1);
        gridPane.add(tbShowRentals,4,1);
        gridPane.add(bAdministration,6,1);

        gridPane.add(spProductComps,0,2,2,1);
        gridPane.add(spOrderLines,2,2,5,1);

        gridPane.add(lTotalPrice,1,3);
        gridPane.add(tfTotalPrice,2,3);
        gridPane.add(lKr,3,3);

        gridPane.add(lTotalClips,1,4);
        gridPane.add(tfTotalClips,2,4);
        gridPane.add(lClips,3,4);
        gridPane.add(bDone,5,4);
        gridPane.add(bCancel,6,4);

        GridPane.setHalignment(lArrangement,HPos.CENTER);
        GridPane.setHalignment(lCategory,HPos.CENTER);
        GridPane.setHalignment(lCreateOrder,HPos.CENTER);
        GridPane.setHalignment(lCreateRental,HPos.CENTER);

        GridPane.setHalignment(bAdministration, HPos.RIGHT);
        GridPane.setHalignment(tbShowRentals, HPos.RIGHT);
        GridPane.setHalignment(cbArrangement, HPos.CENTER);
        GridPane.setHalignment(cbCategory, HPos.CENTER);

        GridPane.setHalignment(lTotalPrice, HPos.RIGHT);
        GridPane.setHalignment(lKr, HPos.LEFT);

        GridPane.setHalignment(lTotalClips, HPos.RIGHT);
        GridPane.setHalignment(lClips, HPos.LEFT);
        GridPane.setHalignment(bDone, HPos.CENTER);
        GridPane.setHalignment(bCancel, HPos.RIGHT);

        cbCategory.     setOnAction(actionEvent -> choseCategory());
        bNewOrder.      setOnAction(actionEvent -> createNewOrder());
        bNewRental.     setOnAction(actionEvent -> createRental());
        bAdministration.setOnAction(actionEvent -> gui.administrationWindow());
        tbShowRentals.  setOnAction(actionEvent -> viewRentalOrders());
        bFinishRental.  setOnAction(actionEvent -> finishRental());
        bDone.          setOnAction(actionEvent -> finishOrder());
        bCancel.        setOnAction(actionEvent -> cancelOrder());


        if (cbArrangement.getItems().size() > 0)
            Platform.runLater(() -> cbArrangement.setValue(cbArrangement.getItems().get(0)));

        if (cbCategory.getItems().size() > 0)
            Platform.runLater(() -> cbCategory.setValue(cbCategory.getItems().get(0)));

        update();
        return gridPane;
    }

    private void choseCategory() {
        String category = cbCategory.getSelectionModel().getSelectedItem();
        if (category == null || category.isBlank()) return;
        gProductDisplay.getChildren().clear();
        if (!hmCategoryProducts.containsKey(category)) {
            return;
        }

        ArrayList<ToggleButton> buttons = hmCategoryProducts.get(category);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setOpacity(0);
            buttons.get(i).setFocusTraversable(false);
            buttons.get(i).setDisable(selectedOrder == null);
            gProductDisplay.add(buttons.get(i),(int)(i % 2.),i / 2);
            fadeIn(buttons.get(i),i * 2);
        }
    }

    private void createNewOrder() {
        Arrangement arrangement = cbArrangement.getValue();
        if (arrangement == null) return;

        selectedOrder = Controller.createOrder(arrangement);
        bNewOrder.setText("Ordre : " + arrangement);
        bNewOrder.setDisable(true);
        bNewRental.setDisable(true);
        bAdministration.setDisable(true);
        tbShowRentals.setDisable(true);
        cbArrangement.setDisable(true);
        bDone.setDisable(false);
        bCancel.setDisable(false);

        cbCategory.setDisable(false);
        cbCategory.getItems().add(all);
        cbCategory.setValue(all);

        hmCategoryProducts.clear();
        hmCategoryProducts.put(all,new ArrayList<>());
        for (Price price : arrangement.getPrices()) {
            Category category = price.getProduct().getCategory();
            if (!cbCategory.getItems().contains(category.getName()) && !category.getName().equals(all) && category.getClass() == Category.class) {
                updateProductButtons(category,arrangement);
            }
        }
        choseCategory();
    }

    private void createRental() {
        Arrangement arrangement = cbArrangement.getValue();
        if (arrangement == null) return;

        selectedOrder = Controller.createRental(arrangement,null,null,null,0);
        bNewRental.setText("Udlejning : " + arrangement);
        bNewRental.setDisable(true);
        bNewOrder.setDisable(true);
        bAdministration.setDisable(true);
        tbShowRentals.setDisable(true);
        cbArrangement.setDisable(true);
        cbCategory.setDisable(false);
        bDone.setDisable(false);
        bCancel.setDisable(false);

        cbCategory.setDisable(false);
        cbCategory.getItems().add(all);
        cbCategory.setValue(all);

        hmCategoryProducts.clear();
        hmCategoryProducts.put(all,new ArrayList<>());
        for (Price price : arrangement.getPrices()) {
            Category category = price.getProduct().getCategory();
            if (!cbCategory.getItems().contains(category.getName()) && !category.getName().equals(all) && category.getClass() == DepositCategory.class) {
                updateProductButtons(category,arrangement);
            }
        }
        choseCategory();
    }

    private void updateProductButtons(Category category, Arrangement arrangement) {
        //categoryList.add(category.getName());
        cbCategory.getItems().add(category.getName());
        hmCategoryProducts.put(category.getName(),new ArrayList<>());
        for (ProductComponent product : category.getProducts()) {
            Price price = Controller.getProductPrice(product,arrangement);
            if (price == null) continue;
            Integer clip = price.getClips();
            double cost;
            if (product.getCategory().getClass() == DepositCategory.class) {
                cost = price.getPrice() + ((DepositCategory)product.getCategory()).getDeposit();
            } else {
                cost = price.getPrice();
            }
            ToggleButton bAddProducts = new ToggleButton(product.getName() + "\n" + cost + " Kr.  " + (clip != null ? clip + " Clips" : ""));

            bAddProducts.setTextAlignment(TextAlignment.CENTER);
            bAddProducts.setPrefSize(175, 50);
            bAddProducts.setOnAction(event -> addOrderLine(bAddProducts,product));
            hmCategoryProducts.get(all).add(bAddProducts);
            hmCategoryProducts.get(category.getName()).add(bAddProducts);
        }
    }

    private void viewRentalOrders() {
        if (tbShowRentals.isSelected()) {
            bNewOrder.setDisable(true);
            bNewRental.setDisable(true);
            bAdministration.setDisable(true);
            cbArrangement.setDisable(true);
            cbArrangement.setDisable(true);

            lvOpenOrder.getItems().clear();
            for (Order order : Controller.getOrders()) {
                if (!order.isFinished()) {
                    lvOpenOrder.getItems().add(order);
                }
            }

            root.getChildren().remove(spOrderLines);
            root.add(lvOpenOrder, 2,2,5,1);
            root.add(bFinishRental,5,3);
        } else {
            bNewOrder.setDisable(false);
            bNewRental.setDisable(false);
            bAdministration.setDisable(false);
            cbArrangement.setDisable(false);

            root.getChildren().remove(bFinishRental);
            root.getChildren().remove(lvOpenOrder);
            root.add(spOrderLines,2,2,5,1);
        }
    }

    private void finishRental() {
        Order openOrder = lvOpenOrder.getSelectionModel().getSelectedItem();
        if (openOrder == null) return;
        openOrder.finishOrder();
        lvOpenOrder.getItems().remove(openOrder);
    }

    //----------------------- OrderLine --------------------------------------------------------------------------------

    private final HashMap<OrderLine,ArrayList<Control>> controls = new HashMap<>();

    private void addOrderLine(ToggleButton addButton, ProductComponent product) {
        if (selectedOrder == null) return;
        OrderLine orderLine;
        if (product.getCategory().getName().equals("Rundvisning")) {
            AddTourOrderLine addTourOrderLine = new AddTourOrderLine(selectedOrder,product,1);
            addTourOrderLine.showAndWait();
            orderLine = addTourOrderLine.getTourOrderLine();
        } else {
            orderLine = Controller.createOrderLine(selectedOrder,product,1);
        }
        if (orderLine == null) {
            addButton.setSelected(false);
            return;
        }

        Label lName = new Label("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        lName.setPrefSize(200,30);
        lName.setOpacity(0);

        TextField tfPrice = new TextField(orderLine.getCost() + "");
        tfPrice.setPrefSize(60,30);
        tfPrice.setAlignment(Pos.CENTER_RIGHT);
        tfPrice.setOpacity(0);

        Label lKr = new Label(" Kr.");
        lKr.setPrefSize(25,30);
        lKr.setOpacity(0);

        TextField tfClips = new TextField(orderLine.getClips() + "");
        tfClips.setPrefSize(40,30);
        tfClips.setAlignment(Pos.CENTER_RIGHT);
        tfClips.setOpacity(0);

        Label lClips = new Label(" Klip");
        lClips.setPrefSize(40,30);
        lClips.setOpacity(0);

        ComboBox<String> cbDiscounts = new ComboBox<>();
        cbDiscounts.getItems().addAll("Ingen rabat","Beløb rabat","Procent rabat");
        cbDiscounts.setValue(cbDiscounts.getItems().get(0));
        cbDiscounts.setPrefSize(150,30);
        cbDiscounts.setOpacity(0);

        TextField tfDiscount = new TextField();
        tfDiscount.setPrefSize(60,30);
        tfDiscount.setAlignment(Pos.CENTER_RIGHT);
        tfDiscount.setDisable(true);
        tfDiscount.setVisible(false);

        Label lDiscount = new Label();
        lDiscount.setPrefSize(25,30);
        lDiscount.setVisible(false);

        Button bAppend = new Button("+");
        bAppend.setPrefSize(30,30);
        bAppend.setOpacity(0);

        Button bDeduct = new Button("-");
        bDeduct.setPrefSize(30,30);
        bDeduct.setOpacity(0);

        Button bRemove = new Button("X");
        bRemove.setPrefSize(30,30);
        bRemove.setOpacity(0);

        ArrayList<Control> controls = new ArrayList<>(List.of(lName,tfPrice,lKr,tfClips,lClips,cbDiscounts,tfDiscount,lDiscount,bAppend,bDeduct,bRemove));

        tfPrice.    setOnAction(event -> changePrice(orderLine,tfPrice));
        cbDiscounts.setOnAction(event -> setDiscount(orderLine,cbDiscounts,tfDiscount,lDiscount,tfPrice,tfClips));
        tfDiscount. setOnAction(event -> changeValue(orderLine,tfDiscount,tfPrice,tfClips));
        bAppend.    setOnAction(event -> appendProduct(orderLine,lName,tfPrice,tfClips));
        bDeduct.    setOnAction(event -> deductProduct(orderLine,lName,tfPrice,tfClips));
        bRemove.    setOnAction(event -> removeProduct(orderLine,addButton,controls));

        this.controls.put(orderLine,controls);
        updateList();

        for (Control control : controls) {
            fadeIn(control,5);
        }
        Controller.updateOrderLine(orderLine);
        Controller.updateOrderPrices(selectedOrder);
        tfTotalPrice.setText(selectedOrder.getCollectedCost()+"");
        tfTotalClips.setText(selectedOrder.getCollectedClips()+"");

        Platform.runLater(() -> addButton.setDisable(true));
    }

    private void changePrice(OrderLine orderLine, TextField tfPrice) {
        try {
            orderLine.setCost(Double.parseDouble(tfPrice.getText()));
            tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Pris skal være et tal");
            alert.showAndWait();
            return;
        }
        root.requestFocus();
    }

    private void setDiscount(OrderLine orderLine, ComboBox<String> cbDiscounts, TextField tfDiscount, Label lDiscount,TextField tfPrice, TextField tfClips) {
        int index = cbDiscounts.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        switch (index) {
            case 1 -> {
                orderLine.setDiscountStrategy(Controller.createAmountDiscountStrategy());
                tfDiscount.setOpacity(0);
                lDiscount.setOpacity(0);
                if (!tfDiscount.isVisible()) {
                    fadeIn(tfDiscount,0);
                    fadeIn(lDiscount,0);
                } else {
                    tfDiscount.setOpacity(1);
                    lDiscount.setOpacity(1);
                }
                tfDiscount.clear();
                tfDiscount.setDisable(false);
                tfDiscount.setVisible(true);
                lDiscount.setText(" Kr.");
                lDiscount.setVisible(true);
            }
            case 2 -> {
                orderLine.setDiscountStrategy(Controller.createPercentageDiscountStrategy());
                tfDiscount.setOpacity(0);
                lDiscount.setOpacity(0);
                if (!tfDiscount.isVisible()) {
                    fadeIn(tfDiscount,0);
                    fadeIn(lDiscount,0);
                } else {
                    tfDiscount.setOpacity(1);
                    lDiscount.setOpacity(1);
                }
                tfDiscount.clear();
                tfDiscount.setDisable(false);
                tfDiscount.setVisible(true);
                lDiscount.setText(" %");
                lDiscount.setVisible(true);
            }
            default -> orderLine.setDiscountStrategy(Controller.createNoDiscountStrategy());
        }
        updatePrice(orderLine,tfPrice,tfClips);
        updateList();
        root.requestFocus();
    }

    private void changeValue(OrderLine orderLine, TextField tfPercent, TextField tfPrice, TextField tfClips) {
        if (tfPercent.getText().isBlank()) {
            orderLine.getDiscountStrategy().setValue(0);
        }
        try {
            orderLine.getDiscountStrategy().setValue(Double.parseDouble(tfPercent.getText()));
        } catch (NumberFormatException ignore) {
        }
        updatePrice(orderLine,tfPrice,tfClips);
        root.requestFocus();
    }

    private void appendProduct(OrderLine orderLine, Label lName, TextField tfPrice, TextField tfClips) {
        orderLine.append();
        lName.setText("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        updatePrice(orderLine,tfPrice,tfClips);
    }

    private void deductProduct(OrderLine orderLine, Label lName, TextField tfPrice, TextField tfClips) {
        orderLine.deduct();
        lName.setText("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        updatePrice(orderLine,tfPrice,tfClips);
    }

    private void removeProduct(OrderLine orderLine, ToggleButton addButton, ArrayList<Control> controls) {
        addButton.setDisable(false);
        addButton.setSelected(false);
        Controller.removeOrderLine(selectedOrder,orderLine);
        gOrderLineDisplay.getChildren().removeAll(controls);
        updateTotalPrices();
        updateList();
    }

    private void updatePrice(OrderLine orderLine, TextField tfPrice, TextField tfClips) {
        orderLine.updatePrice();
        tfPrice.setText(orderLine.getCost()+"");
        tfClips.setText(orderLine.getClips()+"");
        updateTotalPrices();
    }

    private void updateTotalPrices() {
        Controller.updateOrderPrices(selectedOrder);
        tfTotalPrice.setText(selectedOrder.getCollectedCost() + "");
        tfTotalClips.setText(selectedOrder.getCollectedClips() + "");
    }

    private void updateList() {
        gOrderLineDisplay.getChildren().clear();
        for (OrderLine orderLine : selectedOrder.getOrderLines()) {
            int y = Controller.getOrderLine(selectedOrder).indexOf(orderLine);
            ArrayList<Control> controls = this.controls.get(orderLine);
            for (int x = 0; x < controls.size(); x++) {
                gOrderLineDisplay.add(controls.get(x),x,y);
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private void cancelOrder() {
        Controller.getOrders().remove(selectedOrder);
        selectedOrder = null;
        exitOrder();
    }

    private void finishOrder() {
        if (selectedOrder == null || selectedOrder.getOrderLines().isEmpty()) return;
        FinishOrder finishOrder = new FinishOrder(selectedOrder);
        finishOrder.showAndWait();
        if(finishOrder.isCompleted()) exitOrder();
    }

    public void exitOrder() {
        cbArrangement.setDisable(false);
        cbCategory.getItems().clear();
        cbCategory.setDisable(true);
        bNewOrder.setDisable(false);
        bNewOrder.setText("+ Ordre");
        bNewRental.setDisable(false);
        bNewRental.setText("+ Udlejning");
        bAdministration.setDisable(false);
        tbShowRentals.setDisable(false);
        gOrderLineDisplay.getChildren().clear();
        gProductDisplay.getChildren().clear();
        bCancel.setDisable(true);
        bDone.setDisable(true);
        tfTotalPrice.clear();
        tfTotalClips.clear();
        controls.clear();
    }

    private void fadeIn(Control pane, int delay) {
        double time = 15;
        new Thread(() -> {
            try {
                for (int[] i = {-delay}; i[0] < time; i[0]++) {
                    Platform.runLater(() -> pane.setOpacity(i[0]/time));
                    Thread.sleep((long) (1000 / 60.));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public GridPane getRoot() {
        return root;
    }

    @Override
    public void update() {
        cbArrangement.getItems().clear();
        cbArrangement.getItems().addAll(Controller.getArrangements());
        if (!cbArrangement.getItems().isEmpty()) {
            cbArrangement.setValue(cbArrangement.getItems().get(0));
        }
    }
}
