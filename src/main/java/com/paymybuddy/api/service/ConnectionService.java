package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface ConnectionService {

    /**
     * Create a new connection with an other user present in the database
     *
     * @param connection information's to create
     * @return connection information's created
     */
    Connection createConnection(Connection connection);

    /**
     * Get a list of connections belong to current user
     *
     * @return a list of connections belong to current user with name and email of users connected
     */
    List<Connection> getAllConnections();

    /**
     * Get a pair with a list of connections and a long number
     *
     * @param pageable the result list
     * @return a pair with a list of connections belong to current user with name and email of users connected and the total of elements of this list
     */
    Pair<Page<Connection>, Long> getConnections(Pageable pageable);

    /**
     * Delete a connection belong to current user
     *
     * @param email belong to user connected in the connection to delete
     */
    void deleteConnection(String email);
}
