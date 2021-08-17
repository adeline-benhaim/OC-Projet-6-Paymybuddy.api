package com.paymybuddy.api.service;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.dto.BankAccountDto;

import java.util.List;

public interface BankAccountService {

    /**
     * Create a new bank account belonging to current user
     *
     * @param bankAccount information's to create
     * @return bank account information's to create
     */
    BankAccount createBankAccount(BankAccount bankAccount);

    /**
     * Get a list of bank account belonging to current user
     *
     * @return a list of bank account belonging to current user with name, bic and iban information's
     */
    List<BankAccountDto> getAllByIdUser();

    /**
     * Get a bank account by account ID
     *
     * @param accountId of the requested bank account
     * @return bank account
     */
    BankAccount getBankAccount(int accountId);

    /**
     * Update a bank account belonging to current user
     *
     * @param bankAccount information's to update
     * @return bank account information's to update
     */
    BankAccount updateBankAccount(int accountId, BankAccount bankAccount);

    /**
     * Delete a bank account by account ID
     *
     * @param accountId ID of bank account to delete
     */
    void deleteBankAccount(int accountId);
}
