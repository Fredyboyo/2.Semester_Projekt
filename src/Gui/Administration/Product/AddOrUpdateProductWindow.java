package Gui.Administration.Product;

import Controller.Controller;
import Gui.Observer;
import Model.Arrangement;
import Model.Category;
import Model.Price;
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

public class AddOrUpdateProductWindow extends Stage implements Observer {

    private final ProductComponent product;
    private final TextField txfName = new TextField();
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Category createNewCategoryCategory = new Category("Tilføj ny kategori");
    private final Arrangement createNewArrangementArrangement = new Arrangement("Tilføj ny salgssituation");
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");
    private final VBox vBox = new VBox();
    private final ArrayList<PriceLineComponents> priceLineComponents = new ArrayList<>();

    public AddOrUpdateProductWindow(ProductComponent product) {
        this.product = product;
        Controller.addObserver(this);

        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
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
        populateComboBoxes();
        cbCategories.setPromptText("Vælg kategori");
        cbCategories.setOnAction(event -> selectionChangedCategory());

        TitledPane titledPane = new TitledPane("Priser", vBox);
        pane.add(titledPane, 1, 3, 3, 1);
        titledPane.setCollapsible(false);
        vBox.setSpacing(10);
        pane.add(btnAddPrice, 1, 4);
        btnAddPrice.setOnAction(event -> addPriceAction(null));

        Button btnCancel = new Button("Annuller");
        pane.add(btnCancel, 4, 6);
        btnCancel.setOnAction(event -> cancelAction());

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 5, 6);
        btnOK.setOnAction(event -> okAction());

        if (product != null) {
            txfName.setText(product.getName());
            cbCategories.getSelectionModel().select(product.getCategory());
            for (Price price : product.getPrices()) {
                addPriceAction(price);
            }
        } else {
            addPriceAction(null);
        }
    }

    private void populateComboBoxes() {
        cbCategories.getItems().clear();
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getItems().add(createNewCategoryCategory);

        for (PriceLineComponents priceLineComponents : priceLineComponents) {
            ComboBox<Arrangement> cbArrangements = priceLineComponents.getCbArrangements();
            int index = cbArrangements.getSelectionModel().getSelectedIndex();
            cbArrangements.getItems().clear();
            cbArrangements.getItems().addAll(Controller.getArrangements());
            cbArrangements.getItems().add(createNewArrangementArrangement);
            cbArrangements.getSelectionModel().select(index);
        }
    }

    private void selectionChangedCategory() {
        if (cbCategories.getSelectionModel().getSelectedItem() == createNewCategoryCategory) {
            AddCategoryWindow newCategoryWindow = new AddCategoryWindow();
            newCategoryWindow.showAndWait();
            cbCategories.getSelectionModel().select(cbCategories.getItems().size() - 2);
        }
    }

    private void selectionChangedArrangement(ComboBox<Arrangement> source) {
        if (source.getSelectionModel().getSelectedItem() == createNewArrangementArrangement) {
            AddArrangementWindow newArrangementWindow = new AddArrangementWindow();
            newArrangementWindow.showAndWait();
        }
    }

    private void addPriceAction(Price price) {
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

        vBox.getChildren().add(hBox);
        this.sizeToScene();

        populateComboBoxes();
        if (price != null) {
            txfPrice.setText(Double.toString(price.getPrice()));
            cbArrangements.getSelectionModel().select(price.getArrangement());
            if(price.getClips() != null){
                txfClip.setText(Integer.toString(price.getClips()));
            }
        }
    }

    private void okAction() {
        String productName = txfName.getText();
        Category category = cbCategories.getSelectionModel().getSelectedItem();

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

            if(!clipsFromTextField.isBlank()) {
                try {
                    Integer.parseInt(clipsFromTextField);
                } catch (NumberFormatException ex) {
                    showAlert("Antal klip skal være et tal");
                    return;
                }
            }

            Arrangement selectedArrangement = priceLineComponents.getCbArrangements().getSelectionModel().getSelectedItem();
            if (selectedArrangement == null) {
                isAllArrangementsSpecified = false;
            }
        }

        if (productName.isEmpty() || category == null || !isAllArrangementsSpecified) {
            showAlert("Alle felterne skal udfyldes");
            return;
        } else {
            ProductComponent productComponent;
            if (product == null) {
                productComponent = Controller.createProduct(productName, category);
            } else {
                productComponent = product;
                Controller.updateProduct(product, productName, category);
            }

            for (PriceLineComponents priceLineComponents : priceLineComponents) {
                String priceFromTextField = priceLineComponents.getTxfPrice().getText();
                double price = Double.parseDouble(priceFromTextField);
                String clipsFromTextField = priceLineComponents.getTxfClips().getText();
                Integer clips = clipsFromTextField.isBlank() ? null : Integer.parseInt(clipsFromTextField);
                Arrangement selectedArrangement = priceLineComponents.getCbArrangements().getSelectionModel().getSelectedItem();
                Controller.createProductPrice(productComponent, selectedArrangement, price, clips);
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
