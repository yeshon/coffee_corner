package com.charlenes.coffee_corner.parser;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderPart;
import com.charlenes.coffee_corner.storage.DataLoader;
import com.charlenes.coffee_corner.storage.Menu;
import com.charlenes.coffee_corner.storage.SimpleDataLoader;
import com.charlenes.coffee_corner.storage.SimpleMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class UserInputParserTest {

    private DataLoader dataLoader;
    private Menu menu;
    private UserInputParser userInputParser;

    @BeforeEach
    void setup(){
        dataLoader = new SimpleDataLoader();
        menu = new SimpleMenu(dataLoader);
        userInputParser = new UserInputParser(menu);
    }

    @Test
    void testParseValidInput(){
        // medium special roast coffee with foamed milk, bacon roll and juice
        String input = "2+7+8,4,5";

        OrderPart firstPart = new OrderPart();
        firstPart.setMainItem(menu.getItemById(2));
        firstPart.addExtra(menu.getItemById(7));
        firstPart.addExtra(menu.getItemById(8));

        OrderPart secondPart = new OrderPart();
        secondPart.setMainItem(menu.getItemById(4));

        OrderPart thirdPart = new OrderPart();
        thirdPart.setMainItem(menu.getItemById(5));

        Order expectedOrder = new Order();
        expectedOrder.addOrderPart(firstPart);
        expectedOrder.addOrderPart(secondPart);
        expectedOrder.addOrderPart(thirdPart);

        Order parsedOrder = userInputParser.parseOrderFromUserInput(input);
        assertEquals(3, parsedOrder.getOrderParts().size(), "Order should have 3 parts");

        assertEquals(expectedOrder, parsedOrder);

    }

    @Test
    void testParseInvalidInput_notAllowedSign(){
        String input = "2+7,4*3,5";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userInputParser.parseOrderFromUserInput(input));

        String expectedMessage = "Wrong input format";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testParseInvalidInput_notExistingItem(){
        String input = "2+9,3,5";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userInputParser.parseOrderFromUserInput(input));

        String expectedMessage = "No item with id='9' found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
