package Gui;

import Controller.Controller;
import Model.*;
import Model.DiscountStrategy.PercentageDiscountStrategy;
import Storage.ListStorage;
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
import java.util.HashMap;
import java.util.List;

public class Gui extends Application implements Observer {
    private GridPane shop;
    private GridPane administration;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        shop = initContentOrderScene();
        administration = initContentAdministrationScene();
        scene = new Scene(shop);

        stage.setScene(scene);
        stage.setTitle("BREW-BREW");
        stage.show();
    }

    @Override
    public void init() {
        Controller.addObserver(this);
    }

    private Order selectedOrder = null;

    private final ComboBox<Arrangement> cbArrangement = new ComboBox<>();
    private final ComboBox<Category> cbCategory = new ComboBox<>();

    private final Button bNewRental = new Button("+ Rental");
    private final Button bNewOrder = new Button("+ Order");
    private final Button bAdministration = new Button("Administration");
    private final ToggleButton tbShowRentals = new ToggleButton("View Rental Orders");
    private final Button bCancel = new Button("Cancel");
    private final Button bDone = new Button("Done");
    private final Button bRemove = new Button("Remove");

    private final Group gProductDisplay = new Group(new Text());
    private final Group gOrderLineDisplay = new Group(new Text());
    private final TextField tfTotalPrice = new TextField("* * *");
    private final ScrollPane spOrderLines = new ScrollPane(gOrderLineDisplay);

    private final HashMap<Category,ArrayList<ToggleButton>> hmCategoryProducts = new HashMap<>();
    private final Category all = new Category("* All");

    private GridPane initContentOrderScene() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        cbArrangement.getItems().addAll(Controller.getArrangements());
        cbArrangement.setPrefSize(150,25);
        cbArrangement.setFocusTraversable(false);

        cbCategory.getItems().add(all);
        cbCategory.getItems().addAll(Controller.getCategories());
        cbCategory.setPrefSize(150,25);
        cbCategory.setFocusTraversable(false);

        ScrollPane spProductComps = new ScrollPane(gProductDisplay);
        spProductComps.setPrefSize(360,500);
        spProductComps.setFocusTraversable(false);
        spProductComps.setPickOnBounds(false);
        spProductComps.setFocusTraversable(false);

        spOrderLines.setPrefSize(580,300);
        spOrderLines.setFocusTraversable(false);
        spOrderLines.setPickOnBounds(false);
        spOrderLines.setFocusTraversable(false);

        Label lTotalPrice = new Label("Total Cost :");

        bNewRental.setPrefSize(100,25);
        bNewOrder.setPrefSize(100,25);
        bAdministration.setPrefSize(100,25);
        tbShowRentals.setPrefSize(150,25);
        bCancel.setPrefSize(100,25);
        bDone.setPrefSize(100,25);
        bRemove.setPrefSize(100,25);

        tfTotalPrice.setPrefSize(100,25);
        tfTotalPrice.setEditable(false);
        tfTotalPrice.setAlignment(Pos.CENTER);
        tfTotalPrice.setFocusTraversable(false);

        gridPane.add(bAdministration,6,1);

        gridPane.add(cbArrangement,1,1);
        gridPane.add(cbCategory,2,1);
        gridPane.add(bNewOrder,3,1);
        gridPane.add(bNewRental,4,1);
        gridPane.add(tbShowRentals,5,1);

        gridPane.add(spProductComps,1,2,2,1);
        gridPane.add(spOrderLines,3,2,4,1);
        gridPane.add(lTotalPrice,2,3);

        gridPane.add(tfTotalPrice,3,3);
        gridPane.add(bDone,5,3);
        gridPane.add(bCancel,6,3);

        GridPane.setHalignment(bAdministration, HPos.RIGHT);
        GridPane.setHalignment(tbShowRentals, HPos.RIGHT);
        GridPane.setHalignment(cbCategory, HPos.RIGHT);
        GridPane.setHalignment(lTotalPrice, HPos.RIGHT);
        GridPane.setHalignment(bDone, HPos.RIGHT);
        GridPane.setHalignment(bCancel, HPos.LEFT);

        cbArrangement.  setOnAction(actionEvent -> choseArrangement());
        cbCategory.     setOnAction(actionEvent -> choseCategory());
        bNewOrder.      setOnAction(actionEvent -> createNewOrder());
        bNewRental.     setOnAction(actionEvent -> createRental());
        bAdministration.setOnAction(actionEvent -> shopWindow());
        tbShowRentals.  setOnAction(actionEvent -> viewRentalOrders());
        bRemove.        setOnAction(actionEvent -> finishRental());
        bDone.          setOnAction(actionEvent -> finishOrder());
        bCancel.        setOnAction(actionEvent -> cancelOrder());


        if (cbArrangement.getItems().size() > 0)
            Platform.runLater(() -> cbArrangement.setValue(cbArrangement.getItems().get(0)));

        if (cbCategory.getItems().size() > 0)
            Platform.runLater(() -> cbCategory.setValue(cbCategory.getItems().get(0)));

        return gridPane;
    }

    private void choseArrangement() {
        hmCategoryProducts.clear();
        Arrangement arrangement = cbArrangement.getSelectionModel().getSelectedItem();
        ArrayList<Price> prices = arrangement.getPrices();

        hmCategoryProducts.put(all, new ArrayList<>());
        for (Price price : prices) {
            ProductComponent product = price.getProduct();

            ToggleButton bAddProducts = new ToggleButton(product.getName() + "\n" + price.getPrice() + " kr");
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

    private void choseCategory() {
        Category category = cbCategory.getSelectionModel().getSelectedItem();
        if (category == null)
            return;
        gProductDisplay.getChildren().clear();
        gProductDisplay.getChildren().add(new Text());

        if (!hmCategoryProducts.containsKey(category)) {
            return;
        }

        ArrayList<ToggleButton> buttons = hmCategoryProducts.get(category);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setTranslateX(177 * (i % 2.) + 2);
            buttons.get(i).setTranslateY(52 * (i / 2) - 10);
            buttons.get(i).setOpacity(0);
            buttons.get(i).setFocusTraversable(false);
            buttons.get(i).setDisable(selectedOrder == null);
            gProductDisplay.getChildren().add(buttons.get(i));
            fadeIn(buttons.get(i),i * 5);
        }
    }

    private final ListView<Rental> lvRentals = new ListView<>();

    private void viewRentalOrders() {
        if (tbShowRentals.isSelected()) {
            bNewOrder.setDisable(true);
            bNewRental.setDisable(true);
            bAdministration.setDisable(true);
            cbArrangement.setDisable(true);
            bDone.setDisable(true);
            bCancel.setDisable(true);
            cbArrangement.setDisable(true);

            lvRentals.getItems().clear();
            for (Order soonToBeRental : Controller.getOrders()) {
                if (soonToBeRental.getClass() == Rental.class && !((Rental) soonToBeRental).isFinished()) {
                    lvRentals.getItems().add((Rental) soonToBeRental);
                }
            }

            shop.getChildren().remove(spOrderLines);
            shop.add(lvRentals,3,2,4,1);
            shop.add(bRemove,4,3);
        } else {
            bNewOrder.setDisable(false);
            bNewRental.setDisable(false);
            bAdministration.setDisable(false);
            cbArrangement.setDisable(false);
            bDone.setDisable(false);
            bCancel.setDisable(false);

            shop.getChildren().remove(bRemove);
            shop.getChildren().remove(lvRentals);
            shop.add(spOrderLines,3,2,4,1);
        }
    }

    private void finishRental() {
        Rental rental = lvRentals.getSelectionModel().getSelectedItem();
        if (rental == null) return;
        rental.finish();
    }

    private void createNewOrder() {
        Arrangement arrangement = cbArrangement.getValue();
        if (arrangement == null) {
            return;
        }
        selectedOrder = Controller.createOrder(arrangement);
        bNewOrder.setText("Order : " + arrangement);
        bNewOrder.setDisable(true);
        bNewRental.setDisable(true);
        bAdministration.setDisable(true);
        tbShowRentals.setDisable(true);
        cbArrangement.setDisable(true);
        hmCategoryProducts.forEach((category, toggleButtons) -> {
            for (ToggleButton toggleButton : toggleButtons) {
                toggleButton.setDisable(false);
            }
        });
    }

    private void createRental() {
        Arrangement arrangement = cbArrangement.getValue();
        if (arrangement == null) return;

        selectedOrder = Controller.createRental(arrangement,null,null,null,0);
        bNewRental.setText("Rental : " + arrangement);
        bNewRental.setDisable(true);
        bNewOrder.setDisable(true);
        bAdministration.setDisable(true);
        tbShowRentals.setDisable(true);
        cbArrangement.setDisable(true);

        hmCategoryProducts.forEach((category, toggleButtons) -> {
            for (ToggleButton toggleButton : toggleButtons) {
                toggleButton.setDisable(false);
            }
        });
    }

    private final HashMap<OrderLine,ArrayList<Control>> controls = new HashMap<>();

    private void createOrderLine(ToggleButton addButton, ProductComponent p) {
        if (selectedOrder == null) return;

        OrderLine orderLine = Controller.createOrderLine(selectedOrder,p,1);

        Label lName = new Label("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        lName.setPrefSize(spOrderLines.getWidth()-284,30);

        TextField tfPrice = new TextField(orderLine.getCost() + "");
        tfPrice.setTranslateX(spOrderLines.getWidth()-284);
        tfPrice.setPrefSize(60,30);
        tfPrice.setAlignment(Pos.CENTER_RIGHT);
        tfPrice.setOnAction(event -> changePrice(tfPrice,orderLine));

        Label lKr = new Label(" kr");
        lKr.setTranslateX(spOrderLines.getWidth()-222);
        lKr.setPrefSize(30,30);

        ComboBox<Discount> cbDiscounts = new ComboBox<>();
        cbDiscounts.setTranslateX(spOrderLines.getWidth()-190);
        cbDiscounts.setPrefSize(90,30);
        cbDiscounts.setOnAction(event -> setDiscount(cbDiscounts,orderLine));

        TextField tfPercent = new TextField(orderLine.getCost() + "");
        tfPercent.setTranslateX(spOrderLines.getWidth()-284);
        tfPercent.setPrefSize(60,30);
        tfPercent.setAlignment(Pos.CENTER_RIGHT);
        tfPercent.setOnAction(event -> changePercent(cbDiscounts,tfPercent));

        Button bAppend = new Button("+");
        bAppend.setTranslateX(spOrderLines.getWidth()-98);
        bAppend.setPrefSize(30,30);
        bAppend.setOnAction(event -> appendProduct(lName,tfPrice,orderLine));

        Button bDeduct = new Button("-");
        bDeduct.setTranslateX(spOrderLines.getWidth()-66);
        bDeduct.setPrefSize(30,30);
        bDeduct.setOnAction(event -> deductProduct(lName,tfPrice,orderLine));

        Button bRemove = new Button("X");
        bRemove.setTranslateX(spOrderLines.getWidth()-34);
        bRemove.setPrefSize(30,30);
        bRemove.setOnAction(event -> removeProduct(addButton,orderLine,lName,tfPrice,lKr,cbDiscounts,bAppend,bDeduct,bRemove));

        ArrayList<Control> controls = new ArrayList<>(List.of(lName,tfPrice,lKr,cbDiscounts,bAppend,bDeduct,bRemove));
        this.controls.put(orderLine,controls);
        updateList();

        for (Control control : controls) {
            fadeIn(control,5);
        }

        tfTotalPrice.setText(selectedOrder.getUpdatedPrice() + " kr");
        gOrderLineDisplay.getChildren().addAll(controls);
        Platform.runLater(() -> addButton.setDisable(true));
    }

    private void changePrice(TextField tfPrice, OrderLine orderLine) {
        try {
            orderLine.setCost(Double.parseDouble(tfPrice.getText()));
            tfTotalPrice.setText(selectedOrder.getUpdatedPrice() + " kr");
        } catch (NumberFormatException e) {
            System.out.println("Not a number");
        }
        shop.requestFocus();
    }

    private void setDiscount(ComboBox<Discount> cbDiscounts, OrderLine orderLine) {
        Discount discount = cbDiscounts.getSelectionModel().getSelectedItem();
        if (discount == null) return;
        orderLine.setDiscountStrategy(discount);
    }

    private void changePercent(ComboBox<Discount> cbDiscounts, TextField tfPercent) {
        PercentageDiscountStrategy discountStrategy = new PercentageDiscountStrategy(0);
        Discount discount = cbDiscounts.getSelectionModel().getSelectedItem();
        double percent = Double.parseDouble(tfPercent.getText());

        ((PercentageDiscountStrategy)discount).setPercentage(percent);
    }

    private void appendProduct(Label lName, TextField tfPrice, OrderLine orderLine) {
        orderLine.append();
        lName.setText("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        tfPrice.setText(orderLine.getCost()+"");
        tfTotalPrice.setText(selectedOrder.getUpdatedPrice() + " kr");
    }

    private void deductProduct(Label lName, TextField tfPrice, OrderLine orderLine) {
        orderLine.deduct();
        lName.setText("  (" + orderLine.getAmount() + ") " + orderLine.getProduct().getName());
        tfPrice.setText(orderLine.getCost()+"");
        tfTotalPrice.setText(selectedOrder.getUpdatedPrice() + " kr");
    }

    private void removeProduct(ToggleButton addButton, OrderLine orderLine, Label lName, TextField tfPrice, Label lKr, ComboBox<Discount> cbDiscounts, Button bAppend, Button bDeduct, Button bRemove) {
        addButton.setDisable(false);
        addButton.setSelected(false);
        Controller.removeOrderLine(selectedOrder,orderLine);
        selectedOrder.getOrderLines().remove(orderLine);
        gOrderLineDisplay.getChildren().removeAll(lName,tfPrice,lKr,cbDiscounts,bAppend,bDeduct,bRemove);
        updateList();
    }

    private void updateList() {
        for (OrderLine orderLine : selectedOrder.getOrderLines()) {
            int y = 30 * selectedOrder.getOrderLines().indexOf(orderLine) - 10;
            for (Control control : controls.get(orderLine)) {
                control.setTranslateY(y);
            }
        }
    }

    private void cancelOrder() {
        Controller.getOrders().remove(selectedOrder);
        selectedOrder = null;
        cbArrangement.setDisable(false);
        bNewOrder.setDisable(false);
        bNewOrder.setText("+ Order");
        bNewRental.setDisable(false);
        bNewRental.setText("+ Rental");
        bAdministration.setDisable(false);
        tbShowRentals.setDisable(false);
        controls.clear();
        gOrderLineDisplay.getChildren().clear();
        gOrderLineDisplay.getChildren().add(new Text());
        choseArrangement();
    }

    private void finishOrder() {
        if (selectedOrder != null) {
            selectedOrder.updateCollectedCost();
            selectedOrder = null;
        }
        cbArrangement.setDisable(false);
        bNewOrder.setDisable(false);
        bNewOrder.setText("+ Order");
        bNewRental.setDisable(false);
        bNewRental.setText("+ Rental");
        bAdministration.setDisable(false);
        tbShowRentals.setDisable(false);
        controls.clear();
        gOrderLineDisplay.getChildren().clear();
        gOrderLineDisplay.getChildren().add(new Text());
        choseArrangement();
    }

    private void fadeIn(Control pane, int delay) {
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

    //------------------------------------------------------------------------------------------------------------------
    // initAdministration()

    private GridPane initContentAdministrationScene() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        TabPane tabPane = new TabPane();
        this.initTabPane(tabPane);
        gridPane.add(tabPane, 0, 0, 1, 2);
        tabPane.setPrefWidth(670);

        Button btnBack = new Button("<");
        btnBack.setOnAction(event -> administrationWindow());
        gridPane.add(btnBack, 1, 0);
        return gridPane;
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

        Tab paymentMethodTab = new Tab("Betalingsmetoder");
        tabPane.getTabs().add(paymentMethodTab);
        PaymentMethodPane paymentMethodPane = new PaymentMethodPane();
        paymentMethodTab.setContent(paymentMethodPane);

        Tab statsTab = new Tab("Statistik");
        tabPane.getTabs().add(statsTab);
        StatsPane statsPane = new StatsPane();
        statsTab.setContent(statsPane);
    }

    //------------------------------------------------------------------------------------------------------------------
    // TV - Remote

    private void administrationWindow() {
        scene.setRoot(shop);
    }

    private void shopWindow() {
        scene.setRoot(administration);
    }

    @Override
    public void update() {

    }
}
