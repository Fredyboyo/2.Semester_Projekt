package Gui.Administration.Product;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ProductPane extends GridPane {
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Category allCategoriesCategory = new Category("Alle produktkategorier");
    private final Category createNewCategoryCategory = new Category("Tilføj ny kategori");
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Arrangement allArrangementsArrangement = new Arrangement("Alle salgssituationer");
    private final Arrangement createNewArrangementArrangement = new Arrangement("Tilføj ny salgssituation");
    private final ListView<ProductComponent> lvwProducts = new ListView<>();

    public ProductPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        cbArrangements.getItems().add(allArrangementsArrangement);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.getItems().add(createNewArrangementArrangement);
        cbArrangements.setMinSize(150, 25);
        cbArrangements.setOnAction(event -> selectionChangedArrangement());
        cbArrangements.getSelectionModel().select(allArrangementsArrangement);

        cbCategories.getItems().add(allCategoriesCategory);
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getItems().add(createNewCategoryCategory);
        cbCategories.setMinSize(150, 25);
        cbCategories.setOnAction(event -> selectionChangedCategory());
        cbCategories.getSelectionModel().select(allCategoriesCategory);

        Button btnUpdate = new Button("Opdater produkt");
        btnUpdate.setOnAction(event -> updateAction());
        btnUpdate.setDisable(true);

        Button btnDelete = new Button("Slet produkt");
        btnDelete.setOnAction(event -> deleteAction());
        btnDelete.setDisable(true);

        Button btnAdd = new Button("Tilføj nyt produkt");
        btnAdd.setOnAction(event -> addAction());

        lvwProducts.getItems().addAll(Controller.getProducts());
        lvwProducts.setPrefWidth(500);
        lvwProducts.setOnMouseClicked(event -> {
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        });

        this.add(cbArrangements, 1, 1);
        this.add(cbCategories, 2, 1);
        this.add(lvwProducts, 1, 2, 3, 3);
        this.add(btnUpdate, 4, 2);
        this.add(btnDelete, 4, 3);
        this.add(btnAdd, 4, 4);
    }

    private void selectionChangedCategory() {
        if(getSelectedCategory() == createNewCategoryCategory){
            AddCategoryWindow newCategoryWindow = new AddCategoryWindow();
            newCategoryWindow.showAndWait();
            Category newCategory = newCategoryWindow.getNewCategory();
            cbCategories.getItems().add(cbCategories.getItems().indexOf(createNewCategoryCategory), newCategory);
            cbCategories.getSelectionModel().select(allCategoriesCategory);
        }

        updateProductList();
    }

    private void selectionChangedArrangement() {
        if(getSelectedArrangement() == createNewArrangementArrangement){
            AddArrangementWindow newArrangementWindow = new AddArrangementWindow();
            newArrangementWindow.showAndWait();
            Arrangement newArrangement = newArrangementWindow.getNewArrangement();
            cbArrangements.getItems().add(cbArrangements.getItems().indexOf(createNewArrangementArrangement), newArrangement);
            cbArrangements.getSelectionModel().select(allArrangementsArrangement);
        }

        updateProductList();
    }

    private void updateProductList(){
        ArrayList<ProductComponent> filteredProducts = new ArrayList<>();

        for(ProductComponent product : Controller.getProducts()){

            boolean isCategoryMatched = getSelectedCategory() == allCategoriesCategory || product.getCategory() == getSelectedCategory();

            boolean isArrangementMatched = getSelectedArrangement() == allArrangementsArrangement;
            for(Price price : product.getPrices()){
                if(price.getArrangement() == getSelectedArrangement()){
                    isArrangementMatched = true;
                }
            }

            if(isArrangementMatched && isCategoryMatched){
                filteredProducts.add(product);
            }
        }

        lvwProducts.getItems().clear();
        lvwProducts.getItems().addAll(filteredProducts);
    }

    private Category getSelectedCategory() {
        return cbCategories.getSelectionModel().getSelectedItem();
    }

    private Arrangement getSelectedArrangement() {
        return cbArrangements.getSelectionModel().getSelectedItem();
    }

    private void addAction() {
        AddProductWindow addProductWindow = new AddProductWindow();
        addProductWindow.showAndWait();
        lvwProducts.getItems().clear();
        selectionChangedArrangement();
        selectionChangedCategory();
        if(addProductWindow.getNewCategory() != null){
            cbCategories.getItems().add(cbCategories.getItems().indexOf(createNewCategoryCategory), addProductWindow.getNewCategory());
        }
        if(addProductWindow.getNewArrangement() != null){
            cbArrangements.getItems().add(cbArrangements.getItems().indexOf(createNewArrangementArrangement), addProductWindow.getNewArrangement());
        }
    }

    private void updateAction() {
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        UpdateProductWindow updateProductWindow = new UpdateProductWindow(product);
        updateProductWindow.showAndWait();

        lvwProducts.getItems().clear();
        selectionChangedArrangement();
        selectionChangedCategory();
        if(updateProductWindow.getNewCategory() != null){
            cbCategories.getItems().add(cbCategories.getItems().indexOf(createNewCategoryCategory), updateProductWindow.getNewCategory());
        }
        if(updateProductWindow.getNewArrangement() != null){
            cbArrangements.getItems().add(cbArrangements.getItems().indexOf(createNewArrangementArrangement), updateProductWindow.getNewArrangement());
        }
    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Er du sikker på, at du vil slette dette produkt?");
        alert.showAndWait();
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        Controller.deleteProduct(product);
        lvwProducts.getItems().clear();
        selectionChangedArrangement();
        selectionChangedCategory();
    }
}
