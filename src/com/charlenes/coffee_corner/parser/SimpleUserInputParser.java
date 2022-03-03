package com.charlenes.coffee_corner.parser;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.OrderPart;
import com.charlenes.coffee_corner.storage.Menu;

public class SimpleUserInputParser implements UserInputParser {

    private Menu menu;

    public SimpleUserInputParser(Menu menu) {
        this.menu = menu;
    }

    /*
        allowed format example '2+7,4' :
        - ',' as a separator
        - '+' to join main product with extra
        product with given id must exist
    */
    public Order parseOrderFromUserInput(String input) {
        Order order = new Order();
        String[] parts = input.split(",");
        for (String part : parts) {
            OrderPart orderPart = new OrderPart();
            String[] items = part.split("\\+");
            for (String item : items) {
                OrderItem orderItem;
                try {
                    long id = Long.parseLong(item);
                    orderItem = menu.getItemById(id);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Wrong input format");
                }
                if (orderItem == null) {
                    throw new IllegalArgumentException(String.format("No item with id='%s' found", item));
                } else {
                    if (orderPart.getMainItem() == null) {
                        orderPart.setMainItem(orderItem);
                    } else {
                        orderPart.addExtra(orderItem);
                    }
                }
            }
            order.addOrderPart(orderPart);
        }
        return order;
    }
}
