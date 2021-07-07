package com.paymybuddy.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "connection")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connection_id")
    private int connectionId;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "email_of_user_linked")
    private String emailOfUserLinked;

    private String name;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id_connection")
    private List<Transaction> transactionList;
}
