package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.service.ConnectionService;
import com.paymybuddy.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
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
    private TransactionService transactionService;
    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/transactions")
    public String transaction(Model model, @PageableDefault(size = 2) Pageable pageable) {
        String baseUri = "/transactions?page=";
        Pair<List<Transaction>, Long> transactionsPair = transactionService.getTransactions(pageable);
        model.addAttribute("transactions", transactionsPair.getFirst());
        PaginationUtils.paginationBuilder(model, pageable, transactionsPair.getSecond(), baseUri);
        List<Connection> connections = connectionService.getAllConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("transactionDto", new TransactionDto());
        return "transaction";
    }

    @PostMapping("/createTransaction")
    public String createTransaction(@Valid @ModelAttribute TransactionDto transactionDto, BindingResult result, Model model, Pageable pageable) {

        if (!result.hasErrors()) {
            try {
                transactionService.createTransaction(transactionDto);
                return "redirect:/transactions";
            } catch (TransactionException e) {
                ObjectError objectError = new ObjectError("objectError", e.getMessage());
                result.addError(objectError);
            }
        }
        Pair<List<Transaction>, Long> transactionsPair = transactionService.getTransactions(pageable);
        model.addAttribute("transactions", transactionsPair.getFirst());
        List<Connection> connections = connectionService.getAllConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("transactionDto", transactionDto);
        return "transaction";
    }
}
