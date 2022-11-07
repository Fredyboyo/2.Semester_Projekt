package Model;

import java.util.ArrayList;

public class GiftBasket implements ProductComponent {
    private String name;
    private Category category;
    private ArrayList<Price> prices = new ArrayList<>();
    private final ArrayList<ProductComponent> products;

    public GiftBasket(
            String name,
            Category category,
            ArrayList<ProductComponent> products) {
        this.name = name;
        this.category = category;
        category.products.add(this);
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
    public Price createPrice(Arrangement arrangement, double price) {
        Price p = new Price(arrangement, price);
        prices.add(p);
        p.product = this;
        return p;
    }

    public ProductComponent getChild(int i) {
        return products.get(i);
    }
}
