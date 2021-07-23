package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.BankAccountException;
import com.paymybuddy.api.mapper.MapperDto;
import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    @Autowired
    BankAccountRepository bankAccountRepository;

    int idCurrentUser = User.getCurrentUser();

    /**
     * Create a new bank account belonging to current user
     *
     * @param bankAccount information's to create
     * @return bank account information's to create
     */
    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) {
        logger.info("Create new bank account");
        if (bankAccountRepository.existsByIdUserAndName(idCurrentUser, bankAccount.getName())) {
            logger.error("Unable to create bank account because bank account name : " + bankAccount.getName() + " already exist");
            throw new BankAccountException("Unable to create bank account because bank account name : " + bankAccount.getName() + " already exist");
        }
        BankAccount newBankAccount = BankAccount.builder()
                .idUser(idCurrentUser)
                .name(bankAccount.getName())
                .bic(bankAccount.getBic())
                .iban(bankAccount.getIban())
                .build();
        logger.info("New bank account created");
        return bankAccountRepository.save(newBankAccount);
    }

    /**
     * Get a list of bank account belonging to current user
     *
     * @return a list of bank account belonging to current user with name, bic and iban information's
     */
    @Override
    public List<BankAccountDto> getAllByIdUser() {
        logger.info("Get a list of bank accounts");
        List<BankAccount> bankAccountList = bankAccountRepository.findByIdUser(idCurrentUser);
        return bankAccountList
                .stream()
                .map(MapperDto::convertToBankAccountDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a bank account by account ID
     *
     * @param accountId of the requested bank account
     * @return bank account
     */
    public Optional<BankAccount> getBankAccount(int accountId) {
        logger.info("Get a bank account by ID account");
        return bankAccountRepository.findById(accountId);
    }

    /**
     * Update a bank account belonging to current user
     *
     * @param bankAccount information's to update
     * @return bank account information's to update
     */
    @Override
    public BankAccount updateBankAccount(int accountId, BankAccount bankAccount) {
        logger.info("Update a bank account");
        BankAccount bankAccountToUpdate = bankAccountRepository.findByAccountId(accountId);
        bankAccountToUpdate.setAccountId(accountId);
        bankAccountToUpdate.setName(bankAccount.getName());
        bankAccountToUpdate.setBic(bankAccount.getBic());
        bankAccountToUpdate.setIban(bankAccount.getIban());
        logger.info("Bank account updated");
        return bankAccountRepository.save(bankAccountToUpdate);

    }

    /**
     * Delete a bank account by account ID
     *
     * @param accountId ID of bank account to delete
     */
    @Override
    public void deleteBankAccount(int accountId) {
        logger.info("Delete bank account : " + accountId);
        bankAccountRepository.deleteById(accountId);
    }

}
