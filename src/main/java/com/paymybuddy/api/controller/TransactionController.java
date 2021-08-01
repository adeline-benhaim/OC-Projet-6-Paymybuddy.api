package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.service.ConnectionService;
import com.paymybuddy.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    ConnectionService connectionService;

    @GetMapping("/transactions")
    public String transaction(Model model) {
        List<Transaction> transactionList = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactionList);
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("transactionDto", new TransactionDto());
        return "transaction";
    }

    @PostMapping("/createTransaction")
    public String createTransaction(@Valid @ModelAttribute TransactionDto transactionDto, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                transactionService.createTransaction(transactionDto);
                return "redirect:/transactions";
            } catch (TransactionException e) {
                ObjectError objectError = new ObjectError("objectError", e.getMessage());
                result.addError(objectError);
            }
        }

        List<Transaction> transactionList = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactionList);
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("transactionDto", transactionDto);

        return "transaction";
    }


    @GetMapping("/createTransaction")
    public String createTransaction(Model model) {
        List<Transaction> transactionList = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactionList);
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connection", connections);
        TransactionDto transactionDto = new TransactionDto();
        model.addAttribute("transactionDto", transactionDto);
        return "transaction";
    }
}
