package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.Commission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CommissionRepository extends CrudRepository<Commission, Integer> {

    /**
     * Find list of commissions between two dates
     * @param from start date
     * @param to end date
     * @return list of commissions found between two dates
     */
    @Query("select c from Commission c where c.date >= :to and c.date <= :from ")
    List<Commission> findByDateBetween(@Param("from") Timestamp from, @Param("to") Timestamp to);

}

