package Gui.Administration.Product;

import Controller.Controller;
import Gui.Observer;
import Model.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class AddGiftBasketWindow extends Stage implements Observer {
    private GiftBasket product;
    private final ArrayList<TextField> alPrices = new ArrayList<>();
    private final ArrayList<TextField> alClips = new ArrayList<>();
    private int index = 0;

    private final TextField txfName = new TextField();
    private final TextField txfPrice = new TextField();
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final Button btnAddPrice = new Button("Tilføj endnu en pris");
    private final TextField txfSecondPrice = new TextField();
    private final ComboBox<Arrangement> cbArrangementsSecondPrice = new ComboBox<>();
    private final ComboBox<ProductComponent> cbProducts = new ComboBox<>();
    private final Button btnCancel = new Button("Annuller");
    private final Button btnOK = new Button("Bekræft");


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
        btnAddPrice.setOnAction(event -> addPriceAction(pane,null));

        Label lblProducts = new Label("Produkter");
        pane.add(lblProducts, 1, 4);
        pane.add(cbProducts, 2, 4);

        pane.add(btnCancel, 3, 6);
        btnCancel.setOnAction(event -> cancelAction());

        pane.add(btnOK, 4, 6);
        btnOK.setOnAction(event -> okAction());
    }

    private void addPriceAction(GridPane pane, Price price) {
        TextField tfPrice = new TextField();
        TextField tfClip = new TextField();

        pane.add(tfPrice, 1,index+3);
        pane.add(tfClip, 2,index+3);

        alPrices.add(tfPrice);
        alClips.add(tfClip);

        if(price != null){
            tfPrice.setText(price.getPrice() + "");
            tfClip.setText(price.getClips() + "");
            cbArrangements.getSelectionModel().select(price.getArrangement());
        }

        pane.getChildren().removeAll(btnAddPrice,btnCancel,btnOK);

        pane.add(btnAddPrice, 1, index+4);
        pane.add(btnCancel, 2, index+5);
        pane.add(btnOK, 3, index+5);

        GridPane.setHalignment(btnCancel, HPos.RIGHT);
        GridPane.setHalignment(btnOK, HPos.RIGHT);

        this.sizeToScene();
        index++;
    }

    private void okAction() {
        Category category = null;
        for (Category category1 : Controller.getCategories()) {
            if (category1.getName().equals("Sampakning")) {
                category = category1;
            }
        }
        if (category == null) {
            category = Controller.createCategory("Sampakning");
        }

        String productName = txfName.getText();
        if (productName.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Alle felterne skal udfyldes");
            alert.showAndWait();
            return;
        }

        GiftBasket giftBasket = Controller.createGiftBasket(productName, category, new ArrayList<>());
/*
        for (int i = 0; i < index; i++) {
            Arrangement arrangement = alArrangements.get(i).getSelectionModel().getSelectedItem();
            if (arrangement == null) continue;
            try {
                double price = Double.parseDouble(alPrices.get(i).getText());
                Integer clips = Integer.valueOf(alClips.get(i).getText());
                Controller.createPrice(giftBasket, arrangement,price,clips);
            } catch (NumberFormatException e) {
                System.out.println(e);
            }
        }

 */

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
            //Controller.createPrice(product, arrangementSecondPrice, secondPrice);
        }
    }

    private void cancelAction() {
        this.close();
    }

    @Override
    public void update() {

    }
}
