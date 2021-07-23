package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.dto.TransactionDto;

import java.util.List;

public interface TransactionService {

    /**
     * Get a list of transactions issued and received belong to current user
     *
     * @return a list of transactions issued and received belong to current user
     */
    List<Transaction> getAllTransactions();

    /**
     * Create a new transaction between the current user and a user connected
     *
     * @param transactionDto transaction to create
     * @return transaction created
     */
    Transaction createTransaction(TransactionDto transactionDto);
}
