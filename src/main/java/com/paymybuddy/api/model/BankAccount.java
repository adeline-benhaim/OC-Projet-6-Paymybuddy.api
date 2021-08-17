package com.paymybuddy.api.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    @Column(name = "id_user")
    private int idUser;

    private String name;

    private String bic;

    private String iban;

    @OneToMany(
                    orphanRemoval = true,
                    fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bank_account")
    private List<Transfer> transferList;
}
