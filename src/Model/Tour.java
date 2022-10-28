package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tour implements ProductComponent {
    private final String name;
    private final Category category;
    private final ArrayList<Price> prices;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String person;

    public Tour(
            String name,
            ArrayList<Price> prices,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String person) {
        this.name = name;
        this.prices = prices;
        this.category = new Category("Tour");
        this.startTime = startTime;
        this.endTime = endTime;
        this.person = person;
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
}
