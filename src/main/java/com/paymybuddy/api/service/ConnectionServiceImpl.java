package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.ConnectionAlreadyExistException;
import com.paymybuddy.api.exception.ConnectionNotFoundException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repository.ConnectionRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.paymybuddy.api.service.SecurityUtils.getIdCurrentUser;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Check if current user connections contains user's email
     *
     * @param email user's email to check
     * @return true if email belong to a user linked
     */
    private boolean isUserLinked(String email) {
        logger.info("Check if is a user linked to current user connection");
        return connectionRepository.existsByIdUserAndEmailOfUserLinked(getIdCurrentUser(), email);
    }

    /**
     * Check if the email belongs to a user present in the database
     *
     * @param email user's email to check
     * @return true if user's email is present in the database
     */
    private boolean isExistingUser(String email) {
        logger.info("Check if is an existing user");
        return userRepository.existsByEmail(email);
    }

    /**
     * Create a new connection between the current user and an other user present in the database
     *
     * @param connection information's to create
     * @return connection information's created
     */
    @Override
    @Transactional
    public Connection createConnection(Connection connection) {
        logger.info("Create a new connection");
        User currentUser = userRepository.findByUserId(getIdCurrentUser());
        String currentUserEmail = currentUser.getEmail();
        if (currentUserEmail.equals(connection.getEmailOfUserLinked())) {
            logger.error("Unable to create connection with the current user email");
            throw new UserNotFoundException("Unable to create connection with your own email");
        } else if (!isExistingUser(connection.getEmailOfUserLinked())) {
            logger.error("Unable to create connection because email doesn't exist in database");
            throw new UserNotFoundException("Unable to create this connection because email : " + connection.getEmailOfUserLinked() + " doesn't exist");
        } else if (!isUserLinked(connection.getEmailOfUserLinked())) {
            logger.info("New connection created");
            Connection newConnection = Connection.builder()
                    .idUser(getIdCurrentUser())
                    .emailOfUserLinked(connection.getEmailOfUserLinked())
                    .name(connection.getName())
                    .build();
            return connectionRepository.save(newConnection);
        }
        logger.error("Unable to create connection because email already exist in connections of current user");
        throw new ConnectionAlreadyExistException("Unable to create this connection because email : " + connection.getEmailOfUserLinked() + " already exist in your connections");

    }

    /**
     * Get a list of connections belong to current user
     *
     * @return a list of connections belong to current user with name and email of users connected
     */
    @Override
    public List<Connection> getAllConnections() {
        logger.info("Get a list of all connections");
        return connectionRepository.findByIdUserOrderByConnectionIdDesc(getIdCurrentUser());
    }

    /**
     * Get a pair with a list of connections and a long number
     *
     * @param pageable the result list
     * @return a pair with a list of connections belong to current user with name and email of users connected and the total of elements of this list
     */
    @Override
    public Pair<Page<Connection>, Long> getConnections(Pageable pageable) {
        logger.info("Get a list of connections");
        Page<Connection> connectionPage = connectionRepository.findByIdUserOrderByConnectionIdDesc(getIdCurrentUser(), pageable);
        return Pair.of(connectionPage, connectionPage.getTotalElements());
    }

    /**
     * Delete a connection belong to current user
     *
     * @param email belong to user linked in the connection to delete
     */
    @Override
    @Transactional
    public void deleteConnection(String email) {
        logger.info("Delete an existing connection");
        if (!isUserLinked(email)) {
            logger.error("Unable to delete connection because email doesn't exist in connections of current user");
            throw new ConnectionNotFoundException("Unable to delete this connection because email : " + email + " doesn't exist in connections of user id : " + getIdCurrentUser());
        }
        connectionRepository.deleteByIdUserAndEmailOfUserLinked(getIdCurrentUser(), email);
        logger.info("Connection deleted");
    }
}
