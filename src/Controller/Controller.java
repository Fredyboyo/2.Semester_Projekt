package Controller;

import Model.*;
import Storage.ListStorage;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Controller {

    private static Storage storage;

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

    public static GiftBasket createGiftBasket(String name, Category category, ArrayList<Price> prices, ArrayList<ProductComponent> products) {
        GiftBasket giftBasket = new GiftBasket(name, category, prices, products);
        storage.storeProduct(giftBasket);
        notifyObservers();
        return giftBasket;
    }

    public static void deleteProduct(ProductComponent product){
        storage.removeProduct(product);
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

    public static void init() {
        Arrangement butik = createArrangement("Butik");
        Arrangement fredagsbar = createArrangement("Fredagsbar");

        Category fadøl = createCategory("Fadøl");
        Category flaske = createCategory("Flaske");
        Category spriritus = createCategory("Spiritus");

        ProductComponent beer = createProduct("Beer",fadøl);
        ProductComponent beerII = createProduct("Beer 2 (The sequel)",fadøl);
        ProductComponent drink = createProduct("Drink",fadøl);
        ProductComponent beverage = createProduct("Beverage",fadøl);
        ProductComponent liquid = createProduct("Liquid",fadøl);
        ProductComponent brew = createProduct("Brew",fadøl);
        ProductComponent glass = createProduct("Glass",fadøl);
        ProductComponent cup = createProduct("Cup",fadøl);
        ProductComponent booze = createProduct("Booze",fadøl);
        ProductComponent liquor = createProduct("Liquor",fadøl);

        ProductComponent klosterbryg = createProduct("Klosterbryg", fadøl);
        ProductComponent klosterbrygfl = createProduct("Klosterbryg flaske", flaske);
        ProductComponent whisky = createProduct("Whisky 45% 50 cl rør", spriritus);

        beer.createPrice(butik,30);
        beerII.createPrice(butik,50);
        drink.createPrice(butik,30);
        beverage.createPrice(butik,50);
        liquid.createPrice(butik,100);
        brew.createPrice(butik,80);
        glass.createPrice(butik,20);
        cup.createPrice(butik,35);
        booze.createPrice(butik,45);
        liquor.createPrice(butik,55);

        createPrice(klosterbryg, fredagsbar,38);
        createPrice(klosterbrygfl, fredagsbar,70);
        createPrice(klosterbrygfl, butik,36);
        createPrice(whisky, butik,599);

        Order order1 = createOrder(fredagsbar);
        createOrderLine(order1, klosterbryg, 4);

        Order order2 = createOrder(butik);
        createOrderLine(order2, whisky, 2);
        createOrderLine(order2, klosterbrygfl, 2);

        Order order3 = createOrder(fredagsbar);
        createOrderLine(order3, klosterbrygfl, 10);

        Rental order4 = createRental(butik,LocalDate.now(),LocalDate.now(),"bob",0.95);
        createOrderLine(order4, whisky, 2);
        createOrderLine(order4, klosterbrygfl, 10);

        Rental order5 = createRental(butik,LocalDate.now(),LocalDate.now(),"lejer",500);
        createOrderLine(order5, whisky, 2);
        createOrderLine(order5, klosterbrygfl, 10);
        order5.setIsCompletedToTrue();

        PaymentMethod mobilePay = createPaymentMethod("MobilePay");
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
