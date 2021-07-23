package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.UserAlreadyExistException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.model.dto.PasswordDto;
import com.paymybuddy.api.service.BankAccountService;
import com.paymybuddy.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/profile")
    public String user(Model model) {
        User user = userService.getUser();
        model.addAttribute("user", user);
        List<BankAccountDto> bankAccount = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccount);
        return "profile";
    }

    @GetMapping("/home")
    public String home(Model model) {
        User user = userService.getUser();
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/")
    public String login(Model model) {
        User user = userService.getUser();
        model.addAttribute("user", user);
        return "login";
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {

        if (!result.hasErrors()) {
            try {
                userService.createUser(user);
                return "redirect:/home";
            } catch (UserAlreadyExistException e) {
                ObjectError errorEmail = new ObjectError("email", e.getMessage());
                result.addError(errorEmail);
                model.addAttribute("user", user);
                return "formNewUser";
            }
        }
        model.addAttribute("user", user);
        return "formNewUser";
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "formNewUser";
    }

    @PostMapping("/updateUser")
    public ModelAndView updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return new ModelAndView("redirect:/profile");
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute PasswordDto passwordDto, BindingResult result, Model model) {
//        if (!result.hasErrors()) {
//            try {
        userService.updatePassword(passwordDto);
        return "redirect:/profile";
//            } catch (UserNotFoundException e) {
//                ObjectError errorPassword = new ObjectError("passwordDto", e.getMessage());
//                result.addError(errorPassword);
//                model.addAttribute("passwordDto", passwordDto);
//                return "profile";
//            }
//        }
//        model.addAttribute("passwordDto", passwordDto);
//        return "profile";
    }


}
