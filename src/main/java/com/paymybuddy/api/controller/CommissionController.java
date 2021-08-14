package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.Commission;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.service.CommissionService;
import com.paymybuddy.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CommissionController {

    @Autowired
    private CommissionService commissionService;
    @Autowired
    private UserService userService;

    @GetMapping("/dailyCommissions")
    public String getDailyCommissions(Model model) {
        List<Commission> dailyCommissions = commissionService.getDailyCommissions();
        model.addAttribute("dailyCommissions", dailyCommissions);
        return "dailyCommission";
    }

    @GetMapping("/monthlyCommissions")
    public String getMonthlyCommissions(Model model) {
        List<Commission> monthlyCommissions = commissionService.getMonthlyCommissions();
        model.addAttribute("monthlyCommissions", monthlyCommissions);
        return "monthlyCommission";
    }

    @GetMapping("/yearCommissions")
    public String getYearCommissions(Model model) {
        List<Commission> yearCommissions = commissionService.getYearCommissions();
        model.addAttribute("yearCommissions", yearCommissions);
        return "yearCommission";
    }

    @GetMapping("/homeAdmin")
    public String getTotalDay(Model model) {
        User user = userService.getUser();
        model.addAttribute("user", user);
        Double amountDay = commissionService.getTotalDay();
        model.addAttribute("amountDay", amountDay);
        Double amountMonth = commissionService.getTotalMonth();
        model.addAttribute("amountMonth", amountMonth);
        Double amountYear = commissionService.getTotalYear();
        model.addAttribute("amountYear", amountYear);
        return "homeAdmin";
    }

}
