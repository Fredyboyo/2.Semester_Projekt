package Gui.Shop;

import Controller.Controller;
import Model.Order;
import Model.PaymentMethod;
import Model.Rental;
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
    private final DatePicker dpStartDate = new DatePicker();
    private final DatePicker dpEndDate = new DatePicker();

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

        Label lPrice = new Label("Slut Pris: ");
        lPrice.setPrefSize(150,25);

        TextField tfPrice = new TextField(order.getUpdatedPrice()+"");
        tfPrice.setPrefSize(150,25);
        tfPrice.setEditable(false);

        Button bFinish = new Button("Færdig");
        bFinish.setOnAction(actionEvent -> finish(order));

        gridPane.add(lDate,0,0);
        gridPane.add(lDateValue,1,0);
        gridPane.add(lPaymentMethod,0,1);
        gridPane.add(cbPaymentMethod,1,1);

        if (order.getClass() != Order.class) {
            gridPane.add(lStartDate,0,2);
            gridPane.add(dpStartDate,1,2);
            gridPane.add(lEndDate,0,3);
            gridPane.add(dpEndDate,1,3);

            gridPane.add(lPrice,0,4);
            gridPane.add(tfPrice,1,4);
            gridPane.add(bFinish,1,5);
        } else {
            gridPane.add(lPrice,0,2);
            gridPane.add(tfPrice,1,2);
            gridPane.add(bFinish,1,3);

        }



        this.setScene(new Scene(gridPane));
    }

    private void finish(Order order) {
        PaymentMethod paymentMethod = cbPaymentMethod.getValue();
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

        order.setPaymentMethod(paymentMethod);
        if (order.getClass() == Rental.class) {
            Rental rental = (Rental) order;
            rental.setStartDate(startDate);
            rental.setEndDate(endDate);
        }

        completed = true;
        this.close();
    }

    public boolean isCompleted() {
        return completed;
    }
}
