package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.dto.TransactionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface TransactionService {

    /**
     * Get a pair with a list of transactions and a long number
     *
     * @param pageable the result list
     * @return a pair with a list of transactions issued and received belong to current user and the total of elements of this list
     */
    Pair<List<Transaction>, Long> getTransactions(Pageable pageable);

    /**
     * Create a new transaction between the current user and a user connected
     *
     * @param transactionDto transaction to create
     * @return transaction created
     */
    Transaction createTransaction(TransactionDto transactionDto);
}
