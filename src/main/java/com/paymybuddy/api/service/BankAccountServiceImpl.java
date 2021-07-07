package com.paymybuddy.api.service;

import com.paymybuddy.api.mapper.MapperDto;
import com.paymybuddy.api.exception.BankAccountException;
import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

}
