package Model;

import java.util.ArrayList;

public class GiftBasket implements ProductComponent {
    private String name;
    private Category category;
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
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category newCategory) {
        this.category = newCategory;
    }

    @Override
    public ArrayList<Price> getPrices() {
        return new ArrayList<>(prices);
    }

    @Override
    public Price createPrice(Arrangement arrangement, double kr) {
        Price price = new Price(arrangement, kr, this);
        prices.add(price);
        return price;
    }

    public ProductComponent getChild(int i) {
        return products.get(i);
    }
}
