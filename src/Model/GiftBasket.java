package Model;

import java.util.ArrayList;

public class GiftBasket implements ProductComponent {
    private final String name;
    private final Category category;
    private final ArrayList<Price> prices;
    private final ArrayList<ProductComponent> products;

    public GiftBasket(
            String name,
            Category category,
            ArrayList<Price> prices,
            ArrayList<ProductComponent> products) {
        this.name = name;
        this.category = category;
        this.prices = prices;
        this.products = products;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public ArrayList<Price> getPrices() {
        return new ArrayList<>(prices);
    }

    @Override
    public Price createPrice(double kr, Arrangement arrangement) {
        Price price = new Price(kr, arrangement, this);
        prices.add(price);
        return price;
    }

    public ProductComponent getChild(int i) {
        return products.get(i);
    }
}
