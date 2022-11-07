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

import java.util.ArrayList;

public class AddGiftBasketWindow extends Stage implements Observer {

    private final TextField txfName = new TextField();
    private final TextField txfPrice = new TextField();
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");
    private final TextField txfSecondPrice = new TextField();
    private final ComboBox<Arrangement> cbArrangementsSecondPrice = new ComboBox<>();
    private final ComboBox<ProductComponent> cbProducts = new ComboBox<>();

    public AddGiftBasketWindow() {
        Controller.addObserver(this);

        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);
        this.setTitle("Ny sampakning");

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

        Label lblPrice = new Label("Pris");
        pane.add(lblPrice, 1, 2);
        pane.add(txfPrice, 2, 2);
        pane.add(cbArrangements, 3, 2, 2, 1);
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.setPromptText("Salgssituation");

        pane.add(btnAddPrice, 2, 3);
        btnAddPrice.setOnAction(event -> addPriceAction(pane));

        Label lblProducts = new Label("Produkter");
        pane.add(lblProducts, 1, 4);
        pane.add(cbProducts, 2, 4);

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 3, 6);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 4, 6);
        btnOK.setOnAction(event -> okAction());
    }

    private void addPriceAction(GridPane pane) {
        Label lblPrice = new Label("Pris");
        pane.add(lblPrice, 1, 3);
        pane.add(txfSecondPrice, 2, 3);
        pane.add(cbArrangementsSecondPrice, 3, 3, 2, 1);
        cbArrangementsSecondPrice.getItems().addAll(Controller.getArrangements());
        cbArrangementsSecondPrice.setPromptText("Salgssituation");
        btnAddPrice.setVisible(false);
    }

    private void okAction() {
        String productName = txfName.getText();
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

        if (productName.isEmpty() || priceFromTextField.isEmpty() || arrangement == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Alle felterne skal udfyldes");
            alert.showAndWait();
        } else {
            Category category = null;
            for(Category cat : Controller.getCategories()){
                if(cat.getName().equals("Sampakning")){
                    category = cat;
                }
            }
            ArrayList<ProductComponent> products = new ArrayList<>();
            ProductComponent newGiftBasket = Controller.createGiftBasket(productName, category, products);
            Controller.createPrice(newGiftBasket, arrangement, price);
            secondPriceAdded(secondPriceFromTextField, arrangementSecondPrice, newGiftBasket);
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
