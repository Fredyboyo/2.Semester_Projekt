package Controller;

import Gui.Observer;
import Model.*;
import Storage.ListStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Controller {

    private static Storage storage = new ListStorage();

    public static Storage getStorage() {
        return storage;
    }

    public static void setStorage(Storage storage) {
        Controller.storage = storage;
    }

    public static Product createProduct(String name, Category category) {
        Product product = new Product(name, category);
        storage.storeProduct(product);
        notifyObservers();
        return product;
    }

    public static double getProductPrice(ProductComponent product, Arrangement arrangement) {
        for (Price price : product.getPrices()) {
            if (price.getArrangement() == arrangement) {
                return price.getValue();
            }
        }
        return 0;
    }

    public static GiftBasket createGiftBasket(String name, Category category, ArrayList<Price> prices, ArrayList<ProductComponent> products) {
        GiftBasket giftBasket = new GiftBasket(name, category, prices, products);
        storage.storeProduct(giftBasket);
        notifyObservers();
        return giftBasket;
    }

    public static void deleteProduct(ProductComponent product){
        storage.removeProduct(product);
        notifyObservers();
    }

    public static void updateProduct(ProductComponent product, String newName, Category newCategory){
        product.setName(newName);
        product.setCategory(newCategory);
    }

    public static ArrayList<ProductComponent> getProducts() {
        return storage.getProducts();
    }

    public static Category createCategory(String name) {
        Category category = new Category(name);
        storage.storeCategory(category);
        notifyObservers();
        return category;
    }

    public static ArrayList<Category> getCategories() {
        return storage.getCategories();
    }

    public static Arrangement createArrangement(String name) {
        Arrangement arrangement = new Arrangement(name);
        storage.storeArrangement(arrangement);
        notifyObservers();
        return arrangement;
    }

    public static ArrayList<Arrangement> getArrangements() {
        return storage.getArrangements();
    }

    public static Order createOrder(Arrangement arrangement) {
        Order order = new Order(arrangement);
        storage.storeOrder(order);
        notifyObservers();
        return order;
    }

    public static Rental createRental(Arrangement arrangement, LocalDate startDate, LocalDate endDate, String person, double payedMortgage) {
        Rental rental = new Rental(arrangement, startDate, endDate, person, payedMortgage);
        storage.storeOrder(rental);
        notifyObservers();
        return rental;
    }

    public static ArrayList<Order> getOrders() {
        return storage.getOrders();
    }

    public static ArrayList<Order> getOrdersNotRental(){
        ArrayList<Order> ordersNotRental = new ArrayList<>();
        for(Order order : storage.getOrders()){
            if(!(order instanceof Rental)){
                ordersNotRental.add(order);
            }
        }
        return ordersNotRental;
    }

    public static ArrayList<Order> getRentals(){
        ArrayList<Order> rentals = new ArrayList<>();
        for(Order order : storage.getOrders()){
            if(order instanceof Rental){
                rentals.add(order);
            }
        }
        return rentals;
    }

    public static OrderLine createOrderLine(Order order, ProductComponent product, int amount) {
        OrderLine orderLine = order.createOrderLine(product, amount);
        notifyObservers();
        return orderLine;

    }

    public static void removeOrderLine(Order order, OrderLine orderLine) {
        order.removeOrderLine(orderLine);
        notifyObservers();
    }

    public static PaymentMethod createPaymentMethod(String name) {
        PaymentMethod paymentMethod = new PaymentMethod(name);
        storage.storePaymentMethod(paymentMethod);
        notifyObservers();
        return paymentMethod;
    }

    public static void deletPaymentMethod(PaymentMethod paymentMethod){
        storage.removePaymentMethod(paymentMethod);
    }

    public static ArrayList<PaymentMethod> getPaymentMethods() {
        return storage.getPaymentMethods();
    }

    public static Price createPrice(ProductComponent product, Arrangement arrangement, double price){
        Price p = product.createPrice(arrangement, price);
        storage.storePrice(p);
        notifyObservers();
        return p;
    }

    private static final List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    private static void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
