package com.Uber.User.Service.repository;

import com.Uber.User.Service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo  extends JpaRepository<User,Long> {

User findByEmail(String email);


}
