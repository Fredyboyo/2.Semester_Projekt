package Gui.Administration.Product;

import Controller.Controller;
import Model.*;
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
    private final Arrangement allArrangements = new Arrangement("Alle");
    private final Arrangement newArrangementArrangement = new Arrangement("Tilføj ny salgssituation");
    private final Category allCategories = new Category("Alle");
    private final Category newCategoryCategory = new Category("Tilføj ny salgssituation");

    public ProductPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.getItems().add(allArrangements);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.getItems().add(newArrangementArrangement);
        cbArrangements.setMinSize(150,25);
        cbArrangements.setOnAction(event -> selectionChangedArrangement());

        cbCategories.setPromptText("Produkt kategori");
        cbCategories.getItems().add(allCategories);
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getItems().add(newCategoryCategory);
        cbCategories.setMinSize(150,25);
        cbCategories.setOnAction(event -> selectionChangedCategory());

        Button btnUpdate = new Button("Opdater");
        btnUpdate.setOnAction(event -> updateAction());
        btnUpdate.setDisable(true);

        Button btnDelete = new Button("Slet");
        btnDelete.setOnAction(event -> deleteAction());
        btnDelete.setDisable(true);

        Button btnAdd = new Button("Opret");
        btnAdd.setOnAction(event -> addAction());

        lvwProducts.getItems().addAll(Controller.getProducts());
        lvwProducts.setPrefWidth(500);
        lvwProducts.setOnMouseClicked(event -> {
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        });

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

        Arrangement selectedArrangement = cbArrangements.getSelectionModel().getSelectedItem();
        if(selectedArrangement != null) {

//            if(selectedArrangement == allArrangements) {
//                for (Order order : Controller.getOrders()) {
//                    if (!(order instanceof Rental)) {
//                        lvwProducts.getItems().add(pr);
//                    }
//                }
//            } else if(selectedArrangement == newArrangementArrangement){
//                AddArrangementWindow addArrangement = new AddArrangementWindow();
//                addArrangement.showAndWait();
//            } else {
//                for(Order order : Controller.getOrders()) {
//                    if (!(order instanceof Rental)) {
//                        if (order.getArrangement() == selectedArrangement) {
//                            lvwProducts.getItems().add(order);
//                        }
//                    }
//                }
//            }

            for(ProductComponent product : selectedCategory.getProducts()){
                for(Price price : product.getPrices()){
                    if(price.getArrangement() == selectedArrangement){
                        lvwProducts.getItems().add(product);
                    }
                }
            }
        } else {
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

    private void addAction(){
        AddProductWindow addProductWindow = new AddProductWindow();
        addProductWindow.showAndWait();
        lvwProducts.getItems().clear();
        lvwProducts.getItems().addAll(Controller.getProducts());
    }

    private void updateAction() {
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        UpdateProductWindow updateWindow = new UpdateProductWindow(product);
        updateWindow.showAndWait();
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
