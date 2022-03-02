package com.charlenes.coffee_corner.parser;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.OrderPart;
import com.charlenes.coffee_corner.storage.Menu;

public class UserInputParser {

    private Menu menu;

    public UserInputParser(Menu menu) {
        this.menu = menu;
    }

    public Order parseOrderFromUserInput(String input) {
        Order order = new Order();
        String[] parts = input.split(",");
        for (String part : parts) {
            OrderPart orderPart = new OrderPart();
            String[] items = part.split("\\+");
            for(String item: items){
                OrderItem orderItem;
                try {
                    long id = Long.parseLong(item);
                    orderItem = menu.getItemById(id);
                } catch (NumberFormatException e){
                    throw new IllegalArgumentException("Wrong input format");
                }
                if(orderItem == null){
                    throw new IllegalArgumentException(String.format("No item with id='%s' found", item));
                }
                else {
                    if(orderPart.getMainItem() == null){
                        orderPart.setMainItem(orderItem);
                    }
                    else {
                        orderPart.addExtra(orderItem);
                    }
                }
            }
            order.addOrderPart(orderPart);
        }
        return order;
    }
}
