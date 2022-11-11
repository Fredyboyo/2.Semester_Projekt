package Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    private final String name;
    private final String email;
    private final int telephoneNumber;
    private final String address;
    ArrayList<Rental> rentals = new ArrayList<>();
    ArrayList<TourOrderLine> tours = new ArrayList<>();

    public Customer(String name, String email, int telephoneNumber, String address){
        this.name = name;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "  addr: " + address;
    }
}
