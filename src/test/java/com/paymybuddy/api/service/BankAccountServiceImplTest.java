package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.exception.BankAccountException;
import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    BankAccountRepository bankAccountRepository;
    @InjectMocks
    BankAccountServiceImpl bankAccountService;
    @InjectMocks
    DataSourceTest dataSourceTest;

    @BeforeEach
    void init() {
        dataSourceTest.clearBankAccountListMocked();
        dataSourceTest.createBankAccountListMocked();
    }

    @Test
    @DisplayName("Create new bank account belonging to the current user")
    void createBankAccountTest() {

        //GIVEN
        Mockito.when(bankAccountRepository.existsByIdUserAndName(1, "bank3")).thenReturn(false);
        BankAccount newBankAccount = BankAccount.builder().idUser(1).name("bank3").iban(3333).bic(3333).build();

        //WHEN
        bankAccountService.createBankAccount(newBankAccount);

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).save(any());
    }

    @Test
    @DisplayName("Create bank account which already exists return bank account exception")
    void createBankAccountWithNameAlreadyExistTest() {

        //GIVEN
        Mockito.when(bankAccountRepository.existsByIdUserAndName(1, "bank1")).thenReturn(true);
        BankAccount newBankAccount = BankAccount.builder().idUser(1).name("bank1").iban(3333).bic(3333).build();

        //THEN
        assertThrows(BankAccountException.class, () -> bankAccountService.createBankAccount(newBankAccount));
    }

    @Test
    @DisplayName("Get a list of bank accounts belonging to the current user")
    void getAllByIdUserTest() {

        //GIVEN
        int idCurrentUser = 1;

        //WHEN
        bankAccountService.getAllByIdUser();

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).findByIdUser(idCurrentUser);

    }
}