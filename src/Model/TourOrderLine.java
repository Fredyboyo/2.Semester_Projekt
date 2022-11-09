package Model;

import java.time.LocalDate;

public class TourOrderLine extends OrderLine {
    private final LocalDate date;
    private final Customer customer;

    TourOrderLine(ProductComponent product, int amount, Arrangement arrangement, LocalDate date, Customer customer) {
        super(product, amount, arrangement);
        this.date = date;
        this.customer = customer;
        customer.tours.add(this);
    }

    public LocalDate getDate() {
        return date;
    }

    public Customer getCustomer() {
        return customer;
    }
}