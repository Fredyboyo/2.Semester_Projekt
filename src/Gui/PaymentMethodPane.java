package Gui;

import Controller.Controller;
import Model.*;
import com.sun.javafx.scene.control.DatePickerContent;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;

public class PaymentMethodPane extends GridPane {
    private final ListView<PaymentMethod> lvwPaymentMethods = new ListView<>();
    private DateCell iniCell;
    private DateCell endCell;
    private DatePicker datePicker = new DatePicker();
    private Text boughtTicketCouponCount = new Text("0");
    private Text usedTicketCouponCount = new Text("0");

    public PaymentMethodPane() {
        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        this.add(lvwPaymentMethods, 1, 1, 1, 2);
        lvwPaymentMethods.getItems().addAll(Controller.getPaymentMethods());
        lvwPaymentMethods.setPrefHeight(150);

        Button btnDelete = new Button("Slet");
        btnDelete.setOnAction(event -> deleteAction());
        this.add(btnDelete, 2, 1);

        Button btnAdd = new Button("Tilføj");
        btnAdd.setOnAction(event -> addAction());
        this.add(btnAdd, 2, 2);

        Label lblTicketCoupon = new Label("Købte og brugte klip");
        lblTicketCoupon.setFont(new Font(15));
        this.add(lblTicketCoupon, 1, 4);

        this.add(datePicker, 1, 5);
        datePicker.setValue(LocalDate.now());
        datePicker.showingProperty().addListener((obs, b, b1) -> setDatePicker(obs, b, b1));

        Label lblBoughtTicketCoupons = new Label("Antal købte klippekort i perioden:");
        this.add(lblBoughtTicketCoupons, 1, 6);
        this.add(boughtTicketCouponCount, 2, 6);
        Label lblUsedTicketCoupons = new Label("Antal brugte klippekort i perioden:");
        this.add(lblUsedTicketCoupons, 1, 7);
        this.add(usedTicketCouponCount, 2, 7);
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

    private void setDatePicker(ObservableValue<? extends Boolean> obs, boolean b, boolean b1) {
        if (b1) {
            DatePickerContent content = (DatePickerContent) ((DatePickerSkin) datePicker.getSkin()).getPopupContent();

            List<DateCell> cells = content.lookupAll(".day-cell").stream()
                    .filter(ce -> !ce.getStyleClass().contains("next-month"))
                    .map(n -> (DateCell) n).toList();

            content.setOnMouseDragged(e -> {
                Node n = e.getPickResult().getIntersectedNode();
                DateCell c = null;
                if (n instanceof DateCell) {
                    c = (DateCell) n;
                } else if (n instanceof Text) {
                    c = (DateCell) (n.getParent());
                }
                if (c != null && c.getStyleClass().contains("day-cell") &&
                        !c.getStyleClass().contains("next-month")) {
                    if (iniCell == null) {
                        iniCell = c;
                    }
                    endCell = c;
                }
                if (iniCell != null && endCell != null) {
                    int ini = (int) Math.min(Integer.parseInt(iniCell.getText()),
                            Integer.parseInt(endCell.getText()));
                    int end = (int) Math.max(Integer.parseInt(iniCell.getText()),
                            Integer.parseInt(endCell.getText()));
                    cells.forEach(ce -> ce.getStyleClass().remove("selected"));
                    cells.stream()
                            .filter(ce -> Integer.parseInt(ce.getText()) >= ini)
                            .filter(ce -> Integer.parseInt(ce.getText()) <= end)
                            .forEach(ce -> ce.getStyleClass().add("selected"));
                }
            });
            content.setOnMouseReleased(e -> {
                if (iniCell != null && endCell != null) {
                    System.out.println("Selection from " + iniCell.getText() + " to " + endCell.getText());
                    dateChanged(iniCell.getItem(), endCell.getItem());
                }
                endCell = null;
                iniCell = null;
            });
        }
    }

    private void dateChanged(LocalDate start, LocalDate end) {
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
                if (order.getDate().toLocalDate().isAfter(start) && order.getDate().toLocalDate().isBefore(end)) {
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
        for (Order order : Controller.getOrders()) {
            for (OrderLine orderLine : order.getOrderLines()) {
                if (orderLine.getProduct().getCategory() == ticketCouponCategory) {
                    if(order.getDate().toLocalDate().isAfter(start) && order.getDate().toLocalDate().isBefore(end)) {
                        boughtCount += orderLine.getAmount() * 4;
                    }
                }
            }
        }
        return boughtCount;
    }
}
