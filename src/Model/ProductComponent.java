package Model;

import java.util.ArrayList;

public interface ProductComponent {
    String getName();
    void setName(String newName);
    Category getCategory();
    void setCategory(Category newCategory);
    ArrayList<Price> getPrices();
    Price createPrice(Arrangement arrangement, double price, Integer clips);
}
