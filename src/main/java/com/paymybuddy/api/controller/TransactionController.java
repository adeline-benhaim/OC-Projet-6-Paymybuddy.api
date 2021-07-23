package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.service.ConnectionService;
import com.paymybuddy.api.service.TransactionService;
import com.paymybuddy.api.service.UserService;
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
    @Autowired
    UserService userService;

    @GetMapping("/transaction")
    public String transaction(Model model) {
        List<Transaction> transactionList = transactionService.getAllTransactions();
        model.addAttribute("transaction", transactionList);
        User user = userService.getUser();
        model.addAttribute("user", user);
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connection", connections);
        return "transaction";
    }

    @PostMapping("/createTransaction")
    public String createTransaction(@Valid @ModelAttribute TransactionDto transactionDto, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                transactionService.createTransaction(transactionDto);
                return "redirect:/transaction";
            } catch (TransactionException e) {
                ObjectError objectError = new ObjectError("objectError", e.getMessage());
                result.addError(objectError);
                model.addAttribute("transactionDto", transactionDto);
                return "transaction";
            }
        }
        model.addAttribute("transactionDto", transactionDto);
        return "transaction";
    }


    @GetMapping("/createTransaction")
    public String createTransaction(Model model) {
        List<Connection> connection = connectionService.getConnections();
        model.addAttribute("connection", connection);
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        return "transaction";
    }
}
