package com.assessment.controller;

import com.assessment.entity.User;
import com.assessment.repository.UserRepository;
import com.assessment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepo;

    @PostMapping("/login")
    public User login(@RequestBody User u){
        return userRepo.findByUsername(u.getUsername())
                .filter(x -> x.getPassword().equals(u.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid login"));
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return authService.createUser(user);
    }

}