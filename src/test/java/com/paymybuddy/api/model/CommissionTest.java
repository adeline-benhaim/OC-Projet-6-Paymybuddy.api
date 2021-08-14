package com.paymybuddy.api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class CommissionTest {

    @Test
    @DisplayName("Get commission")
    void getCommissionTest() {

        //GIVEN

        int commissionId = 1;
        int idTransaction = 1;
        double amount = 10;
        Timestamp date = null;

        //WHEN
        Commission commission = Commission.builder()
                .commissionId(1)
                .idTransaction(1)
                .amount(10)
                .date(null)
                .build();

        //THEN
        assertThat(commissionId).isEqualTo(commission.getCommissionId());
        assertThat(idTransaction).isEqualTo(commission.getIdTransaction());
        assertThat(amount).isEqualTo(commission.getAmount());
        assertThat(date).isEqualTo(commission.getDate());

    }
}
