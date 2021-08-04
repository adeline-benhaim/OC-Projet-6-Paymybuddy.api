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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        Mockito.when(bankAccountRepository.existsByIdUserAndName(1, "bank3")).thenReturn(false);
        BankAccount newBankAccount = BankAccount.builder().idUser(1).name("bank3").iban("3333").bic("3333").build();

        //WHEN
        bankAccountService.createBankAccount(newBankAccount);

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).save(any());
    }

    @Test
    @DisplayName("Create bank account which already exists return bank account exception")
    void createBankAccountWithNameAlreadyExistTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        Mockito.when(bankAccountRepository.existsByIdUserAndName(1, "bank1")).thenReturn(true);
        BankAccount newBankAccount = BankAccount.builder().idUser(1).name("bank1").iban("3333").bic("3333").build();

        //THEN
        assertThrows(BankAccountException.class, () -> bankAccountService.createBankAccount(newBankAccount));
    }

    @Test
    @DisplayName("Get a list of bank accounts belonging to the current user")
    void getAllByIdUserTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        int idCurrentUser = 1;

        //WHEN
        bankAccountService.getAllByIdUser();

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).findByIdUserOrderByAccountIdDesc(idCurrentUser);

    }

    @Test
    @DisplayName("Get a  bank account by account id")
    void getBankAccountByAccountIdTest() {

        //GIVEN
        int accountId = 1;

        //WHEN
        bankAccountService.getBankAccount(accountId);

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).findById(accountId);

    }

    @Test
    @DisplayName("Update bank account")
    void updateBankAccountTest() {

        //GIVEN
        Mockito.when(bankAccountRepository.findByAccountId(1)).thenReturn(dataSourceTest.getBankAccountListMocked().get(0));
        BankAccount bankAccount = BankAccount.builder()
                .name("newName")
                .bic("newBic")
                .iban("newIban")
                .build();

        //WHEN
        bankAccountService.updateBankAccount(1, bankAccount);

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).save(any());

    }

    @Test
    @DisplayName("Delete bank account")
    void deleteBankAccountTest() {

        //GIVEN

        //WHEN
        bankAccountService.deleteBankAccount(1);

        //THEN
        Mockito.verify(bankAccountRepository, Mockito.times(1)).deleteById(any());

    }
}