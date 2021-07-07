package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.exception.TransferException;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.repository.TransferRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

    @Mock
    TransferRepository transferRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TransferServiceImpl transferService;
    @InjectMocks
    DataSourceTest dataSourceTest;

    @BeforeEach
    void init() {
        dataSourceTest.clearTransferListMocked();
        dataSourceTest.clearUserListMocked();
        dataSourceTest.createTransferListMocked();
        dataSourceTest.createUserListMocked();
    }

    int idCurrentUser = 1;

    @Test
    @DisplayName("Get the list of transfers for the current user")
    void getTransfersTest() {

        //GIVEN
        when(transferRepository.findByIdUser(idCurrentUser)).thenReturn(dataSourceTest.getTransferListMocked());

        //WHEN
        List<Transfer> transferList = transferService.getTransfers();

        //THEN
        assertEquals(2, transferList.size());
        assertEquals(Transfer.TransferType.DEBIT, transferList.get(0).getTransferType());

    }

    @Test
    @DisplayName("Create a new transfer debit between the current user and their bank account")
    void createTransferDebitTest() {

        //GIVEN
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        long currentDate = System.currentTimeMillis();
        Transfer newTransferDebit = Transfer.builder().idUser(idCurrentUser).idBankAccount(1).amount(100).transferType(Transfer.TransferType.DEBIT).date(new java.sql.Timestamp(currentDate)).success(true).build();

        //WHEN
        transferService.createTransfer(newTransferDebit);

        //THEN
        assertEquals(0, dataSourceTest.getUserListMocked().get(0).getBalance());
    }

    @Test
    @DisplayName("Create a new transfer credit between the current user and their bank account")
    void createTransferCreditTest() {

        //GIVEN
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        long currentDate = System.currentTimeMillis();
        Transfer newTransferCredit = Transfer.builder().idUser(idCurrentUser).idBankAccount(1).amount(100).transferType(Transfer.TransferType.CREDIT).date(new java.sql.Timestamp(currentDate)).success(true).build();

        //WHEN
        transferService.createTransfer(newTransferCredit);

        //THEN
        assertEquals(200, dataSourceTest.getUserListMocked().get(0).getBalance());
    }

    @Test
    @DisplayName("Create a new transfer debit with insufficient balance")
    void createTransferDebitWithInsufficientBalanceTest() {

        //GIVEN
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        long currentDate = System.currentTimeMillis();
        Transfer newTransferCredit = Transfer.builder().idUser(idCurrentUser).idBankAccount(1).amount(200).transferType(Transfer.TransferType.DEBIT).date(new java.sql.Timestamp(currentDate)).success(true).build();

        //THEN
        assertThrows(TransferException.class, () -> transferService.createTransfer(newTransferCredit));
    }
}
