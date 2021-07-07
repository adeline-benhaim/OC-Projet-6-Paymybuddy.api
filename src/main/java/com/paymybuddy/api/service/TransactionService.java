package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Transaction;

import java.util.List;

public interface TransactionService {

    /**
     * Get a list of transactions issued and received belong to current user
     *
     * @return a list of transactions issued and received belong to current user
     */
    List<Transaction> getAllTransactions();

    /**
     * Create a new transfer between the current user and a user connected
     *
     * @param transaction to create
     * @return transaction created
     */
    Transaction createTransaction(Transaction transaction);
}
