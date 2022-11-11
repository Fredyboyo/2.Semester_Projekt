package Model;

import Model.DiscountStrategy.AmountDiscountStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountTest {



    @Test
    public void shouldGiveAmountDiscount(){
        Arrangement fredagsbar = new Arrangement("Fredagsbar");
        Category fadøl = new Category("Fadøl");
        Product product1 = new Product("Klosterbryg", fadøl);
        Order order1 = new Order(fredagsbar);
        Order order2 = new Order(fredagsbar);
        Discount discount1 = new AmountDiscountStrategy();
        discount1.setValue(8);
        Discount discount2 = new AmountDiscountStrategy();
        discount2.setValue(40);

        product1.createPrice(fredagsbar,38,null);

        order1.createOrderLine(product1, 1);
        order2.createOrderLine(product1,1);

        order1.setDiscountStrategy(discount1);
        order2.setDiscountStrategy(discount2);

        assertEquals(order1.getUpdatedPrice(), 30);
        assertEquals(order2.getUpdatedPrice(), -2);

    }

}
