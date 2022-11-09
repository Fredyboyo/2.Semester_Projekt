package Model;

public class RentalCategory extends Category {
    private int mortgage;
    public RentalCategory(String name, int mortgage) {
        super(name);
        this.mortgage = mortgage;
    }

    public int getMortgage() {
        return mortgage;
    }

    public void setMortgage(int mortgage) {
        this.mortgage = mortgage;
    }
}
