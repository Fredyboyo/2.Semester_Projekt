package Model;

import java.util.HashMap;
import java.util.Hashtable;

public class Product {
    private final String name;
    private final HashMap<Arrangement,Double[]> priceTable = new HashMap<>();
    private Category category;

    public Product(String name, Category category) {
        this.category = category;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Arrangement arrangement, Integer price, Integer clip) {
        Double[] doubles = priceTable.get(arrangement);
        doubles[0] = doubles[0] + price;
        doubles[1] = doubles[1] + clip;
        priceTable.replace(arrangement, doubles);
    }

    public HashMap<Arrangement,Double[]> getPriceTable() {
        return priceTable;
    }
}
