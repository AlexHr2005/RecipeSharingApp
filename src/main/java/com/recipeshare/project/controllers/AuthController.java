package com.recipeshare.project.controllers;

import com.recipeshare.project.dto.RegistrationDto;
import com.recipeshare.project.models.UserEntity;
import com.recipeshare.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto registrationDto = new RegistrationDto();
        model.addAttribute("user", registrationDto);
        return "register";
    }

    @PostMapping("/register")
    public String saveRegisteredUser(@Valid @ModelAttribute("user") RegistrationDto user,
                                     BindingResult result,
                                     Model model) {
        UserEntity existingUserWithUsername = userService.findByUsername(user.getUsername());
        if(existingUserWithUsername != null) {
            result.rejectValue("username", "username.used");
        }
        UserEntity existingUserWithEmail = userService.findByEmail(user.getEmail());
        if(existingUserWithEmail != null) {
            result.rejectValue("email", "password.used");
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/recipes?success";
    }
}
