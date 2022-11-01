package Gui;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class OrderPane extends GridPane {

    private OrderWindow orderWindow;
    DatePicker datePicker = new DatePicker();
    ComboBox<Category> cbCategories = new ComboBox<>();
    ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private ListView<Order> lvwOrders = new ListView<>();

    public OrderPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(event -> dateChanged());
        dateChanged();

        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.setMinSize(150,25);
        cbArrangements.setOnAction(event -> selectionChangedArrangement());

        cbCategories.setPromptText("Produkt kategori");
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.setMinSize(150,25);
        cbCategories.setOnAction(event -> selectionChangedCategory());

        lvwOrders.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if(mouseEvent.getClickCount() == 2){
                    Order order = lvwOrders.getSelectionModel().getSelectedItem();
                    orderWindow = new OrderWindow(order);
                }
            }
        });

        this.add(datePicker, 1, 1);
        this.add(cbArrangements,2,1);
        this.add(cbCategories,3,1);
        this.add(lvwOrders,1,2, 3, 3);
    }

    private void dateChanged() {
        lvwOrders.getItems().clear();
        LocalDate selectedDate = datePicker.getValue();
        for(Order order : Controller.getOrders()){
            if(order.getDate().toLocalDate().isEqual(selectedDate)){
                lvwOrders.getItems().add(order);
            }
        }
    }

    private void selectionChangedCategory() {
        lvwOrders.getItems().clear();
        Category selectedCategory = cbCategories.getSelectionModel().getSelectedItem();

        for(Order order : Controller.getOrders()){
            for(OrderLine orderLine : order.getOrderLines()){
                if(orderLine.getProduct().getCategory() == selectedCategory){
                    lvwOrders.getItems().add(order);
                }
            }
        }
    }

    private void selectionChangedArrangement() {
        lvwOrders.getItems().clear();
        Arrangement selectedArrangement = cbArrangements.getSelectionModel().getSelectedItem();

        for(Order order : Controller.getOrders()){
            if(order.getArrangement() == selectedArrangement){
                lvwOrders.getItems().add(order);
            }
        }
    }
}
