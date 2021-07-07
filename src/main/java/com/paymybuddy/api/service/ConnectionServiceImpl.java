package com.paymybuddy.api.service;

import com.paymybuddy.api.mapper.MapperDto;
import com.paymybuddy.api.exception.ConnectionAlreadyExistException;
import com.paymybuddy.api.exception.ConnectionNotFoundException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.ConnectionDto;
import com.paymybuddy.api.repository.ConnectionRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);

    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    UserRepository userRepository;

    int idCurrentUser = User.getCurrentUser();

    /**
     * Check if current user connections contains user's email
     *
     * @param email user's email to check
     * @return true if email belong to a user linked
     */
    private boolean isUserLinked(String email) {
        logger.info("Check if is a user linked to current user connection");
        return connectionRepository.existsByIdUserAndEmailOfUserLinked(idCurrentUser, email);
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
        User currentUser = userRepository.findByUserId(idCurrentUser);
        String currentUserEmail = currentUser.getEmail();
        if (currentUserEmail.equals(connection.getEmailOfUserLinked())) {
            logger.error("Unable to create connection with the current user email");
            throw new UserNotFoundException("Unable to create connection with the current user email");
        } else if (!isExistingUser(connection.getEmailOfUserLinked())) {
            logger.error("Unable to create connection because email doesn't exist in database");
            throw new UserNotFoundException("Unable to create this connection because email : " + connection.getEmailOfUserLinked() + " doesn't exist in database");
        } else if (!isUserLinked(connection.getEmailOfUserLinked())) {
            logger.info("New connection created");
            Connection newConnection = Connection.builder()
                    .idUser(idCurrentUser)
                    .emailOfUserLinked(connection.getEmailOfUserLinked())
                    .name(connection.getName())
                    .build();
            return connectionRepository.save(newConnection);
        }
        logger.error("Unable to create connection because email already exist in connections of current user");
        throw new ConnectionAlreadyExistException("Unable to create this connection because email : " + connection.getEmailOfUserLinked() + " already exist in connections of user id : " + idCurrentUser);

    }

    /**
     * Get a list of connections belonging to current user
     *
     * @return a list of connections belonging to current user with name and email of users linked
     */
    @Override
    public List<ConnectionDto> getConnections() {
        logger.info("Get a list of connections");
        List<Connection> connections = connectionRepository.findByIdUser(idCurrentUser);
        return connections
                .stream()
                .map(MapperDto::convertToConnectionDto)
                .collect(Collectors.toList());
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
            throw new ConnectionNotFoundException("Unable to delete this connection because email : " + email + " doesn't exist in connections of user id : " + idCurrentUser);
        }
        connectionRepository.deleteByIdUserAndEmailOfUserLinked(idCurrentUser, email);
        logger.info("Connection deleted");
    }
}
