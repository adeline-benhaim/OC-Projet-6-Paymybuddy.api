package com.paymybuddy.api.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @Column(name = "id_connection")
    private int idConnection;

    @Column(name = "id_transmitter")
    private int idTransmitter;

    @Column(name = "id_beneficiary")
    private int idBeneficiary;

    @Column(name = "connection_name")
    private String connectionName;

    private String description;

    private double amount;

    private Timestamp date;

    private boolean success;
}
