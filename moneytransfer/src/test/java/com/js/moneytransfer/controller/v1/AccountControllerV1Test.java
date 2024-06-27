package com.js.moneytransfer.controller.v1;

import com.js.moneytransfer.constants.AppConstants;
import com.js.moneytransfer.dto.AccountDTO;
import com.js.moneytransfer.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AccountControllerV1Test {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setOwnerId(1L);
        accountDTO.setCurrency("USD");
        accountDTO.setInitialBalance(BigDecimal.valueOf(1000));

        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(accountDTO);

        mockMvc.perform(post(AppConstants.API_V1 + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerId\":1,\"currency\":\"USD\",\"initialBalance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerId", is(1)))
                .andExpect(jsonPath("$.currency", is("USD")))
                .andExpect(jsonPath("$.initialBalance", is(1000)));
    }

    @Test
    void transferFunds_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post(AppConstants.API_V1 + "/transfer")
                        .param("fromOwnerId", "1")
                        .param("toOwnerId", "2")
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Transfer successful")));
    }

    @Test
    void getBalance_ShouldReturnBalance() throws Exception {
        when(accountService.getBalance(anyLong())).thenReturn(BigDecimal.valueOf(1000));

        mockMvc.perform(get(AppConstants.API_V1 + "/balance")
                        .param("ownerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1000)));
    }
}
