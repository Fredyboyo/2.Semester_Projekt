package Model;

import java.util.ArrayList;

public interface ProductComponent {
    String getName();
    Category getCategory();
    ArrayList<Price> getPrices();
}
