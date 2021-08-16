package com.paymybuddy.api.integration;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.dto.TransactionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(username = "1", password = "12345678", roles = "USER")
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("GET request (/transactions) must return a list of transactions by user id")
    public void getTransactionListTest() throws Exception {

        mockMvc.perform(get("/transactions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(content().string(containsString("Cinema refund")));
    }

    @Test
    @DisplayName("POST request (/createTransaction) must save a new transaction")
    public void postNewTransactionTest() throws Exception {

        TransactionDto transactionDto = TransactionDto.builder()
                .idBeneficiary(1)
                .idTransmitter(2)
                .amount(20)
                .description("test")
                .build();

        mockMvc.perform(post("/createTransaction")
                .sessionAttr("idBeneficiary", transactionDto.getIdBeneficiary())
                .sessionAttr("idTransmitter", transactionDto.getIdTransmitter())
                .sessionAttr("amount", transactionDto.getAmount())
                .param("description", transactionDto.getDescription()))
                .andDo(print())
                .andExpect(view().name("transaction"))
                .andExpect(status().isOk())
                .andReturn().getResponse().containsHeader("test");
    }
}
