package Gui.Shop;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class FinishOrder extends Stage {
    private boolean completed = false;
    private final ComboBox<PaymentMethod> cbPaymentMethod = new ComboBox<>();
    private final DatePicker dpStartDate = new DatePicker(LocalDate.now());
    private final DatePicker dpEndDate = new DatePicker(LocalDate.now().plusDays(1));
    private final TextField tfName = new TextField();
    private final TextField tfEmail = new TextField();
    private final TextField tfNumber = new TextField();
    private final TextField tfAddress = new TextField();

    public FinishOrder(Order order) {
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label lDate = new Label("Dato: ");
        lDate.setPrefSize(150,25);

        Label lDateValue = new Label(order.getTimestamp().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        lDateValue.setPrefSize(150,25);

        Label lPaymentMethod = new Label("Betaling Metode: ");
        lPaymentMethod.setPrefSize(150,25);

        cbPaymentMethod.setPrefSize(150,25);
        cbPaymentMethod.getItems().addAll(Controller.getPaymentMethods());

        Label lStartDate = new Label("Start dato: ");
        lStartDate.setPrefSize(150,25);

        Label lEndDate = new Label("Slut dato: ");
        lEndDate.setPrefSize(150,25);

        Label lPrice = new Label("pris: ");
        lPrice.setPrefSize(150,25);

        order.updateCollectedPrices();
        TextField tfPrice = new TextField(order.getCollectedCost()+"");
        tfPrice.setPrefSize(150,25);

        Label lClip = new Label("klip pris: ");
        lPrice.setPrefSize(150,25);

        TextField tfClip = new TextField(order.getCollectedClips()+"");
        tfClip.setPrefSize(150,25);
        tfClip.setEditable(false);

        Label lDeposit = new Label("Pant: ");
        TextField tfDeposit = new TextField();

        Label lName = new Label("Navn:");
        Label lEmail = new Label("Email:");
        Label lNumber = new Label("Telefonummer:");
        Label lAddress = new Label("Adresse:");

        Button bFinish = new Button("Færdig");

        bFinish.setOnAction(event -> finish(order));

        gridPane.add(lDate,0,0);
        gridPane.add(lDateValue,1,0);
        gridPane.add(lPaymentMethod,0,1);
        gridPane.add(cbPaymentMethod,1,1);


        if (order.getClass() == Rental.class) {
            gridPane.add(lStartDate,0,2);
            gridPane.add(dpStartDate,1,2);
            gridPane.add(lEndDate,0,3);
            gridPane.add(dpEndDate,1,3);
            double deposit = 0;
            for (OrderLine orderLine : order.getOrderLines()) {
                DepositCategory depositCategory = (DepositCategory) orderLine.getProduct().getCategory();
                double productDeposit = depositCategory.getDeposit() * orderLine.getAmount();
                deposit += orderLine.getDiscountStrategy().discount(productDeposit);
            }

            tfDeposit.setText(deposit+"");
            tfPrice.setText(order.getCollectedCost() - deposit +"");

            gridPane.add(lPrice,0,4);
            gridPane.add(tfPrice,1,4);
            gridPane.add(lClip,0,5);
            gridPane.add(tfClip,1,5);
            gridPane.add(lDeposit,0,6);
            gridPane.add(tfDeposit,1,6);
            gridPane.add(bFinish,1,7);
            gridPane.add(lName,2,2);
            gridPane.add(tfName,3,2);
            gridPane.add(lEmail,2,3);
            gridPane.add(tfEmail,3,3);
            gridPane.add(lNumber,2,4);
            gridPane.add(tfNumber,3,4);
            gridPane.add(lAddress,2,5);
            gridPane.add(tfAddress,3,5);
        } else {
            gridPane.add(lPrice,0,2);
            gridPane.add(tfPrice,1,2);
            gridPane.add(lClip,0,3);
            gridPane.add(tfClip,1,3);
            gridPane.add(bFinish,1,4);
        }

        this.setScene(new Scene(gridPane));
    }

    private void finish(Order order) {
        PaymentMethod paymentMethod = cbPaymentMethod.getValue();
        if (order.getClass() == Rental.class) {
            Rental rental = (Rental) order;
            LocalDate startDate = dpStartDate.getValue();
            LocalDate endDate = dpEndDate.getValue();
            if (paymentMethod == null || startDate == null || endDate == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Alle felter skal udfyldes");
                alert.showAndWait();
                return;
            }
            if (startDate.isAfter(endDate)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("start datoen er efter slutdatoen");
                alert.showAndWait();
                return;
            }

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
                rental.setCustomer(Controller.createCustomer(name,email,number,address));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Telefonnummer skal være et tal");
                alert.showAndWait();
                return;
            }
            order.setPaymentMethod(paymentMethod);
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);

        } else {
            if (paymentMethod == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Fejl");
                alert.setContentText("Alle felter skal udfyldes");
                alert.showAndWait();
                return;
            }
            order.setPaymentMethod(paymentMethod);
        }
        completed = true;
        this.close();
    }

    public boolean isCompleted() {
        return completed;
    }
}
