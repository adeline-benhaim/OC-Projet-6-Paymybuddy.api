package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.exception.UserAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.PasswordDto;
import com.paymybuddy.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @InjectMocks
    DataSourceTest dataSourceTest;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        dataSourceTest.clearUserListMocked();
        dataSourceTest.createUserListMocked();
    }


    @Test
    @DisplayName("Get user by id")
    void getUserTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
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
//        Authentication authentication = Mockito.mock(Authentication.class);
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        Mockito.when(authentication.getName()).thenReturn(String.valueOf("1"));
//        Mockito.when(userRepository.findByUserId(1)).thenReturn(dataSourceTest.getUserListMocked().get(0));
//        User user = dataSourceTest.getUserListMocked().get(0);
////        User userToUpdate = User.builder().userId(1).firstName("new").build();
//
//        //WHEN
//        userService.updateUser(user);
//
//        //THEN
//        Mockito.verify(userRepository, Mockito.times(1)).save(any());
//    }

    @Test
    @DisplayName("Update user password with correct current password")
    void updateUserPasswordTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        Mockito.when(userRepository.findByUserId(1)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        PasswordDto passwordDto = PasswordDto.builder()
                .currentPassword("password1")
                .newPassword("newPassword")
                .confirmPassword("newPassword")
                .build();
        Mockito.when(passwordEncoder.matches(passwordDto.getCurrentPassword(), dataSourceTest.getUserListMocked().get(0).getPassword())).thenReturn(true);

        //WHEN
        userService.updatePassword(passwordDto);

        //THEN
        Mockito.verify(userRepository, Mockito.times(1)).save(any());

    }

    @Test
    @DisplayName("Update user password with wrong current password return user not found exception")
    void updateUserPasswordWithWringCurrentPasswordTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        Mockito.when(userRepository.findByUserId(1)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        PasswordDto passwordDto = PasswordDto.builder()
                .currentPassword("123456789")
                .newPassword("newPassword")
                .confirmPassword("newPassword")
                .build();
        Mockito.when(passwordEncoder.matches(passwordDto.getCurrentPassword(), dataSourceTest.getUserListMocked().get(0).getPassword())).thenReturn(false);

        //THEN
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(passwordDto));

    }

    @Test
    @DisplayName("Update user password with a new password different from the confirmed one, return user not found exception")
    void updateUserPasswordWithDifferentPasswordsTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        Mockito.when(userRepository.findByUserId(1)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        PasswordDto passwordDto = PasswordDto.builder()
                .currentPassword("password1")
                .newPassword("newPassword")
                .confirmPassword("otherNewPassword")
                .build();
        Mockito.when(passwordEncoder.matches(passwordDto.getCurrentPassword(), dataSourceTest.getUserListMocked().get(0).getPassword())).thenReturn(true);

        //THEN
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(passwordDto));

    }

    @Test
    @DisplayName("Delete a user with wrong password return user not found exception")
    void deleteCurrentUserByWrongPasswordTest() {

        //GIVEN
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
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
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
        int idCurrentUser = 1;
        String password = "password";
        Mockito.when(userRepository.existsByUserIdAndPassword(idCurrentUser, password)).thenReturn(true);

        //WHEN
        userService.deleteCurrentUserByPassword(password);

        //THEN
        Mockito.verify(userRepository, Mockito.times(1)).deleteByUserIdAndPassword(idCurrentUser, password);
    }
}