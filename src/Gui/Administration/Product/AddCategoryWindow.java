package Gui.Administration.Product;

import Controller.Controller;
import Model.Category;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddCategoryWindow extends Stage {

    private final TextField txfName = new TextField();

    public AddCategoryWindow(){
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

        Label lblName = new Label("Produktkategori");
        pane.add(lblName, 1, 1);

        pane.add(txfName, 2, 1);

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 3, 1);
        btnOK.setOnAction(event -> okAction());
    }

    private void okAction() {
        String name = txfName.getText();
        if(name.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Kategorien skal have en beskrivelse");
            alert.showAndWait();
        } else{
            Controller.createCategory(name);
            this.close();
        }
    }
}
