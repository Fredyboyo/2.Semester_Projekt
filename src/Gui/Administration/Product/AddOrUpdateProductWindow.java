package Gui.Administration.Product;

import Controller.Controller;
import Gui.Observer;
import Model.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class AddOrUpdateProductWindow extends Stage implements Observer {
    private ProductComponent product;

    private final ArrayList<ArrayList<Control>> alControls = new ArrayList<>();

    private final Category createNewCategoryCategory = new Category("Tilføj ny kategori");
    private final Arrangement createNewArrangementArrangement = new Arrangement("Tilføj ny salgssituation");


    private final TextField txfName = new TextField();
    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");

    private final Button btnCancel = new Button("Annuller");
    private final Button btnOK = new Button("Bekræft");

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
        pane.add(lblName, 0, 0);
        pane.add(txfName, 1, 0);

        Label lblCategory = new Label("Produkt kategori");
        pane.add(lblCategory, 0, 1);
        pane.add(cbCategories, 1, 1);

        cbCategories.setPromptText("Vælg kategori");
        cbCategories.setOnAction(event -> selectionChangedCategory());

        Label lPrice = new Label("Pris");
        Label lClip = new Label("Klip");
        Label lArrangement = new Label("Arrangement");

        pane.add(lPrice, 0, 2);
        pane.add(lClip, 1, 2);
        pane.add(lArrangement, 2, 2);

        populateComboBoxes();

        GridPane.setHalignment(btnCancel, HPos.RIGHT);
        GridPane.setHalignment(btnOK, HPos.RIGHT);

        btnCancel.setOnAction(event -> cancelAction());
        btnOK.setOnAction(event -> okAction());
        btnAddPrice.setOnAction(event -> addPriceAction(pane,null));

        if (product != null) {
            txfName.setText(product.getName());
            cbCategories.getSelectionModel().select(product.getCategory());
            ArrayList<Price> prices = product.getPrices();
            for (Price price : prices) {
                addPriceAction(pane,price);
            }
        } else {
            addPriceAction(pane,null);
        }
    }

    private void populateComboBoxes() {
        for (ArrayList<Control> alControl : alControls) {
            ComboBox<Arrangement> cbArrangement = (ComboBox<Arrangement>) alControl.get(2);
            Arrangement arrangement = cbArrangement.getSelectionModel().getSelectedItem();
            cbArrangement.getItems().clear();
            cbArrangement.getItems().addAll(Controller.getArrangements());
            cbArrangement.getItems().add(createNewArrangementArrangement);
            if (arrangement != null && arrangement != createNewArrangementArrangement) {
                cbArrangement.setValue(arrangement);
            }
        }
        Category category = cbCategories.getValue();
        cbCategories.getItems().clear();
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.getItems().add(createNewCategoryCategory);
        if (category != null && category != createNewCategoryCategory) {
            cbCategories.setValue(category);
        }
    }

    private void selectionChangedCategory() {
        if (cbCategories.getValue() == createNewCategoryCategory) {
            AddCategoryWindow newCategoryWindow = new AddCategoryWindow();
            newCategoryWindow.showAndWait();
            Category category = newCategoryWindow.getCategory();
            if (category == null) {
                cbCategories.setValue(cbCategories.getItems().get(0));
                return;
            }
            cbCategories.getItems().add(category);
            cbCategories.setValue(category);
        }
    }

    private void selectionChangedArrangement(ComboBox<Arrangement> cbArrangement) {
        if (cbArrangement.getValue() == createNewArrangementArrangement) {
            AddArrangementWindow newArrangementWindow = new AddArrangementWindow();
            newArrangementWindow.showAndWait();
            Arrangement arrangement = newArrangementWindow.getArrangment();
            if (arrangement == null) {
                cbArrangement.setValue(cbArrangement.getItems().get(0));
                return;
            }
            cbArrangement.getItems().add(arrangement);
            cbArrangement.setValue(arrangement);
        }
    }

    private void addPriceAction(GridPane pane, Price price) {
        TextField tfPrice = new TextField();
        TextField tfClip = new TextField();
        ComboBox<Arrangement> cbArrangements = new ComboBox<>();
        Button bDelete = new Button("X");

        ArrayList<Control> controls = new ArrayList<>(List.of(tfPrice, tfClip, cbArrangements, bDelete));
        alControls.add(controls);

        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.setOnAction(event -> selectionChangedArrangement(cbArrangements));
        bDelete.setOnAction(event -> removePriceAction(pane,controls));

        if(price != null){
            tfPrice.setText(price.getPrice() + "");
            tfClip.setText(price.getClips() + "");
            cbArrangements.getSelectionModel().select(price.getArrangement());
        }
        updateLayout(pane);
        populateComboBoxes();
        this.sizeToScene();
    }

    private void removePriceAction(GridPane pane, ArrayList<Control> controls) {
        alControls.remove(controls);
        pane.getChildren().removeAll(controls);
        updateLayout(pane);
    }

    private void updateLayout(GridPane pane) {
        for (int i = 0; i < alControls.size(); i++) {
            ArrayList<Control> controls = alControls.get(i);
            pane.getChildren().removeAll(controls);
            pane.add(controls.get(0), 0,i + 3);
            pane.add(controls.get(1), 1,i + 3);
            pane.add(controls.get(2), 2,i + 3);
            pane.add(controls.get(3), 3,i + 3);
        }

        pane.getChildren().removeAll(btnAddPrice,btnCancel,btnOK);
        pane.add(btnAddPrice, 0, alControls.size() + 4);
        pane.add(btnCancel, 1, alControls.size() + 5);
        pane.add(btnOK, 2, alControls.size() + 5);
    }

    private void okAction() {
        Category category = cbCategories.getSelectionModel().getSelectedItem();
        String productName = txfName.getText();
        if (category == null || productName.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Alle felterne skal udfyldes");
            alert.showAndWait();
            return;
        }

        if (product == null) {
            product = Controller.createProduct(productName, category);
        } else {
            Controller.updateProduct(product, productName, category);
        }

        for (ArrayList<Control> controls : alControls) {
            TextField tfPrices = (TextField) controls.get(0);
            TextField tfClips = (TextField) controls.get(1);
            ComboBox<Arrangement> cbArrangement = (ComboBox<Arrangement>) controls.get(2);

            Arrangement arrangement = cbArrangement.getSelectionModel().getSelectedItem();
            if (arrangement == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Arrangement skal udfyldes");
                alert.showAndWait();
                return;
            }
            double price = 0;
            Integer clips = null;
            try {
                price = Double.parseDouble(tfPrices.getText());
                clips = Integer.valueOf(tfClips.getText());
            } catch (NumberFormatException ignore) {
            }
            Controller.createProductPrice(product, arrangement, price, clips);
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
