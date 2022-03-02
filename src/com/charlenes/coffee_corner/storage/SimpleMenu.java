package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.OrderItemType;
import com.charlenes.coffee_corner.model.OrderPart;

import java.lang.IllegalStateException;

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
     * in not 'simple' data is loaded from assortment.properties file
     * extras have a defined type to which they can be added
     * such approach allows for adding new products with validation logic build in
     */
    public SimpleMenu(DataLoader dataLoader) {
this.availableItems = dataLoader.loadAssortmentData();
            }

}
