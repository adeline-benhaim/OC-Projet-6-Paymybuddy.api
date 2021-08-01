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
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("GET request (/bankAccount) must return a list of bank account by user id")
    public void getBankAccountListTest() throws Exception {

        mockMvc.perform(get("/bankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccount"))
                .andExpect(content().string(containsString("Societe Generale")));
    }

    @Test
    @DisplayName("GET request (/saveBankAccount)")
    public void getBankAccountSavedTest() throws Exception {

        mockMvc.perform(get("/saveBankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("formNewBankAccount"));
    }

    @Test
    @DisplayName("GET request (/updateBankAccount/{accountId})")
    public void getBankAccountUpdatedTest() throws Exception {

        mockMvc.perform(get("/updateBankAccount/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("formUpdateBankAccount"))
                .andExpect(content().string(containsString("Societe Generale")));
    }

    @Test
    @DisplayName("GET request (deleteBankAccount/{accountId})")
    public void getBankAccountDeletedTest() throws Exception {

        mockMvc.perform(get("/profileBankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileBankAccount"));
    }

//    @Test
//    @DisplayName("POST request (/saveBankAccount) must save new bank account")
//    public void postNewBankAccountTest() throws Exception {
//
//        //GIVEN
//        BankAccount bankAccount = BankAccount.builder()
//                .name("NewBankAccount")
//                .bic("11111")
//                .iban("22222")
//                .build();
//
//
//        RequestBuilder request = post("/formNewBankAccount.html")
//                .param("name", bankAccount.getName())
//                .param("bic", bankAccount.getBic())
//                .param("iban",bankAccount.getIban());
//
//        mockMvc.perform(post("/saveBankAccount")
//                .content(asJsonString(bankAccount))
//                .contentType("application/json"))
//                .andDo(print()).andExpect(status().isOk());
//
//        mockMvc
//                .perform(request)
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(redirectedUrl("/profileBankAccount"));
//
//    }
}
