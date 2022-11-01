package Gui;

import Controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddPaymentMethodWindow extends Stage {

    private TextField txfName = new TextField();

    public AddPaymentMethodWindow(){
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

        Label lblName = new Label("Betalingsmetode");
        pane.add(lblName, 1, 1);

        pane.add(txfName, 2, 1);

        Button btnOK = new Button("Bekræft");
        pane.add(btnOK, 3, 1);
        btnOK.setOnAction(event -> okAction());
    }

    private void okAction() {
        String name = txfName.getText();
        Controller.createPaymentMethod(name);
        this.close();
    }
}
