package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransferController {

    @Autowired
    TransferService transferService;

    @GetMapping("/transfer")
    public List<Transfer> getTransfersByIdUser() {
        return transferService.getTransfers();
    }

    @PostMapping("/transfer")
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferService.createTransfer(transfer);
    }
}
