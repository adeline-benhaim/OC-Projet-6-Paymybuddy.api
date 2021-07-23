package com.paymybuddy.api.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private int transferId;

    @Column(name = "id_bank_account")
    private int idBankAccount;

    @Column(name = "id_user")
    private int idUser;

    @Max(value=999)
    @Min(value = 1)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type")
    public TransferType transferType;

    public enum TransferType {
        DEBIT,
        CREDIT
    }

    private Timestamp date;

    private boolean success;
}
