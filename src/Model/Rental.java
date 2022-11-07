package Model;

import java.time.LocalDate;

public class Rental extends Order {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String person;
    private final double collectedMortgage;

    public Rental(Arrangement arrangement, LocalDate startDate, LocalDate endDate, String person, double payedMortgage) {
        super(arrangement);
        this.startDate = startDate;
        this.endDate = endDate;
        this.person = person;
        this.collectedMortgage = payedMortgage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getPerson() {
        return person;
    }

    public double getCollectedMortgage() {
        return collectedMortgage;
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate + " (" + person + " har betalt " + collectedMortgage + ")";
    }
}
