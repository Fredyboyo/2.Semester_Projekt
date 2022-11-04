package Gui.Administration.PaymentMethod;

import Controller.Controller;
import Model.*;
import com.sun.javafx.scene.control.DatePickerContent;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaymentMethodPane extends GridPane {
    private final ListView<PaymentMethod> lvwPaymentMethods = new ListView<>();
    private final DatePicker datePickerStart = new DatePicker();
    private final DatePicker datePickerEnd = new DatePicker();
    private final Text boughtTicketCouponCount = new Text("0");
    private final Text usedTicketCouponCount = new Text("0");

    public PaymentMethodPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        Button btnDelete = new Button("Slet");
        btnDelete.setOnAction(event -> deleteAction());
        this.add(btnDelete, 2, 1);
        btnDelete.setDisable(true);

        Button btnAdd = new Button("Tilføj");
        btnAdd.setOnAction(event -> addAction());
        this.add(btnAdd, 2, 2);

        this.add(lvwPaymentMethods, 1, 1, 1, 2);
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
        lvwPaymentMethods.setPrefHeight(150);
        lvwPaymentMethods.setOnMouseClicked(event -> {
            btnDelete.setDisable(false);
        });

        Label lblTicketCoupon = new Label("Købte og brugte klip");
        lblTicketCoupon.setFont(new Font(15));
        this.add(lblTicketCoupon, 1, 4);

        HBox boxStart = new HBox();
        this.add(boxStart, 1, 5);
        boxStart.setSpacing(16);
        Label lblFromDate = new Label("Startdato:");
        boxStart.getChildren().add(lblFromDate);
        boxStart.getChildren().add(datePickerStart);
        datePickerStart.setValue(LocalDate.now().minusDays(7));
        datePickerStart.setOnAction(event -> dateChanged());

        HBox boxEnd = new HBox();
        this.add(boxEnd, 1, 6);
        boxEnd.setSpacing(20);
        Label lblToDate = new Label("Slutdato:");
        boxEnd.getChildren().add(lblToDate);
        boxEnd.getChildren().add(datePickerEnd);
        datePickerEnd.setValue(LocalDate.now());
        datePickerEnd.setOnAction(event -> dateChanged());
        dateChanged();

        Label lblBoughtTicketCoupons = new Label("Antal købte klip på klippekort i den valgte periode:");
        this.add(lblBoughtTicketCoupons, 1, 8);
        this.add(boughtTicketCouponCount, 2, 8);
        Label lblUsedTicketCoupons = new Label("Antal brugte klip i den valgte periode:");
        this.add(lblUsedTicketCoupons, 1, 9);
        this.add(usedTicketCouponCount, 2, 9);
    }

    private void deleteAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Er du sikker på, at du vil slette denne betalingsmetode?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            PaymentMethod paymentMethod = lvwPaymentMethods.getSelectionModel().getSelectedItem();
            Controller.deletPaymentMethod(paymentMethod);
            lvwPaymentMethods.getItems().clear();
            lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
        }
    }

    private void addAction() {
        AddPaymentMethodWindow paymentMethodWindow = new AddPaymentMethodWindow();
        paymentMethodWindow.showAndWait();
        lvwPaymentMethods.getItems().clear();
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
    }

    private void dateChanged() {
        LocalDate start = datePickerStart.getValue();
        LocalDate end = datePickerEnd.getValue();

        if (end.isBefore(start)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Slutdato skal være efter startdao");
            alert.showAndWait();
            return;
        }

        int boughtCount = getBoughtCount(start, end);
        boughtTicketCouponCount.setText(Integer.toString(boughtCount));
        int usedCount = getUsedCount(start, end);
        usedTicketCouponCount.setText(Integer.toString(usedCount));
    }

    private int getUsedCount(LocalDate start, LocalDate end) {
        PaymentMethod ticketCouponPaymentMethod = null;
        for (PaymentMethod paymentMethod : Controller.getPaymentMethods()) {
            if (paymentMethod.getName().equals("Klippekort")) {
                ticketCouponPaymentMethod = paymentMethod;
            }
        }
        int usedCount = 0;
        for (Order order : Controller.getOrders()) {
            if (order.getPaymentMethod() == ticketCouponPaymentMethod) {
                LocalDate date = order.getDate().toLocalDate();
                if (date.isEqual(start) || date.isAfter(start) && date.isEqual(end) || date.isBefore(end)) {
                    usedCount += order.getCollectedCost();
                }
            }
        }
        return usedCount;
    }

    private int getBoughtCount(LocalDate start, LocalDate end) {
        Category ticketCouponCategory = null;
        for (Category category : Controller.getCategories()) {
            if (category.getName().equals("Klippekort")) {
                ticketCouponCategory = category;
            }
        }
        int boughtCount = 0;
        final int amountOfTicketsPrTicketCoupon = 4;
        for (Order order : Controller.getOrders()) {
            for (OrderLine orderLine : order.getOrderLines()) {
                if (orderLine.getProduct().getCategory() == ticketCouponCategory) {
                    if (order.getDate().toLocalDate().isAfter(start) && order.getDate().toLocalDate().isBefore(end)) {
                        boughtCount += orderLine.getAmount() * amountOfTicketsPrTicketCoupon;
                    }
                }
            }
        }
        return boughtCount;
    }
}
