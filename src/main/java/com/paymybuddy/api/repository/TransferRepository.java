package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Integer> {

    /**
     * Find a list of transfers by id user
     *
     * @param idUser of transfers sought
     * @param pageable result
     * @return a list of transfers belong to user with id informed
     */
    @Query("FROM Transfer t WHERE t.idUser = ?1 ORDER BY t.date DESC")
    Page<Transfer> findByIdUser(int idUser, Pageable pageable);

}
