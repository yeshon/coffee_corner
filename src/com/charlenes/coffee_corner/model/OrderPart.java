package com.charlenes.coffee_corner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderPart {

    private OrderItem mainItem;

    private List<OrderItem> extras = new ArrayList<>();

    public OrderItem getMainItem() {
        return mainItem;
    }

    public List<OrderItem> getExtras() {
        return extras;
    }

    public void setMainItem(OrderItem mainItem) {
        this.mainItem = mainItem;
    }

    public void addExtra(OrderItem orderItem) {
        this.extras.add(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderPart orderPart = (OrderPart) o;

        if (!Objects.equals(mainItem, orderPart.mainItem)) return false;
        return Objects.equals(extras, orderPart.extras);
    }

    @Override
    public int hashCode() {
        int result = mainItem != null ? mainItem.hashCode() : 0;
        result = 31 * result + (extras != null ? extras.hashCode() : 0);
        return result;
    }
}
