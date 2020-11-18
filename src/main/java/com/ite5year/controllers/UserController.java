package com.ite5year.controllers;


import com.ite5year.repositories.ApplicationUserRepository;
import org.springframework.web.bind.annotation.*;


import static com.ite5year.utils.GlobalConstants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/user")
public class UserController {
    private final ApplicationUserRepository applicationUserRepository;
    public UserController(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }
}
