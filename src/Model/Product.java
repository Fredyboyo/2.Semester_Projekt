package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements ProductComponent, Serializable {
    private String name;
    private Category category;
    private final ArrayList<Price> prices = new ArrayList<>();

    public Product(String name, Category category) {
        this.category = category;
        category.products.add(this);
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
    public Price createPrice(Arrangement arrangement, double price, Integer clip) {
        Price p = new Price(arrangement, price, clip);
        prices.add(p);
        p.product = this;
        return p;
    }

    @Override
    public String toString() {
        return name + " " + category + " " + prices;
    }
}
