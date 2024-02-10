/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package tax.cli.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import tax.cli.app.Records.StockMarketOperation;
import tax.cli.app.Records.Tax;

public class App {
    public static final BigDecimal TAX_MIN_AMOUNT = BigDecimal.valueOf(20000);
    public static final String BUY_OPERATION = "buy";
    public static final String SELL_OPERATION = "sell";

    public static List<Tax> processOperations(StockMarketOperation[] operations) {
        var taxes = new ArrayList<Tax>();
        BigDecimal stockQuantity = BigDecimal.ZERO;
        BigDecimal weightedAverage = BigDecimal.ZERO;
        BigDecimal losses = BigDecimal.ZERO;
        for (StockMarketOperation operation : operations) {
            if (operation.operation().equals(BUY_OPERATION)) {
                var currentStockQuantity = stockQuantity;
                var newStockQuantity = currentStockQuantity.add(operation.quantity());
                stockQuantity = newStockQuantity;
                System.out.println("Buying stocks do not pay taxes");
                taxes.add(new Tax(BigDecimal.ZERO));
                weightedAverage = ((weightedAverage.multiply(currentStockQuantity))
                        .add(
                                operation.unitCost().multiply(operation.quantity())))
                        .divide(newStockQuantity);
                System.out.println("weighted Avg: " + weightedAverage);
            } else if (operation.operation().equals(SELL_OPERATION)) {
                stockQuantity = stockQuantity.subtract(operation.quantity());
                var quantity = (operation.quantity());
                var ammount = quantity.multiply(operation.unitCost());
                var profit = ammount.subtract(weightedAverage.multiply(quantity));

                if (profit.floatValue() < 0) {
                    losses = losses.add(profit.abs());
                    System.out.println("Loss of $ " + losses + ": no tax");
                    taxes.add(new Tax(BigDecimal.ZERO));
                } else {
                    if (ammount.compareTo(TAX_MIN_AMOUNT) < 0) {
                        System.out.println("Total amount less than $ 20,000");
                        taxes.add(new Tax(BigDecimal.ZERO));
                        continue;
                    }
                    losses = losses.subtract(profit);
                    System.out.println(String.format("Profit of $ %s, losses: %s", profit, losses));
                    if (losses.floatValue() > 0) {
                        taxes.add(new Tax(BigDecimal.ZERO));
                    } else {
                        taxes.add(new Tax(losses.abs().divide(BigDecimal.valueOf(5))));
                        losses = BigDecimal.ZERO;
                    }
                }
            }
        }
        return taxes;
    }

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        // read from stdin
        try (BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in))) {
            var line = reader.readLine();
            while (!line.isEmpty()) {
                StockMarketOperation[] operations = objectMapper.readValue(line, StockMarketOperation[].class);
                var taxes = processOperations(operations);
                System.out.println(objectMapper.writeValueAsString(taxes));
                line = reader.readLine();
            }
            System.out.println("Finished reading from stdin");
        } catch (IOException e) {
            System.out.println("Error reading from stdin");
        }

    }
}
