package com.paymybuddy.api.repository;

import com.paymybuddy.api.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    boolean existsByEmail(String Email);

    boolean existsByUserIdAndPassword(int userId, String password);

//    User findByEmail(String email);

    User findByUserId(int userId);

    @Transactional
    void deleteByUserIdAndPassword(int userId, String password);

}
