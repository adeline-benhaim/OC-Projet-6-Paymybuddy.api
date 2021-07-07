package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.model.dto.ConnectionDto;
import com.paymybuddy.api.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @GetMapping("/connection")
    public List<ConnectionDto> getConnectionByCurrentUserId() {
        return connectionService.getConnections();
    }

    @PostMapping("/connection")
    public Connection createConnection(@RequestBody Connection connection) {
        return connectionService.createConnection(connection);
    }

    @DeleteMapping("/connection/{email}")
    public void deleteConnectionByEmailOfUserLinked(@PathVariable("email") String email) {
        connectionService.deleteConnection(email);
    }

}
