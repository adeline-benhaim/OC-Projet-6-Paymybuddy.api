package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.Transfer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Integer> {

    /**
     * Find a list of transfers by id user
     *
     * @param idUser of transfers sought
     * @return a list of transfers belong to user with id informed
     */
    @Query("FROM Transfer t WHERE t.idUser = ?1 ORDER BY t.date DESC")
    List<Transfer> findByIdUser(int idUser);

}
