package Gui;

import Controller.Controller;
import Model.Arrangement;
import Model.Category;
import Model.Price;
import Model.ProductComponent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class ProductPane extends GridPane {
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final ListView<ProductComponent> lvwProducts = new ListView<>();

    public ProductPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.setMinSize(150,25);
        cbArrangements.setOnAction(event -> selectionChangedArrangement());

        cbCategories.setPromptText("Produkt kategori");
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.setMinSize(150,25);
        cbCategories.setOnAction(event -> selectionChangedCategory());

        lvwProducts.getItems().addAll(Controller.getProducts());

        Button btnUpdate = new Button("Opdater");
        btnUpdate.setOnAction(event -> addOrUpdateAction());

        Button btnDelete = new Button("Slet");
        btnDelete.setOnAction(event -> deleteAction());

        Button btnAdd = new Button("Opret");
        btnAdd.setOnAction(event -> addOrUpdateAction());

        this.add(cbArrangements,1,1);
        this.add(cbCategories,2,1);
        this.add(lvwProducts,1,2, 3, 3);
        this.add(btnUpdate, 4, 2);
        this.add(btnDelete, 4, 3);
        this.add(btnAdd, 4, 4);
    }

    private void selectionChangedCategory() {
        lvwProducts.getItems().clear();
        Category selectedCategory = cbCategories.getSelectionModel().getSelectedItem();

        Arrangement selectedArrangment = cbArrangements.getSelectionModel().getSelectedItem();
        if(selectedArrangment != null){
            for(ProductComponent product : selectedCategory.getProducts()){
                for(Price price : product.getPrices()){
                    if(price.getArrangement() == selectedArrangment){
                        lvwProducts.getItems().add(product);
                    }
                }
            }
        } else{
            lvwProducts.getItems().addAll(selectedCategory.getProducts());
        }
    }

    private void selectionChangedArrangement() {
        lvwProducts.getItems().clear();
        Arrangement selectedArrangement = cbArrangements.getSelectionModel().getSelectedItem();

        Category selectedCategory = cbCategories.getSelectionModel().getSelectedItem();
        if(selectedCategory != null){
            for(Price price : selectedArrangement.getPrices()){
                ProductComponent product = price.getProduct();
                if(product.getCategory() == selectedCategory){
                    lvwProducts.getItems().add(product);
                }
            }
        } else {
            for(Price price : selectedArrangement.getPrices()){
                lvwProducts.getItems().add(price.getProduct());
            }
        }
    }

    private void addOrUpdateAction() {
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        AddOrUpdateProductWindow addOrUpdateWindow = new AddOrUpdateProductWindow(product);
        addOrUpdateWindow.showAndWait();
        lvwProducts.getItems().clear();
        lvwProducts.getItems().addAll(Controller.getProducts());
    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Er du sikker på, at du vil slette dette produkt?");
        alert.showAndWait();
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        Controller.deleteProduct(product);
        lvwProducts.getItems().clear();
        lvwProducts.getItems().addAll(Controller.getProducts());
    }
}
