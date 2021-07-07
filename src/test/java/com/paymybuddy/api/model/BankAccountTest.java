package com.paymybuddy.api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountTest {

    @Test
    @DisplayName("Get bank account")
    void getBankAccountTest() {

        //GIVEN
        int accountId = 1;
        int idUser = 1;
        String name = "name";
        int bic = 1;
        int iban = 1;
        List<Transfer> transferList = null;

        //WHEN
        BankAccount bankAccount = BankAccount.builder()
                .accountId(1)
                .idUser(1)
                .name("name")
                .bic(1)
                .iban(1)
                .transferList(null)
                .build();

        //THEN
        assertThat(accountId).isEqualTo(bankAccount.getAccountId());
        assertThat(idUser).isEqualTo(bankAccount.getIdUser());
        assertThat(name).isEqualTo(bankAccount.getName());
        assertThat(bic).isEqualTo(bankAccount.getBic());
        assertThat(iban).isEqualTo(bankAccount.getIban());
        assertThat(transferList).isEqualTo(bankAccount.getTransferList());
    }

}