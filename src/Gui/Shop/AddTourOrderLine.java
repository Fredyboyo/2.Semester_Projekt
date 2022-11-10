package Gui.Shop;

import Controller.Controller;
import Model.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class AddTourOrderLine extends Stage {
    private TourOrderLine tour;

    private final DatePicker tfDate = new DatePicker(LocalDate.now());
    private final TextField tfName = new TextField();
    private final TextField tfEmail = new TextField();
    private final TextField tfNumber = new TextField();
    private final TextField tfAddress = new TextField();

    public AddTourOrderLine(Order order, ProductComponent product, int amount) {
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label lDate = new Label("Dato for Rundvisning:");
        lDate.setPrefWidth(150);

        Label lCostumer = new Label("Kunde");
        lCostumer.setFont(Font.font("", FontWeight.BOLD,12));
        Label lName = new Label("Navn:");
        Label lEmail = new Label("Email:");
        Label lNumber = new Label("Telefonummer:");
        Label lAddress = new Label("Adresse:");


        Button finished = new Button("Færdig");

        gridPane.add(lDate,0,0);
        gridPane.add(tfDate,1,0);

        gridPane.add(lCostumer,0,1,2,1);
        gridPane.add(lName,0,2);
        gridPane.add(tfName,1,2);
        gridPane.add(lEmail,0,3);
        gridPane.add(tfEmail,1,3);
        gridPane.add(lNumber,0,4);
        gridPane.add(tfNumber,1,4);
        gridPane.add(lAddress,0,5);
        gridPane.add(tfAddress,1,5);
        gridPane.add(finished,1,6);

        GridPane.setHalignment(lCostumer, HPos.CENTER);

        finished.setOnAction(actionEvent -> actionEvent(order,product,amount));

        this.setScene(new Scene(gridPane));
    }

    private void actionEvent(Order order, ProductComponent product, int amount) {
        LocalDate date = tfDate.getValue();
        Customer customer;
        try {
            String name = tfName.getText();
            String email = tfEmail.getText();
            int number = Integer.parseInt(tfNumber.getText());
            String address = tfAddress.getText();
            if (address.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Der skal være en adresse");
                alert.showAndWait();
                return;
            }
            customer = Controller.createCustomer(name,email,number,address);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Telefonnummer skal være et nummer");
            alert.showAndWait();
            return;
        }
        tour = Controller.createTourOrderLine(order,product,amount,date,customer);
        this.close();
    }

    public TourOrderLine getTourOrderLine() {
        return tour;
    }
}
