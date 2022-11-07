package Model;

import java.time.LocalDate;

public class Rental extends Order {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Customer customer;
    private final double payedDeposit;

    public Rental(Arrangement arrangement, LocalDate startDate, LocalDate endDate, Customer customer, double payedDeposit) {
        super(arrangement);
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        this.payedDeposit = payedDeposit;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getPayedDeposit() {
        return payedDeposit;
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate + " (" + customer + " har betalt " + payedDeposit + ")";
    }
}
