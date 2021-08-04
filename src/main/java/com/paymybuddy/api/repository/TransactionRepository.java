package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    /**
     * Find a list with all transactions by transmitter user and beneficiary user
     * @param idTransmitter id of the transmitting user of the transaction
     * @param idBeneficiary id of the beneficiary user of the transaction
     * @return a list with all transactions by transmitter user and beneficiary user
     */
    Page<Transaction> findByIdTransmitterOrIdBeneficiaryOrderByDateDesc(int idTransmitter, int idBeneficiary, Pageable pageable);

}
