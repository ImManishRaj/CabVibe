package com.Uber.User.Service.service;

import com.Uber.User.Service.model.AuthUser;
import com.Uber.User.Service.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    /*User addUser(@Valid User user);
*/
    boolean isEmailAndPhoneExist(String email,String phone);

    Optional<User> findById(Long id);

    User findByEmail(String email);

  /*  public List<User> addUser(List<User> users);*/

    User addUser(User user);

    String login(AuthUser authUser);
}
