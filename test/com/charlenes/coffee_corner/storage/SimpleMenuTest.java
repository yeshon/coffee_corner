package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class SimpleMenuTest {

    private Menu menu;


    @BeforeEach
    void setup() {
        menu = new SimpleMenu();
    }

    @Test
    void testSimpleMenuInitialization() {
        assertEquals(8,
                menu.getAvailableItems().size(), "After initialization menu should contain 8 elements");
    }

    @Test
    void testGettingItemById() {
        assertNotNull(menu.getItemById(1), "Item should exist");
    }

    @Test
    void testValidOrder() {
        // small coffee with foamed milk
        Order validOrder = new Order();
        OrderPart orderPart1 = new OrderPart();
        orderPart1.setMainItem(menu.getItemById(1));
        orderPart1.addExtra(menu.getItemById(7));
        validOrder.addOrderPart(orderPart1);
        // large cofffee
        OrderPart orderPart2 = new OrderPart();
        orderPart2.setMainItem(menu.getItemById(3));
        validOrder.addOrderPart(orderPart2);
        // bacon roll
        OrderPart orderPart3 = new OrderPart();
        orderPart3.setMainItem(menu.getItemById(4));
        validOrder.addOrderPart(orderPart3);

        menu.validateOrder(validOrder);

    }

    @Test
    void testInvalidOrder_extraAsMainItem() {
        Order invalidOrder = new Order();
        // small coffee with foamed milk
        OrderPart orderPart1 = new OrderPart();
        orderPart1.setMainItem(menu.getItemById(1));
        orderPart1.addExtra(menu.getItemById(7));
        invalidOrder.addOrderPart(orderPart1);
        // large coffee
        OrderPart orderPart2 = new OrderPart();
        orderPart2.setMainItem(menu.getItemById(3));
        invalidOrder.addOrderPart(orderPart2);
        OrderPart orderPart3 = new OrderPart();
        // special roast as main item
        orderPart3.setMainItem(menu.getItemById(8));
        invalidOrder.addOrderPart(orderPart3);

        Exception exception = assertThrows(IllegalStateException.class, () -> menu.validateOrder(invalidOrder));

        String expectedMessage = "Extras can't be ordered as a first item";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }


    @Test
    void testInvalidOrder_baconRollWithFoamedMilk() {
        Order inValidOrder = new Order();
        OrderPart orderPart1 = new OrderPart();
        // bacon roll + foamed milk
        orderPart1.setMainItem(menu.getItemById(4));
        orderPart1.addExtra(menu.getItemById(7));
        inValidOrder.addOrderPart(orderPart1);
        OrderPart orderPart2 = new OrderPart();
        // large coffee
        orderPart2.setMainItem(menu.getItemById(3));
        inValidOrder.addOrderPart(orderPart2);

        Exception exception = assertThrows(IllegalStateException.class, () -> menu.validateOrder(inValidOrder));

        String expectedMessage = "Foamed milk can't be ordered as an extra to Bacon Roll";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

    }
}
