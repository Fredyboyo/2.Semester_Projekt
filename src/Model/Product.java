package Model;

import java.util.HashMap;
import java.util.Hashtable;

public class Product {
    private final String name;
    private final HashMap<Arrangement,Integer[]> priceTable = new HashMap<>();
    private Category category;

    public Product(String name, Category category) {
        this.category = category;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Arrangement arrangement, Integer price, Integer clip) {
        Integer[] integers = priceTable.get(arrangement);
        integers[0] = integers[0] + price;
        integers[1] = integers[1] + clip;
        priceTable.replace(arrangement, integers);
    }

    public HashMap<Arrangement,Integer[]> getPriceTable() {
        return priceTable;
    }
}
