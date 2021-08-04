package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.ConnectionAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.Connection;
import com.paymybuddy.api.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    int page = 0;
    int size = 4;

    @GetMapping("/connections")
    public String connection(Model model, @RequestParam(name= "page", defaultValue = "0") int page, Pageable pageable) {
        if(pageable.getPageNumber()>=1) {
            int previousPage = pageable.getPageNumber()-1;
            String previousPageUrl = "/connections?page="+previousPage;
            model.addAttribute("previousPageUrl", previousPageUrl);
        }
        int nextPage = pageable.getPageNumber()+1;
        String nextPageUrl = "/connections?page="+nextPage;
        model.addAttribute("nextPageUrl", nextPageUrl);
        List<Connection> connections = connectionService.getConnections(PageRequest.of(page, size));
        model.addAttribute("connections", connections);
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
        List<Connection> connections = connectionService.getConnections(pageable);
        model.addAttribute("connections", connections);
        return "connection";
    }


    @GetMapping("/createConnection")
    public String createConnection(Model model, Pageable pageable) {
        List<Connection> connections = connectionService.getConnections(pageable);
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
