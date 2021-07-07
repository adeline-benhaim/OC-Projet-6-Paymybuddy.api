package com.paymybuddy.api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferTest {

    @Test
    @DisplayName("Get transfer")
    void getTransferTest() {

        //GIVEN
        int transferId = 1;
        int idBankAccount = 1;
        int idUser = 1;
        int amount = 100;
        Transfer.TransferType transferType = Transfer.TransferType.DEBIT;
        Timestamp date = null;
        boolean success = true;

        //WHEN
        Transfer transfer = Transfer.builder()
                .transferId(1)
                .idBankAccount(1)
                .idUser(1)
                .amount(100)
                .transferType(Transfer.TransferType.DEBIT)
                .date(null)
                .success(true)
                .build();

        //THEN
        assertThat(transferId).isEqualTo(transfer.getTransferId());
        assertThat(idBankAccount).isEqualTo(transfer.getIdBankAccount());
        assertThat(idUser).isEqualTo(transfer.getIdUser());
        assertThat(amount).isEqualTo(transfer.getAmount());
        assertThat(transferType).isEqualTo(transfer.getTransferType());
        assertThat(date).isEqualTo(transfer.getDate());
        assertTrue(transfer.isSuccess());
    }

}