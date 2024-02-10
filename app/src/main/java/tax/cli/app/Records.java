package tax.cli.app;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Records {
    public record StockMarketOperation(String operation, @JsonAlias("unit-cost") BigDecimal unitCost,
            BigDecimal quantity) {
    }

    public record Tax(BigDecimal tax) {
    }

}
