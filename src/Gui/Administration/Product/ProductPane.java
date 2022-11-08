package Gui.Administration.Product;

import Controller.Controller;
import Gui.Observer;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class ProductPane extends GridPane implements Observer {
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Category allCategoriesCategory = new Category("Alle produktkategorier");
    private final Category createNewCategoryCategory = new Category("Tilføj ny kategori");
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Arrangement allArrangementsArrangement = new Arrangement("Alle salgssituationer");
    private final Arrangement createNewArrangementArrangement = new Arrangement("Tilføj ny salgssituation");
    private final ListView<ProductComponent> lvwProducts = new ListView<>();

    public ProductPane() {
        Controller.addObserver(this);

        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        populateComboBoxes();
        cbArrangements.setMinSize(150, 25);
        cbArrangements.setOnAction(event -> selectionChangedArrangement());
        cbArrangements.getSelectionModel().select(allArrangementsArrangement);

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

        Button btnAddGiftBasket = new Button("Tilføj ny sampakning");
        btnAddGiftBasket.setOnAction(event -> addGiftBasketAction());

        updateProductList();
        lvwProducts.setPrefWidth(500);
        lvwProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            btnUpdate.setDisable(newValue == null);
            btnDelete.setDisable(newValue == null);
        });

        this.add(cbArrangements, 1, 1);
        this.add(cbCategories, 2, 1);
        this.add(lvwProducts, 1, 2, 3, 3);
        this.add(btnUpdate, 4, 2);
        this.add(btnDelete, 4, 3);

        VBox box = new VBox();
        this.add(box, 4, 4);
        box.getChildren().add(btnAdd);
        box.getChildren().add(btnAddGiftBasket);
        box.setSpacing(10);
    }

    private void populateComboBoxes() {
        int arrangementIndex = cbArrangements.getSelectionModel().getSelectedIndex();
        cbArrangements.getItems().clear();
        cbArrangements.getItems().add(allArrangementsArrangement);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.getItems().add(createNewArrangementArrangement);
        cbArrangements.getSelectionModel().select(arrangementIndex);

        int categoryIndex = cbCategories.getSelectionModel().getSelectedIndex();
        cbCategories.getItems().clear();
        cbCategories.getItems().add(allCategoriesCategory);
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getItems().add(createNewCategoryCategory);
        cbCategories.getSelectionModel().select(categoryIndex);
    }

    private void selectionChangedCategory() {
        if(getSelectedCategory() == createNewCategoryCategory){
            AddCategoryWindow newCategoryWindow = new AddCategoryWindow();
            newCategoryWindow.showAndWait();
            cbCategories.getSelectionModel().select(allCategoriesCategory);
        }

        updateProductList();
    }

    private void selectionChangedArrangement() {
        if(getSelectedArrangement() == createNewArrangementArrangement){
            AddArrangementWindow newArrangementWindow = new AddArrangementWindow();
            newArrangementWindow.showAndWait();
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
        AddOrUpdateProductWindow addProductWindow = new AddOrUpdateProductWindow(null);
        addProductWindow.showAndWait();
    }

    private void addGiftBasketAction(){
        AddGiftBasketWindow addGiftBasketWindow = new AddGiftBasketWindow();
        addGiftBasketWindow.showAndWait();
    }

    private void updateAction() {
        ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
        AddOrUpdateProductWindow updateProductWindow = new AddOrUpdateProductWindow(product);
        updateProductWindow.showAndWait();
    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Er du sikker på, at du vil slette dette produkt?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ProductComponent product = lvwProducts.getSelectionModel().getSelectedItem();
            Controller.deleteProduct(product);
        }
    }

    @Override
    public void update() {
        populateComboBoxes();
        updateProductList();
    }
}
