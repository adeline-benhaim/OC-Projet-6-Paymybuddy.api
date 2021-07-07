package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccount")
    public List<BankAccountDto> getAllBankAccountsByUserId() {
        return bankAccountService.getAllByIdUser();
    }

    @PostMapping("/bankAccount")
    public BankAccount createBankAccount(@RequestBody BankAccount bankAccount) {
        return bankAccountService.createBankAccount(bankAccount);
    }

}
