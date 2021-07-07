package com.paymybuddy.api.service;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.dto.BankAccountDto;

import java.util.List;

public interface BankAccountService {

    /**
     * Create a new bank account belonging to current user
     * @param bankAccount information's to create
     * @return bank account information's to create
     */
    BankAccount createBankAccount(BankAccount bankAccount);

    /**
     * Get a list of bank account belonging to current user
     * @return a list of bank account belonging to current user with name, bic and iban information's
     */
    List<BankAccountDto> getAllByIdUser();

}
