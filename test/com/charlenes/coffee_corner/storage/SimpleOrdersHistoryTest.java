package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleOrdersHistoryTest {


    private OrdersHistory ordersHistory;
    private DataLoader dataLoader;
    private Menu menu;

    private final String cardID1 = "24353643";
    private final String cardID2 = "65464221";

    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    void setup() {
        dataLoader = new SimpleDataLoader();
        menu = new SimpleMenu(dataLoader);
        ordersHistory = new SimpleOrdersHistory();

        OrderPart orderPart1 = new OrderPart();
        orderPart1.setMainItem(menu.getItemById(1));
        orderPart1.addExtra(menu.getItemById(7));

        OrderPart orderPart2 = new OrderPart();
        orderPart2.setMainItem(menu.getItemById(4));

        OrderPart orderPart3 = new OrderPart();
        orderPart3.setMainItem(menu.getItemById(3));

        // 3 items
        order1 = new Order();
        order1.addOrderPart(orderPart1);
        order1.addOrderPart(orderPart2);

        // 2 items
        order2 = new Order();
        order2.addOrderPart(orderPart2);
        order2.addOrderPart(orderPart3);

        // 1 item
        order3 = new Order();
        order3.addOrderPart(orderPart3);
    }


    @Test
    void testAddingOrdersToHistory() {

        // add a three-item order to the first card history
        ordersHistory.addOrderToHistory(order1, cardID1);
        assertEquals(3, ordersHistory.getOrdersSummary(cardID1).values().stream().reduce(0L, Long::sum),
                "For card " + cardID1 + " we should have 3 elements in history");
        assertEquals(0, ordersHistory.getOrdersSummary(cardID2).values().stream().reduce(0L, Long::sum),
                "For card " + cardID2 + " we should have no elements in history");

        // add a two-item order to the second card history
        ordersHistory.addOrderToHistory(order2, cardID2);
        assertEquals(3, ordersHistory.getOrdersSummary(cardID1).values().stream().reduce(0L, Long::sum),
                "For card " + cardID1 + " we should have 3 still elements in history");
        assertEquals(2, ordersHistory.getOrdersSummary(cardID2).values().stream().reduce(0L, Long::sum),
                "For card " + cardID2 + " we should have 2 elements in history");

        // add a one-item order to the first card history
        ordersHistory.addOrderToHistory(order3, cardID1);
        assertEquals(4, ordersHistory.getOrdersSummary(cardID1).values().stream().reduce(0L, Long::sum),
                "For card " + cardID1 + " we should have 4 elements in history");
        assertEquals(2, ordersHistory.getOrdersSummary(cardID2).values().stream().reduce(0L, Long::sum),
                "For card " + cardID2 + " we should have 2 still elements in history");

    }
}
