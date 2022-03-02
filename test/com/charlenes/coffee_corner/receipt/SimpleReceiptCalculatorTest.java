package com.charlenes.coffee_corner.receipt;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderPart;
import com.charlenes.coffee_corner.model.Receipt;
import com.charlenes.coffee_corner.storage.Menu;
import com.charlenes.coffee_corner.storage.OrdersHistory;
import com.charlenes.coffee_corner.storage.SimpleMenu;
import com.charlenes.coffee_corner.storage.SimpleOrdersHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleReceiptCalculatorTest {

    private Menu menu;
    private ReceiptCalculator receiptCalculator;
    private OrdersHistory ordersHistory;

    private String cardID1 = "24353643";
    private String cardID2 = "65464221";

    private OrderPart orderPart1;
    private OrderPart orderPart2;
    private OrderPart orderPart3;
    private OrderPart orderPart4;


    @BeforeEach
    void setup(){

        ordersHistory = new SimpleOrdersHistory();
        receiptCalculator = new SimpleReceiptCalculator(ordersHistory);
        menu = new SimpleMenu();

        // small coffee with extra milk
        orderPart1 = new OrderPart();
        orderPart1.setMainItem(menu.getItemById(1));
        orderPart1.addExtra(menu.getItemById(6));

        // bacon roll
        orderPart2 = new OrderPart();
        orderPart2.setMainItem(menu.getItemById(4));

        // medium special roast coffee
        orderPart3 = new OrderPart();
        orderPart3.setMainItem(menu.getItemById(2));
        orderPart3.addExtra(menu.getItemById(8));

        // juice
        orderPart4 = new OrderPart();
        orderPart4.setMainItem(menu.getItemById(5));

    }

    @Test
    void testNoDiscountOrder(){
        // small coffee with extra milk
        Order order1 = new Order();
        order1.addOrderPart(orderPart1);

        Receipt receipt = receiptCalculator.calculate(order1, null);
        assertEquals(2, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have two elements on receipt");
        assertEquals(0, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have no discount elements on receipt");
        assertEquals(new BigDecimal("2.8"), receipt.getTotalPrice());

    }

    @Test
    void testDiscountOrder(){
        // small coffee with extra milk and a bacon roll
        Order order1 = new Order();
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart2);

        Receipt receipt = receiptCalculator.calculate(order1, null);
        assertEquals(3, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have three elements on receipt");
        assertEquals(1, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have one discount element on receipt");
        assertEquals(new BigDecimal("7.0"), receipt.getTotalPrice());

    }

    @Test
    void testDiscountOrder_cheapestExtra(){
        // small coffee with extra milk and a bacon roll and special roast medium coffee
        Order order1 = new Order();
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart2);
        order1.addOrderPart(orderPart3);

        Receipt receipt = receiptCalculator.calculate(order1, null);
        assertEquals(5, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have five elements on receipt");
        assertEquals(1, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have one discount element on receipt");
        assertEquals(menu.getItemById(6), receipt.getDiscountItems().keySet().toArray()[0],
                "Small coffee should be on discount list");
        assertEquals(1, receipt.getDiscountItems().values().toArray()[0],
                "There should be one Small coffee on discount list");
        assertEquals(new BigDecimal("10.9"), receipt.getTotalPrice());
    }

    @Test
    void testFiveBeveragesWithoutStampCard(){
        // 5 * small coffee with extra milk
        Order order1 = new Order();
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        Receipt receipt = receiptCalculator.calculate(order1, null);
        assertEquals(10, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have ten elements on receipt");
        assertEquals(0, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have no discount elements on receipt");
        assertEquals(new BigDecimal("14.0"), receipt.getTotalPrice());
    }

    @Test
    void testFiveBeveragesWithStampCard_oneOrder(){
        Order order1 = new Order();
        // 5 * small coffee with extra milk
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        Receipt receipt = receiptCalculator.calculate(order1, cardID1);
        assertEquals(10, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have ten elements on receipt");
        assertEquals(1, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have one discount element on receipt");
        assertEquals(new BigDecimal("11.5"), receipt.getTotalPrice());
    }


    @Test
    void testBeveragesBonusWithStampCard_twoOrdersTheSameCard() {
        Order order1 = new Order();
        // 4 * small coffee with extra milk
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        ordersHistory.addOrderToHistory(order1, cardID1);
        // 2 * small coffee with extra milk + medium special roast coffee
        Order order2 = new Order();
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart3);
        Receipt receipt = receiptCalculator.calculate(order2, cardID1);
        assertEquals(6, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have six elements on receipt");
        assertEquals(1, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have one discount element on receipt");
        assertEquals(2, receipt.getStampCount(),
                "We should have two stamps left for future use");
        assertEquals(new BigDecimal("7.0"), receipt.getTotalPrice());

    }

    @Test
    void testBeveragesBonusWithStampCard_twoOrdersDifferentCards() {
        Order order1 = new Order();
        // 4 * small coffee with extra milk
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        ordersHistory.addOrderToHistory(order1, cardID1);
        // 2 * small coffee with extra milk + medium special roast coffee
        Order order2 = new Order();
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart3);
        Receipt receipt = receiptCalculator.calculate(order2, cardID2);
        assertEquals(6, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have six elements on receipt");
        assertEquals(0, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have no discount element on receipt");
        assertEquals(3, receipt.getStampCount(),
                "We should have three stamps left for future use");
        assertEquals(new BigDecimal("9.5"), receipt.getTotalPrice());

    }


    @Test
    void testBeveragesBonusWithStampCard_twoOrdersTheSameCard_cheapestBonus() {
        Order order1 = new Order();
        // 4 * small coffee with extra milk
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        ordersHistory.addOrderToHistory(order1, cardID1);
        // juice + 2 * small coffee with extra milk + medium special roast coffee
        Order order2 = new Order();
        order2.addOrderPart(orderPart4);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart3);
        Receipt receipt = receiptCalculator.calculate(order2, cardID1);
        assertEquals(7, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have seven elements on receipt");
        assertEquals(1, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have one discount element on receipt");
        assertEquals(3, receipt.getStampCount(),
                "We should have three stamps left for future use");
        assertEquals(menu.getItemById(1), receipt.getDiscountItems().keySet().toArray()[0],
                "Small coffee should be on discount list");
        assertEquals(1, receipt.getDiscountItems().values().toArray()[0],
                "There should be one Small coffee on discount list");
        assertEquals(new BigDecimal("10.95"), receipt.getTotalPrice());
    }

    @Test
    void testBeveragesBonusWithStampCard_twoOrdersTheSameCard_cheapestBonus_twoCoffees() {
        Order order1 = new Order();
        // 4 * small coffee with extra milk
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart1);
        ordersHistory.addOrderToHistory(order1, cardID1);
        // juice + 7 * small coffee with extra milk + medium special roast coffee
        Order order2 = new Order();
        order2.addOrderPart(orderPart4);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart1);
        order2.addOrderPart(orderPart3);
        Receipt receipt = receiptCalculator.calculate(order2, cardID1);
        assertEquals(17, receipt.getItems().values().stream().reduce(0, Integer::sum),
                "We should have seventeen elements on receipt");
        assertEquals(2, receipt.getDiscountItems().values().stream().reduce(0, Integer::sum),
                "We should have two discount element on receipt");
        assertEquals(3, receipt.getStampCount(),
                "We should have three stamps left for future use");
        assertEquals(menu.getItemById(1), receipt.getDiscountItems().keySet().toArray()[0],
                "Small coffee should be on discount list");
        assertEquals(2, receipt.getDiscountItems().values().toArray()[0],
                "There should be two Small coffee on discount list");
        assertEquals(new BigDecimal("22.45"), receipt.getTotalPrice());
    }
}
