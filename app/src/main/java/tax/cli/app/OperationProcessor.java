package tax.cli.app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import tax.cli.app.Records.StockMarketOperation;
import tax.cli.app.Records.Tax;

public class OperationProcessor {

    public static final BigDecimal TAX_MIN_AMOUNT = BigDecimal.valueOf(20000);
    public static final int DEFAULT_SCALE = 2;
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final String BUY_OPERATION = "buy";
    public static final String SELL_OPERATION = "sell";
    private final List<Tax> taxes = new ArrayList<>();
    private BigDecimal stockQuantity = BigDecimal.ZERO;
    private BigDecimal weightedAverage = BigDecimal.ZERO;
    private BigDecimal losses = BigDecimal.ZERO;

    public void reset() {
        taxes.clear();
        stockQuantity = BigDecimal.ZERO;
        weightedAverage = BigDecimal.ZERO;
        losses = BigDecimal.ZERO;
    }

    public List<Tax> processOperations(StockMarketOperation[] operations) {
        for (StockMarketOperation operation : operations) {
            if (operation.operation().equals(BUY_OPERATION)) {
                var currentStockQuantity = stockQuantity;
                stockQuantity = currentStockQuantity.add(operation.quantity());
                taxes.add(new Tax(BigDecimal.ZERO));
                weightedAverage = calculateWeightedAverage(operation, weightedAverage, currentStockQuantity,
                        stockQuantity);
            } else if (operation.operation().equals(SELL_OPERATION)) {
                processSellOperation(operation);
            }
        }
        return taxes;
    }

    private void processSellOperation(StockMarketOperation operation) {
        stockQuantity = stockQuantity.subtract(operation.quantity());
        var amount = operation.quantity().multiply(operation.unitCost());
        var profit = amount.subtract(weightedAverage.multiply(operation.quantity()));

        if (profit.floatValue() < 0) {
            processLoss(profit);
        } else {
            processProfit(amount, profit);
        }
    }

    private void processLoss(BigDecimal profit) {
        losses = losses.add(profit.abs());
        taxes.add(new Tax(BigDecimal.ZERO));
    }

    private void processProfit(BigDecimal amount, BigDecimal profit) {
        if (amount.compareTo(TAX_MIN_AMOUNT) <= 0) {
            taxes.add(new Tax(BigDecimal.ZERO));
        } else {
            calculateTaxesAndLosses(profit);
        }
    }

    private void calculateTaxesAndLosses(BigDecimal profit) {
        losses = losses.subtract(profit);
        if (losses.floatValue() > 0) {
            taxes.add(new Tax(BigDecimal.ZERO));
        } else {
            taxes.add(new Tax(losses.abs().divide(BigDecimal.valueOf(5), DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)));
            losses = BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateWeightedAverage(StockMarketOperation operation, BigDecimal weightedAverage,
            BigDecimal currentStockQuantity, BigDecimal stockQuantity) {
        return ((weightedAverage.multiply(currentStockQuantity))
                .add(
                        operation.unitCost().multiply(operation.quantity())))
                .divide(stockQuantity, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
