package Controller;

import Model.*;
import Storage.Storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Controller {
    private static final Storage storage = new Storage();

    public static Product createProduct(String name, Category category) {
        Product product = new Product(name, category);
        category.addProduct(product);
        storage.storeProduct(product);
        return product;
    }

    public static GiftBasket createGiftBasket(String name, Category category, ArrayList<Price> prices, ArrayList<ProductComponent> products) {
        GiftBasket giftBasket = new GiftBasket(name, category, prices, products);
        storage.storeProduct(giftBasket);
        return giftBasket;
    }

    public static Tour createTour(String name, ArrayList<Price> prices, LocalDateTime startTime, LocalDateTime endTime, String person) {
        Tour tour = new Tour(name, prices, startTime, endTime, person);
        storage.storeProduct(tour);
        return tour;
    }

    public static ArrayList<ProductComponent> getProducts() {
        return storage.getProducts();
    }

    public static Category createCategory(String name) {
        Category category = new Category(name);
        storage.storeCategory(category);
        return category;
    }

    public static ArrayList<Category> getCategories() {
        return storage.getCategories();
    }

    public static Arrangement createArrangement(String name) {
        Arrangement arrangement = new Arrangement(name);
        storage.storeArrangement(arrangement);
        return arrangement;
    }

    public static ArrayList<Arrangement> getArrangements() {
        return storage.getArrangements();
    }

    public static Order createOrder(Arrangement arrangement) {
        Order order = new Order(arrangement);
        storage.storeOrder(order);
        return order;
    }

    public static Rental createRental(Arrangement arrangement, LocalDate startDate, LocalDate endDate, String person, double payedMortgage) {
        Rental rental = new Rental(arrangement, startDate, endDate, person, payedMortgage);
        storage.storeOrder(rental);
        return rental;
    }

    public static ArrayList<Order> getOrders() {
        return storage.getOrders();
    }

    public static OrderLine createOrderLine(Order order, ProductComponent product, int count) {
        return order.createOrderLine(product, count);
    }

    public static PaymentMethod createPaymentMethod(String name) {
        PaymentMethod paymentMethod = new PaymentMethod(name);
        storage.storePaymentMethod(paymentMethod);
        return paymentMethod;
    }

    public static ArrayList<PaymentMethod> getPaymentMethods() {
        return storage.getPaymentMethods();
    }

    public static void init() {
        Arrangement arrangement = createArrangement("Shop");
        Category beers = createCategory("Beers");
        Category flasks = createCategory("Flasks");
        Product beer = createProduct("Beer",beers);
        beer.createPrice(arrangement,30);
    }
}
