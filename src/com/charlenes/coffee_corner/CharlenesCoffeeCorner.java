package com.charlenes.coffee_corner;


import com.charlenes.coffee_corner.model.Order;
import com.charlenes.coffee_corner.model.OrderItem;
import com.charlenes.coffee_corner.model.Receipt;
import com.charlenes.coffee_corner.parser.UserInputParser;
import com.charlenes.coffee_corner.receipt.ReceiptCalculator;
import com.charlenes.coffee_corner.receipt.SimpleReceiptCalculator;
import com.charlenes.coffee_corner.storage.Menu;
import com.charlenes.coffee_corner.storage.OrdersHistory;
import com.charlenes.coffee_corner.storage.SimpleMenu;
import com.charlenes.coffee_corner.storage.SimpleOrdersHistory;

import java.math.BigDecimal;
import java.util.Scanner;

public class CharlenesCoffeeCorner {

    private Menu menu;
    private UserInputParser userInputParser;
    private ReceiptCalculator receiptCalculator;
    private OrdersHistory ordersHistory;

    private void init() {
        menu = new SimpleMenu();
        userInputParser = new UserInputParser(menu);
        ordersHistory = new SimpleOrdersHistory();
        receiptCalculator = new SimpleReceiptCalculator(ordersHistory);
    }

    public CharlenesCoffeeCorner() {
        init();
    }

    public static void main(String[] args) {
        CharlenesCoffeeCorner coffeeCorner = new CharlenesCoffeeCorner();
        coffeeCorner.startConversation();
    }


    private void startConversation() {

        Order userOrder = null;
        Scanner scanner = new Scanner(System.in);
        boolean continueShopping = true;

        displayWelcomeMessage();

        while(continueShopping) {
            boolean continueOrdering = true;
            displayMenu();
            while (continueOrdering) {
                try {
                    userOrder = userInputParser.parseOrderFromUserInput(scanner.next());
                    menu.validateOrder(userOrder);
                    continueOrdering = false;
                } catch (IllegalStateException | IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    System.out.print("Try again: ");
                }
            }
            displayBonusInformation();
            boolean continueAskingForCard = true;
            String cardNumber = null;
            while (continueAskingForCard) {
                String userInput = scanner.next();
                if ("n".equalsIgnoreCase(userInput)) {
                    continueAskingForCard = false;
                } else if (!userInput.matches("^\\d{6}$")) {
                    System.out.print("Wrong input, try again: ");
                } else {
                    cardNumber = userInput;
                    continueAskingForCard = false;
                }
            }
            Receipt receipt = receiptCalculator.calculate(userOrder, cardNumber);
            ordersHistory.addOrderToHistory(userOrder, cardNumber);
            displayReceipt(receipt);

            System.out.println("");
            System.out.print("To add new order type 'Y', to exit program type 'N': ");
            boolean continueAskingAboutAnotherOrder = true;
            while(continueAskingAboutAnotherOrder){
                String userInput = scanner.next();
                if ("n".equalsIgnoreCase(userInput)) {
                    continueAskingAboutAnotherOrder = false;
                    continueShopping = false;
                }
                else if ("y".equalsIgnoreCase(userInput)) {
                    continueAskingAboutAnotherOrder = false;
                } else {
                    System.out.print("Wrong input, try again: ");
                }
            }
        }
    }

    private void displayReceipt(Receipt receipt) {
        System.out.println("Tank you for your order, receipt:");
        System.out.println("********************************************");
        System.out.printf("%-9s %s%10s%s", "*", "Charlene's Coffee Corner", "*", System.lineSeparator());
        System.out.printf("%-9s %s%11s%s", "*", "Tax no. CHE-999.999.999", "*", System.lineSeparator());
        System.out.printf("*%42s*%s", "", System.lineSeparator());
        for (OrderItem orderItem : receipt.getItems().keySet()) {
            String displayName = getDisplayName(orderItem);
            int count = receipt.getItems().get(orderItem);
            BigDecimal pricePerItem = orderItem.getPrice();
            printReceiptLine(displayName, count, pricePerItem);
        }
        if (!receipt.getDiscountItems().isEmpty()) {
            System.out.printf("*%42s*%s", "", System.lineSeparator());
            System.out.printf("* %-40s *%s", "DISCOUNT:", System.lineSeparator());
            for (OrderItem orderItem : receipt.getDiscountItems().keySet()) {
                String displayName = getDisplayName(orderItem);
                int count = (-1) * receipt.getItems().get(orderItem);
                BigDecimal pricePerItem = orderItem.getPrice();
                printReceiptLine(displayName, count, pricePerItem);
            }
        }
        System.out.printf("*%42s*%s", "", System.lineSeparator());
        System.out.printf("*%-42s*%s", " TOTAL CHF: ", System.lineSeparator());
        System.out.printf("* %40.2f *%s", receipt.getTotalPrice(), System.lineSeparator());
        System.out.printf("*%42s*%s", "", System.lineSeparator());
        if(receipt.getStampCardNumber()!=null){
            System.out.printf("*%42s*%s", "", System.lineSeparator());
            System.out.println("*------------------------------------------*");
            System.out.printf("* %-31s %8d *%s", "NUMBER OF STAMPS ON YOUR CARD:", receipt.getStampCount(), System.lineSeparator());
        }
        System.out.println("********************************************");
    }

    private void printReceiptLine(String displayName, int count, BigDecimal itemPrice) {

        String line = String.format("%-18s %d * %.2f %.2f", displayName, count, itemPrice, itemPrice.multiply(new BigDecimal(count)));
        System.out.printf("%s %-40s %s%s", "*", line, "*", System.lineSeparator());
    }

    private String getDisplayName(OrderItem orderItem) {
        String displayName = orderItem.getName().length() > 13 ? orderItem.getName().substring(0,13) : orderItem.getName();
        return displayName;
    }


    private void displayBonusInformation() {
        System.out.print(
                "Please enter your stamp card number (six digits)." + System.lineSeparator() +
                "Card number entered for the first time means its registration." + System.lineSeparator() +
                "If you don't want use our bonuses enter 'N'." + System.lineSeparator() + ": ");
    }

    private void displayWelcomeMessage(){
        String sampleOrderInput = "2+7,4";
        Order sampleOrder = userInputParser.parseOrderFromUserInput(sampleOrderInput);
        System.out.printf("Welcome to Charlie's Coffee Corner!" + System.lineSeparator() +
                        "If you order beverage and a snack, one of the extra's is free." + System.lineSeparator() +
                        "We also offer a customer stamp card, where every 5th beverage is for free." + System.lineSeparator() +
                        "Please place an order. " + System.lineSeparator() + "(type numbers, separating with a comma, " +
                        "to add extras to coffee use '+' sign," + System.lineSeparator() +
                        " for example '%s' means %s with %s and %s)" + System.lineSeparator(),
                sampleOrderInput,
                sampleOrder.getOrderParts().get(0).getMainItem().getName(),
                sampleOrder.getOrderParts().get(0).getExtras().get(0).getName(),
                sampleOrder.getOrderParts().get(1).getMainItem().getName());
    }

    private void displayMenu() {
        for (OrderItem orderItem : menu.getAvailableItems()) {
            System.out.printf("%d - %s - %.2f CHF %s", orderItem.getId(), orderItem.getName(), orderItem.getPrice(), System.lineSeparator());
        }
        System.out.print(": ");
    }

}
