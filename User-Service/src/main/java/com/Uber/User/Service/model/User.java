package com.Uber.User.Service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.sql.rowset.serial.SerialStruct;

@Entity
@Data
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NotBlank(message = "Name can not be Blank")
   private String name;
   @NotBlank(message = "Email is Mandatory")
   private String email;
   @NotBlank(message = "Password is Mandatory")
   private String password;
   @NotBlank(message = "Phone is Mandatory")
   private String phone;

}
