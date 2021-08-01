package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Chek if user exist with an email
     *
     * @param Email to chek
     * @return true if user existing
     */
    boolean existsByEmail(String Email);

    /**
     * Chek if user exist with a ID and a password
     *
     * @param userId ID user to chek
     * @param password user to chek
     * @return true if user existing
     */
    boolean existsByUserIdAndPassword(int userId, String password);

    /**
     * Find user by their ID
     *
     * @param userId ID of the user to find
     * @return user found
     */
    User findByUserId(int userId);

    /**
     * Find user by their email
     * @param email of the user to find
     * @return user found
     */
    User findUserByEmail(String email);

    /**
     * Delete user with their ID and password
     *
     * @param userId ID of the user to delete
     * @param password of the user to delete
     */
    @Transactional
    void deleteByUserIdAndPassword(int userId, String password);

}
