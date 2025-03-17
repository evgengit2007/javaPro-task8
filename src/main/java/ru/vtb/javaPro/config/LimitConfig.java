package ru.vtb.javaPro.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.vtb.javaPro.config.properties.LimitProperties;

@Configuration
@EnableConfigurationProperties(LimitProperties.class)
public class LimitConfig {
    private final LimitProperties limitProperties;

    public LimitConfig(LimitProperties limitProperties) {
        this.limitProperties = limitProperties;
    }
}
