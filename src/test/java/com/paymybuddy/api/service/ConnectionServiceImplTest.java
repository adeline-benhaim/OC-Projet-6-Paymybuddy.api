package com.paymybuddy.api.service;

import com.paymybuddy.api.config.DataSourceTest;
import com.paymybuddy.api.exception.ConnectionAlreadyExistException;
import com.paymybuddy.api.exception.ConnectionNotFoundException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.repository.ConnectionRepository;
import com.paymybuddy.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceImplTest {

    @Mock
    ConnectionRepository connectionRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ConnectionServiceImpl connectionService;
    @InjectMocks
    DataSourceTest dataSourceTest;

    @BeforeEach
    void init() {
        dataSourceTest.clearConnectionListMocked();
        dataSourceTest.clearUserListMocked();
        dataSourceTest.createConnectionListMocked();
        dataSourceTest.createUserListMocked();
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authentication.getName()).thenReturn("1");
    }

    int idCurrentUser = 1;

    @Test
    @DisplayName("Create a new connection between the current user and an other user present in the database")
    void createConnectionTest() {

        //GIVEN
        String email = "email3";
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        when(connectionRepository.existsByIdUserAndEmailOfUserLinked(idCurrentUser, email)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        Connection newConnection = Connection.builder()
                .idUser(idCurrentUser)
                .emailOfUserLinked(email)
                .build();

        //WHEN
        connectionService.createConnection(newConnection);

        //THEN
        verify(connectionRepository, Mockito.times(1)).save(any());
    }

    @Test
    @DisplayName("Create a new connection between the current user and an other user already connected with the current user return connection already exist exception")
    void createConnectionWithUserAlreadyConnectedTest() {

        //GIVEN
        Connection newConnection = Connection.builder()
                .emailOfUserLinked("email2")
                .build();
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        when(connectionRepository.existsByIdUserAndEmailOfUserLinked(idCurrentUser, newConnection.getEmailOfUserLinked())).thenReturn(true);
        when(userRepository.existsByEmail(newConnection.getEmailOfUserLinked())).thenReturn(true);

        //THEN
        assertThrows(ConnectionAlreadyExistException.class, () -> connectionService.createConnection(newConnection));
    }

    @Test
    @DisplayName("Create a new connection between the current user and an other user unknown in the database return user not found exception")
    void createConnectionWithUnknownEmailTest() {

        //GIVEN
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        Connection connection1 = Connection.builder()
                .emailOfUserLinked("email5")
                .build();

        //THEN
        assertThrows(UserNotFoundException.class, () -> connectionService.createConnection(connection1));
    }

    @Test
    @DisplayName("Create a new connection between the current user and the current user email return user not found exception")
    void createConnectionWithCurrentUserEmailTest() {

        //GIVEN
        when(userRepository.findByUserId(idCurrentUser)).thenReturn(dataSourceTest.getUserListMocked().get(0));
        Connection connection1 = Connection.builder()
                .emailOfUserLinked("email1")
                .build();

        //THEN
        assertThrows(UserNotFoundException.class, () -> connectionService.createConnection(connection1));
    }

    @Test
    @DisplayName("Get a list of connections belong to current user")
    void getConnectionsTest() {

        //GIVEN
        Pageable pageable = PageRequest.of(0, 4);
        List<Connection> connectionList = dataSourceTest.getConnectionListMocked();
        when(connectionRepository.findByIdUserOrderByConnectionIdDesc(idCurrentUser, pageable)).thenReturn(connectionList);

        //WHEN
        List<Connection> connection = connectionService.getConnections(pageable);

        //THEN
        assertEquals(connection.get(0).getEmailOfUserLinked(), dataSourceTest.getConnectionListMocked().get(0).getEmailOfUserLinked());
        assertEquals("connection1", connection.get(0).getName());
    }

    @Test
    @DisplayName("Delete a connection belong to current user")
    void deleteConnectionTest() {

        //GIVEN
        String emailOfUserLinked = "email2";
        when(connectionRepository.existsByIdUserAndEmailOfUserLinked(idCurrentUser, emailOfUserLinked)).thenReturn(true);

        //WHEN
        connectionService.deleteConnection("email2");

        //THEN
        verify(connectionRepository, Mockito.times(1)).deleteByIdUserAndEmailOfUserLinked(1, emailOfUserLinked);

    }

    @Test
    @DisplayName("Delete a connection which does not exist in the connection of the current user return connection not found exception")
    void deleteUnknownConnectionTest() {

        //GIVEN
        String emailOfUserLinked = "email3";
        when(connectionRepository.existsByIdUserAndEmailOfUserLinked(idCurrentUser, emailOfUserLinked)).thenReturn(false);

        //THEN
        assertThrows(ConnectionNotFoundException.class, () -> connectionService.deleteConnection("email3"));

    }
}
