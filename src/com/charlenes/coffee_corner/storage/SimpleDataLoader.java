package com.charlenes.coffee_corner.storage;

import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.OrderItemType;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


public class SimpleDataLoader implements DataLoader {

    /**
     * data is loaded from assortment.properties file
     * extras have a defined type to which they can be added
     * such approach allows for adding new products with validation logic build in
     */
    public List<OrderItem> loadAssortmentData() {

        List<OrderItem> assortment = new ArrayList<>();
        try (InputStream input = SimpleDataLoader.class.getClassLoader().getResourceAsStream("assortment.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            Map<Integer, Map<String, String>> itemsMap = new HashMap<>();
            Set<String> keys = properties.stringPropertyNames();

            for (String key : keys) {
                String value = properties.getProperty(key);
                String id = key.substring(5).substring(0, key.substring(5).indexOf("."));
                String propertyName = key.substring(5).substring(key.substring(5).indexOf(".") + 1);
                if (itemsMap.get(Integer.parseInt(id)) == null) {
                    itemsMap.put(Integer.parseInt(id), new HashMap<>());
                }
                itemsMap.get(Integer.parseInt(id)).put(propertyName, value);
            }
            for (Integer id : itemsMap.keySet()) {
                Map<String, String> propertiesValues = itemsMap.get(id);
                String name = propertiesValues.get("name");
                String type = propertiesValues.get("type");
                String canBeAddedToType = propertiesValues.get("canBeAddedToType");
                String price = propertiesValues.get("price");
                if (canBeAddedToType != null) {
                    assortment.add(new OrderItem(id, name, OrderItemType.valueOf(type), OrderItemType.valueOf(canBeAddedToType), new BigDecimal(price)));
                } else {
                    assortment.add(new OrderItem(id, name, OrderItemType.valueOf(type), new BigDecimal(price)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assortment;
    }

}
