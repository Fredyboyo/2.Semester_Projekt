package Model;

import java.io.Serializable;

public interface Discount extends Serializable {

    double discount(double collectedCost);
    void setValue(double var);

}
