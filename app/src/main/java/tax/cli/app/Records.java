package tax.cli.app;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Records {
    public record StockMarketOperation(String operation, @JsonAlias("unit-cost") BigDecimal unitCost,
            BigDecimal quantity) {
    }

    public record Tax(BigDecimal tax) {
        public Tax(BigDecimal tax) {
            this.tax = tax.setScale(2, RoundingMode.HALF_UP);
        }
    }

}
