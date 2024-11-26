package com.Uber.User.Service.controller;

import com.Uber.User.Service.model.AuthUser;
import com.Uber.User.Service.model.User;
import com.Uber.User.Service.service.UserService;
import com.Uber.User.Service.util.JwtUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;




    private static  final Logger logger= LoggerFactory.getLogger(UserController.class);

    @PostMapping("/addUser")
    public ResponseEntity<String> signup(@RequestBody @Valid User user) {
        try {
            User savedUser = userService.addUser(user);
            if (savedUser != null) {
                return ResponseEntity.ok("User has been saved to the database");
            }
            return ResponseEntity.badRequest().body("Unable to save user");
        } catch (IllegalArgumentException e) {
            // Return error response with custom message and BAD_REQUEST status
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Catch any other exceptions and return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

  /*  @PostMapping("/addUser")
    public ResponseEntity<String> signup(@RequestBody @Valid List<User> users) {
        try {
            // Process the list of users and save them
            List<User> savedUsers = userService.addUser(users); // Assuming addUsers is a method to save multiple users

            if (savedUsers != null && !savedUsers.isEmpty()) {
                return ResponseEntity.ok("Users have been saved to the database");
            }
            return ResponseEntity.badRequest().body("Unable to save users");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }*/




    @PostMapping("/login")
    public String login(@RequestBody AuthUser authUser) {
       return userService.login(authUser);
    }

    @PostMapping ("/validateToken")
    public ResponseEntity<String> validate(@RequestBody  String JwtToken)
    {
        String token = JwtToken.trim();
        if (jwtUtils.validateJwt(token,"jwt@mail.com"))
        {
            return ResponseEntity.ok().body("Valid Token ");
        }
        return  ResponseEntity.badRequest().body("Invalid Token");
    }

    @GetMapping("/findById")
    public Optional<User> findUserById(@RequestParam Long id) {
        return userService.findById(id);
    }

    @GetMapping("/findByEmail")
    public Optional<User> findUserByEmail(@RequestParam String email) {
        return Optional.ofNullable(userService.findByEmail(email));
    }

    @GetMapping("/hello")
    public String sayHello()
    {
        return "Hello";
    }


}
