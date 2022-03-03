package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.OrderPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleOrdersHistory implements OrdersHistory {

    private HashMap<String, List<Order>> ordersHistory = new HashMap<>();

    @Override
    public void addOrderToHistory(Order order, String stampCardId) {
        if (!ordersHistory.containsKey(stampCardId)) {
            List<Order> ordersList = new ArrayList<>();
            ordersList.add(order);
            ordersHistory.put(stampCardId, ordersList);
        } else {
            ordersHistory.get(stampCardId).add(order);
        }
    }

    @Override
    public Map<OrderItem, Long> getOrdersSummary(String stampCardId) {
        Map<OrderItem, Long> summaryMap;

        List<OrderPart> orderParts = new ArrayList<>();
        List<Order> ordersList = ordersHistory.get(stampCardId);
        if (ordersList != null) {
            for (Order order : ordersList) {
                orderParts.addAll(order.getOrderParts());
            }
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderPart orderPart : orderParts) {
            orderItems.add(orderPart.getMainItem());
            orderItems.addAll(orderPart.getExtras());
        }

        summaryMap = orderItems.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return summaryMap;
    }
}
