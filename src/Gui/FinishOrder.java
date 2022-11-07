package Gui;

import Controller.Controller;
import Model.Order;
import Model.PaymentMethod;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;

public class FinishOrder extends Stage {
    private boolean isFinished = false;

    public FinishOrder(Order order) {
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label lDatePicker = new Label("Date: ");
        lDatePicker.setPrefSize(150,25);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setEditable(false);

        Label lArrangement = new Label("Arrangement: ");
        lArrangement.setPrefSize(150,25);

        TextField tfArrangement = new TextField(order.getArrangement().getName());
        tfArrangement.setEditable(false);

        Label lPaymentMethod = new Label("Payment Method: ");
        lPaymentMethod.setPrefSize(150,25);

        ComboBox<PaymentMethod> cbPaymentMethod = new ComboBox<>();
        cbPaymentMethod.getItems().addAll(Controller.getPaymentMethods());

        Label lPrice = new Label("Price: ");
        lPrice.setPrefSize(150,25);

        TextField tfPrice = new TextField(order.getUpdatedPrice()+"");
        tfPrice.setPrefSize(150,25);
        tfPrice.setEditable(false);

        Button bFinish = new Button("Færdig");
        bFinish.setOnAction(actionEvent -> {
            if (cbPaymentMethod.getValue() == null) return;

            isFinished = true;
            this.close();
        });

        gridPane.add(lDatePicker,0,0);
        gridPane.add(datePicker,1,0);
        gridPane.add(lArrangement,0,1);
        gridPane.add(tfArrangement,1,1);
        gridPane.add(lPaymentMethod,0,2);
        gridPane.add(cbPaymentMethod,1,2);
        gridPane.add(lPrice,0,3);
        gridPane.add(tfPrice,1,3);
        gridPane.add(bFinish,1,4);

        this.setScene(new Scene(gridPane));
    }

    public boolean isFinished() {
        return isFinished;
    }
}
