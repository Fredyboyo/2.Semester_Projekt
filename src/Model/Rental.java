package Model;

import java.time.LocalDate;

public class Rental extends Order {
    private LocalDate startDate;
    private LocalDate endDate;
    private Customer customer;
    private double payedDeposit;

    public Rental(Arrangement arrangement) {
        super(arrangement);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.rentals.add(this);
    }

    public void setPayedDeposit(double payedDeposit) {
        this.payedDeposit = payedDeposit;
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate + " (" + customer + " har betalt " + payedDeposit + ")";
    }
}
