package Gui;

import Controller.Controller;
import Model.PaymentMethod;
import Model.ProductComponent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class PaymentMethodPane extends GridPane {

    private ListView<PaymentMethod> lvwPaymentMethods = new ListView<>();

    public PaymentMethodPane(){
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        this.add(lvwPaymentMethods, 1, 1,1,2);
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
        
        Button btnDelete = new Button("Slet");
        btnDelete.setOnAction(event -> deleteAction());
        this.add(btnDelete, 2, 1);
        
        Button btnAdd = new Button("Tilføj");
        btnAdd.setOnAction(event -> addAction());
        this.add(btnAdd, 2, 2);
    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Er du sikker på, at du vil slette denne betalingsmetode?");
        alert.showAndWait();
        PaymentMethod paymentMethod = lvwPaymentMethods.getSelectionModel().getSelectedItem();
        Controller.deletPaymentMethod(paymentMethod);
        lvwPaymentMethods.getItems().clear();
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
    }

    private void addAction() {
        AddPaymentMethodWindow paymentMethodWindow = new AddPaymentMethodWindow();
        paymentMethodWindow.showAndWait();
        lvwPaymentMethods.getItems().clear();
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
    }
}
