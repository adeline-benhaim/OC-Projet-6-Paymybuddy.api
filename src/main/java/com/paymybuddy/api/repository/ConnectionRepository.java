package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.Connection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, Integer> {

    /**
     * Find a list of connections by id user
     *
     * @param idUser of connections sought
     * @return a list of connections belong to user with id informed
     */
    List<Connection> findByIdUserOrderByConnectionIdDesc(int idUser, Pageable pageable);

    /**
     * Find a list of connections by id user
     *
     * @param idUser of connections sought
     * @return a list of connections belong to user with id informed
     */
    List<Connection> findByIdUserOrderByConnectionIdDesc(int idUser);

    /**
     * Find a connection by id user and email user connected
     * @param idUser id user of current user
     * @param emailOfUserLinked email of user linked to the connection
     * @return connection found
     */
    Connection findByIdUserAndEmailOfUserLinked(int idUser, String emailOfUserLinked);

    /**
     * Check if connection exist between a user id(first user) and a user email(second user)
     * @param idUser user's id from the first user to check
     * @param emailOfUserLinked user's email from the second user to check
     * @return true if connection exist between the two users
     */
    boolean existsByIdUserAndEmailOfUserLinked(int idUser, String emailOfUserLinked);

    /**
     * Delete a connection found by id used and email of user linked
     * @param idUser id of current user
     * @param emailOfUserLinked email of user linked in the connection
     */
    @Transactional
    void deleteByIdUserAndEmailOfUserLinked(int idUser, String emailOfUserLinked);

}
