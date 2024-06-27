package com.js.moneytransfer.service.impl;

import com.js.moneytransfer.dto.AccountDTO;
import com.js.moneytransfer.entity.Account;
import com.js.moneytransfer.exception.AccountNotFoundException;
import com.js.moneytransfer.exception.ExchangeRateNotFoundException;
import com.js.moneytransfer.exception.InsufficientFundsException;
import com.js.moneytransfer.exception.LockAcquisitionException;
import com.js.moneytransfer.mapper.AccountMapper;
import com.js.moneytransfer.repository.AccountRepository;
import com.js.moneytransfer.service.AccountService;
import com.js.moneytransfer.service.ExchangeRateService;
import com.js.moneytransfer.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ExchangeRateService exchangeRateService;

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        log.info(AppConstants.ACCOUNT_CREATION_LOG, accountDTO.getOwnerId());
        Account account = accountMapper.toAccount(accountDTO);
        Account savedAccount = accountRepository.save(account);
        AccountDTO createdAccount = accountMapper.toAccountDTO(savedAccount);
        log.info(AppConstants.ACCOUNT_CREATION_SUCCESS, createdAccount.getId());
        return createdAccount;
    }

    @Override
    @Transactional
    public void transferFunds(Long fromOwnerId, Long toOwnerId, BigDecimal amount) {
        log.info(AppConstants.TRANSFER_LOG, amount, fromOwnerId, toOwnerId);
        try {
            Account fromAccount = accountRepository.findByOwnerIdForUpdate(fromOwnerId)
                    .orElseThrow(() -> new AccountNotFoundException(fromOwnerId.toString()));
            Account toAccount = accountRepository.findByOwnerIdForUpdate(toOwnerId)
                    .orElseThrow(() -> new AccountNotFoundException(toOwnerId.toString()));

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                log.error(AppConstants.INSUFFICIENT_BALANCE, fromOwnerId);
                throw new InsufficientFundsException(fromOwnerId.toString());
            }

            String fromCurrency = fromAccount.getCurrency();
            String toCurrency = toAccount.getCurrency();
            BigDecimal exchangeRate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);

            BigDecimal convertedAmount = amount.multiply(exchangeRate);

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(convertedAmount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

                log.info(AppConstants.TRANSFER_SUCCESS, fromOwnerId, toOwnerId);
        } catch (PessimisticLockingFailureException e) {
            log.error(AppConstants.LOCK_ACQUISITION_ERROR, fromOwnerId, toOwnerId);
            throw new LockAcquisitionException(String.format(AppConstants.LOCK_ACQUISITION_ERROR, fromOwnerId, toOwnerId), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long ownerId) {
        log.info(AppConstants.FETCHING_BALANCE_FOR_OWNER_ID, ownerId);
        Account account = accountRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> {
                    log.error(AppConstants.ACCOUNT_NOT_FOUND, ownerId);
                    return new AccountNotFoundException(ownerId.toString());
                });
        BigDecimal balance = account.getBalance();
        log.info(AppConstants.BALANCE_FOR_OWNER_ID, ownerId, balance);
        return balance;
    }
}
