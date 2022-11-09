package Gui.Administration.Product;

import Controller.Controller;
import Gui.Observer;
import Model.Arrangement;
import Model.Category;
import Model.GiftBasket;
import Model.ProductComponent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;

public class AddGiftBasketWindow extends Stage implements Observer {

    private final TextField txfName = new TextField();
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");
    private final VBox vBoxPrices = new VBox();
    private HashMap<TextField[], ComboBox<Arrangement>> priceMap = new HashMap<>();
    private final Arrangement createNewArrangementArrangement = new Arrangement("Tilføj ny salgssituation");
    private final Button btnAddProduct = new Button("Tilføj endnu et produkt");
    private final VBox vBoxProducts = new VBox();
    private final HashMap<ComboBox<ProductComponent>, TextField> productMap = new HashMap<>();

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

        pane.add(vBoxPrices, 1, 2, 3, 1);
        vBoxPrices.setSpacing(10);
        pane.add(btnAddPrice, 1, 3);
        btnAddPrice.setOnAction(event -> addPriceAction());

        pane.add(vBoxProducts, 1, 4, 3, 1);
        vBoxProducts.setSpacing(10);
        pane.add(btnAddProduct, 1, 5);
        btnAddProduct.setOnAction(event -> addProductAction());

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 4, 7);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 5, 7);
        btnOK.setOnAction(event -> okAction());

        addPriceAction();
        addProductAction();
    }

    private void populateComboBoxes() {
        for (ComboBox<Arrangement> cbArrangements : priceMap.values()) {
            int index = cbArrangements.getSelectionModel().getSelectedIndex();
            cbArrangements.getItems().clear();
            cbArrangements.getItems().addAll(Controller.getArrangements());
            cbArrangements.getItems().add(createNewArrangementArrangement);
            cbArrangements.getSelectionModel().select(index);
        }

        for (ComboBox<ProductComponent> cbProducts : productMap.keySet()) {
            int index = cbProducts.getSelectionModel().getSelectedIndex();
            cbProducts.getItems().clear();
            cbProducts.getItems().addAll(Controller.getProducts());
            cbProducts.getSelectionModel().select(index);
        }
    }

    private void selectionChangedArrangement(ComboBox<Arrangement> source) {
        if (source.getSelectionModel().getSelectedItem() == createNewArrangementArrangement) {
            AddArrangementWindow newArrangementWindow = new AddArrangementWindow();
            newArrangementWindow.showAndWait();
        }
    }

    private void addPriceAction() {
        TextField txfPrice = new TextField();
        TextField txfClip = new TextField();
        ComboBox<Arrangement> cbArrangements = new ComboBox<>();
        TextField[] textArray = new TextField[2];
        textArray[0] = txfPrice;
        textArray[1] = txfClip;
        priceMap.put(textArray, cbArrangements);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label lblPrice = new Label("Pris");
        hBox.getChildren().add(lblPrice);
        hBox.getChildren().add(txfPrice);
        txfPrice.setPromptText("Kr");
        hBox.getChildren().add(txfClip);
        txfClip.setPromptText("Antal klip");
        hBox.getChildren().add(cbArrangements);
        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.setOnAction(event -> selectionChangedArrangement((ComboBox<Arrangement>) event.getSource()));

        vBoxPrices.getChildren().add(hBox);
        this.sizeToScene();

        populateComboBoxes();
    }

    private void addProductAction() {
        ComboBox<ProductComponent> cbProducts = new ComboBox<>();
        cbProducts.setPromptText("Vælg produkt");
        TextField txfAmount = new TextField();
        txfAmount.setPromptText("Antal");
        productMap.put(cbProducts, txfAmount);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label lblProduct = new Label("Produkt");
        hBox.getChildren().add(lblProduct);
        hBox.getChildren().add(cbProducts);
        hBox.getChildren().add(txfAmount);

        vBoxProducts.getChildren().add(hBox);
        this.sizeToScene();

        populateComboBoxes();
    }

    private void okAction() {
        String productName = txfName.getText();

        boolean isAllArrangementsSpecified = true;
        for (var entry : priceMap.entrySet()) {
            String priceFromTextField = entry.getKey()[0].getText();
            String clipsFromTextField = entry.getKey()[1].getText();

            try {
                Double.parseDouble(priceFromTextField);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Prisen skal være et tal");
                alert.showAndWait();
                return;
            }

            try {
                Integer.parseInt(clipsFromTextField);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Antal klip skal være et tal");
                alert.showAndWait();
                return;
            }

            Arrangement selectedArrangement = entry.getValue().getSelectionModel().getSelectedItem();
            if (selectedArrangement == null) {
                isAllArrangementsSpecified = false;
            }
        }

        for(var entry : productMap.entrySet()){
            String amountFromTextField = entry.getValue().getText();

            try {
                Integer.parseInt(amountFromTextField);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Produktantallet skal være et tal");
                alert.showAndWait();
                return;
            }

            ProductComponent selectedProduct = entry.getKey().getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                isAllArrangementsSpecified = false;
            }
        }

        if (productName.isEmpty() || !isAllArrangementsSpecified) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Alle felterne skal udfyldes");
            alert.showAndWait();
        } else {
            Category category = null;
            for (Category giftBasketCategory : Controller.getCategories()) {
                if (giftBasketCategory.getName().equals("Sampakninger")) {
                    category = giftBasketCategory;
                }
            }

            GiftBasket giftBasket = Controller.createGiftBasket(productName, category);

            for (var entry : priceMap.entrySet()) {
                String priceFromTextField = entry.getKey()[0].getText();
                double price = Double.parseDouble(priceFromTextField);
                String clipsFromTextField = entry.getKey()[1].getText();
                Integer clips = Integer.parseInt(clipsFromTextField);
                Arrangement selectedArrangement = entry.getValue().getSelectionModel().getSelectedItem();
                Controller.createProductPrice(giftBasket, selectedArrangement, price, clips);
            }

            for (var entry : productMap.entrySet()) {
                ProductComponent selectedProduct = entry.getKey().getSelectionModel().getSelectedItem();
                String amountFromTextField = entry.getValue().getText();
                int amount = Integer.parseInt(amountFromTextField);
                for(int i = 0; i <= amount; i++){
                    Controller.addProductToGiftBasket(giftBasket, selectedProduct);
                }
            }
        }

        this.close();
    }

    private void cancelAction() {
        this.close();
    }

    @Override
    public void update() {
        populateComboBoxes();
    }
}
