package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class GiftBasket implements ProductComponent, Serializable {
    private String name;
    private Category category;
    private ArrayList<Price> prices = new ArrayList<>();
    private final ArrayList<ProductComponent> products = new ArrayList<>();

    public GiftBasket(
            String name,
            Category category) {
        this.name = name;
        this.category = category;
        category.getProducts().add(this);
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

    public void addProduct(ProductComponent product){
        products.add(product);
    }

    public ProductComponent getChild(int i) {
        return products.get(i);
    }

    @Override
    public String toString() {
        return name + " " + category + " " + prices;
    }
}
