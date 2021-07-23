package com.paymybuddy.api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionTest {

    @Test
    @DisplayName("Get transaction")
    void getTransactionTest() {

        //GIVEN
        int transactionId = 1;
        int idConnection = 1;
        int idTransmitter = 1;
        int idBeneficiary = 1;
        String connectionName = "name";
        String description = "description";
        double amount = 100;
        Timestamp date = null;
        boolean success = true;

        //WHEN
        Transaction transaction = Transaction.builder()
                .transactionId(1)
                .idConnection(1)
                .idTransmitter(1)
                .idBeneficiary(1)
                .connectionName("name")
                .description("description")
                .amount(100.0)
                .date(null)
                .success(true)
                .build();

        //THEN
        assertThat(transactionId).isEqualTo(transaction.getTransactionId());
        assertThat(idConnection).isEqualTo(transaction.getIdConnection());
        assertThat(idTransmitter).isEqualTo(transaction.getIdTransmitter());
        assertThat(idBeneficiary).isEqualTo(transaction.getIdBeneficiary());
        assertThat(connectionName).isEqualTo(transaction.getConnectionName());
        assertThat(description).isEqualTo(transaction.getDescription());
        assertThat(amount).isEqualTo(transaction.getAmount());
        assertThat(date).isEqualTo(transaction.getDate());
        assertTrue(transaction.isSuccess());
    }

}