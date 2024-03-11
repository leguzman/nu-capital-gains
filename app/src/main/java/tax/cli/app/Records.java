package tax.cli.app;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;

import static tax.cli.app.OperationProcessor.DEFAULT_ROUNDING_MODE;
import static tax.cli.app.OperationProcessor.DEFAULT_SCALE;

public class Records {
    public record StockMarketOperation(String operation, @JsonAlias("unit-cost") BigDecimal unitCost,
            BigDecimal quantity) {
    }
    public record Error (String error) {
    }

    public record Tax(BigDecimal tax) {
        public Tax(BigDecimal tax) {
            this.tax = tax.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        }
    }

}
