package com.js.moneytransfer.mapper;

import com.js.moneytransfer.dto.AccountDTO;
import com.js.moneytransfer.entity.Account;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountMapperTest {

    private final AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void toAccountDTO_ShouldMapEntityToDto() {
        Account account = new Account();
        account.setId(1L);
        account.setOwnerId(2L);
        account.setCurrency("USD");
        account.setBalance(BigDecimal.valueOf(1000));

        AccountDTO accountDTO = accountMapper.toAccountDTO(account);

        assertEquals(1L, accountDTO.getId());
        assertEquals(2L, accountDTO.getOwnerId());
        assertEquals("USD", accountDTO.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), accountDTO.getInitialBalance());
    }

    @Test
    void toAccount_ShouldMapDtoToEntity() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setOwnerId(2L);
        accountDTO.setCurrency("USD");
        accountDTO.setInitialBalance(BigDecimal.valueOf(1000));

        Account account = accountMapper.toAccount(accountDTO);

        assertEquals(2L, account.getOwnerId());
        assertEquals("USD", account.getCurrency());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
    }
}
