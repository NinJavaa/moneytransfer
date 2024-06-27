package com.js.moneytransfer.service;

import com.js.moneytransfer.dto.AccountDTO;
import java.math.BigDecimal;

/**
 * Service interface for managing accounts.
 */
public interface AccountService {

    /**
     * Creates a new account.
     *
     * @param accountDTO the account data
     * @return the created account
     */
    AccountDTO createAccount(AccountDTO accountDTO);

    /**
     * Transfers funds between accounts.
     *
     * @param fromOwnerId the ID of the owner of the debit account
     * @param toOwnerId the ID of the owner of the credit account
     * @param amount the amount to transfer
     */
    void transferFunds(Long fromOwnerId, Long toOwnerId, BigDecimal amount);

    /**
     * Retrieves the balance for the given owner ID.
     *
     * @param ownerId the owner ID
     * @return the balance
     */
    BigDecimal getBalance(Long ownerId);
}
