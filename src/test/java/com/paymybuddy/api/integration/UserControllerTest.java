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
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/contact) must return contact page with user logged")
    public void getContactTest() throws Exception {

        mockMvc.perform(get("/contact"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("formContact"));
    }

    @Test
    @DisplayName("GET request (/contact) must return contact page with user logged")
    public void getContactWithoutUserConnectedTest() throws Exception {

        mockMvc.perform(get("/contact"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("GET request (/login) must return login page")
    public void getLoginTest() throws Exception {

        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/login) with user logged must return home page")
    public void getLoginWithUserLoggedTest() throws Exception {

        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
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
    public void getHomePageWithoutUserLoggedTest() throws Exception {

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/) must return home page with user logged")
    public void getHomePageWithUserLoggedTest() throws Exception {

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/logout) must return homepage")
    public void getLogoutTest() throws Exception {

        mockMvc.perform(get("/logout"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("homePage"));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/profile)")
    public void getProfileTest() throws Exception {

        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileCoordinates"))
                .andExpect(content().string(containsString("Paul")));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/profileBankAccount)")
    public void getProfileBankAccountTest() throws Exception {

        mockMvc.perform(get("/profileBankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileBankAccount"))
                .andExpect(content().string(containsString("Societe Generale")));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/profileSettings)")
    public void getProfileSettingsTest() throws Exception {

        mockMvc.perform(get("/profileSettings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profileSettings"));
    }

    @Test
    @WithMockUser(username = "1", password = "12345678", roles = "USER")
    @DisplayName("GET request (/createUser)")
    public void getUserCreatedTest() throws Exception {

        mockMvc.perform(get("/createUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("formNewUser"));
    }
}
