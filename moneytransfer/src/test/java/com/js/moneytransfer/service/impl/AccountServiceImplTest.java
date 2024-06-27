package com.js.moneytransfer.service.impl;

import com.js.moneytransfer.dto.AccountDTO;
import com.js.moneytransfer.entity.Account;
import com.js.moneytransfer.exception.AccountNotFoundException;
import com.js.moneytransfer.exception.InsufficientFundsException;
import com.js.moneytransfer.exception.LockAcquisitionException;
import com.js.moneytransfer.mapper.AccountMapper;
import com.js.moneytransfer.repository.AccountRepository;
import com.js.moneytransfer.service.AccountService;
import com.js.moneytransfer.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceImplTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private AccountServiceImpl accountService;

    @Test
    void createAccount_ShouldCreateAccountSuccessfully() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setOwnerId(1L);
        accountDTO.setCurrency("USD");
        accountDTO.setInitialBalance(BigDecimal.valueOf(1000));

        Account account = new Account();
        account.setOwnerId(1L);
        account.setCurrency("USD");
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountMapper.toAccount(any(AccountDTO.class))).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toAccountDTO(any(Account.class))).thenReturn(accountDTO);

        AccountDTO createdAccount = accountService.createAccount(accountDTO);

        verify(accountRepository, times(1)).save(any(Account.class));
        assertEquals(accountDTO, createdAccount);
    }

    @Test
    void transferFunds_ShouldThrowInsufficientFundsException() {
        Account fromAccount = new Account();
        fromAccount.setOwnerId(1L);
        fromAccount.setCurrency("USD");
        fromAccount.setBalance(BigDecimal.valueOf(500));

        Account toAccount = new Account();
        toAccount.setOwnerId(2L);
        toAccount.setCurrency("USD");
        toAccount.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findByOwnerIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByOwnerIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () ->
                accountService.transferFunds(1L, 2L, BigDecimal.valueOf(1000)));

        assertEquals("Insufficient balance in debit account for owner ID: 1", exception.getMessage());

        verify(accountRepository, times(1)).findByOwnerIdForUpdate(1L);
        verify(accountRepository, times(1)).findByOwnerIdForUpdate(2L);
    }

    @Test
    void transferFunds_ShouldThrowLockAcquisitionException() {
        Account fromAccount = new Account();
        fromAccount.setOwnerId(1L);
        fromAccount.setCurrency("USD");

        Account toAccount = new Account();
        toAccount.setOwnerId(2L);
        toAccount.setCurrency("USD");

        when(accountRepository.findByOwnerIdForUpdate(1L)).thenThrow(new LockAcquisitionException("Failed to acquire lock for accounts 1 or 2", new Exception()));
        when(accountRepository.findByOwnerIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        LockAcquisitionException exception = assertThrows(LockAcquisitionException.class, () ->
                accountService.transferFunds(1L, 2L, BigDecimal.valueOf(100)));

        assertEquals("Failed to acquire lock for accounts 1 or 2", exception.getMessage());

        verify(accountRepository, times(1)).findByOwnerIdForUpdate(1L);
        verify(accountRepository, times(0)).findByOwnerIdForUpdate(2L);
    }

    @Test
    void getBalance_ShouldThrowAccountNotFoundException() {
        when(accountRepository.findByOwnerId(1L)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.getBalance(1L));

        assertEquals("Account not found for owner ID: 1", exception.getMessage());

        verify(accountRepository, times(1)).findByOwnerId(1L);
    }

    @Test
    void transferFunds_ShouldTransferFundsSuccessfully() {
        Account fromAccount = new Account();
        fromAccount.setOwnerId(1L);
        fromAccount.setCurrency("USD");
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        Account toAccount = new Account();
        toAccount.setOwnerId(2L);
        toAccount.setCurrency("USD");
        toAccount.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.findByOwnerIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByOwnerIdForUpdate(2L)).thenReturn(Optional.of(toAccount));
        when(exchangeRateService.getExchangeRate("USD", "USD")).thenReturn(BigDecimal.ONE);

        accountService.transferFunds(1L, 2L, BigDecimal.valueOf(500));

        assertEquals(BigDecimal.valueOf(500), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(1000), toAccount.getBalance());

        verify(accountRepository, times(1)).save(fromAccount);
        verify(accountRepository, times(1)).save(toAccount);
    }
}
