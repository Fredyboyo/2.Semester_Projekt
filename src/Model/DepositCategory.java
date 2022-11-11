package Model;

public class DepositCategory extends Category {
    private int deposit;

    public DepositCategory(String name, int deposit) {
        super(name);
        this.deposit = deposit;
    }

    public int getDeposit() {
        return deposit;
    }
}
