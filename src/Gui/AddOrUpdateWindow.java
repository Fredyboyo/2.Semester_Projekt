package Gui;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddOrUpdateWindow extends Stage {

    private TextField txfName = new TextField();
    private TextField txfCategory = new TextField();
    private TextField txfPrice = new TextField();
    private TextField txfContext = new TextField();
    private ProductComponent product;

    public AddOrUpdateWindow(ProductComponent product) {
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
        pane.add(txfCategory, 2, 2);

        Label lblPrice = new Label("Pris");
        pane.add(lblPrice, 1, 3);
        pane.add(txfPrice, 2, 3);

        Label lblContext = new Label("Salgssituation");
        pane.add(lblContext, 1, 4);
        pane.add(txfContext, 2, 4);

        Button btnAddPrice = new Button("Tilføj endnu en pris");
        pane.add(btnAddPrice, 2, 5);

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 3, 6);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 4, 6);
        btnOK.setOnAction(event -> okAction());

        if(product != null){
            lblName.setText(product.getName());
            lblCategory.setText(product.getCategory().getName());
            lblPrice.setText(Double.toString(product.getPrices().get(0).getPrice()));
            lblContext.setText(product.getPrices().get(0).getArrangement().getName());
        }
    }

    private void okAction() {
        String productName = txfName.getText();
        String categoryName = txfCategory.getText();
        double priceFromTextField = Double.parseDouble(txfPrice.getText());
        String contextName = txfContext.getText();

        ProductComponent product;

        Category category = new Category(categoryName);
        if(Controller.getCategories().contains(category)){
            product = Controller.createProduct(productName,category);
        } else{
            Category newCategory = Controller.createCategory(categoryName);
            product = Controller.createProduct(productName, newCategory);
        }

        Arrangement arrangement = new Arrangement(contextName);
        if(Controller.getArrangements().contains(arrangement)){
            Controller.createPrice(product, arrangement, priceFromTextField);
        } else{
            Arrangement newArrangement = Controller.createArrangement(contextName);
            Controller.createPrice(product, newArrangement, priceFromTextField);
        }

        this.close();
    }

    private void cancelAction() {
        this.close();
    }
}
