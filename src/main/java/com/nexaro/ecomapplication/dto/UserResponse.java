package com.nexaro.ecomapplication.dto;

import com.nexaro.ecomapplication.model.Address;
import com.nexaro.ecomapplication.model.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO  address;
}