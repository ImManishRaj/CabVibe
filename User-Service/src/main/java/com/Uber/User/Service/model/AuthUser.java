package com.Uber.User.Service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthUser {
    @NotBlank(message = "Email is Mandatory")
    private  String email;
    @NotBlank(message = "password is Mandatory")
    private  String password;

}
