package com.js.moneytransfer.config;

import com.js.moneytransfer.entity.Account;
import com.js.moneytransfer.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AccountLoaderTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountProperties accountProperties;

    private AccountLoader accountLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountLoader = new AccountLoader(accountRepository, accountProperties);
    }

    @Test
    void loadData_ShouldLoadInitialAccountsWhenRepositoryIsEmpty() throws Exception {
        List<AccountProperties.AccountConfig> accountConfigs = List.of(
                createAccountConfig(1L, "USD", BigDecimal.valueOf(1000)),
                createAccountConfig(2L, "EUR", BigDecimal.valueOf(2000))
        );

        when(accountRepository.count()).thenReturn(0L);
        when(accountProperties.getAccounts()).thenReturn(accountConfigs);

        CommandLineRunner runner = accountLoader.loadData();
        runner.run();

        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void loadData_ShouldNotLoadInitialAccountsWhenRepositoryIsNotEmpty() throws Exception {
        when(accountRepository.count()).thenReturn(1L);

        CommandLineRunner runner = accountLoader.loadData();
        runner.run();

        verify(accountRepository, never()).save(any(Account.class));
    }

    private AccountProperties.AccountConfig createAccountConfig(Long ownerId, String currency, BigDecimal balance) {
        AccountProperties.AccountConfig config = new AccountProperties.AccountConfig();
        config.setOwnerId(ownerId);
        config.setCurrency(currency);
        config.setBalance(balance);
        return config;
    }
}
