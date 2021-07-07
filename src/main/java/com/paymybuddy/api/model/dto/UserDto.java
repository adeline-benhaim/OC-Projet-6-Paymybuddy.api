package com.paymybuddy.api.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private int zip;
    private String city;
    private String phone;

}