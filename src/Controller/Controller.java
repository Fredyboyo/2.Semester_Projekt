package Controller;

import Model.*;
import Storage.Storage;

import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {
    private static final Storage storage = new Storage();

    public static Product createProduct(String name, Category category) {
        Product product = new Product(name, category);
        storage.storeProduct(product);
        return product;
    }

    public static GiftBasket createGiftBasket(String name, Category category, ArrayList<Price> prices, ArrayList<ProductComponent> products) {
        GiftBasket giftBasket = new GiftBasket(name, category, prices, products);
        storage.storeProduct(giftBasket);
        return giftBasket;
    }

    public static Tour createTour(String name, ArrayList<Price> prices, LocalDate date, String person) {
        Tour tour = new Tour(name, prices, date, person);
        storage.storeProduct(tour);
        return tour;
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

    public static OrderLine createOrderLine(Order order, ProductComponent product, int amount) {
        return order.createOrderLine(product, amount);
    }

    public static void removerOrderLine(Order order, OrderLine orderLine) {
        order.removeOrderLine(orderLine);
    }

    public static PaymentMethod createPaymentMethod(String name) {
        PaymentMethod paymentMethod = new PaymentMethod(name);
        storage.storePaymentMethod(paymentMethod);
        return paymentMethod;
    }

    public static void deletPaymentMethod(PaymentMethod paymentMethod){
        storage.removePaymentMethod(paymentMethod);
    }

    public static ArrayList<PaymentMethod> getPaymentMethods() {
        return storage.getPaymentMethods();
    }

    public static Price createPrice(ProductComponent product, Arrangement arrangement, double kr){
        Price price =  product.createPrice(arrangement, kr);
        storage.storePrice(price);
        return price;
    }

    public static void init() {
        Category fadøl = createCategory("Fadøl");
        Category flaske = createCategory("Flaske");
        Category spriritus = createCategory("Spiritus");

        Arrangement fredagsbar = createArrangement("Fredagsbar");
        Arrangement butik = createArrangement("Butik");

        ProductComponent klosterbryg = createProduct("Klosterbryg", fadøl);
        createPrice(klosterbryg, fredagsbar,38);
        ProductComponent klosterbrygfl = createProduct("Klosterbryg", flaske);
        createPrice(klosterbrygfl, fredagsbar,70);
        createPrice(klosterbrygfl, butik,36);
        ProductComponent whisky = createProduct("Whisky 45% 50 cl rør", spriritus);
        createPrice(whisky, butik,599);

        Order order1 = createOrder(fredagsbar);
        createOrderLine(order1, klosterbryg, 4);

        Order order2 = createOrder(fredagsbar);
        createOrderLine(order2, klosterbrygfl, 2);

        Order order3 = createOrder(butik);
        createOrderLine(order3, whisky, 2);
        createOrderLine(order3, klosterbrygfl, 10);

        PaymentMethod mobilePay = createPaymentMethod("MobilePay");
    }
}
