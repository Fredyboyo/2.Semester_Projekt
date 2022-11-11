package Model;

import java.time.LocalDate;

public class Rental extends Order {
    private LocalDate startDate;
    private LocalDate endDate;
    private Customer customer;
    private final double payedDeposit;

    public Rental(Arrangement arrangement, LocalDate startDate, LocalDate endDate, Customer customer, double payedDeposit) {
        super(arrangement);
        this.startDate = startDate;
        this.endDate = endDate;
        this.customer = customer;
        customer.rentals.add(this);
        this.payedDeposit = payedDeposit;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate + " (" + customer + " har betalt " + payedDeposit + ")";
    }
}
