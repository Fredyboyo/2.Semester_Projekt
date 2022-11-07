package Model;

import java.util.ArrayList;

public class Customer {
    private String name;
    private String email;
    private int telephoneNumber;
    private String address;
    ArrayList<Rental> rentals = new ArrayList<>();
    ArrayList<TourOrderLine> tours = new ArrayList<>();

    public Customer(String name, String email, int telephoneNumber, String address){
        this.name = name;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }
}
