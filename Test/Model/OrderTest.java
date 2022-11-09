package Model;

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
        Category spiritus = new Category("Spiritus");

        Product product2 = new Product("Celebration", fadøl);
        Product product3 = new Product("Jazz Classic", fadøl);
        Product product4 = new Product("Whisky", spiritus);

        Order order2 = new Order(fredagsbar);
        Order order3 = new Order(fredagsbar);

        product2.createPrice(fredagsbar, 38,null);
        product3.createPrice(fredagsbar, 38,null);
        product4.createPrice(fredagsbar, 599,null);

        order1.createOrderLine(product2, 10);

        Order noOrder = new Order(fredagsbar);

        order1.updateCollectedCost();
        order2.updateCollectedCost();

        // Order for 0 product
        assertEquals(noOrder.getUpdatedPrice(), 0);
        // Order for 1 product
        assertEquals(order1.getUpdatedPrice(), 38);
        // Order for 2 products
        assertEquals(order2.getUpdatedPrice(), 684);
    }

    @Test
    public void shouldSetPayment(){
        PaymentMethod paymentMethod = new PaymentMethod("Mastercard");
        order1.setPaymentMethod(paymentMethod);

        assertEquals(order1.getPaymentMethod(), paymentMethod);
    }

    @Test
    public void shouldCountOrders(){
        Category spiritus = new Category("Spiritus");
        
        Product product2 = new Product("Celebration", fadøl);
        Product product3 = new Product("Jazz Classic", fadøl);
        Product product4 = new Product("Whisky", spiritus);

        Order order2 = new Order(fredagsbar);

        product2.createPrice(fredagsbar, 38,null);
        product3.createPrice(fredagsbar, 38,null);
        product4.createPrice(fredagsbar, 599,null);

        order1.createOrderLine(product2, 10);

        order2.createOrderLine(product1, 8);
        order2.createOrderLine(product2, 10);
        order2.createOrderLine(product3, 2);
        order2.createOrderLine(product4, 1);

        HashMap<ProductComponent, Integer> map = order1.countSoldProduct(fadøl, fredagsbar);

        assertEquals(map.get(product2), 10);
    }


}
