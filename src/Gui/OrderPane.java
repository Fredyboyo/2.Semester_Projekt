package Gui;

import Controller.Controller;
import Model.Arrangement;
import Model.Category;
import Model.Order;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class OrderPane extends GridPane {

    private OrderWindow orderWindow;
    private ListView<Order> lvwOrders = new ListView<>();

    public OrderPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        DatePicker datePicker = new DatePicker();

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);

        lvwOrders.getItems().addAll(Controller.getOrders());

        lvwOrders.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    Order order = lvwOrders.getSelectionModel().getSelectedItem();
                    orderWindow = new OrderWindow(order);
                }
            }
        });

        this.add(datePicker, 1, 1);
        this.add(arrangementComboBox,2,1);
        this.add(categoryComboBox,3,1);
        this.add(lvwOrders,1,2, 3, 3);
    }
}
