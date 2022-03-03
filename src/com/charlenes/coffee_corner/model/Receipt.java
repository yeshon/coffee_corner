package com.charlenes.coffee_corner.model;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Receipt {

    Map<OrderItem, Integer> items;
    Map<OrderItem, Integer> discountItems = new HashMap<>();
    String stampCardNumber;
    int stampCount;

    public int getStampCount() {
        return stampCount;
    }

    public String getStampCardNumber() {
        return stampCardNumber;
    }

    public Map<OrderItem, Integer> getItems() {
        return items;
    }

    public void setItems(Map<OrderItem, Integer> items) {
        this.items = items;
    }

    public Map<OrderItem, Integer> getDiscountItems() {
        return discountItems;
    }

    public void setStampCount(int stampCount) {
        this.stampCount = stampCount;
    }

    public void setStampCardNumber(String stampCardNumber) {
        this.stampCardNumber = stampCardNumber;
    }

    public void addDiscountItem(OrderItem discountItem) {
        if (!discountItems.containsKey(discountItem)) {
            discountItems.put(discountItem, 1);
        } else {
            discountItems.put(discountItem, discountItems.get(discountItem) + 1);
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal cost = new BigDecimal(0);
        for (OrderItem orderItem : items.keySet()) {
            cost = cost.add(orderItem.getPrice().multiply(BigDecimal.valueOf((items.get(orderItem)))));
        }
        BigDecimal discount = new BigDecimal(0);
        for (OrderItem orderItem : discountItems.keySet()) {
            discount = discount.add(orderItem.getPrice().multiply(BigDecimal.valueOf((discountItems.get(orderItem)))));
        }
        return cost.subtract(discount);
    }
}
