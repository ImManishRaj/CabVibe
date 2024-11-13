package com.Uber.User.Service.controller;

import com.Uber.User.Service.model.User;
import com.Uber.User.Service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


        @PostMapping("/addUser")
        public String signup(@RequestBody @Valid User user) {
            return userService.addUser(user);
        }

    @GetMapping("/findById")
    public Optional<User> findUserById(@RequestParam Long id) {
        return userService.findById(id);
    }


    @GetMapping("/hello")
    public String sayHello()
    {
        return "Hello";
    }


}
