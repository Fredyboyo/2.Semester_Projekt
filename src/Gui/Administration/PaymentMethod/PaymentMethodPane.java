package Gui.Administration.PaymentMethod;

import Controller.Controller;
import Gui.Observer;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.Optional;

public class PaymentMethodPane extends GridPane implements Observer {

    private final ListView<PaymentMethod> lvwPaymentMethods = new ListView<>();
    private final DatePicker datePickerStart = new DatePicker();
    private final DatePicker datePickerEnd = new DatePicker();
    private final Text boughtTicketCouponCount = new Text("0");
    private final Text usedTicketCouponCount = new Text("0");

    public PaymentMethodPane() {
        Controller.addObserver(this);

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
            if (lvwPaymentMethods.getSelectionModel().getSelectedItem() != null)
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
            Controller.deletePaymentMethod(paymentMethod);
        }
    }

    private void addAction() {
        AddPaymentMethodWindow paymentMethodWindow = new AddPaymentMethodWindow();
        paymentMethodWindow.showAndWait();
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

        updateCounts();
    }

    private void updateCounts() {
        int boughtCount = getBoughtCount();
        boughtTicketCouponCount.setText(Integer.toString(boughtCount));
        int usedCount = getUsedCount();
        usedTicketCouponCount.setText(Integer.toString(usedCount));
    }

    private int getUsedCount() {
        LocalDate start = datePickerStart.getValue();
        LocalDate end = datePickerEnd.getValue();

        PaymentMethod ticketCouponPaymentMethod = null;
        for (PaymentMethod paymentMethod : Controller.getPaymentMethods()) {
            if (paymentMethod.getName().equals("Klippekort")) {
                ticketCouponPaymentMethod = paymentMethod;
            }
        }
        int usedCount = 0;
        for (Order order : Controller.getOrders()) {
            PaymentMethod paymentMethod = order.getPaymentMethod();
            if (paymentMethod != null && paymentMethod == ticketCouponPaymentMethod) {
                LocalDate date = order.getTimestamp().toLocalDate();
                if (date.isEqual(start) || date.isAfter(start) && date.isEqual(end) || date.isBefore(end)) {
                    usedCount += order.getCollectedClips();
                }
            }
        }
        return usedCount;
    }

    private int getBoughtCount() {
        LocalDate start = datePickerStart.getValue();
        LocalDate end = datePickerEnd.getValue();

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
                Category category = orderLine.getProduct().getCategory();
                if (category != null && category == ticketCouponCategory) {
                    if (order.getTimestamp().toLocalDate().isAfter(start) && order.getTimestamp().toLocalDate().isBefore(end)) {
                        boughtCount += orderLine.getAmount() * amountOfTicketsPrTicketCoupon;
                    }
                }
            }
        }
        return boughtCount;
    }

    @Override
    public void update() {
        updateCounts();
        lvwPaymentMethods.getItems().clear();
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
    }
}
