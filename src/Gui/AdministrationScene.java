package Gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AdministrationScene extends Stage {
    
    public AdministrationScene(String title, Stage owner) {
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

        Tab prisliste = new Tab("Prisliste");
        tabPane.getTabs().add(prisliste);
        PricePane pricePane = new PricePane();
        prisliste.setContent(pricePane);

        Tab salg = new Tab("Salg");
        tabPane.getTabs().add(salg);
        OrderPane orderPane = new OrderPane();
        salg.setContent(orderPane);

        Tab etc = new Tab("Etc.");
        tabPane.getTabs().add(etc);
        EtcPane etcPane = new EtcPane();
        etc.setContent(etcPane);
    }
}
