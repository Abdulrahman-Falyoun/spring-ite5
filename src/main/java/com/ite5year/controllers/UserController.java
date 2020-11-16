package com.ite5year.controllers;


import com.ite5year.models.ApplicationUser;
import com.ite5year.repositories.ApplicationUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.ite5year.utils.GlobalConstants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/user")
public class UserController {
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public HashMap<String, Object> signUp(@RequestBody ApplicationUser applicationUser) {
        System.out.println("Incoming user: " + applicationUser.toString());
        applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
        applicationUserRepository.save(applicationUser);

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "successfully signed up");
        map.put("user", applicationUser);
        return map;
    }


    @PostMapping("/login")
    public HashMap<String, Object> logIn(@RequestBody ApplicationUser applicationUser) {
        System.out.println("Incoming user: " + applicationUser.toString());
        HashMap<String, Object> map = new HashMap<>();
        ApplicationUser user = applicationUserRepository.findByUsername(applicationUser.getUsername()).get();

        if(bCryptPasswordEncoder.matches(applicationUser.getPassword(), user.getPassword())) {
            map.put("message", "successfully logged up");
            map.put("user", user);
            return map;
        } else {
            map.put("message", "failed to log in");
            map.put("user", applicationUser);
            return map;
        }
    }




}
