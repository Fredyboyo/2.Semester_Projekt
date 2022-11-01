package Gui;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddOrUpdateProductWindow extends Stage {

    private TextField txfName = new TextField();
    private ComboBox<Category> cbCategories = new ComboBox<>();
    private TextField txfPrice = new TextField();
    private ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private ProductComponent product;

    public AddOrUpdateProductWindow(ProductComponent product) {
        this.product = product;
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);

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

        Label lblPrice = new Label("Pris");
        pane.add(lblPrice, 1, 3);
        pane.add(txfPrice, 2, 3);

        Label lblContext = new Label("Salgssituation");
        pane.add(lblContext, 1, 4);
        pane.add(cbArrangements, 2, 4);
        cbArrangements.getItems().addAll(Controller.getArrangements());

        Button btnAddPrice = new Button("Tilføj endnu en pris");
        pane.add(btnAddPrice, 2, 5);

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 3, 6);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 4, 6);
        btnOK.setOnAction(event -> okAction());

        if (product != null) {
            txfName.setText(product.getName());
            cbCategories.getSelectionModel().select(product.getCategory());
            txfPrice.setText(Double.toString(product.getPrices().get(0).getPrice()));
            cbArrangements.getSelectionModel().select(product.getPrices().get(0).getArrangement());
        }
    }

    private void okAction() {
        String productName = txfName.getText();
        Category category = cbCategories.getSelectionModel().getSelectedItem();
        double priceFromTextField = Double.parseDouble(txfPrice.getText());
        Arrangement arrangement = cbArrangements.getSelectionModel().getSelectedItem();

        if (product == null) {
            ProductComponent newProduct = Controller.createProduct(productName, category);
            Controller.createPrice(newProduct, arrangement, priceFromTextField);
        } else {
            Controller.updateProduct(product, productName, category);
        }
        this.close();

    }

    private void cancelAction() {
        this.close();
    }
}
