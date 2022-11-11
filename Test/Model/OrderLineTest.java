package Model;

import Model.DiscountStrategy.PercentageDiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class OrderLineTest {

    private Arrangement fredagsbar;
    private Order order1;
    private Category fadøl;
    private Product product1;
    private Price price;

    @BeforeEach
    public void setUp(){
        fredagsbar = new Arrangement("Fredagsbar");
        order1 = new Order(fredagsbar);
        fadøl = new Category("Fadøl");
        product1 = new Product("Klosterbryg", fadøl);
        price = product1.createPrice(fredagsbar,38,null);
    }

    @Test
    public void shouldUpdateCost(){

        // OrderLines
        OrderLine orderLine1 = order1.createOrderLine(product1, 0);
        OrderLine orderLine2 = order1.createOrderLine(product1, 1);
        OrderLine orderLine3 = order1.createOrderLine(product1, 2);

        // Update
        orderLine1.updatePrice();
        orderLine2.updatePrice();
        orderLine3.updatePrice();

        // OrderLine with 0 amount
        assertEquals(orderLine1.getUpdatedPrice(),0);
        // OrderLine with 1 amount
        assertEquals(orderLine2.getUpdatedPrice(),38);
        // OrderLine with 2 amount
        assertEquals(orderLine3.getUpdatedPrice(),76);

        // Percentage discount - 5%
        Discount percentage = new PercentageDiscountStrategy();
        percentage.setValue(5);
        // OrderLine with 0 amount
        orderLine1.setDiscountStrategy(percentage);
        assertEquals(orderLine1.getUpdatedPrice(),0);
        // OrderLine with 1 amount
        orderLine2.setDiscountStrategy(percentage);
        assertEquals(orderLine2.getUpdatedPrice(),36.1);
        // OrderLine with 2 amount
        orderLine3.setDiscountStrategy(percentage);
        assertEquals(orderLine3.getUpdatedPrice(),72.2);

    }

}
