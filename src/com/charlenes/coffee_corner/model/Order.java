package com.charlenes.coffee_corner.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<OrderPart> orderParts = new ArrayList<>();

    public List<OrderPart> getOrderParts() {
        return orderParts;
    }

    public void addOrderPart(OrderPart orderPart) {
        this.orderParts.add(orderPart);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return orderParts.equals(order.orderParts);
    }

    @Override
    public int hashCode() {
        return orderParts.hashCode();
    }
}
