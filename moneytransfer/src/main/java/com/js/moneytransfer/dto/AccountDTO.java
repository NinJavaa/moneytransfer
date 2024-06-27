package com.js.moneytransfer.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountDTO {
    private Long id;

    @NotNull(message = "Owner ID cannot be null")
    private Long ownerId;

    @NotNull(message = "Currency cannot be null")
    private String currency;

    @NotNull(message = "Initial balance cannot be null")
    private BigDecimal initialBalance;
}
