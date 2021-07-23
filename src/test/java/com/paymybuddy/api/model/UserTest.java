package com.paymybuddy.api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("Get user")
    void getUserTest() {

        //GIVEN
        int userId = 1;
        String email = "email";
        String password = "password";
        String firstName = "firstName";
        String lastName = "lastName";
        double balance = 100;
        String address = "address";
        int zip = 10000;
        String city = "city";
        String phone = "phone";
        List<BankAccount> bankAccounts = null;
        List<Connection> connectionsUser = null;
        List<Transfer> transferList = null;
        List<Transaction> transactionsTransmitter = null;
        List<Transaction> transactionsBeneficiary = null;

        //WHEN
        User user = User.builder()
                .userId(1)
                .email("email")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .balance(100.0)
                .address("address")
                .zip(10000)
                .city("city")
                .phone("phone")
                .bankAccounts(null)
                .connectionsUser(null)
                .transferList(null)
                .transactionsTransmitter(null)
                .transactionsBeneficiary(null)
                .build();

        //THEN
        assertThat(userId).isEqualTo(user.getUserId());
        assertThat(email).isEqualTo(user.getEmail());
        assertThat(password).isEqualTo(user.getPassword());
        assertThat(firstName).isEqualTo(user.getFirstName());
        assertThat(lastName).isEqualTo(user.getLastName());
        assertThat(balance).isEqualTo(user.getBalance());
        assertThat(address).isEqualTo(user.getAddress());
        assertThat(zip).isEqualTo(user.getZip());
        assertThat(city).isEqualTo(user.getCity());
        assertThat(phone).isEqualTo(user.getPhone());
        assertThat(bankAccounts).isEqualTo(user.getBankAccounts());
        assertThat(connectionsUser).isEqualTo(user.getConnectionsUser());
        assertThat(transferList).isEqualTo(user.getTransferList());
        assertThat(transactionsTransmitter).isEqualTo(user.getTransactionsTransmitter());
        assertThat(transactionsBeneficiary).isEqualTo(user.getTransactionsBeneficiary());
    }

}