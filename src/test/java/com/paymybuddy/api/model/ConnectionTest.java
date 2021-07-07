package com.paymybuddy.api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectionTest {

    @Test
    @DisplayName("Get connection")
    void getConnectionTest() {

        //GIVEN
        int connectionId = 1;
        int idUser = 1;
        String emailOfUserLinked = "email";
        String name = "name";
        List<Transaction> transactionList = null;

        //WHEN
        Connection connection = Connection.builder()
                .connectionId(1)
                .idUser(1)
                .emailOfUserLinked("email")
                .name("name")
                .transactionList(null)
                .build();

        //THEN
        assertThat(connectionId).isEqualTo(connection.getConnectionId());
        assertThat(idUser).isEqualTo(connection.getIdUser());
        assertThat(emailOfUserLinked).isEqualTo(connection.getEmailOfUserLinked());
        assertThat(name).isEqualTo(connection.getName());
        assertThat(transactionList).isEqualTo(connection.getTransactionList());
    }

}