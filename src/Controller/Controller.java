package Controller;

import Gui.Observer;
import Model.*;
import Storage.ListStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Controller {

    private static final List<Observer> observers = new ArrayList<>();
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

    public static Price getProductPrice(ProductComponent product, Arrangement arrangement) {
        for (Price price : product.getPrices()) {
            if (price.getArrangement() == arrangement) {
                return price;
            }
        }
        return null;
    }

    public static GiftBasket createGiftBasket(String name, Category category) {
        GiftBasket giftBasket = new GiftBasket(name, category);
        storage.storeProduct(giftBasket);
        notifyObservers();
        return giftBasket;
    }

    public static void addProductToGiftBasket(GiftBasket giftBasket, ProductComponent product) {
        giftBasket.addProduct(product);
    }

    public static void deleteProduct(ProductComponent product) {
        storage.removeProduct(product);
        notifyObservers();
    }

    public static void updateProduct(ProductComponent product, String newName, Category newCategory) {
        product.setName(newName);
        product.setCategory(newCategory);
        notifyObservers();
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

    public static DepositCategory createRentalCategory(String name, int mortgage) {
        DepositCategory rentalCategory = new DepositCategory(name, mortgage);
        storage.storeCategory(rentalCategory);
        notifyObservers();
        return rentalCategory;
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

    public static Rental createRental(
            Arrangement arrangement,
            LocalDate startDate,
            LocalDate endDate,
            Customer customer,
            double payedMortgage) {
        Rental rental = new Rental(arrangement, startDate, endDate, customer, payedMortgage);
        storage.storeOrder(rental);
        notifyObservers();
        return rental;
    }

    public static ArrayList<Order> getOrders() {
        return storage.getOrders();
    }

    public static ArrayList<Order> getOrdersNotRental() {
        ArrayList<Order> ordersNotRental = new ArrayList<>();
        for (Order order : storage.getOrders()) {
            if (!(order instanceof Rental)) {
                ordersNotRental.add(order);
            }
        }
        return ordersNotRental;
    }

    public static ArrayList<Order> getRentals() {
        ArrayList<Order> rentals = new ArrayList<>();
        for (Order order : storage.getOrders()) {
            if (order instanceof Rental) {
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

    public static TourOrderLine createTourOrderLine(Order order, ProductComponent product, int amount, LocalDate localDate, Customer customer) {
        TourOrderLine tourOrderLine = order.createTourOrderLine(product, amount, localDate, customer);
        notifyObservers();
        return tourOrderLine;
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

    public static void deletePaymentMethod(PaymentMethod paymentMethod) {
        storage.removePaymentMethod(paymentMethod);
        notifyObservers();
    }

    public static ArrayList<PaymentMethod> getPaymentMethods() {
        return storage.getPaymentMethods();
    }

    public static Price createProductPrice(ProductComponent product, Arrangement arrangement, double price, Integer clip) {
        Price p = product.createPrice(arrangement, price, clip);
        storage.storePrice(p);
        notifyObservers();
        return p;
    }

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    private static void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public static Customer createCustomer(String name, String email, int number, String address) {
        return new Customer(name, email, number, address);
    }

    public static void init() {
        storage = new ListStorage();
        Arrangement butik = Controller.createArrangement("Butik");
        Arrangement fredagsbar = Controller.createArrangement("Fredagsbar");

        Category flaske = Controller.createCategory("Flaske");
        Category fadøl = Controller.createCategory("Fadøl");
        Category spiritus = Controller.createCategory("Spiritus");
        Category fustage = Controller.createRentalCategory("Fustage", 200);
        Category kulsyre = Controller.createRentalCategory("Kulsyre", 1000);
        Category malt = Controller.createCategory("Malt");
        Category beklædning = Controller.createCategory("Beklædning");
        Category anlæg = Controller.createCategory("Anlæg");
        Category glas = Controller.createCategory("Glas");
        Category sampakninger = Controller.createCategory("Sampakninger");
        Category rundvisning = Controller.createCategory("Rundvisning");

        Product klosterbrygFlaske = Controller.createProduct("Klosterbryg", flaske);
        Product sweet_georgia_brownFlaske = Controller.createProduct("Sweet Georgia Brown", flaske);
        Product extra_pilsnerFlaske = Controller.createProduct("Extra Pilsner", flaske);
        Product celebrationFlaske = Controller.createProduct("Celebration", flaske);
        Product blondieFlaske = Controller.createProduct("Blondie", flaske);
        Product forårsbrygFlaske = Controller.createProduct("Forårsbryg", flaske);
        Product india_pale_aleFlaske = Controller.createProduct("India Pale Ale", flaske);
        Product julebrygFlaske = Controller.createProduct("Julebryg", flaske);
        Product juletøndenFlaske = Controller.createProduct("Juletønden", flaske);
        Product old_strong_aleFlaske = Controller.createProduct("Old Strong Ale", flaske);
        Product fregatten_jyllandFlaske = Controller.createProduct("Fregatten Jylland", flaske);
        Product imperial_stoutFlaske = Controller.createProduct("Imperial Stout", flaske);
        Product tributeFlaske = Controller.createProduct("Tribute", flaske);
        Product black_monsterFlaske = Controller.createProduct("Black Monster", flaske);

        Product klosterbrygFadøl = Controller.createProduct("Klosterbryg", fadøl);
        Product jazz_classicFadøl = Controller.createProduct("Jazz Classic", fadøl);
        Product extra_pilsnerFadøl = Controller.createProduct("Extra Pilsner", fadøl);
        Product celebrationFadøl = Controller.createProduct("Celebration", fadøl);
        Product blondieFadøl = Controller.createProduct("Blondie", fadøl);
        Product forårsbrygFadøl = Controller.createProduct("Forårsbryg", fadøl);
        Product india_pale_aleFadøl = Controller.createProduct("India Pale Ale", fadøl);
        Product julebrygFadøl = Controller.createProduct("Julebryg", fadøl);
        Product imperial_stoutFadøl = Controller.createProduct("Imperial Stout", fadøl);
        Product specialFadøl = Controller.createProduct("Special", fadøl);

        Product æblebrusFadøl = Controller.createProduct("Æblebrus", fadøl);
        Product chipsFadøl = Controller.createProduct("chips", fadøl);
        Product peanutsFadøl = Controller.createProduct("peanuts", fadøl);
        Product colaFadøl = Controller.createProduct("cola", fadøl);
        Product nikolineFadøl = Controller.createProduct("Nikoline", fadøl);
        Product Seven_upFadøl = Controller.createProduct("7-Up", fadøl);
        Product vandFadøl = Controller.createProduct("vand", fadøl);
        Product ølpølserFadøl = Controller.createProduct("Ølpølser", fadøl);

        Product whisky = Controller.createProduct("Whisky 45% 50 cl rør", spiritus);
        Product whisky1 = Controller.createProduct("Whisky 4 cl.", spiritus);
        Product whisky2 = Controller.createProduct("Whisky 43% 50 cl rør", spiritus);
        Product whisky3 = Controller.createProduct("u/ egesplint", spiritus);
        Product whisky4 = Controller.createProduct("m/ egesplint", spiritus);
        Product whisky5 = Controller.createProduct("2*whisky glas + brikker", spiritus);
        Product whisky6 = Controller.createProduct("Liquor of Aarhus", spiritus);
        Product whisky7 = Controller.createProduct("Lyng gin 50 cl", spiritus);
        Product whisky8 = Controller.createProduct("Lyng gin 4 cl", spiritus);

        Product KlosterbrygFustage = Controller.createProduct("Klosterbryg, 20 liter", fustage);
        Product jazz_ClassicFustage = Controller.createProduct("Jazz Classic, 25 liter", fustage);
        Product Extra_PilsnerFustage = Controller.createProduct("Extra Pilsner, 25 liter", fustage);
        Product celebrationFustage = Controller.createProduct("Celebration, 20 liter", fustage);
        Product blondieFustage = Controller.createProduct("Blondie, 25 liter", fustage);
        Product forårsbrygFustage = Controller.createProduct("Forårsbryg, 20 liter", fustage);
        Product india_pale_aleFustage = Controller.createProduct("India Pale Ale, 20 liter", fustage);
        Product julebrygFustage = Controller.createProduct("Julebryg, 20 liter", fustage);
        Product imperial_StoutFustage = Controller.createProduct("Imperial Stout, 20 liter", fustage);

        Product kulsyre6kg = Controller.createProduct("6 kg", kulsyre);
        Product kulsyre4kg = Controller.createProduct("4 kg", kulsyre);
        Product kulsyre10kg = Controller.createProduct("10 kg", kulsyre);

        Product malt_25kg_sæk = Controller.createProduct("25 kg sæk", malt);

        Product t_shirt = Controller.createProduct("T-shirt", beklædning);
        Product polo = Controller.createProduct("Polo", beklædning);
        Product cap = Controller.createProduct("Cap", beklædning);

        Product en_hane = Controller.createProduct("1-hane", anlæg);
        Product to_haner = Controller.createProduct("2-haner", anlæg);
        Product bar_med_flere_haner = Controller.createProduct("Bar med flere haner", anlæg);
        Product levering = Controller.createProduct("Levering", anlæg);
        Product krus = Controller.createProduct("Krus", anlæg);

        Product glas_uanset_størelse = Controller.createProduct("Glas", glas);

        GiftBasket gaveæske1 = Controller.createGiftBasket("Gaveæske 2 øl, 2 glas", sampakninger);
        GiftBasket gaveæske2 = Controller.createGiftBasket("Gaveæske 4 øl", sampakninger);
        GiftBasket gaveæske3 = Controller.createGiftBasket("Trækasse 6 øl", sampakninger);
        GiftBasket gaveæske4 = Controller.createGiftBasket("Gavekurv 6 øl, 2 glas", sampakninger);
        GiftBasket gaveæske5 = Controller.createGiftBasket("Trækasse 6 øl, 6 glas", sampakninger);
        GiftBasket gaveæske6 = Controller.createGiftBasket("Trækasse 12 øl", sampakninger);
        GiftBasket gaveæske7 = Controller.createGiftBasket("Papkasse 12 øl", sampakninger);

        Product rundvisning_pr_person = Controller.createProduct("Rundvisning", rundvisning);

        // ------------------------------------ Prices ----------------------------------------

        Controller.createProductPrice(klosterbrygFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(sweet_georgia_brownFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(extra_pilsnerFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(celebrationFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(blondieFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(forårsbrygFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(india_pale_aleFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(julebrygFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(juletøndenFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(old_strong_aleFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(fregatten_jyllandFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(imperial_stoutFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(tributeFlaske, fredagsbar, 70, 2);
        Controller.createProductPrice(black_monsterFlaske, fredagsbar, 100, 3);

        Controller.createProductPrice(klosterbrygFlaske, butik, 36, null);
        Controller.createProductPrice(sweet_georgia_brownFlaske, butik, 36, null);
        Controller.createProductPrice(extra_pilsnerFlaske, butik, 36, null);
        Controller.createProductPrice(celebrationFlaske, butik, 36, null);
        Controller.createProductPrice(blondieFlaske, butik, 36, null);
        Controller.createProductPrice(forårsbrygFlaske, butik, 36, null);
        Controller.createProductPrice(india_pale_aleFlaske, butik, 36, null);
        Controller.createProductPrice(julebrygFlaske, butik, 36, null);
        Controller.createProductPrice(juletøndenFlaske, butik, 36, null);
        Controller.createProductPrice(old_strong_aleFlaske, butik, 36, null);
        Controller.createProductPrice(fregatten_jyllandFlaske, butik, 36, null);
        Controller.createProductPrice(imperial_stoutFlaske, butik, 36, null);
        Controller.createProductPrice(tributeFlaske, butik, 36, null);
        Controller.createProductPrice(black_monsterFlaske, butik, 60, null);

        Controller.createProductPrice(klosterbrygFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(jazz_classicFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(extra_pilsnerFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(celebrationFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(blondieFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(forårsbrygFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(india_pale_aleFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(julebrygFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(imperial_stoutFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(specialFadøl, fredagsbar, 38, 1);
        Controller.createProductPrice(æblebrusFadøl, fredagsbar, 15, null);
        Controller.createProductPrice(chipsFadøl, fredagsbar, 10, null);
        Controller.createProductPrice(peanutsFadøl, fredagsbar, 15, null);
        Controller.createProductPrice(colaFadøl, fredagsbar, 15, null);
        Controller.createProductPrice(nikolineFadøl, fredagsbar, 15, null);
        Controller.createProductPrice(Seven_upFadøl, fredagsbar, 15, null);
        Controller.createProductPrice(vandFadøl, fredagsbar, 10, null);
        Controller.createProductPrice(ølpølserFadøl, fredagsbar, 30, 1);

        Controller.createProductPrice(whisky, fredagsbar, 599, null);
        Controller.createProductPrice(whisky1, fredagsbar, 50, null);
        Controller.createProductPrice(whisky2, fredagsbar, 499, null);
        Controller.createProductPrice(whisky3, fredagsbar, 300, null);
        Controller.createProductPrice(whisky4, fredagsbar, 350, null);
        Controller.createProductPrice(whisky5, fredagsbar, 80, null);
        Controller.createProductPrice(whisky6, fredagsbar, 175, null);
        Controller.createProductPrice(whisky7, fredagsbar, 350, null);
        Controller.createProductPrice(whisky8, fredagsbar, 40, null);

        Controller.createProductPrice(whisky, butik, 599, null);
        Controller.createProductPrice(whisky2, butik, 499, null);
        Controller.createProductPrice(whisky3, butik, 300, null);
        Controller.createProductPrice(whisky4, butik, 350, null);
        Controller.createProductPrice(whisky5, butik, 80, null);
        Controller.createProductPrice(whisky6, butik, 175, null);
        Controller.createProductPrice(whisky7, butik, 350, null);

        Controller.createProductPrice(KlosterbrygFustage, butik, 775, null);
        Controller.createProductPrice(jazz_ClassicFustage, butik, 625, null);
        Controller.createProductPrice(Extra_PilsnerFustage, butik, 575, null);
        Controller.createProductPrice(celebrationFustage, butik, 775, null);
        Controller.createProductPrice(blondieFustage, butik, 700, null);
        Controller.createProductPrice(forårsbrygFustage, butik, 775, null);
        Controller.createProductPrice(india_pale_aleFustage, butik, 775, null);
        Controller.createProductPrice(julebrygFustage, butik, 775, null);
        Controller.createProductPrice(imperial_StoutFustage, butik, 775, null);

        Controller.createProductPrice(kulsyre6kg, fredagsbar, 400, null);

        Controller.createProductPrice(kulsyre6kg, butik, 400, null);

        Controller.createProductPrice(malt_25kg_sæk, butik, 300, null);

        Controller.createProductPrice(t_shirt, fredagsbar, 70, null);
        Controller.createProductPrice(polo, fredagsbar, 100, null);
        Controller.createProductPrice(cap, fredagsbar, 30, null);

        Controller.createProductPrice(t_shirt, butik, 70, null);
        Controller.createProductPrice(polo, butik, 100, null);
        Controller.createProductPrice(cap, butik, 30, null);

        Controller.createProductPrice(en_hane, butik, 250, null);
        Controller.createProductPrice(to_haner, butik, 400, null);
        Controller.createProductPrice(bar_med_flere_haner, butik, 500, null);
        Controller.createProductPrice(levering, butik, 500, null);
        Controller.createProductPrice(krus, butik, 60, null);

        Controller.createProductPrice(glas_uanset_størelse, butik, 15, null);

        Controller.createProductPrice(gaveæske1, fredagsbar, 110, null);
        Controller.createProductPrice(gaveæske2, fredagsbar, 140, null);
        Controller.createProductPrice(gaveæske3, fredagsbar, 260, null);
        Controller.createProductPrice(gaveæske4, fredagsbar, 260, null);
        Controller.createProductPrice(gaveæske5, fredagsbar, 350, null);
        Controller.createProductPrice(gaveæske6, fredagsbar, 410, null);
        Controller.createProductPrice(gaveæske7, fredagsbar, 370, null);

        Controller.createProductPrice(gaveæske1, butik, 110, null);
        Controller.createProductPrice(gaveæske2, butik, 140, null);
        Controller.createProductPrice(gaveæske3, butik, 260, null);
        Controller.createProductPrice(gaveæske4, butik, 260, null);
        Controller.createProductPrice(gaveæske5, butik, 350, null);
        Controller.createProductPrice(gaveæske6, butik, 410, null);
        Controller.createProductPrice(gaveæske7, butik, 370, null);

        Controller.createProductPrice(rundvisning_pr_person, butik, 100, null);

        // ------------------------------------ Sale ----------------------------------------

        Controller.createPaymentMethod("Kontant");
        Controller.createPaymentMethod("Credit-kort");
        Controller.createPaymentMethod("MobilePay");

    }

    public HashMap<ProductComponent, Integer> countSoldProduct(Category category, Arrangement arrangement) {
        HashMap<ProductComponent, Integer> map = new HashMap<>();
        for (Order order : storage.getOrders()) {
            for (OrderLine ol : order.getOrderLines()) {
                if (ol.getArrangement() == arrangement && ol.getProduct().getCategory() == category) {
                    if (map.containsKey(ol.getProduct())) {
                        map.put(ol.getProduct(), map.get(ol.getProduct()) + ol.getAmount());
                    } else
                        map.put(ol.getProduct(), ol.getAmount());
                }
            }
        }
        notifyObservers();
        return map;
    }
}

