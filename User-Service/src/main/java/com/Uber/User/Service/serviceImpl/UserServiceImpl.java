package com.Uber.User.Service.serviceImpl;


import com.Uber.User.Service.model.User;
import com.Uber.User.Service.repository.UserRepo;
import com.Uber.User.Service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;


    @Override
    public String addUser( User user) {
        if (isEmailAndPhoneExist(user.getEmail(), user.getPhone())) {
            throw new IllegalArgumentException("Email or phone number already Exist. Please use a different one.");
        }/*
        String EncodedPassword=securityConfig.passwordEncoder().encode(user.getPassword());
        user.setPassword(EncodedPassword);*/
        userRepository.save(user);
        return "User added SuccessFully";
    }

    @Override
    public boolean isEmailAndPhoneExist(String email,String phone) {
        User userExample=new User();
        userExample.setEmail(email);
        userExample.setPhone(phone);


        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnorePaths("id", "name", "password");

        Example<User> example = Example.of(userExample, matcher);

        return userRepository.exists(example);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
