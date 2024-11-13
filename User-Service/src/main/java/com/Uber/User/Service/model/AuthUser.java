package com.Uber.User.Service.model;

import jakarta.validation.constraints.NotBlank;

public class AuthUser {
    @NotBlank
    private  String email;
    @NotBlank
    private  String password;

}
