package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Commission;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.Transaction;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ConnectionRepository connectionRepository;
    @Mock
    CommissionRepository commissionRepository;
    @InjectMocks
    TransactionServiceImpl transactionService;
    @InjectMocks
    DataSourceTest dataSourceTest;

    @BeforeEach
    void init() {
        dataSourceTest.clearConnectionListMocked();
        dataSourceTest.clearUserListMocked();
        dataSourceTest.createUserListMocked();
        dataSourceTest.createConnectionListMocked();
    }

    @Test
    @DisplayName("Get a list of all transactions belonging to the current user")
    void getAllTransactionsTest() {

        //GIVEN
        int idCurrentUser = 1;

        //WHEN
        transactionService.getAllTransactions();

        //THEN
        Mockito.verify(transactionRepository, Mockito.times(1)).findByIdTransmitterOrIdBeneficiary(idCurrentUser, idCurrentUser);
    }

    @Test
    @DisplayName("Create transaction between current user and an unknown user beneficiary return a transaction exception")
    void createTransactionWithUnknownUserBeneficiaryTest() {

        //GIVEN
        int idCurrentUser = 1;
        User currentUser = dataSourceTest.getUserListMocked().get(0);
        TransactionDto transactionDto = TransactionDto.builder().emailBeneficiary("unknown").build();
        Mockito.when(userRepository.findByUserId(idCurrentUser)).thenReturn(currentUser);

        //THEN
        assertThrows(TransactionException.class, () -> transactionService.createTransaction(transactionDto));
    }

    @Test
    @DisplayName("Create transaction with not sufficient balance to make transaction return a transaction exception")
    void createTransactionWithNotSufficientBalanceTest() {

        //GIVEN
        int idCurrentUser = 1;
        User currentUser = dataSourceTest.getUserListMocked().get(0);
        User userBeneficiary = dataSourceTest.getUserListMocked().get(1);
        TransactionDto transactionDto = TransactionDto.builder().emailBeneficiary("email2").amount(500).build();
        Mockito.when(userRepository.findByUserId(idCurrentUser)).thenReturn(currentUser);
        Mockito.when(userRepository.findUserIdByEmail(transactionDto.getEmailBeneficiary())).thenReturn(userBeneficiary);

        //THEN
        assertThrows(TransactionException.class, () -> transactionService.createTransaction(transactionDto));
    }

    @Test
    @DisplayName("Create new transaction")
    void createNewTransactionTest() {

        //GIVEN
        int idCurrentUser = 1;
        User currentUser = dataSourceTest.getUserListMocked().get(0);
        User userBeneficiary = dataSourceTest.getUserListMocked().get(1);
        TransactionDto transactionDto = TransactionDto.builder().emailBeneficiary("email2").amount(10).build();
        Mockito.when(userRepository.findByUserId(idCurrentUser)).thenReturn(currentUser);
        Mockito.when(userRepository.findUserIdByEmail(transactionDto.getEmailBeneficiary())).thenReturn(userBeneficiary);
        Connection connection = dataSourceTest.getConnectionListMocked().get(0);
        Mockito.when(connectionRepository.findByIdUserAndEmailOfUserLinked(currentUser.getUserId(), userBeneficiary.getEmail())).thenReturn(connection);
        double balanceCurrentUserBeforeTransaction = dataSourceTest.getUserListMocked().get(0).getBalance();
        double balanceUserBeneficiaryBeforeTransaction = dataSourceTest.getUserListMocked().get(1).getBalance();

        //WHEN
        transactionService.createTransaction(transactionDto);
        double balanceCurrentUserAfterTransaction = dataSourceTest.getUserListMocked().get(0).getBalance();
        double balanceUserBeneficiaryAfterTransaction = dataSourceTest.getUserListMocked().get(1).getBalance();

        //THEN
        assertEquals(89.5, dataSourceTest.getUserListMocked().get(0).getBalance());
        assertEquals(60, dataSourceTest.getUserListMocked().get(1).getBalance());
        assertEquals(balanceCurrentUserBeforeTransaction - 10.5, balanceCurrentUserAfterTransaction);
        assertEquals(balanceUserBeneficiaryBeforeTransaction + 10, balanceUserBeneficiaryAfterTransaction);
    }

}