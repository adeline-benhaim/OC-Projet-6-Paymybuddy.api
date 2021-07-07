package com.paymybuddy.api.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private String email;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private int balance;

    private String address;

    private int zip;

    private String city;

    private String phone;

//    private String role;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_user")
    private List<BankAccount> bankAccounts;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_user")
    private List<Connection> connectionsUser;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_user")
    private List<Transfer> transferList;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_transmitter")
    private List<Transaction> transactionsTransmitter;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "id_beneficiary")
    private List<Transaction> transactionsBeneficiary;

    public static int getCurrentUser(){
        return 1;
    }
}
