package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @GetMapping("/connection")
    public String connection(Model model) {
        List<Connection> connections = connectionService.getConnections();
        model.addAttribute("connection", connections);
        return "connection";
    }

    @PostMapping("/createConnection")
    public ModelAndView createConnection(@ModelAttribute Connection connection) {
        connectionService.createConnection(connection);
        return new ModelAndView("redirect:/connection");
    }


    @GetMapping("/createConnection")
    public String createConnection(Model model) {
        Connection connection = new Connection();
        model.addAttribute("connection", connection);
        return "connection";
    }

    @DeleteMapping("/connection/{email}")
    public void deleteConnectionByEmailOfUserLinked(@PathVariable("email") String email) {
        connectionService.deleteConnection(email);
    }

}
