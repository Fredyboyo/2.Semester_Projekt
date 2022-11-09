package Gui.Administration.Product;

import Controller.Controller;
import Model.Category;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddCategoryWindow extends Stage {
    private final TextField tfName = new TextField();
    private final TextField tfMortgage = new TextField();
    private Category category;

    public AddCategoryWindow(){
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setMinHeight(100);
        this.setMinWidth(200);
        this.setResizable(false);
        this.setTitle("Ny produktkategori");

        GridPane pane = new GridPane();
        this.initContent(pane);

        Scene scene = new Scene(pane);
        this.setScene(scene);
    }

    private void initContent(GridPane gridPane) {
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label lName = new Label("produktkategori: ");
        RadioButton rbPant = new RadioButton("Pant: ");
        Button bOk = new Button("Bekræft");

        tfMortgage.setDisable(true);

        gridPane.add(lName, 1, 1);
        gridPane.add(tfName, 2, 1);
        gridPane.add(rbPant, 1, 2);
        gridPane.add(tfMortgage, 2, 2);
        gridPane.add(bOk, 3, 2);

        rbPant.setOnAction(event -> pantAction(rbPant));
        bOk.setOnAction(event -> okAction());
    }

    private void pantAction(RadioButton bPant) {
        tfMortgage.setDisable(!bPant.isSelected());
    }


    private void okAction() {
        String name = tfName.getText();
        if(name.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Kategorien skal have en beskrivelse");
            alert.showAndWait();
        }
        if (tfMortgage.isDisabled()) {
            category = Controller.createCategory(name);
        } else {
            int mortgage;
            try {
                mortgage = Integer.parseInt(tfMortgage.getText());
            } catch (NumberFormatException ignore) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Pant skal være et tal");
                alert.showAndWait();
                return;
            }
            category = Controller.createRentalCategory(name,mortgage);
        }
        this.close();
    }

    public Category getCategory() {
        return category;
    }
}
