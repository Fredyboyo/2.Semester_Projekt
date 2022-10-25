package Model;

public class Price {
    private final String name;
    private final int price;

    public Price(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
