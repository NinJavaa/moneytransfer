package com.js.moneytransfer.config;

import com.js.moneytransfer.constants.AppConstants;
import com.js.moneytransfer.entity.Account;
import com.js.moneytransfer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class AccountLoader {

    private final AccountRepository accountRepository;
    private final AccountProperties accountProperties;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            try {
                if (accountRepository.count() == 0) {
                    List<AccountProperties.AccountConfig> accounts = accountProperties.getAccounts();
                    for (AccountProperties.AccountConfig accountConfig : accounts) {
                        Account account = new Account();
                        account.setOwnerId(accountConfig.getOwnerId());
                        account.setCurrency(accountConfig.getCurrency());
                        account.setBalance(accountConfig.getBalance());
                        account.setVersion(0L);

                        accountRepository.save(account);
                    }
                    log.info(AppConstants.SUCCESSFULLY_LOADED_INITIAL_ACCOUNTS, "DEMO! " +  accounts);
                } else {
                    log.info(AppConstants.ACCOUNTS_ALREADY_EXIST_IN_THE_DATABASE_SKIPPING_INITIAL_DATA_LOADING);
                }
            } catch (Exception e) {
                log.error(AppConstants.ERROR_OCCURRED_WHILE_LOADING_INITIAL_ACCOUNTS, e);
            }
        };
    }
}
