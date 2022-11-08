package Model;

import Model.DiscountStrategy.AmountDiscountStrategy;
import Model.DiscountStrategy.NoDiscountStrategy;
import Model.DiscountStrategy.PercentageDiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreeMethodTest {

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
        price = product1.createPrice(fredagsbar,75,null);
        orderLine1 = order1.createOrderLine(product1, 10);
    }

    @Test
    public void shouldCalculateTotalPrice(){
        // OrderLine method updateCost()
        orderLine1.updateCost();
        assertEquals(orderLine1.getUpdatedPrice(), 750);
    }

    @Test
    public void shouldGiveDiscount(){
        // Discount method giveDiscount()
        Discount noDiscount = new NoDiscountStrategy();
        order1.setDiscountStrategy(noDiscount);
        assertEquals(order1.getDiscountStrategy(), noDiscount);
        assertEquals(order1.getUpdatedPrice(), 750);

        Discount percentageDiscount = new PercentageDiscountStrategy(10);
        order1.setDiscountStrategy(percentageDiscount);
        assertEquals(order1.getDiscountStrategy(), percentageDiscount);
        assertEquals(order1.getUpdatedPrice(), 675);

        Discount amountDiscount = new AmountDiscountStrategy(100);
        order1.setDiscountStrategy(amountDiscount);
        assertEquals(order1.getDiscountStrategy(), amountDiscount);
        assertEquals(order1.getUpdatedPrice(), 650);


    }


}
