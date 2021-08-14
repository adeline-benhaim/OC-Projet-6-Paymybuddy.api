package com.paymybuddy.api.controller;

import com.paymybuddy.api.exception.UserAlreadyExistException;
import com.paymybuddy.api.exception.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.model.dto.PasswordDto;
import com.paymybuddy.api.service.BankAccountService;
import com.paymybuddy.api.service.CustomUserDetailsService;
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
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.paymybuddy.api.service.SecurityUtils.getIdCurrentUser;
import static com.paymybuddy.api.service.SecurityUtils.isUserConnected;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/contact")
    public String contact() {
        return "formContact";
    }

    @GetMapping("/home")
    public String home(Model model) {
        User user = userService.getUser();
        if(user.getRole().equals("ADMIN")) {
            model.addAttribute("user", user);
            return "redirect:/homeAdmin";
        }
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (isUserConnected()) return "redirect:/home";
        return "login";
    }

    @GetMapping("/")
    public ModelAndView homePage() {
        if (isUserConnected()) return new ModelAndView("redirect:/home");
        return new ModelAndView("homePage");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        customUserDetailsService.logout(request, response);
        return "homePage";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        PasswordDto passwordDto = new PasswordDto();
        model.addAttribute("passwordDto", passwordDto);
        User user = userService.getUser();
        model.addAttribute("user", user);
        List<BankAccountDto> bankAccount = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccount);
        return "profileCoordinates";
    }

    @GetMapping("/profileBankAccount")
    public String profileBankAccount(Model model) {
        PasswordDto passwordDto = new PasswordDto();
        model.addAttribute("passwordDto", passwordDto);
        User user = userService.getUser();
        model.addAttribute("user", user);
        List<BankAccountDto> bankAccount = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccount);
        return "profileBankAccount";
    }

    @GetMapping("/profileSettings")
    public String profileSetting(Model model) {
        PasswordDto passwordDto = new PasswordDto();
        model.addAttribute("passwordDto", passwordDto);
        User user = userService.getUser();
        model.addAttribute("user", user);
        List<BankAccountDto> bankAccount = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccount);
        return "profileSettings";
    }

    @PostMapping("/createUser")
    public String createUser(@Valid @ModelAttribute User user, BindingResult result, Model model, HttpServletRequest request) {

        if (!result.hasErrors()) {
            try {
                userService.createUser(user);
//                customUserDetailsService.authWithHttpServletRequest(request, user.getEmail(), user.getPassword());
                return "home";
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
        if (!result.hasErrors()) {
            try {
                userService.updatePassword(passwordDto);
                return "redirect:/profileSettings";
            } catch (UserNotFoundException e) {
                ObjectError objectError = new ObjectError("objectError", e.getMessage());
                result.addError(objectError);
                User user = userService.getUser();
                model.addAttribute("user", user);
                List<BankAccountDto> bankAccount = bankAccountService.getAllByIdUser();
                model.addAttribute("bankAccount", bankAccount);
            }
        }
        User user = userService.getUser();
        model.addAttribute("user", user);
        List<BankAccountDto> bankAccount = bankAccountService.getAllByIdUser();
        model.addAttribute("bankAccount", bankAccount);
        return "profileSettings";
    }
}
