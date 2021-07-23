package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.exception.UserAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repository.ConnectionRepository;
import com.paymybuddy.api.repository.TransactionRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @InjectMocks
    DataSourceTest dataSourceTest;

    @BeforeEach
    void init() {
        dataSourceTest.clearUserListMocked();
        dataSourceTest.createUserListMocked();
    }

    @Test
    @DisplayName("Get user by id")
    void getUserTest() {

        //GIVEN
        int idCurrentUser = 1;

        //WHEN
        userService.getUser();

        //THEN
        Mockito.verify(userRepository, Mockito.times(1)).findByUserId(idCurrentUser);

    }

    @Test
    @DisplayName("Create a new user with an email already existing in the database return user already exist exception")
    void createUserAlreadyExistingTest() {

        //GIVEN
        User user = User.builder().email("email1").build();
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        //THEN
        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(user));
    }

    @Test
    @DisplayName("Create a new user")
    void createNewUserTest() {

        //GIVEN
        User user = User.builder().email("email4").build();
        Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        //WHEN
        userService.createUser(user);

        //THEN
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

//    @Test
//    @DisplayName("Update user information")
//    void updateUserTest() {
//
//        //GIVEN
//        int idCurrentUser = 1;
//        Mockito.when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
//        User user = dataSourceTest.getUserListMocked().get(0);
//        User user = User.builder().userId(1).password("newPassword").build();
//
//        //WHEN
//        userService.updateUser(user);
//
//        //THEN
//        Mockito.verify(userRepository, Mockito.times(1)).save(any());
//    }

    @Test
    @DisplayName("Delete a user with wrong password return user not found exception")
    void deleteCurrentUserByWrongPasswordTest() {

        //GIVEN
        int idCurrentUser = 1;
        String password = "password";
        Mockito.when(userRepository.existsByUserIdAndPassword(idCurrentUser, password)).thenReturn(false);

        //THEN
        assertThrows(UserNotFoundException.class, () -> userService.deleteCurrentUserByPassword(password));
    }

    @Test
    @DisplayName("Delete a user")
    void deleteCurrentUserTest() {

        //GIVEN
        int idCurrentUser = 1;
        String password = "password";
        Mockito.when(userRepository.existsByUserIdAndPassword(idCurrentUser, password)).thenReturn(true);

        //WHEN
        userService.deleteCurrentUserByPassword(password);

        //THEN
        Mockito.verify(userRepository, Mockito.times(1)).deleteByUserIdAndPassword(idCurrentUser, password);
    }
}