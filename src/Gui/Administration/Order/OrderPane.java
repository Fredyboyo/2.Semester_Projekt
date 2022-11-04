package Gui.Administration.Order;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrderPane extends GridPane {
    private final DatePicker datePicker = new DatePicker();
    private LocalDate selectedDate;
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Category allCategories = new Category("Alle produktkategorier");
    private Category selectedCategory;
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Arrangement allArrangements = new Arrangement("Alle salgssituationer");
    private Arrangement selectedArrangement;
    private final ListView<Order> lvwOrders = new ListView<>();

    public OrderPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(event -> updateOrderList());
        updateOrderList();

        cbArrangements.getItems().add(allArrangements);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.setMinSize(150, 25);
        cbArrangements.setOnAction(event -> updateOrderList());
        cbArrangements.getSelectionModel().select(allArrangements);

        cbCategories.getItems().add(allCategories);
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.setMinSize(150, 25);
        cbCategories.setOnAction(event -> updateOrderList());
        cbCategories.getSelectionModel().select(allCategories);

        lvwOrders.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    Order order = lvwOrders.getSelectionModel().getSelectedItem();
                    OrderWindow orderWindow = new OrderWindow(order);
                    orderWindow.show();
                }
            }
        });

        this.add(datePicker, 1, 1);
        this.add(cbArrangements, 2, 1);
        this.add(cbCategories, 3, 1);
        this.add(lvwOrders, 1, 2, 3, 3);
    }

    private void updateOrderList(){
        ArrayList<Order> filteredOrders = new ArrayList<>();

        for(Order order : Controller.getOrdersNotRental()){

            boolean isSelectedDateMatched = order.getDate().toLocalDate().isEqual(datePicker.getValue());

            boolean isArrangementMatched = selectedArrangement == allArrangements || order.getArrangement() == selectedArrangement;

            boolean isCategoryMatched = selectedCategory == allCategories;
            for(OrderLine orderLine : order.getOrderLines()){
                if(orderLine.getProduct().getCategory() == selectedCategory){
                    isCategoryMatched = true;
                }
            }

            if(isSelectedDateMatched && isArrangementMatched && isCategoryMatched){
                filteredOrders.add(order);
            }
        }

        lvwOrders.getItems().clear();
        lvwOrders.getItems().addAll(filteredOrders);
    }
}
