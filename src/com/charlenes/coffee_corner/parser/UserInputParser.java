package com.charlenes.coffee_corner.parser;

import com.charlenes.coffee_corner.model.Order;

public interface UserInputParser {

    Order parseOrderFromUserInput(String input);
}
