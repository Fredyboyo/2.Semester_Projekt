package Gui;

import Controller.Controller;
import Gui.Administration.Order.OrderPane;
import Gui.Administration.PaymentMethod.PaymentMethodPane;
import Gui.Administration.Product.ProductPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Gui extends Application {
    private final ShopWindow shopWindow = new ShopWindow(this);
    private GridPane administration;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        administration = initContentAdministrationScene();
        scene = new Scene(shopWindow.getRoot());

        stage.setScene(scene);
        stage.setTitle("BREW-BREW");
        stage.show();
    }

    @Override
    public void init() {
        Controller.addObserver(shopWindow);
    }



    //------------------------------------------------------------------------------------------------------------------
    // initAdministration()

    private GridPane initContentAdministrationScene() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);

        TabPane tabPane = new TabPane();
        this.initTabPane(tabPane);
        gridPane.add(tabPane, 0, 0, 1, 2);
        tabPane.setPrefWidth(1050);

        Button btnBack = new Button("<");
        btnBack.setOnAction(event -> shopWindow());
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

    public void administrationWindow() {
        scene.setRoot(administration);
    }

    public void shopWindow() {
        scene.setRoot(shopWindow.getRoot());
    }
}
