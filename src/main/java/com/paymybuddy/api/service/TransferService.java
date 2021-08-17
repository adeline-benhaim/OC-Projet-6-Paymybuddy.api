package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

public interface TransferService {

    /**
     * Get a pair with a list of transfer and a long number
     *
     * @param pageable the result list
     * @return a pair with a list of transfer belong to current user and the total of elements of this list
     */
    Pair<Page<Transfer>, Long> getTransfers(Pageable pageable);

    /**
     * Create a new transfer between the current user and their bank account
     *
     * @param transfer to create
     * @return transfer created
     */
    Transfer createTransfer(Transfer transfer);
}
