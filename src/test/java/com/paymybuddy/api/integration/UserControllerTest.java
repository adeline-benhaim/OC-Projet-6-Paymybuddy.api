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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET request (/home) must return home page with user logged")
    public void getHomeTest() throws Exception {

        mockMvc.perform(get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Paul")));
    }

    @Test
    @DisplayName("GET request (/) must return home page without user logged")
    public void getLoginTest() throws Exception {

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"));
    }

    @Test
    @DisplayName("GET request (/profile)")
    public void getProfileTest() throws Exception {

        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileCoordinates"))
                .andExpect(content().string(containsString("Paul")));
    }

    @Test
    @DisplayName("GET request (/profileBankAccount)")
    public void getProfileBankAccountTest() throws Exception {

        mockMvc.perform(get("/profileBankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileBankAccount"))
                .andExpect(content().string(containsString("Societe Generale")));
    }

    @Test
    @DisplayName("GET request (/profileSettings)")
    public void getProfileSettingsTest() throws Exception {

        mockMvc.perform(get("/profileSettings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileSettings"));
    }

    @Test
    @DisplayName("GET request (/createUser)")
    public void getUserCreatedTest() throws Exception {

        mockMvc.perform(get("/createUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("formNewUser"));
    }
}
