package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.OrderItemType;
import com.charlenes.coffee_corner.model.OrderPart;

import java.lang.IllegalStateException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SimpleMenu implements Menu {

    private List<OrderItem> availableItems = new ArrayList<>();

    public List<OrderItem> getAvailableItems() {
        return availableItems;
    }

    @Override
    public OrderItem getItemById(long id) {
        return getAvailableItems().stream()
                .filter(c -> id == (c.getId()))
                .findAny()
                .orElse(null);
    }

    @Override
    public void validateOrder(Order order) {
        for (OrderPart orderPart : order.getOrderParts()) {
            if (orderPart.getMainItem().getType() == OrderItemType.EXTRA) {
                throw new IllegalStateException("Extras can't be ordered as a first item");
            }
            for (OrderItem extra : orderPart.getExtras()) {
                if (extra.getType() != OrderItemType.EXTRA || orderPart.getMainItem().getType() != extra.getCanBeAddedToType()) {
                    throw new IllegalStateException(String.format("%s can't be ordered as an extra to %s", extra.getName(), orderPart.getMainItem().getName()));
                }
            }
        }
    }

    /**
     * in not 'simple' version list could be loaded from database or configuration
     * extras have a defined type to which they can be added
     * such approach allows to add new products with validation logic build in
     */
    public SimpleMenu() {
        availableItems.add(new OrderItem(1, "Small Coffee", OrderItemType.COFFEE, new BigDecimal("2.5")));
        availableItems.add(new OrderItem(2, "Medium Coffee", OrderItemType.COFFEE, new BigDecimal("3.0")));
        availableItems.add(new OrderItem(3, "Large Coffee", OrderItemType.COFFEE, new BigDecimal("3.5")));
        availableItems.add(new OrderItem(4, "Bacon Roll", OrderItemType.SNACK, new BigDecimal("4.5")));
        availableItems.add(new OrderItem(5, "Orange juice (freshly squeezed, 0.25l) ", OrderItemType.JUICE, new BigDecimal("3.95")));
        availableItems.add(new OrderItem(6, "Extra milk", OrderItemType.EXTRA, OrderItemType.COFFEE, new BigDecimal("0.3")));
        availableItems.add(new OrderItem(7, "Foamed milk", OrderItemType.EXTRA, OrderItemType.COFFEE, new BigDecimal("0.5")));
        availableItems.add(new OrderItem(8, "Special roast", OrderItemType.EXTRA, OrderItemType.COFFEE, new BigDecimal("0.9")));
    }

}
