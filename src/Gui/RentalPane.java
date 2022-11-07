package Gui;

import Controller.Controller;
import Gui.Administration.Order.OrderWindow;
import Model.Order;
import Model.Rental;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class RentalPane extends GridPane {

    private final CheckBox cbIsCompleted = new CheckBox("Vis kun udlejninger, som endnu ikke er afleveret");
    private final ListView<Order> lvwRentals = new ListView<>();

    public RentalPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        cbIsCompleted.setOnAction(event -> checkBoxChanged());

        lvwRentals.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    Order order = lvwRentals.getSelectionModel().getSelectedItem();
                    OrderWindow orderWindow = new OrderWindow(order);
                    orderWindow.show();
                }
            }
        });
        lvwRentals.setPrefWidth(500);

        this.add(cbIsCompleted, 2, 1);
        this.add(lvwRentals, 1, 2, 3, 3);
        updateRentalList();
    }

    private void updateRentalList() {
        lvwRentals.getItems().clear();
        lvwRentals.getItems().addAll(Controller.getRentals());
    }

    private void checkBoxChanged() {
        if (cbIsCompleted.isSelected()) {
            lvwRentals.getItems().clear();
            for (Order order : Controller.getRentals()) {
                if (!order.isFinished()) {
                    lvwRentals.getItems().add(order);
                }
            }
        } else {
            updateRentalList();
        }
    }
}