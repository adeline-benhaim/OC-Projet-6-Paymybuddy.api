package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.service.ConnectionService;
import com.paymybuddy.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    int page = 0;
    int size = 4;

    @GetMapping("/transactions")
    public String transaction(Model model, @PageableDefault(size = 2) Pageable pageable) {
//        List<Transaction> transactionList = transactionService.getAllTransactions(pageable);
        Pair<List<Transaction>, Long> transactionsPair = transactionService.getTransactions(pageable);
        model.addAttribute("transactions", transactionsPair.getFirst());
        paginationBuilder(model, pageable, transactionsPair.getSecond());
        List<Connection> connections = connectionService.getAllConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("transactionDto", new TransactionDto());
        return "transaction";
    }

    private void paginationBuilder(Model model, Pageable pageable, long totalElements) {
        int actualPageNumber = pageable.getPageNumber();
        model.addAttribute("intActualPage", actualPageNumber);
        String actualPageUrl = "/transactions?page=" + actualPageNumber;
        model.addAttribute("actualPageUrl", actualPageUrl);

        if (pageable.getPageNumber() >= 1) {
            int previousPage = pageable.getPageNumber() - 1;
            String previousPageUrl = "/transactions?page=" + previousPage;
            model.addAttribute("previousPageUrl", previousPageUrl);
            model.addAttribute("intPreviousPage", previousPage);
        }
        if (totalElements > (long) (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
            int nextPage = pageable.getPageNumber() + 1;
            model.addAttribute("intNextPage", nextPage);
            String nextPageUrl = "/transactions?page=" + nextPage;
            model.addAttribute("nextPageUrl", nextPageUrl);

        }
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
        List<Transaction> transactionList = transactionService.getAllTransactions(PageRequest.of(page, size));
        model.addAttribute("transactions", transactionList);
        List<Connection> connections = connectionService.getAllConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("transactionDto", transactionDto);
        return "transaction";
    }
}
