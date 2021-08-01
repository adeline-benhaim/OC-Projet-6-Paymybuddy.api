package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.ConnectionAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @GetMapping("/connection")
    public String connection(Model model) {
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connections", connections);
        model.addAttribute("connection", new Connection());
        return "connection";
    }

    @PostMapping("/createConnection")
    public String createConnection(@ModelAttribute Connection connection, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                connectionService.createConnection(connection);
                return "redirect:/connection";
            } catch (UserNotFoundException | ConnectionAlreadyExistException e) {
                ObjectError objectError = new ObjectError("error", e.getMessage());
                result.addError(objectError);
            }
        }
        model.addAttribute("connection", connection);
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connections", connections);
        return "connection";
    }


    @GetMapping("/createConnection")
    public String createConnection(Model model) {
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connections", connections);
        Connection connection = new Connection();
        model.addAttribute("connection", connection);
        return "connection";
    }

//    @DeleteMapping("/connection/{email}")
//    public void deleteConnectionByEmailOfUserLinked(@PathVariable("email") String email) {
//        connectionService.deleteConnection(email);
//    }

}
