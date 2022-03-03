package com.charlenes.coffee_corner.receipt;

import com.charlenes.coffee_corner.model.*;
import com.charlenes.coffee_corner.storage.OrdersHistory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleReceiptCalculator implements ReceiptCalculator {

    private OrdersHistory ordersHistory;

    public SimpleReceiptCalculator(OrdersHistory ordersHistory) {
        this.ordersHistory = ordersHistory;
    }

    @Override
    public Receipt calculate(Order order, String cardId) {
        Receipt receipt = new Receipt();
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderPart orderPart : order.getOrderParts()) {
            orderItems.add(orderPart.getMainItem());
            orderItems.addAll(orderPart.getExtras());
        }
        receipt.setItems(orderItems.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(x -> 1))));

        addBeverageAndSnackDiscount(receipt);
        addStampCardDiscount(receipt, cardId);

        return receipt;
    }


    /**
     * "If a customer orders a beverage and a snack, one of the extra's is free."
     * Assumption: only one, even if there is more than one pair of a drink and a snack
     */
    private void addBeverageAndSnackDiscount(Receipt receipt) {

        long beveragesCount = 0;
        for (OrderItem orderItem : receipt.getItems().keySet()) {
            if (orderItem.getType() == OrderItemType.COFFEE || orderItem.getType() == OrderItemType.JUICE) {
                beveragesCount += receipt.getItems().get(orderItem);
            }
        }
        long snacksCount = 0;
        for (OrderItem orderItem : receipt.getItems().keySet()) {
            if (orderItem.getType() == OrderItemType.SNACK) {
                snacksCount += receipt.getItems().get(orderItem);
            }
        }
        if (beveragesCount > 0 && snacksCount > 0) {
            // let's find the cheapest extra
            OrderItem cheapestExtra = null;
            for (OrderItem orderItem : receipt.getItems().keySet()) {
                if (orderItem.getType() == OrderItemType.EXTRA) {
                    if (cheapestExtra == null || orderItem.getPrice().compareTo(cheapestExtra.getPrice()) < 0) {
                        cheapestExtra = orderItem;
                    }
                }
            }
            if (cheapestExtra != null) {
                receipt.addDiscountItem(cheapestExtra);
            }
        }
    }

    /**
     * "every 5th beverage is for free"
     * Assumption: ony main part is free - like coffee or juice, without extras
     */
    private void addStampCardDiscount(Receipt receipt, String cardNumber) {
        if (cardNumber != null) {
            receipt.setStampCardNumber(cardNumber);
            Map<OrderItem, Long> history = ordersHistory.getOrdersSummary(cardNumber);
            long historyBeveragesCount = 0;
            for (OrderItem item : history.keySet()) {
                if (item.getType() == OrderItemType.COFFEE || item.getType() == OrderItemType.JUICE) {
                    historyBeveragesCount += history.get(item);
                }
            }
            long orderBeveragesCount = 0;
            for (OrderItem orderItem : receipt.getItems().keySet()) {
                if (orderItem.getType() == OrderItemType.COFFEE || orderItem.getType() == OrderItemType.JUICE) {
                    orderBeveragesCount += receipt.getItems().get(orderItem);
                }
            }

            long beveragesCountSum = historyBeveragesCount + orderBeveragesCount;
            long overallFreeBeveragesCount = beveragesCountSum / 5;
            long alreadyGivenBeveragesCount = historyBeveragesCount / 5;
            long beveragesToGive = overallFreeBeveragesCount - alreadyGivenBeveragesCount;
            int stampsCount = (int) (beveragesCountSum % 5);
            receipt.setStampCount(stampsCount);
            if (beveragesToGive > 0) {
                // find the cheapest beverages;
                List<OrderItem> orderItems = new ArrayList<>();
                for (OrderItem orderItem : receipt.getItems().keySet()) {
                    if (orderItem.getType() == OrderItemType.JUICE || orderItem.getType() == OrderItemType.COFFEE) {
                        for (int i = 1; i <= receipt.getItems().get(orderItem); i++) {
                            orderItems.add(orderItem);
                        }
                    }
                }
                orderItems.sort(Comparator.comparing(OrderItem::getPrice));
                for (int i = 1; i <= beveragesToGive; i++) {
                    receipt.addDiscountItem(orderItems.get(i - 1));
                }
            }
        }
    }
}
