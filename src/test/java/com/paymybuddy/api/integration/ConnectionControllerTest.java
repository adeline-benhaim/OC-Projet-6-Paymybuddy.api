package com.paymybuddy.api.integration;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.Connection;
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
public class ConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET request (/connections) must return a list of connections by user id")
    public void getConnectionListTest() throws Exception {

        mockMvc.perform(get("/connections"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("connection"))
                .andExpect(content().string(containsString("email2@test.com")));
    }

    @Test
    @DisplayName("GET request (/createConnection)")
    public void getConnectionCreatedTest() throws Exception {

        mockMvc.perform(get("/createConnection"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("connection"));
    }

    @Test
    @DisplayName("POST request (/createConnection) must save new connection")
    public void postNewConnectionTest() throws Exception {

        Connection connection = Connection.builder()
                .emailOfUserLinked("email6@test.com")
                .name("NewConnection")
                .build();

        mockMvc.perform(post("/createConnection")
                .param("emailOfUserLinked", connection.getEmailOfUserLinked())
                .param("name", connection.getName()))
                .andDo(print())
                .andExpect(view().name("redirect:/connections"))
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().containsHeader("NewConnection");
    }

    @Test
    @DisplayName("POST request (/createConnection) with already existing connection return ConnectionAlreadyExistException")
    public void postNewConnectionWithExceptionTest() throws Exception {

        Connection connection = Connection.builder()
                .emailOfUserLinked("email2@test.com")
                .name("NewConnection")
                .build();

        mockMvc.perform(post("/createConnection")
                .param("emailOfUserLinked", connection.getEmailOfUserLinked())
                .param("name", connection.getName()))
                .andDo(print())
                .andExpect(view().name("connection"));
    }
}
