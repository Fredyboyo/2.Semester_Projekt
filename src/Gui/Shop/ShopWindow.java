package Gui.Shop;

import Controller.Controller;
import Gui.Gui;
import Gui.Observer;
import Model.*;
import Model.DiscountStrategy.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
    private final ComboBox<Category> cbCategory = new ComboBox<>();

    private final Button bNewRental = new Button("+ Udlejning");
    private final Button bNewOrder = new Button("+ Ordre");
    private final Button bAdministration = new Button("Administration");
    private final Button bCancel = new Button("Anuller");
    private final Button bDone = new Button("Færdig");
    private final Button bFinishRental = new Button("Remove");
    private final ToggleButton tbShowRentals = new ToggleButton("Vis udlejnings ordre");

    private final GridPane gProductDisplay = new GridPane();
    private final GridPane gOrderLineDisplay = new GridPane();
    private final TextField tfTotalPrice = new TextField();
    private final ScrollPane spOrderLines = new ScrollPane(gOrderLineDisplay);
    private final ScrollPane spProductComps = new ScrollPane(gProductDisplay);
    private final ListView<Order> lvOpenOrder = new ListView<>();

    private final HashMap<Category, ArrayList<ToggleButton>> hmCategoryProducts = new HashMap<>();
    private final Category all = new Category("* Alle");
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

        spProductComps.setPrefSize(360,500);
        spProductComps.setFocusTraversable(false);

        spOrderLines.setPrefSize(640,300);
        spOrderLines.setFocusTraversable(false);

        lvOpenOrder.setPrefSize(640,300);
        lvOpenOrder.setFocusTraversable(false);

        Label lTotalPrice = new Label("Total Cost :");
        Label lKr = new Label("Kr.");

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

        gridPane.add(cbArrangement,1,1);
        gridPane.add(cbCategory,2,1);
        gridPane.add(bNewOrder,3,1);
        gridPane.add(bNewRental,4,1);
        gridPane.add(tbShowRentals,5,1);
        gridPane.add(bAdministration,7,1);

        gridPane.add(spProductComps,1,2,2,1);
        gridPane.add(spOrderLines,3,2,5,1);

        gridPane.add(lTotalPrice,2,3);
        gridPane.add(tfTotalPrice,3,3);
        gridPane.add(lKr,4,3);
        gridPane.add(bDone,6,3);
        gridPane.add(bCancel,7,3);

        GridPane.setHalignment(bAdministration, HPos.RIGHT);
        GridPane.setHalignment(tbShowRentals, HPos.RIGHT);
        GridPane.setHalignment(cbCategory, HPos.RIGHT);
        GridPane.setHalignment(lTotalPrice, HPos.RIGHT);
        GridPane.setHalignment(lKr, HPos.LEFT);
        GridPane.setHalignment(bDone, HPos.CENTER);
        GridPane.setHalignment(bCancel, HPos.CENTER);

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
        Category category = cbCategory.getSelectionModel().getSelectedItem();
        if (category == null)
            return;
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
            fadeIn(buttons.get(i),i * 5);
        }
    }

    private void createNewOrder() {
        Arrangement arrangement = cbArrangement.getValue();
        if (arrangement == null) {
            return;
        }
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
            if (cbCategory.getItems().contains(category)) {
                continue;
            }
            cbCategory.getItems().add(category);
            hmCategoryProducts.put(category,new ArrayList<>());
            for (ProductComponent product : category.getProducts()) {
                ToggleButton bAddProducts = new ToggleButton(product.getName() + "\n" + Controller.getProductPrice(product, arrangement) + " Kr.");
                bAddProducts.setTextAlignment(TextAlignment.CENTER);
                bAddProducts.setPrefSize(175, 50);
                bAddProducts.setOnAction(event -> createOrderLine((ToggleButton) event.getSource(),product));
                hmCategoryProducts.get(all).add(bAddProducts);
                hmCategoryProducts.get(category).add(bAddProducts);
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

        hmCategoryProducts.clear();
        ArrayList<Price> prices = arrangement.getPrices();

        hmCategoryProducts.put(all, new ArrayList<>());
        for (Price price : prices) {
            ProductComponent product = price.getProduct();

            ToggleButton bAddProducts = new ToggleButton(product.getName() + "\n" + price.getPrice() + " Kr.");
            bAddProducts.setTextAlignment(TextAlignment.CENTER);
            bAddProducts.setPrefSize(175, 50);
            bAddProducts.setOnAction(event -> createOrderLine((ToggleButton) event.getSource(),product));

            hmCategoryProducts.get(all).add(bAddProducts);
            if (hmCategoryProducts.containsKey(product.getCategory())) {
                hmCategoryProducts.get(product.getCategory()).add(bAddProducts);
            } else {
                hmCategoryProducts.put(product.getCategory(),new ArrayList<>(List.of(bAddProducts)));
            }
        }
        choseCategory();
    }

    private void viewRentalOrders() {
        if (tbShowRentals.isSelected()) {
            bNewOrder.setDisable(true);
            bNewRental.setDisable(true);
            bAdministration.setDisable(true);
            cbArrangement.setDisable(true);
            cbArrangement.setDisable(true);
            bDone.setDisable(true);
            bCancel.setDisable(true);

            lvOpenOrder.getItems().clear();
            for (Order soonToBeRental : Controller.getOrders()) {
                if (soonToBeRental.getClass() == Rental.class && !soonToBeRental.isFinished()) {
                    lvOpenOrder.getItems().add(soonToBeRental);
                }
            }

            root.getChildren().remove(spOrderLines);
            root.add(lvOpenOrder,3,2,4,1);
            root.add(bFinishRental,4,3);
        } else {
            bNewOrder.setDisable(false);
            bNewRental.setDisable(false);
            bAdministration.setDisable(false);
            cbArrangement.setDisable(false);
            bDone.setDisable(false);
            bCancel.setDisable(false);

            root.getChildren().remove(bFinishRental);
            root.getChildren().remove(lvOpenOrder);
            root.add(spOrderLines,3,2,4,1);
        }
    }

    private void finishRental() {
        Order openOrder = lvOpenOrder.getSelectionModel().getSelectedItem();
        if (openOrder == null) return;
        openOrder.finishOrder();
        lvOpenOrder.getItems().clear();
        for (Order order : Controller.getOrders()) {
            if (order.getClass() == Rental.class && !order.isFinished()) {
                lvOpenOrder.getItems().add(order);
            }
        }
    }


    //----------------------- OrderLine --------------------------------------------------------------------------------

    private final HashMap<OrderLine,ArrayList<Control>> controls = new HashMap<>();

    private void createOrderLine(ToggleButton addButton, ProductComponent product) {
        if (selectedOrder == null) return;

        OrderLine orderLine = Controller.createOrderLine(selectedOrder,product,1);

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

        ComboBox<String> cbDiscounts = new ComboBox<>();
        cbDiscounts.getItems().addAll("Beløb rabat","Procentrabat","Køberrabat","Studierabat","Ingen rabat");
        cbDiscounts.setValue(cbDiscounts.getItems().get(4));
        cbDiscounts.setPrefSize(150,30);
        cbDiscounts.setOpacity(0);

        TextField tfDiscount = new TextField();
        tfDiscount.setPrefSize(60,30);
        tfDiscount.setAlignment(Pos.CENTER_RIGHT);

        Label lDiscount = new Label();
        lDiscount.setPrefSize(25,30);

        Button bAppend = new Button("+");
        bAppend.setPrefSize(30,30);
        bAppend.setOpacity(0);

        Button bDeduct = new Button("-");
        bDeduct.setPrefSize(30,30);
        bDeduct.setOpacity(0);

        Button bRemove = new Button("X");
        bRemove.setPrefSize(30,30);
        bRemove.setOpacity(0);

        ArrayList<Control> controls = new ArrayList<>(List.of(lName,tfPrice,lKr,cbDiscounts,bAppend,bDeduct,bRemove));

        tfPrice.    setOnAction(event -> changePrice(tfPrice,orderLine));
        cbDiscounts.setOnAction(event -> setDiscount(cbDiscounts,orderLine,tfDiscount,lDiscount));
        tfDiscount.  setOnAction(event -> changeValue(tfDiscount,orderLine,tfPrice));
        bAppend.    setOnAction(event -> appendProduct(lName,tfPrice,orderLine));
        bDeduct.    setOnAction(event -> deductProduct(lName,tfPrice,orderLine));
        bRemove.    setOnAction(event -> removeProduct(addButton,orderLine,controls));

        this.controls.put(orderLine,controls);
        updateList();

        for (Control control : controls) {
            fadeIn(control,5);
        }

        tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");

        Platform.runLater(() -> addButton.setDisable(true));
    }

    private void changePrice(TextField tfPrice, OrderLine orderLine) {
        try {
            orderLine.setCost(Double.parseDouble(tfPrice.getText()));
            tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");
        } catch (NumberFormatException e) {
            System.out.println("Ikke et number");
        }
        root.requestFocus();
    }

    private void setDiscount(ComboBox<String> cbDiscounts, OrderLine orderLine, TextField tfDiscount, Label lDiscount) {
        int index = cbDiscounts.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        controls.get(orderLine).remove(tfDiscount);
        controls.get(orderLine).remove(lDiscount);
        switch (index) {
            case 0 -> {
                orderLine.setDiscountStrategy(new AmountDiscountStrategy(0));
                tfDiscount.clear();
                controls.get(orderLine).add(4,tfDiscount);
                lDiscount.setText(" Kr.");
                controls.get(orderLine).add(5,lDiscount);
            }
            case 1 -> {
                orderLine.setDiscountStrategy(new PercentageDiscountStrategy(0));
                tfDiscount.clear();
                controls.get(orderLine).add(4,tfDiscount);
                lDiscount.setText(" %");
                controls.get(orderLine).add(5,lDiscount);
            }
            case 2 -> orderLine.setDiscountStrategy(new StudentDiscountStrategy());
            case 3 -> orderLine.setDiscountStrategy(new RegCustomerDiscountStrategy());
            default -> orderLine.setDiscountStrategy(new NoDiscountStrategy());
        }
        updateList();
        root.requestFocus();
    }

    private void changeValue(TextField tfPercent, OrderLine orderLine, TextField tfPrice) {
        if (tfPercent.getText().isBlank()) {
            orderLine.getDiscountStrategy().setValue(0);
        }
        try {
            orderLine.getDiscountStrategy().setValue(Double.parseDouble(tfPercent.getText()));
        } catch (NumberFormatException ignore) {
        }
        tfPrice.setText(orderLine.getUpdatedPrice()+"");
        tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");
        root.requestFocus();
    }

    private void appendProduct(Label lName, TextField tfPrice, OrderLine orderLine) {
        orderLine.append();
        lName.setText("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        tfPrice.setText(orderLine.getCost()+"");
        tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");
    }

    private void deductProduct(Label lName, TextField tfPrice, OrderLine orderLine) {
        orderLine.deduct();
        lName.setText("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        tfPrice.setText(orderLine.getCost()+"");
        tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");
    }

    private void removeProduct(ToggleButton addButton, OrderLine orderLine, ArrayList<Control> controls) {
        addButton.setDisable(false);
        addButton.setSelected(false);
        Controller.removeOrderLine(selectedOrder,orderLine);
        selectedOrder.getOrderLines().remove(orderLine);
        gOrderLineDisplay.getChildren().removeAll(controls);
        updateList();
        tfTotalPrice.setText(selectedOrder.getUpdatedPrice()+"");
    }

    private void updateList() {
        gOrderLineDisplay.getChildren().clear();
        for (OrderLine orderLine : selectedOrder.getOrderLines()) {
            int y = selectedOrder.getOrderLines().indexOf(orderLine);
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
        cbArrangement.setDisable(false);
        bNewOrder.setDisable(false);
        bNewOrder.setText("+ Ordre");
        bNewRental.setDisable(false);
        bNewRental.setText("+ Udlejning");
        bAdministration.setDisable(false);
        tbShowRentals.setDisable(false);
        controls.clear();
        gOrderLineDisplay.getChildren().clear();
        cbCategory.getItems().clear();
        cbCategory.setDisable(true);
        hmCategoryProducts.clear();
        gProductDisplay.getChildren().clear();
        bCancel.setDisable(true);
        bFinishRental.setDisable(true);
    }

    private void finishOrder() {
        if (selectedOrder == null || selectedOrder.getOrderLines().isEmpty()) return;

        FinishOrder finishOrder = new FinishOrder(selectedOrder);
        finishOrder.showAndWait();

        if(!finishOrder.isFinished()) return;

        cbArrangement.setDisable(false);
        bNewOrder.setDisable(false);
        bNewOrder.setText("+ Ordre");
        bNewRental.setDisable(false);
        bNewRental.setText("+ Udlejning");
        bAdministration.setDisable(false);
        tbShowRentals.setDisable(false);
        controls.clear();
        gOrderLineDisplay.getChildren().clear();
        gOrderLineDisplay.getChildren().add(new Text());
        bCancel.setDisable(true);
        bFinishRental.setDisable(true);
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
        if (cbArrangement.getItems().size() > 0) {
            cbArrangement.setValue(cbArrangement.getItems().get(0));
        }
    }
}
