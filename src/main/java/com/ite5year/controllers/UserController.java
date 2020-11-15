package com.ite5year.controllers;


import com.ite5year.models.ApplicationUser;
import com.ite5year.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.ite5year.utils.GlobalConstants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/user")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public HashMap<String, Object> signUp(@RequestBody ApplicationUser applicationUser) {
        System.out.println("Incoming user: " + applicationUser.toString());
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        userRepository.save(applicationUser);

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "successfully signed up");
        map.put("user", applicationUser);
        return map;
    }
}
