package ru.vtb.javaPro.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@ConfigurationProperties("service.limits.default")
public class LimitProperties {
    private final BigDecimal amount;

    public LimitProperties(BigDecimal amount) {
        this.amount = amount;
    }
}
