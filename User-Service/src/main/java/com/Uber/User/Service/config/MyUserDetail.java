package com.Uber.User.Service.config;

import com.Uber.User.Service.model.User;
import com.Uber.User.Service.model.UserPrincipal;
import com.Uber.User.Service.repository.UserRepo;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class MyUserDetail implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User found=userRepo.findByEmail(username);
        if (found==null)
        {
            throw new UsernameNotFoundException("Email is not Present");
        }
        return new UserPrincipal(found);
    }
}
