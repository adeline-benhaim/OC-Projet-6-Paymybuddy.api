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
public class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET request (/transfer) must return a list of transfers by user id")
    public void getTransferListTest() throws Exception {

        mockMvc.perform(get("/transfer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(content().string(containsString("50.0")));
    }

    @Test
    @DisplayName("GET request (/createTransfer)")
    public void getTransferCreatedTest() throws Exception {

        mockMvc.perform(get("/createTransfer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("formNewTransfer"));
    }
}
