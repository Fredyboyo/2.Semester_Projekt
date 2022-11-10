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

import java.util.ArrayList;
import java.util.HashMap;

public class AddGiftBasketWindow extends Stage implements Observer {

    private final TextField txfName = new TextField();
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");
    private final VBox vBoxPrices = new VBox();
    private final ArrayList<PriceLineComponents> priceLineComponents = new ArrayList<>();
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

        TitledPane titledPanePrices = new TitledPane("Priser", vBoxPrices);
        pane.add(titledPanePrices, 1, 3, 3, 1);
        titledPanePrices.setCollapsible(false);
        vBoxPrices.setSpacing(10);
        pane.add(btnAddPrice, 1, 4);
        btnAddPrice.setOnAction(event -> addPriceAction());

        TitledPane titledPaneProducts = new TitledPane("Produkter", vBoxProducts);
        pane.add(titledPaneProducts, 1, 6, 3, 1);
        titledPaneProducts.setCollapsible(false);
        vBoxProducts.setSpacing(10);
        pane.add(btnAddProduct, 1, 7);
        btnAddProduct.setOnAction(event -> addProductAction());

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 4, 9);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 5, 9);
        btnOK.setOnAction(event -> okAction());

        addPriceAction();
        addProductAction();
    }

    private void populateComboBoxes() {
        for (PriceLineComponents priceLineComponents : priceLineComponents) {
            ComboBox<Arrangement> cbArrangements = priceLineComponents.getCbArrangements();
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
            source.getSelectionModel().select(source.getItems().size() - 2);
        }
    }

    private void addPriceAction() {
        TextField txfPrice = new TextField();
        TextField txfClip = new TextField();
        ComboBox<Arrangement> cbArrangements = new ComboBox<>();
        priceLineComponents.add(new PriceLineComponents(txfPrice, txfClip, cbArrangements));

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
        for (PriceLineComponents priceLineComponents : priceLineComponents) {
            String priceFromTextField = priceLineComponents.getTxfPrice().getText();
            String clipsFromTextField = priceLineComponents.getTxfClips().getText();

            try {
                Double.parseDouble(priceFromTextField);
            } catch (NumberFormatException ex) {
                showAlert("Prisen skal være et tal");
                return;
            }

            try {
                Integer.parseInt(clipsFromTextField);
            } catch (NumberFormatException ex) {
                showAlert("Antal klip skal være et tal");
                return;
            }

            Arrangement selectedArrangement = priceLineComponents.getCbArrangements().getSelectionModel().getSelectedItem();
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
            showAlert("Alle felterne skal udfyldes");
        } else {
            Category category = null;
            for (Category giftBasketCategory : Controller.getCategories()) {
                if (giftBasketCategory.getName().equals("Sampakninger")) {
                    category = giftBasketCategory;
                }
            }

            GiftBasket giftBasket = Controller.createGiftBasket(productName, category);

            for (PriceLineComponents priceLineComponents : priceLineComponents) {
                String priceFromTextField = priceLineComponents.getTxfPrice().getText();
                double price = Double.parseDouble(priceFromTextField);
                String clipsFromTextField = priceLineComponents.getTxfClips().getText();
                Integer clips = clipsFromTextField.isBlank() ? null : Integer.parseInt(clipsFromTextField);
                Arrangement selectedArrangement = priceLineComponents.getCbArrangements().getSelectionModel().getSelectedItem();
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Fejl");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update() {
        populateComboBoxes();
    }

    private class PriceLineComponents {
        private final TextField txfPrice;
        private final TextField txfClips;
        private final ComboBox<Arrangement> cbArrangements;

        private PriceLineComponents(
                TextField txfPrice,
                TextField txfClips,
                ComboBox<Arrangement> cbArrangements) {
            this.txfPrice = txfPrice;
            this.txfClips = txfClips;
            this.cbArrangements = cbArrangements;
        }

        public TextField getTxfPrice() {
            return txfPrice;
        }

        public TextField getTxfClips() {
            return txfClips;
        }

        public ComboBox<Arrangement> getCbArrangements() {
            return cbArrangements;
        }
    }
}
