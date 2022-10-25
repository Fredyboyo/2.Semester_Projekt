package Model;

public class Price {
    private final String name;
    private final int price;
    private final int clip;

    public Price(String name, int price, int clip) {
        this.name = name;
        this.price = price;
        this.clip = clip;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getClip() {
        return clip;
    }
}
