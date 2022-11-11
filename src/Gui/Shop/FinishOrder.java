package Gui.Shop;

import Controller.Controller;
import Model.*;
import Model.DiscountStrategy.*;
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
    private final ComboBox<String> cbDiscounts = new ComboBox<>();
    private final Label lDiscountSign = new Label();
    private final Label lDiscountValue = new Label("Rabat værdi:");
    private final TextField tfPrice = new TextField();
    private final TextField tfDiscountValue = new TextField();
    private final DatePicker dpStartDate = new DatePicker(LocalDate.now());
    private final DatePicker dpEndDate = new DatePicker(LocalDate.now().plusDays(1));
    private final TextField tfName = new TextField();
    private final TextField tfEmail = new TextField();
    private final TextField tfNumber = new TextField();
    private final TextField tfAddress = new TextField();

    public FinishOrder(Order order) {
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        //this.setResizable(false);
        Scene scene;

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label lDate = new Label("Dato: ");
        lDate.setPrefWidth(125);

        Label lDateValue = new Label(order.getTimestamp().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
        lDateValue.setPrefWidth(125);

        Label lPaymentMethod = new Label("Betaling Metode: ");
        lPaymentMethod.setPrefWidth(125);

        cbPaymentMethod.setPrefWidth(125);
        cbPaymentMethod.getItems().addAll(Controller.getPaymentMethods());

        Label lStartDate = new Label("Start dato: ");
        lStartDate.setPrefWidth(125);

        Label lEndDate = new Label("Slut dato: ");
        lEndDate.setPrefWidth(125);

        Label lDiscounts = new Label("Rabat: ");
        lDiscounts.setPrefWidth(125);

        cbDiscounts.getItems().addAll("Ingen rabat","Beløb rabat","Procent rabat","Student rabat","Butiks rabat");
        cbDiscounts.setValue(cbDiscounts.getItems().get(0));
        cbDiscounts.setPrefWidth(125);
        
        lDiscountSign.setPrefWidth(125);
        lDiscountValue.setPrefWidth(125);
        tfDiscountValue.setPrefWidth(125);

        Label lPrice = new Label("pris: ");
        lPrice.setPrefWidth(125);

        order.updateCollectedPrices();
        tfPrice.setText(order.getCollectedCost()+"");
        tfPrice.setPrefWidth(125);

        Label lClip = new Label("klip pris: ");
        lPrice.setPrefWidth(125);

        TextField tfClip = new TextField(order.getCollectedClips()+"");
        tfClip.setPrefWidth(125);
        tfClip.setEditable(false);

        Label lDeposit = new Label("Pant: ");
        lDeposit.setPrefWidth(125);

        TextField tfDeposit = new TextField();
        
        Label lName = new Label("Navn:");
        Label lEmail = new Label("Email:");
        Label lNumber = new Label("Telefonummer:");
        Label lAddress = new Label("Adresse:");
        Button bFinish = new Button("Færdig");

        lNumber.setPrefWidth(125);
        bFinish.setPrefWidth(125);

        cbDiscounts.setOnAction(actionEvent -> setDiscount(order,gridPane));
        tfDiscountValue.setOnAction(actionEvent -> setDiscountValue(order));
        bFinish.setOnAction(event -> finish(order));

        gridPane.add(lDate,0,0);
        gridPane.add(lDateValue,1,0);
        gridPane.add(lPaymentMethod,0,1);
        gridPane.add(cbPaymentMethod,1,1);
        gridPane.add(lDiscounts,0,2);
        gridPane.add(cbDiscounts,1,2);

        if (order.getClass() == Rental.class) {
            scene = new Scene(gridPane,550,350);
            double deposit = 0;
            for (OrderLine orderLine : order.getOrderLines()) {
                DepositCategory depositCategory = (DepositCategory) orderLine.getProduct().getCategory();
                double productDeposit = depositCategory.getDeposit() * orderLine.getAmount();
                deposit += orderLine.getDiscountStrategy().discount(productDeposit);
            }
            tfDeposit.setText(deposit+"");
            lPrice.setText("pris uden Pant: ");
            tfPrice.setText(order.getCollectedCost() - deposit +"");

            gridPane.add(lPrice,0,5);
            gridPane.add(tfPrice,1,5);
            gridPane.add(lClip,0,6);
            gridPane.add(tfClip,1,6);
            gridPane.add(lDeposit,0,7);
            gridPane.add(tfDeposit,1,7);
            gridPane.add(bFinish,1,8);

            gridPane.add(lStartDate,2,1);
            gridPane.add(dpStartDate,3,1);
            gridPane.add(lEndDate,2,2);
            gridPane.add(dpEndDate,3,2);
            gridPane.add(lName,2,4);
            gridPane.add(tfName,3,4);
            gridPane.add(lEmail,2,5);
            gridPane.add(tfEmail,3,5);
            gridPane.add(lNumber,2,6);
            gridPane.add(tfNumber,3,6);
            gridPane.add(lAddress,2,7);
            gridPane.add(tfAddress,3,7);
        } else {
            scene = new Scene(gridPane,350,325);
            gridPane.add(lPrice,0,4);
            gridPane.add(tfPrice,1,4);
            gridPane.add(lClip,0,5);
            gridPane.add(tfClip,1,5);
            gridPane.add(bFinish,1,6);
        }

        this.setScene(scene);
    }



    private void setDiscount(Order order, GridPane gridPane) {
        int index = cbDiscounts.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        gridPane.getChildren().removeAll(lDiscountValue,lDiscountSign,tfDiscountValue);
        switch (index) {
            case 1 -> {
                order.setDiscountStrategy(new AmountDiscountStrategy(0));
                lDiscountSign.setText("Kr.");
                tfDiscountValue.clear();
                gridPane.add(lDiscountValue,0,3);
                gridPane.add(lDiscountSign,2,3);
                gridPane.add(tfDiscountValue,1,3);
            }
            case 2 -> {
                order.setDiscountStrategy(new PercentageDiscountStrategy(0));
                lDiscountSign.setText("%");
                tfDiscountValue.clear();
                gridPane.add(lDiscountValue,0,3);
                gridPane.add(lDiscountSign,2,3);
                gridPane.add(tfDiscountValue,1,3);
            }
            case 3 -> order.setDiscountStrategy(new StudentDiscountStrategy());
            case 4 -> order.setDiscountStrategy(new RegCustomerDiscountStrategy());
            default -> order.setDiscountStrategy(new NoDiscountStrategy());
        }
    }

    private boolean setDiscountValue(Order order) {
        if (order.getDiscountStrategy().getClass() == NoDiscountStrategy.class) return true;

        try {
            order.getDiscountStrategy().setValue(Double.parseDouble(tfDiscountValue.getText()));
            order.updateCollectedPrices();
            tfPrice.setText(order.getCollectedCost()+"");
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Fejl");
            alert.setContentText("Rabat væriden skal være et tal");
            alert.showAndWait();
            return false;
        }
    }

    private void finish(Order order) {
        if (!setDiscountValue(order)) return;
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
