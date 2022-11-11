package Model;

import Model.DiscountStrategy.AmountDiscountStrategy;
import Model.DiscountStrategy.PercentageDiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    private Arrangement fredagsbar;
    private Order order1;
    private Category fadøl;
    private Product product1;
    private Price price;
    private OrderLine orderLine1;

    @BeforeEach
    public void setUp(){
        fredagsbar = new Arrangement("Fredagsbar");
        order1 = new Order(fredagsbar);
        fadøl = new Category("Fadøl");
        product1 = new Product("Klosterbryg", fadøl);
        price = product1.createPrice(fredagsbar,38,null);
        orderLine1 = order1.createOrderLine(product1, 8);
    }

    @Test
    public void shouldConstructOrder() {
        Order order1 = new Order(fredagsbar);
        assertEquals(order1.getArrangement(), fredagsbar);

    }

    @Test
    public void shouldCreateOrderLine(){
        Product product1 = new Product("p1", fadøl);
        OrderLine orderLine1 = order1.createOrderLine(product1, 10);

        assertEquals(order1.getOrderLines().get(0).getAmount(),8);
        assertEquals(this.orderLine1.getProduct(), this.product1);

        assertEquals(order1.getOrderLines().get(1).getAmount(),10);
        assertEquals(orderLine1.getProduct(), product1);
    }

    @Test
    public void shouldCalculateCost(){
        // Category
        Category spiritus = new Category("Spiritus");

        // Products
        Product product2 = new Product("Celebration", fadøl);
        Product product3 = new Product("Jazz Classic", fadøl);
        Product product4 = new Product("Whisky", spiritus);

        // Orders
        Order noOrder = new Order(fredagsbar);
        Order order2 = new Order(fredagsbar);

        // Prices
        product2.createPrice(fredagsbar, 38,null);
        product3.createPrice(fredagsbar, 38,null);
        product4.createPrice(fredagsbar, 599,null);

        // OrderLines
        order2.createOrderLine(product1,8);
        order2.createOrderLine(product2,10);

        // Update cost
        order1.updateCollectedPrices();
        order2.updateCollectedPrices();

        // Order for 0 product
        assertEquals(noOrder.getUpdatedPrice(), 0);
        // Order for 1 product
        assertEquals(order1.getUpdatedPrice(), 304);
        // Order for 2 products
        assertEquals(order2.getUpdatedPrice(), 684);

        // Amount discount
        Discount amountDiscount = new AmountDiscountStrategy();
        amountDiscount.setValue(50);
        // Order for 0 product
        noOrder.setDiscountStrategy(amountDiscount);
        assertEquals(noOrder.getUpdatedPrice(), 0);
        // Order for 1 product
        order1.setDiscountStrategy(amountDiscount);
        assertEquals(order1.getUpdatedPrice(), 254);
        // Order for 2 products
        order2.setDiscountStrategy(amountDiscount);
        assertEquals(order2.getUpdatedPrice(), 634);

        // Percentage discount
        Discount percentageDiscount = new PercentageDiscountStrategy();
        percentageDiscount.setValue(10);
        // Order for 0 product
        noOrder.setDiscountStrategy(percentageDiscount);
        assertEquals(noOrder.getUpdatedPrice(), 0);
        // Order for 1 product
        order1.setDiscountStrategy(percentageDiscount);
        assertEquals(order1.getUpdatedPrice(), 273.6);
        // Order for 2 products
        order2.setDiscountStrategy(percentageDiscount);
        assertEquals(order2.getUpdatedPrice(), 615.6);

    }

    @Test
    public void shouldSetPayment(){
        PaymentMethod paymentMethod = new PaymentMethod("Mastercard");
        order1.setPaymentMethod(paymentMethod);

        assertEquals(order1.getPaymentMethod(), paymentMethod);
    }

    @Test
    public void shouldCountOrders(){
        // Category
        Category spiritus = new Category("Spiritus");

        // Products
        Product product2 = new Product("Celebration", fadøl);
        Product product3 = new Product("Jazz Classic", fadøl);
        Product product4 = new Product("Whisky", spiritus);

        // Order
        Order order2 = new Order(fredagsbar);

        // Prices
        product2.createPrice(fredagsbar, 38,null);
        product3.createPrice(fredagsbar, 38,null);
        product4.createPrice(fredagsbar, 599,null);

        // OrderLines
        order1.createOrderLine(product2, 10);
        order2.createOrderLine(product1, 8);
        order2.createOrderLine(product2, 10);
        order2.createOrderLine(product3, 2);
        order2.createOrderLine(product4, 1);

        // Hashmap
        HashMap<ProductComponent, Integer> map = order1.countSoldProduct(fadøl, fredagsbar);

        assertEquals(map.get(product2), 10);
    }


}
