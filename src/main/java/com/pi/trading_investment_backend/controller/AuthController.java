package com.pi.trading_investment_backend.controller;


import com.pi.trading_investment_backend.model.User;
import com.pi.trading_investment_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            // Remove password before sending response
            user.setPassword(null);
            return user;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
