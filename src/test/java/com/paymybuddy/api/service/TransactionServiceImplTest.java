package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.constants.CommissionRate;
import com.paymybuddy.api.exception.TransactionException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.TransactionDto;
import com.paymybuddy.api.repository.CommissionRepository;
import com.paymybuddy.api.repository.ConnectionRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
    @InjectMocks
    CommissionRate commissionRate;

    @BeforeEach
    void init() {
        dataSourceTest.clearConnectionListMocked();
        dataSourceTest.clearUserListMocked();
        dataSourceTest.createUserListMocked();
        dataSourceTest.createConnectionListMocked();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
    }

    @Test
    @DisplayName("Get a list of all transactions belonging to the current user")
    void getAllTransactionsTest() {

        //GIVEN
        int idCurrentUser = 1;

        //WHEN
        transactionService.getAllTransactions();

        //THEN
        Mockito.verify(transactionRepository, Mockito.times(1)).findByIdTransmitterOrIdBeneficiaryOrderByDateDesc(idCurrentUser, idCurrentUser);
    }

    @Test
    @DisplayName("Create transaction between current user and an unknown user beneficiary return a transaction exception")
    void createTransactionWithUnknownUserBeneficiaryTest() {

        //GIVEN
        int idCurrentUser = 1;
        User currentUser = dataSourceTest.getUserListMocked().get(0);
        TransactionDto transactionDto = TransactionDto.builder().emailBeneficiary("unknown").build();
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(currentUser);

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
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(currentUser);
        when(userRepository.findUserByEmail(transactionDto.getEmailBeneficiary())).thenReturn(userBeneficiary);

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
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(currentUser);
        when(userRepository.findUserByEmail(transactionDto.getEmailBeneficiary())).thenReturn(userBeneficiary);
        Connection connection = dataSourceTest.getConnectionListMocked().get(0);
        when(connectionRepository.findByIdUserAndEmailOfUserLinked(currentUser.getUserId(), userBeneficiary.getEmail())).thenReturn(connection);
        double balanceCurrentUserBeforeTransaction = dataSourceTest.getUserListMocked().get(0).getBalance();
        double balanceUserBeneficiaryBeforeTransaction = dataSourceTest.getUserListMocked().get(1).getBalance();
        double commission = transactionDto.getAmount() * CommissionRate.COMMISSION_RATE;
        double maximumAuthorizedAmount =  balanceCurrentUserBeforeTransaction / CommissionRate.RATE_CALCULATION_MAXIMUM_AUTHORIZED;

        //WHEN
        transactionService.createTransaction(transactionDto);
        double balanceCurrentUserAfterTransaction = dataSourceTest.getUserListMocked().get(0).getBalance();
        double balanceUserBeneficiaryAfterTransaction = dataSourceTest.getUserListMocked().get(1).getBalance();

        //THEN
        assertEquals(89.5, dataSourceTest.getUserListMocked().get(0).getBalance());
        assertEquals(60, dataSourceTest.getUserListMocked().get(1).getBalance());
        assertEquals(balanceCurrentUserBeforeTransaction - 10.5, balanceCurrentUserAfterTransaction);
        assertEquals(balanceUserBeneficiaryBeforeTransaction + 10, balanceUserBeneficiaryAfterTransaction);
        assertEquals(0.5, commission);
        assertEquals(95.24, Math.round(maximumAuthorizedAmount*100.0)/100.0);
    }

}