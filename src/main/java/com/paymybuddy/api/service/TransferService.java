package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Transfer;

import java.util.List;

public interface TransferService {

    /**
     * Get the list of transfers for the current user
     *
     * @return the list of transfers for the current user
     */
    List<Transfer> getTransfers();

    /**
     * Create a new transfer between the current user and their bank account
     *
     * @param transfer to create
     * @return transfer created
     */
    Transfer createTransfer(Transfer transfer);
}
