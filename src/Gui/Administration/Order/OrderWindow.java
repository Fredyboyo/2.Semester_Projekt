package Gui.Administration.Order;

import Model.Order;
import Model.OrderLine;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OrderWindow extends Stage {
    private final Order order;
    private final ListView<OrderLine> lvwOrderLines = new ListView<>();

    public OrderWindow(Order order){
        this.order = order;
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);
        this.setTitle("Ordrelinjer");

        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(25));
        pane.setVgap(10);
        pane.setHgap(10);

        pane.add(lvwOrderLines, 1, 1);
        lvwOrderLines.getItems().addAll(order.getOrderLines());
        lvwOrderLines.setPrefWidth(400);
    }
}
