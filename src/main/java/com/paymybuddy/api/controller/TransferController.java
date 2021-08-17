package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.TransferException;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.service.BankAccountService;
import com.paymybuddy.api.service.TransferService;
import com.paymybuddy.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class TransferController {

    @Autowired
    private TransferService transferService;
    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/transfer")
    public String transfer(Model model, @PageableDefault(size=2) Pageable pageable) {
        String baseUri = "/transfer?page=";
        Pair<Page<Transfer>, Long> transferPair = transferService.getTransfers(pageable);
        model.addAttribute("transfer", transferPair.getFirst());
        PaginationUtils.paginationBuilder(model, pageable, transferPair.getSecond(), baseUri);
        User user = userService.getUser();
        model.addAttribute("user", user);
        return "transfer";
    }

    @PostMapping("/createTransfer")
    public String createTransfer(@Valid @ModelAttribute Transfer transfer, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                transferService.createTransfer(transfer);
                return "redirect:/transfer";
            } catch (TransferException e) {
                ObjectError objectError = new ObjectError("objectError", e.getMessage());
                result.addError(objectError);
                List<BankAccountDto> bankAccountList = bankAccountService.getAllByIdUser();
                model.addAttribute("bankAccount", bankAccountList);
                return "formNewTransfer";
            }
        }
        List<BankAccountDto> bankAccountList = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccountList);
        return "formNewTransfer";
    }

    @GetMapping("/createTransfer")
    public String createTransfer(Model model) {
        Transfer t = new Transfer();
        model.addAttribute("transfer", t);
        List<BankAccountDto> bankAccountList = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccountList);
        return "formNewTransfer";
    }
}
