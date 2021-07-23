package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.BankAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {

    /**
     * Find a list of bank accounts by id user
     *
     * @param idUser id of the user whose bank accounts are sought
     * @return list of bank account belonging to user found by id
     */
    @Query("FROM BankAccount t WHERE t.idUser = ?1 ORDER BY t.accountId DESC")
    List<BankAccount> findByIdUser(int idUser);

    /**
     * Check if a bank account exist with an id user and a bank account name
     *
     * @param idUser id of the user sought
     * @param name   of the bank account sought
     * @return true if bank account exist
     */
    boolean existsByIdUserAndName(int idUser, String name);

    /**
     * Find a bank account by id account
     * @param accountId id of the account sought
     * @return the bank account found
     */
    BankAccount findByAccountId(int accountId);

}
