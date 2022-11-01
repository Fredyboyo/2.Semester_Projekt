package Gui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdministrationWindow extends Stage {
    
    public AdministrationWindow(String title, Stage owner) {
        this.initOwner(owner);
        this.setTitle("Architecture Demo");
        BorderPane pane = new BorderPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);
    }

    private void initContent(BorderPane pane) {
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
