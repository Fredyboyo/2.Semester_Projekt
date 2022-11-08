package Model;

import java.util.ArrayList;

public class RentalProduct implements ProductComponent{
    private String name;
    private Category category;
    private final ArrayList<Price> prices = new ArrayList<>();

    public RentalProduct(String name, Category category) {
        this.category = category;
        category.products.add(this);
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String newName) {

    }

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public void setCategory(Category newCategory) {

    }

    @Override
    public ArrayList<Price> getPrices() {
        return null;
    }

    @Override
    public Price createPrice(Arrangement arrangement, double price) {
        return null;
    }
}
