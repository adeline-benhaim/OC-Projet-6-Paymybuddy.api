package com.paymybuddy.api.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser(username = "1", password = "12345678", roles = "USER")
@AutoConfigureMockMvc(addFilters = false)
class CommissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET request (/dailyCommissions) must return a list of daily commissions")
    public void getDailyCommissionsListTest() throws Exception {

        mockMvc.perform(get("/dailyCommissions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("dailyCommission"))
                .andExpect(model().attributeExists("dailyCommissions"));
    }

    @Test
    @DisplayName("GET request (/monthlyCommissions) must return a list of monthly commissions")
    public void getMonthlyCommissionsListTest() throws Exception {

        mockMvc.perform(get("/monthlyCommissions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("monthlyCommission"))
                .andExpect(model().attributeExists("monthlyCommissions"));
    }

    @Test
    @DisplayName("GET request (/yearCommissions) must return a list of year commissions")
    public void getYearCommissionsListTest() throws Exception {

        mockMvc.perform(get("/yearCommissions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("yearCommission"))
                .andExpect(model().attributeExists("yearCommissions"));
    }

    @Test
    @DisplayName("GET request (/homeAdmin) must return total of commissions")
    public void getHomeAdminTest() throws Exception {

        mockMvc.perform(get("/homeAdmin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("homeAdmin"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("amountDay"))
                .andExpect(model().attributeExists("amountMonth"))
                .andExpect(model().attributeExists("amountYear"));
    }
}