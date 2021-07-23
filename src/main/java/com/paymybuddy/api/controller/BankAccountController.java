package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccount")
    public String bankAccount(Model model) {
        List<BankAccountDto> bankAccountList = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccountList);
        return "bankAccount";
    }

    @PostMapping("/saveBankAccount")
    public ModelAndView saveBankAccount(@Valid @ModelAttribute BankAccount bankAccount, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/saveBankAccount");
        }
        bankAccountService.createBankAccount(bankAccount);
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/saveBankAccount")
    public String saveBankAccount(Model model) {
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("bankAccount", bankAccount);
        return "formNewBankAccount";
    }

    @GetMapping("/updateBankAccount/{accountId}")
    public String updateBankAccount(@PathVariable("accountId") int accountId, Model model) {
        Optional<BankAccount> updateBankAccount = bankAccountService.getBankAccount(accountId);
        model.addAttribute("bankAccount", updateBankAccount);
        return "formUpdateBankAccount.html";
    }

    @PostMapping("/updateBankAccount")
    public ModelAndView updateBankAccount(int accountId, @ModelAttribute BankAccount bankAccount) {
        bankAccountService.updateBankAccount(accountId, bankAccount);
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/deleteBankAccount/{id}")
    public ModelAndView deleteBankAccount(@PathVariable("id") int accountId) {
        bankAccountService.deleteBankAccount(accountId);
        return new ModelAndView("redirect:/profile#bankAccount");
    }

}
