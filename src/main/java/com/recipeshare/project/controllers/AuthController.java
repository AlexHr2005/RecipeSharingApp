package com.recipeshare.project.controllers;

import com.recipeshare.project.dto.RegistrationDto;
import com.recipeshare.project.models.User;
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
        User existingUserWithUsername = userService.findByUsername(user.getUsername());
        if(existingUserWithUsername != null) {
            result.rejectValue("username", "There is already a user with this username");
        }
        User existingUserWithEmail = userService.findByEmail(user.getEmail());
        if(existingUserWithEmail != null) {
            result.rejectValue("email", "There is already a user with this email");
        }
        if(result.hasErrors()) {
            model.addAttribute("recipe", user);
            return "recipes_create";
        }
        userService.saveUser(user);
        return "redirect:/recipes?success";
    }
}
