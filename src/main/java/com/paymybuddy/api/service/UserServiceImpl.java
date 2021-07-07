package com.paymybuddy.api.service;

import com.paymybuddy.api.mapper.MapperDto;
import com.paymybuddy.api.exception.UserAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    int idCurrentUser = User.getCurrentUser();

    /**
     * Get the current user
     *
     * @return the user sought if it exists in the database
     */
    @Override
    public Optional<User> getUser() {
        logger.info("Get current user by id : {}", idCurrentUser);
        return userRepository.findById(idCurrentUser);
    }

    /**
     * Create a new user
     *
     * @param user to save
     * @return saved user information
     */
    @Override
    @Transactional
    public User createUser(User user) {
        logger.info("Create new user");
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Unable to create user because email : " + user.getEmail() + " already exist");
            throw new UserAlreadyExistException("Unable to create user because email : " + user.getEmail() + " already exist");
        }
        logger.info("New user created");
        return userRepository.save(user);
    }

    /**
     * Update an existing user information
     *
     * @param user to update
     * @return updated user information
     */
    @Override
    @Transactional
    public UserDto updateUser(User user) {
        logger.info("Create new user");
        User currentUser = userRepository.findByUserId(idCurrentUser);
        User userToUpdate = User.builder()
                .userId(idCurrentUser)
                .email(currentUser.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .zip(user.getZip())
                .city(user.getCity())
                .phone(user.getPhone())
                .bankAccounts(currentUser.getBankAccounts())
                .connectionsUser(currentUser.getConnectionsUser())
                .transferList(currentUser.getTransferList())
                .transactionsTransmitter(currentUser.getTransactionsTransmitter())
                .transactionsBeneficiary(currentUser.getTransactionsBeneficiary())
                .build();
        User user1 = userRepository.save(userToUpdate);
        logger.info("New user created");
        return MapperDto.convertToUserDto(user1);
    }

    /**
     * Delete current user with their password
     *
     * @param password of the user to delete
     */
    @Override
    @Transactional
    public void deleteCurrentUserByPassword(String password) {
        logger.info("Delete an existing user");
        if(!userRepository.existsByUserIdAndPassword(idCurrentUser, password)) {
            throw new UserNotFoundException("Unable to delete user id : " + idCurrentUser + " because password is wrong");
        }
        logger.info("User id " + idCurrentUser + " deleted");
        userRepository.deleteByUserIdAndPassword(idCurrentUser, password);
    }


}
