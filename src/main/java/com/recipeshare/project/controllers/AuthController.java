package com.recipeshare.project.controllers;

import com.recipeshare.project.dto.RegistrationDto;
import com.recipeshare.project.models.UserEntity;
import com.recipeshare.project.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private DelegatingSecurityContextRepository securityContextRepository;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, DelegatingSecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
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
                                     Model model,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
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
        authenticateUser(user.getUsername(), user.getPassword(), request, response);

        return "redirect:/recipes?success";
    }

    private void authenticateUser(String username, String rawPassword, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, rawPassword);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
                .getContextHolderStrategy();

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);

        securityContextRepository.saveContext(context, request, response);
    }
}
