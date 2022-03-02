package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;

import java.util.Map;

public interface OrdersHistory {

    void addOrderToHistory(Order order, String stampCardId);

    Map<OrderItem, Long> getOrdersSummary(String stampCardId);
}
