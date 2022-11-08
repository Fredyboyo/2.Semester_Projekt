package Gui;

import Gui.Administration.Order.OrderPane;
import Gui.Administration.PaymentMethod.PaymentMethodPane;
import Gui.Administration.Product.ProductPane;
import Gui.Shop.ShopWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Gui extends Application {
    private final ShopWindow shopWindow = new ShopWindow(this);
    private final Pane administrationRoot = initContentAdministrationScene();
    private final Pane shopRoot = shopWindow.getRoot();
    private Scene scene;

    @Override
    public void start(Stage stage) {
        scene = new Scene(shopRoot);

        stage.setScene(scene);
        stage.setTitle("Kasseapparatet");
        stage.show();
    }

    //------------------------------------------------------------------------------------------------------------------
    // initAdministration()

    private Pane initContentAdministrationScene() {
        Pane pane = new Pane();

        TabPane tabPane = new TabPane();
        this.initTabPane(tabPane);
        pane.getChildren().add(tabPane);
        tabPane.setPrefWidth(1100);

        Button btnBack = new Button("Tilbage til Køb");
        btnBack.setOnAction(event -> shopWindow());
        pane.getChildren().add(btnBack);
        btnBack.setPrefWidth(150);
        btnBack.setTranslateX(900);
        btnBack.setTranslateY(30);
        return pane;
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
        scene.setRoot(administrationRoot);
    }

    public void shopWindow() {
        scene.setRoot(shopWindow.getRoot());
    }
}
