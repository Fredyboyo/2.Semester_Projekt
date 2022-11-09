package Model;

public class DepositCategory extends Category {
    private int deposit;
    public DepositCategory(String name, int mortgage) {
        super(name);
        this.deposit = mortgage;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
