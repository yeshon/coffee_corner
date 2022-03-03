package com.charlenes.coffee_corner;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharlenesCoffeeCornerTest {

    @Test
    void testWholeScenario_simpleOneOrder() {

        /*
           type:
           - '2+8,4' as products selection
           - 'N' for question about card
           - 'N' for question about continue
         */
        String userInput = "2+8,4" + System.lineSeparator()
                + "N" + System.lineSeparator()
                + "N" + System.lineSeparator();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(byteArrayInputStream);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);

        CharlenesCoffeeCorner.main(null);

        String[] lines = byteArrayOutputStream.toString().split(System.lineSeparator());

        String actualWelcomeLine = lines[lines.length - 35];
        String expectedWelcomeLine = "Welcome to Charlie's Coffee Corner!";
        assertEquals(expectedWelcomeLine, actualWelcomeLine);

        String actualMenuLine = lines[lines.length - 29];
        String expectedMenuLine = "1 - Small Coffee - 2,50 CHF ";
        assertEquals(expectedMenuLine, actualMenuLine);

        String actualStampCardLine = lines[lines.length - 20];
        String expectedStampCardLine = "Card number entered for the first time means its registration.";
        assertEquals(expectedStampCardLine, actualStampCardLine);

        String actualBonusLine = lines[lines.length - 8];
        String expectedBonusLine = "* Special roast      -1 * 0,90 -0,90       *";
        assertEquals(expectedBonusLine, actualBonusLine);

        String actualPriceLine = lines[lines.length - 5];
        String expectedPriceLine = "*                                     7,50 *";
        assertEquals(expectedPriceLine, actualPriceLine);

        String actualContinueLine = lines[lines.length -1];
        String expectedContinueLine = "To add new order type 'Y', to exit program type 'N': ";
        assertEquals(expectedContinueLine, actualContinueLine);
    }

    @Test
    void testWholeScenario_simpleOneOrder_continueShopping() {

        /*
           type:
           - '2+8,4' as products selection
           - 'N' for questions about card
           - 'Y' for question about continue
           - '1+7' as products selection
           - 'N' for question about card
           - 'N' for question about continue
         */
        String userInput = "2+8,4" + System.lineSeparator()
                + "N" + System.lineSeparator()
                + "Y" + System.lineSeparator()
                + "1+7" + System.lineSeparator()
                + "N" + System.lineSeparator()
                + "N" + System.lineSeparator();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(byteArrayInputStream);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);

        CharlenesCoffeeCorner.main(null);

        String[] lines = byteArrayOutputStream.toString().split(System.lineSeparator());
        String actual = lines[lines.length - 5];
        String expected = "*                                     3,00 *";

        assertEquals(expected, actual);
    }

    @Test
    void testWholeScenario_notValidInputs() {

        /*
          type:
          - wrong input for product selection - wrong format
          - wrong input for product selection - not existing product
          - wrong input for product selection - extra as main part
          - wrong input for product selection - extra added to not allowed product
          - '2+8,4' as products selection
          - wrong input for questions about card - wrong format
          - wrong input for questions about card - wrong length
          - '123456' for questions about card
          - wrong input for questions about continue - wrong format
          - 'N' for questions about continue
         */
        String userInput = "2f" + System.lineSeparator()
                + "2+8,10" + System.lineSeparator()
                + "8" + System.lineSeparator()
                + "4+8" + System.lineSeparator()
                + "2+8,4" + System.lineSeparator()
                + "I don't" + System.lineSeparator()
                + "1234" + System.lineSeparator()
                + "123456" + System.lineSeparator()
                + "FFF" + System.lineSeparator()
                + "N" + System.lineSeparator();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(byteArrayInputStream);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        System.setOut(printStream);

        CharlenesCoffeeCorner.main(null);

        String[] lines = byteArrayOutputStream.toString().split(System.lineSeparator());
        String totalLineActual = lines[lines.length - 8];
        String totalLineExpected = "*                                     7,50 *";
        assertEquals(totalLineExpected, totalLineActual);

        String cardStampsLineActual = lines[lines.length - 4];
        String cardStampsLineExpected = "* NUMBER OF STAMPS ON YOUR CARD:         1 *";
        assertEquals(cardStampsLineExpected, cardStampsLineActual);
    }
}
