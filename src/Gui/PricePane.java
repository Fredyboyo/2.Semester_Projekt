package Gui;

import Controller.Controller;
import Model.Arrangement;
import Model.Category;
import Model.ProductComponent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class PricePane extends GridPane {

    private AddOrUpdateWindow addOrUpdateWindow;
    private ListView<ProductComponent> lvwProducts = new ListView<>();

    public PricePane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        ComboBox<Arrangement> arrangementComboBox = new ComboBox<>();
        arrangementComboBox.getItems().addAll(Controller.getArrangements());
        arrangementComboBox.setMinSize(150,25);

        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(Controller.getCategories());
        categoryComboBox.setMinSize(150,25);

        lvwProducts.getItems().addAll(Controller.getProducts());

        Button btnUpdate = new Button("Opdater");
        btnUpdate.setOnAction(event -> updateAction());

        Button btnDelete = new Button("Slet");
        btnDelete.setOnAction(event -> deleteAction());

        Button btnAdd = new Button("Opret");
        btnAdd.setOnAction(event -> addAction());

        this.add(arrangementComboBox,1,1);
        this.add(categoryComboBox,2,1);
        this.add(lvwProducts,1,2, 3, 3);
        this.add(btnUpdate, 4, 2);
        this.add(btnDelete, 4, 3);
        this.add(btnAdd, 4, 4);

        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        addOrUpdateWindow = new AddOrUpdateWindow(product);
    }

    private void updateAction() {
        addOrUpdateWindow.showAndWait();
        lvwProducts.getItems().addAll(Controller.getProducts());
    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Er du sikker på, at du vil slette dette produkt?");
        alert.showAndWait();
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        Controller.deleteProduct(product);
        lvwProducts.getItems().addAll(Controller.getProducts());
    }

    private void addAction() {
        addOrUpdateWindow.showAndWait();
        lvwProducts.getItems().addAll(Controller.getProducts());
    }
}
