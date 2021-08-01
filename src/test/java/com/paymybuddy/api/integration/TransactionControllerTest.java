package com.paymybuddy.api.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(username = "1", password = "12345678", roles = "USER")
@AutoConfigureMockMvc
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
                .andExpect(content().string(containsString("Restaurant refund")));
    }

    @Test
    @DisplayName("GET request (/createTransaction)")
    public void getTransactionCreatedTest() throws Exception {

        mockMvc.perform(get("/createTransaction"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"));
    }
}
