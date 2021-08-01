package com.paymybuddy.api.model.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {


    String currentPassword;
    @Length(min=8, message = "Password size must be have 8 characters minimum")
    String newPassword;
    @Length(min=8, message = "Password size must be have 8 characters minimum")
    String confirmPassword;
}
