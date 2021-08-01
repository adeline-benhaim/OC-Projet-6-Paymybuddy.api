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
@Table(name = "commission")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commission_id")
    private int commissionId;

    @Column(name = "id_transmitter")
    private int idTransmitter;

    @Column(name = "id_beneficiary")
    private int idBeneficiary;

    private double amount;

    private Timestamp date;

}
