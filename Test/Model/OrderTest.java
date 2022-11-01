package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {

    private Arrangement arrangement;
    private Order order;
    private Category category;
    private Product product;
    private Price price;
    private OrderLine orderLine;

    @BeforeEach
    public void setUp(){
        arrangement = new Arrangement("Fredagsbar");
        order = new Order(arrangement);
        category = new Category("Fadøl");
        product = new Product("Klosterbryg", category);
        price = product.createPrice(arrangement,75);
        orderLine = order.createOrderLine(product, 8);
    }

    @Test
    public void shouldConstructOrder() {
        assertEquals(order.getClass(), Order.class);

    }

    @Test
    public void shouldCreateOrderLine(){
        Product product1 = new Product("Flaske", category);
        OrderLine orderLine2 = order.createOrderLine(product1, 10);

        assertEquals(order.getOrderLines().get(0).getAmount(),8);
        assertEquals(orderLine.getClass(), OrderLine.class);

        assertEquals(order.getOrderLines().get(1).getAmount(),10);
        assertEquals(orderLine2.getClass(), OrderLine.class);
    }

    @Test
    public void shouldCalculateCost(){
        Product product1 = new Product("Flaske", category);
        product1.createPrice(arrangement, 50);
        order.createOrderLine(product1, 10);

        order.calculateCollectedCost();

        assertEquals(order.getCollectedCost(), 1100);
    }

    @Test
    public void shouldSetPayment(){
        PaymentMethod paymentMethod = new PaymentMethod("Mastercard");
        order.setPaymentMethod(paymentMethod);

        assertEquals(order.getPaymentMethod(), paymentMethod);
    }


}
