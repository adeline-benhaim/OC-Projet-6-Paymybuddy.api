package com.paymybuddy.api.mapper;

import com.paymybuddy.api.model.BankAccount;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BankAccountDto;
import com.paymybuddy.api.model.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapperDto {

    public static BankAccountDto convertToBankAccountDto(BankAccount bankAccount) {
        return BankAccountDto.builder()
                .accountId(bankAccount.getAccountId())
                .name(bankAccount.getName())
                .bic(bankAccount.getBic())
                .iban(bankAccount.getIban())
                .build();
    }

    public static UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .zip(user.getZip())
                .city(user.getCity())
                .phone(user.getPhone())
                .build();
    }
}
