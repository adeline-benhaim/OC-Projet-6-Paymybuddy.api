package com.paymybuddy.api.service;

import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.UserDto;

import java.util.Optional;

public interface UserService {

    /**
     * Get the current user
     *
     * @return the user sought if it exists in the database
     */
    Optional<User> getUser();

    /**
     * Save a new user or update existing user
     * @param user to save
     * @return saved user information
     */
    User createUser(User user);

    /**
     * Update an existing user information
     *
     * @param user to update
     * @return updated user information
     */
    UserDto updateUser(User user);

    /**
     * Delete current user by password
     * @param password of the user to delete
     */
    void deleteCurrentUserByPassword(String password);

}
