package com.js.moneytransfer.mapper;

import com.js.moneytransfer.dto.AccountDTO;
import com.js.moneytransfer.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "balance", target = "initialBalance")
    AccountDTO toAccountDTO(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", source = "initialBalance")
    Account toAccount(AccountDTO accountDTO);
}
