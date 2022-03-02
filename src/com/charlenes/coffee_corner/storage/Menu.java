package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;

import java.util.List;

public interface Menu {

    List<OrderItem> getAvailableItems();

    OrderItem getItemById(long id);

    void validateOrder(Order order);
}
