package Gui.Administration.Product;

import Controller.Controller;
import Gui.Observer;
import Model.Arrangement;
import Model.Category;
import Model.ProductComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddProductWindow extends Stage implements Observer {

    private final TextField txfName = new TextField();
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Category createNewCategoryCategory = new Category("Tilføj ny kategori");
    private final TextField txfPrice = new TextField();
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Arrangement createNewArrangementArrangement = new Arrangement("Tilføj ny salgssituation");
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");
    private final TextField txfSecondPrice = new TextField();
    private final ComboBox<Arrangement> cbArrangementsSecondPrice = new ComboBox<>();

    public AddProductWindow() {
        Controller.addObserver(this);

        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);
        this.setTitle("Nyt produkt");

        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane pane) {
        pane.setPadding(new Insets(25));
        pane.setVgap(10);
        pane.setHgap(10);

        Label lblName = new Label("Navn");
        pane.add(lblName, 1, 1);
        pane.add(txfName, 2, 1);

        Label lblCategory = new Label("Produkt kategori");
        pane.add(lblCategory, 1, 2);
        pane.add(cbCategories, 2, 2);
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getItems().add(createNewCategoryCategory);
        cbCategories.setPromptText("Vælg kategori");
        cbCategories.setOnAction(event -> selectionChangedCategory());

        Label lblPrice = new Label("Pris");
        pane.add(lblPrice, 1, 3);
        pane.add(txfPrice, 2, 3);
        pane.add(cbArrangements, 3, 3, 2, 1);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.getItems().add(createNewArrangementArrangement);
        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.setOnAction(event -> selectionChangedArrangement());

        pane.add(btnAddPrice, 2, 4);
        btnAddPrice.setOnAction(event -> addPriceAction(pane));

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 3, 6);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 4, 6);
        btnOK.setOnAction(event -> okAction());
    }

    private void selectionChangedCategory() {
        if(cbCategories.getSelectionModel().getSelectedItem() == createNewCategoryCategory){
            AddCategoryWindow newCategoryWindow = new AddCategoryWindow();
            newCategoryWindow.showAndWait();
        }
    }

    private void selectionChangedArrangement() {
        if(cbArrangements.getSelectionModel().getSelectedItem() == createNewArrangementArrangement){
            AddArrangementWindow newArrangementWindow = new AddArrangementWindow();
            newArrangementWindow.showAndWait();
        }
    }

    private void addPriceAction(GridPane pane) {
        Label lblPrice = new Label("Pris");
        pane.add(lblPrice, 1, 4);
        pane.add(txfSecondPrice, 2, 4);
        pane.add(cbArrangementsSecondPrice, 3, 4, 2, 1);
        cbArrangementsSecondPrice.getItems().addAll(Controller.getArrangements());
        cbArrangementsSecondPrice.setPromptText("Salgssituation");
        btnAddPrice.setVisible(false);
    }

    private void okAction() {
        String productName = txfName.getText();
        Category category = cbCategories.getSelectionModel().getSelectedItem();
        String priceFromTextField = txfPrice.getText();
        Arrangement arrangement = cbArrangements.getSelectionModel().getSelectedItem();
        String secondPriceFromTextField = txfSecondPrice.getText();
        Arrangement arrangementSecondPrice = cbArrangementsSecondPrice.getSelectionModel().getSelectedItem();
        double price;

        try {
            price = Double.parseDouble(priceFromTextField);
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Prisen skal være et tal");
            alert.showAndWait();
            return;
        }

        if (productName.isEmpty() || category == null || priceFromTextField.isEmpty() || arrangement == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Alle felterne skal udfyldes");
            alert.showAndWait();
        } else {
            ProductComponent newProduct = Controller.createProduct(productName, category);
            Controller.createPrice(newProduct, arrangement, price);
            secondPriceAdded(secondPriceFromTextField, arrangementSecondPrice, newProduct);
        }

        this.close();
    }

    private void secondPriceAdded(
            String secondPriceFromTextField,
            Arrangement arrangementSecondPrice,
            ProductComponent product) {
        if (!secondPriceFromTextField.isEmpty() && arrangementSecondPrice != null) {
            double secondPrice;
            try {
                secondPrice = Double.parseDouble(secondPriceFromTextField);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Prisen skal være et tal");
                alert.showAndWait();
                return;
            }
            Controller.createPrice(product, arrangementSecondPrice, secondPrice);
        }
    }

    private void cancelAction() {
        this.close();
    }

    @Override
    public void update() {

    }
}
