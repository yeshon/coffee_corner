package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.OrderItem;

import java.util.List;

public interface DataLoader {
    List<OrderItem> loadAssortmentData();
}
