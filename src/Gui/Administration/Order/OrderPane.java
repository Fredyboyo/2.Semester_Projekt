package Gui.Administration.Order;

import Controller.Controller;
import Gui.Observer;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrderPane extends GridPane implements Observer {
    private final DatePicker datePicker = new DatePicker();
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Category allCategories = new Category("Alle produktkategorier");
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Arrangement allArrangements = new Arrangement("Alle salgssituationer");
    private final ListView<Order> lvwOrders = new ListView<>();

    public OrderPane() {
        Controller.addObserver(this);

        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(event -> updateOrderList());
        updateOrderList();

        populateComboBoxes();
        cbArrangements.setMinSize(150, 25);
        cbArrangements.setOnAction(event -> updateOrderList());
        cbArrangements.getSelectionModel().select(allArrangements);

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
        this.add(lvwOrders, 1, 2, 3, 1);

        Label lblToolTip = new Label("Dobbeltklik på et salg for at få vist oversigt over solgte produkter og antal");
        this.add(lblToolTip, 1, 3, 3, 1);
    }

    private void populateComboBoxes() {
        int arrangementIndex = cbArrangements.getSelectionModel().getSelectedIndex();
        cbArrangements.getItems().clear();
        cbArrangements.getItems().add(allArrangements);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.getSelectionModel().select(arrangementIndex);

        int categoryIndex = cbCategories.getSelectionModel().getSelectedIndex();
        cbCategories.getItems().clear();
        cbCategories.getItems().add(allCategories);
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getSelectionModel().select(categoryIndex);
    }

    private void updateOrderList() {
        ArrayList<Order> filteredOrders = new ArrayList<>();

        for (Order order : Controller.getOrdersNotRental()) {

            boolean isSelectedDateMatched = order.getTimestamp().toLocalDate().isEqual(datePicker.getValue());

            boolean isArrangementMatched = getSelectedArrangement() == allArrangements || order.getArrangement() == getSelectedArrangement();

            boolean isCategoryMatched = getSelectedCategory() == allCategories;
            for (OrderLine orderLine : order.getOrderLines()) {
                if (orderLine.getProduct().getCategory() == getSelectedCategory()) {
                    isCategoryMatched = true;
                }
            }

            if (isSelectedDateMatched && isArrangementMatched && isCategoryMatched) {
                filteredOrders.add(order);
            }
        }

        lvwOrders.getItems().clear();
        lvwOrders.getItems().addAll(filteredOrders);
    }

    private Category getSelectedCategory() {
        Category selectedCategory = cbCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            return allCategories;
        }
        return selectedCategory;
    }

    private Arrangement getSelectedArrangement() {
        Arrangement selectedArrangement = cbArrangements.getSelectionModel().getSelectedItem();
        if (selectedArrangement == null) {
            return allArrangements;
        }
        return selectedArrangement;
    }

    @Override
    public void update() {
        populateComboBoxes();
        updateOrderList();
    }
}
