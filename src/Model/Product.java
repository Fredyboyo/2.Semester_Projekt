package Model;

import java.util.ArrayList;

public class Product implements ProductComponent {
    private String name;
    private Category category;
    private final ArrayList<Price> prices = new ArrayList<>();

    public Product(
            String name,
            Category category) {
        this.category = category;
        category.addProduct(this);
        this.name = name;
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
    public Price createPrice(Arrangement arrangement, double priceInDkk) {
        Price price = new Price(arrangement, priceInDkk, this);
        price.product = this;
        prices.add(price);
        return price;
    }

    @Override
    public String toString() {
        return name + " " + category + " " + prices;
    }
}
