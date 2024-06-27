package com.js.moneytransfer.controller.v1;

import com.js.moneytransfer.constants.AppConstants;
import com.js.moneytransfer.dto.AccountDTO;
import com.js.moneytransfer.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for managing accounts (version 1).
 */
@RestController
@RequestMapping(AppConstants.API_V1)
@RequiredArgsConstructor
@Tag(name = "Account API V1", description = "API for managing accounts in version 1")
public class AccountControllerV1 {

    private final AccountService accountService;

    @PostMapping("/create")
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountDTO> createAccount(@Validated @RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return ResponseEntity.ok(createdAccount);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds between accounts")
    public ResponseEntity<String> transferFunds(@RequestParam Long fromOwnerId,
                                                @RequestParam Long toOwnerId,
                                                @RequestParam BigDecimal amount) {
        accountService.transferFunds(fromOwnerId, toOwnerId, amount);
        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/balance")
    @Operation(summary = "Get the balance of an account")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam Long ownerId) {
        BigDecimal balance = accountService.getBalance(ownerId);
        return ResponseEntity.ok(balance);
    }
}
