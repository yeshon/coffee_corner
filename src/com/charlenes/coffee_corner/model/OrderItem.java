package com.charlenes.coffee_corner.model;

import java.math.BigDecimal;

public class OrderItem {

    private int id;
    private String name;
    private OrderItemType type;
    // extras have a defined type to which they can be added
    private OrderItemType canBeAddedToType;
    private BigDecimal price;

    public OrderItem(int id, String name, OrderItemType type, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public OrderItem(int id, String name, OrderItemType type, OrderItemType canBeAddedToType, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.canBeAddedToType = canBeAddedToType;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OrderItemType getType() {
        return type;
    }

    public OrderItemType getCanBeAddedToType() {
        return canBeAddedToType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;

        if (id != orderItem.id) return false;
        if (!name.equals(orderItem.name)) return false;
        if (type != orderItem.type) return false;
        if (canBeAddedToType != orderItem.canBeAddedToType) return false;
        return price.equals(orderItem.price);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (canBeAddedToType != null ? canBeAddedToType.hashCode() : 0);
        result = 31 * result + price.hashCode();
        return result;
    }
}
