package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tour implements ProductComponent {
    private String name;
    private Category category;
    private final ArrayList<Price> prices;
    private final LocalDate date;
    private final String person;

    public Tour(
            String name,
            ArrayList<Price> prices,
            LocalDate date,
            String person) {
        this.name = name;
        this.prices = prices;
        this.category = new Category("Tour");
        this.date = date;
        this.person = person;
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
}
