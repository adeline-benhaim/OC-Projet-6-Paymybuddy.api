package com.paymybuddy.api.service;

import com.paymybuddy.api.model.Connection;
import org.springframework.data.domain.Pageable;

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
    List<Connection> getConnections(Pageable pageable);

    /**
     * Delete a connection belong to current user
     *
     * @param email belong to user connected in the connection to delete
     */
    void deleteConnection(String email);
}
