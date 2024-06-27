package com.js.moneytransfer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "account.initial")
@Data
public class AccountProperties {
    private List<AccountConfig> accounts;

    @Data
    public static class AccountConfig {
        private Long ownerId;
        private String currency;
        private BigDecimal balance;
    }
}
