package Gui;

import Controller.Controller;
import Gui.Administration.Order.OrderPane;
import Gui.Administration.PaymentMethod.PaymentMethodPane;
import Gui.Administration.Product.ProductPane;
import Model.*;
import Model.DiscountStrategy.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    private final Button bNewRental = new Button("+ Udlejning");
    private final Button bNewOrder = new Button("+ Ordre");
    private final Button bAdministration = new Button("Administration");
    private final Button bCancel = new Button("Anuller");
    private final Button bDone = new Button("Færdig");
    private final Button bFinishRental = new Button("Remove");
    private final ToggleButton tbShowRentals = new ToggleButton("Vis Udlejning Ordre");

    private final GridPane gProductDisplay = new GridPane();
    private final GridPane gOrderLineDisplay = new GridPane();
    private final TextField tfTotalPrice = new TextField("* * *");
    private final ScrollPane spOrderLines = new ScrollPane(gOrderLineDisplay);
    private final ListView<Order> lvOpenOrder = new ListView<>();

    private final HashMap<Category,ArrayList<ToggleButton>> hmCategoryProducts = new HashMap<>();
    private final Category all = new Category("* Alle");

    private GridPane initContentOrderScene() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        cbArrangement.getItems().addAll(Controller.getArrangements());
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

        ScrollPane spProductComps = new ScrollPane(gProductDisplay);
        spProductComps.setPrefSize(360,500);
        spProductComps.setFocusTraversable(false);

        spOrderLines.setPrefSize(640,300);
        spOrderLines.setFocusTraversable(false);

        lvOpenOrder.setPrefSize(640,300);
        lvOpenOrder.setFocusTraversable(false);

        Label lTotalPrice = new Label("Total Cost :");

        bNewRental.     setPrefSize(100,25);
        bNewOrder.      setPrefSize(100,25);
        bAdministration.setPrefSize(100,25);
        tbShowRentals.  setPrefSize(150,25);
        bCancel.        setPrefSize(100,25);
        bDone.          setPrefSize(100,25);
        bFinishRental.  setPrefSize(100,25);

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

        cbCategory.     setOnAction(actionEvent -> choseCategory());
        bNewOrder.      setOnAction(actionEvent -> createNewOrder());
        bNewRental.     setOnAction(actionEvent -> createRental());
        bAdministration.setOnAction(actionEvent -> shopWindow());
        tbShowRentals.  setOnAction(actionEvent -> viewRentalOrders());
        bFinishRental.  setOnAction(actionEvent -> finishRental());
        bDone.          setOnAction(actionEvent -> finishOrder());
        bCancel.        setOnAction(actionEvent -> cancelOrder());


        if (cbArrangement.getItems().size() > 0)
            Platform.runLater(() -> cbArrangement.setValue(cbArrangement.getItems().get(0)));

        if (cbCategory.getItems().size() > 0)
            Platform.runLater(() -> cbCategory.setValue(cbCategory.getItems().get(0)));

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

        cbCategory.setDisable(false);
        cbCategory.getItems().add(all);
        cbCategory.setValue(all);

        hmCategoryProducts.clear();
        hmCategoryProducts.put(all,new ArrayList<>());
        for (Price price : arrangement.getPrices()) {
            Category category = price.getProduct().getCategory();
            if (!cbCategory.getItems().contains(category)) {
                cbCategory.getItems().add(category);
                hmCategoryProducts.put(category,new ArrayList<>());
                for (ProductComponent product : category.getProducts()) {
                    ToggleButton bAddProducts = new ToggleButton(product.getName() + "\n" + price.getPrice() + " kr");
                    bAddProducts.setTextAlignment(TextAlignment.CENTER);
                    bAddProducts.setPrefSize(175, 50);
                    bAddProducts.setOnAction(event -> createOrderLine((ToggleButton) event.getSource(),product));
                    hmCategoryProducts.get(all).add(bAddProducts);
                    hmCategoryProducts.get(category).add(bAddProducts);
                }
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

        hmCategoryProducts.clear();
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

    private void viewRentalOrders() {
        if (tbShowRentals.isSelected()) {
            bNewOrder.setDisable(true);
            bNewRental.setDisable(true);
            bAdministration.setDisable(true);
            cbArrangement.setDisable(true);
            bDone.setDisable(true);
            bCancel.setDisable(true);
            cbArrangement.setDisable(true);

            lvOpenOrder.getItems().clear();
            for (Order soonToBeRental : Controller.getOrders()) {
                if (soonToBeRental.getClass() == Rental.class && !soonToBeRental.isFinished()) {
                    lvOpenOrder.getItems().add(soonToBeRental);
                }
            }

            shop.getChildren().remove(spOrderLines);
            shop.add(lvOpenOrder,3,2,4,1);
            shop.add(bFinishRental,4,3);
        } else {
            bNewOrder.setDisable(false);
            bNewRental.setDisable(false);
            bAdministration.setDisable(false);
            cbArrangement.setDisable(false);
            bDone.setDisable(false);
            bCancel.setDisable(false);

            shop.getChildren().remove(bFinishRental);
            shop.getChildren().remove(lvOpenOrder);
            shop.add(spOrderLines,3,2,4,1);
        }
    }

    private void finishRental() {
        Order openOrder = lvOpenOrder.getSelectionModel().getSelectedItem();
        if (openOrder == null) return;
        openOrder.finish();
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
        lKr.setPrefSize(20,30);
        lKr.setOpacity(0);

        ComboBox<String> cbDiscounts = new ComboBox<>();
        cbDiscounts.getItems().addAll("AmountDiscount","PercentageDiscount","RegCustomerDiscount","StudentDiscount","NoDiscount");
        cbDiscounts.setValue(cbDiscounts.getItems().get(4));
        cbDiscounts.setPrefSize(120,30);
        cbDiscounts.setOpacity(0);

        TextField tfPercent = new TextField();
        tfPercent.setPrefSize(60,30);
        tfPercent.setAlignment(Pos.CENTER_RIGHT);
        tfPercent.setOpacity(0);

        Label lPercent = new Label(" %");
        lPercent.setPrefSize(20,30);
        lPercent.setOpacity(0);

        Button bAppend = new Button("+");
        bAppend.setPrefSize(30,30);
        bAppend.setOpacity(0);

        Button bDeduct = new Button("-");
        bDeduct.setPrefSize(30,30);
        bDeduct.setOpacity(0);

        Button bRemove = new Button("X");
        bRemove.setPrefSize(30,30);
        bRemove.setOpacity(0);

        ArrayList<Control> controls = new ArrayList<>(List.of(lName,tfPrice,lKr,cbDiscounts,tfPercent,lPercent,bAppend,bDeduct,bRemove));

        tfPrice.    setOnAction(event -> changePrice(tfPrice,orderLine));
        cbDiscounts.setOnAction(event -> setDiscount(cbDiscounts,orderLine,tfPercent));
        tfPercent.  setOnAction(event -> changeValue(tfPercent,orderLine));
        bAppend.    setOnAction(event -> appendProduct(lName,tfPrice,orderLine));
        bDeduct.    setOnAction(event -> deductProduct(lName,tfPrice,orderLine));
        bRemove.    setOnAction(event -> removeProduct(addButton,orderLine,controls));

        this.controls.put(orderLine,controls);
        updateList();

        for (Control control : controls) {
            fadeIn(control,5);
        }

        tfTotalPrice.setText(selectedOrder.getUpdatedPrice() + " kr");

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

    private void setDiscount(ComboBox<String> cbDiscounts, OrderLine orderLine, TextField tfPercent) {
        int index = cbDiscounts.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        orderLine.setDiscountStrategy(switch (index) {
            case 1 -> new AmountDiscountStrategy(0);
            case 2 -> new PercentageDiscountStrategy(0);
            default -> new NoDiscountStrategy();
            case 4 -> new StudentDiscountStrategy();
            case 5 -> new RegCustomerDiscountStrategy();
        });
        tfPercent.clear();
        shop.requestFocus();
    }

    private void changeValue(TextField tfPercent, OrderLine orderLine) {
        try {
            orderLine.getDiscountStrategy().setValue(Double.parseDouble(tfPercent.getText()));
        } catch (NumberFormatException ignore) {

        }
        shop.requestFocus();
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

    private void removeProduct(ToggleButton addButton, OrderLine orderLine, ArrayList<Control> controls) {
        addButton.setDisable(false);
        addButton.setSelected(false);
        Controller.removeOrderLine(selectedOrder,orderLine);
        selectedOrder.getOrderLines().remove(orderLine);
        gOrderLineDisplay.getChildren().removeAll(controls);
        updateList();
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
        bNewOrder.setText("+ Order");
        bNewRental.setDisable(false);
        bNewRental.setText("+ Rental");
        bAdministration.setDisable(false);
        tbShowRentals.setDisable(false);
        controls.clear();
        gOrderLineDisplay.getChildren().clear();
        cbCategory.getItems().clear();
        cbCategory.setDisable(true);
        hmCategoryProducts.clear();
        gProductDisplay.getChildren().clear();
    }

    private void finishOrder() {
        if (selectedOrder == null || selectedOrder.getOrderLines().isEmpty()) return;

        FinishOrder finishOrder = new FinishOrder(selectedOrder);
        finishOrder.showAndWait();

        if(!finishOrder.isFinished()) return;

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

    //------------------------------------------------------------------------------------------------------------------
    // initAdministration()

    private GridPane initContentAdministrationScene() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        TabPane tabPane = new TabPane();
        this.initTabPane(tabPane);
        gridPane.add(tabPane, 0, 0, 1, 2);
        tabPane.setPrefWidth(850);

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
