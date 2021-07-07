package com.paymybuddy.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    @Column(name = "id_user")
    private int idUser;

    private String name;

    private int bic;

    private int iban;

    @OneToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bank_account")
    private List<Transfer> transferList;
}
