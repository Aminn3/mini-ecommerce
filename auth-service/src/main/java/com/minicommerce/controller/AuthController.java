package com.minicommerce.controller;


import com.minicommerce.dto.LoginRequest;
import com.minicommerce.dto.RegisterRequest;
import com.minicommerce.entity.User;
import com.minicommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registration) {

        User user = new User();

        user.setName(registration.getName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));

        userRepository.save(user);

        return "Register Success";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        Optional<User> optionalUser= userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {
            return "User not found";
        }

        User user =  optionalUser.get();

        if(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
            return "Login Success";
        }
        return "Wrong Password";

    }
}
