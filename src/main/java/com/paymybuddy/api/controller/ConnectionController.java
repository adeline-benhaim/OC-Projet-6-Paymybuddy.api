package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.ConnectionAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/connections")
    public String connection(Model model, @PageableDefault(size=2) Pageable pageable) {
        String baseUri = "/connections?page=";
        Pair<Page<Connection>, Long> connectionsPair = connectionService.getConnections(pageable);
        model.addAttribute("connections", connectionsPair.getFirst());
        PaginationUtils.paginationBuilder(model, pageable, connectionsPair.getSecond(), baseUri);
        model.addAttribute("connection", new Connection());
        return "connection";
    }

    @PostMapping("/createConnection")
    public String createConnection(@ModelAttribute Connection connection, BindingResult result, Model model, Pageable pageable) {
        if (!result.hasErrors()) {
            try {
                connectionService.createConnection(connection);
                return "redirect:/connections";
            } catch (UserNotFoundException | ConnectionAlreadyExistException e) {
                ObjectError objectError = new ObjectError("error", e.getMessage());
                result.addError(objectError);
            }
        }
        model.addAttribute("connection", connection);
        Pair<Page<Connection>, Long> connections = connectionService.getConnections(pageable);
        model.addAttribute("connections", connections.getFirst());
        return "connection";
    }

}
