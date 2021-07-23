package com.paymybuddy.api.service;

import com.paymybuddy.api.exception.UserAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.mapper.MapperDto;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.PasswordDto;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public User getUser() {
        logger.info("Get current user by id : {}", idCurrentUser);
        return userRepository.findByUserId(idCurrentUser);
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
                .password(currentUser.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .balance(currentUser.getBalance())
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
     * Update a current user password
     * @param passwordDto new password to update
     * @return password updated
     */
    @Override
    public User updatePassword(PasswordDto passwordDto) {
        User currentUser = userRepository.findByUserId(idCurrentUser);
        String userPassword = userRepository.findByUserId(idCurrentUser).getPassword();
        if (passwordDto.getCurrentPassword().equals(userPassword)) {
            if (passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
                User passwordToUpdate = User.builder()
                        .userId(idCurrentUser)
                        .email(currentUser.getEmail())
                        .firstName(currentUser.getFirstName())
                        .lastName(currentUser.getLastName())
                        .address(currentUser.getAddress())
                        .balance(currentUser.getBalance())
                        .zip(currentUser.getZip())
                        .city(currentUser.getCity())
                        .phone(currentUser.getPhone())
                        .bankAccounts(currentUser.getBankAccounts())
                        .connectionsUser(currentUser.getConnectionsUser())
                        .transferList(currentUser.getTransferList())
                        .transactionsTransmitter(currentUser.getTransactionsTransmitter())
                        .transactionsBeneficiary(currentUser.getTransactionsBeneficiary())
                        .password(passwordDto.getNewPassword())
                        .build();
                return userRepository.save(passwordToUpdate);
            }
            throw new UserNotFoundException("Error between new password and confirm password");
        }
        throw new UserNotFoundException("Wrong current password");
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
        if (!userRepository.existsByUserIdAndPassword(idCurrentUser, password)) {
            throw new UserNotFoundException("Unable to delete user id : " + idCurrentUser + " because password is wrong");
        }
        logger.info("User id " + idCurrentUser + " deleted");
        userRepository.deleteByUserIdAndPassword(idCurrentUser, password);
    }


}
