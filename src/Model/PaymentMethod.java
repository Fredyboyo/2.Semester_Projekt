package Model;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    private final String name;

    public PaymentMethod(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}