package com.Uber.User.Service.service;

import com.Uber.User.Service.model.User;
import jakarta.validation.Valid;

import java.util.Optional;

public interface UserService {

    String addUser(@Valid User user);

    boolean isEmailAndPhoneExist(String email,String phone);

    Optional<User> findById(Long id);
}
