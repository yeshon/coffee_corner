package com.charlenes.coffee_corner.receipt;

import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.Receipt;

public interface ReceiptCalculator {

    Receipt calculate(Order order, String cardId);
}
