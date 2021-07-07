package com.paymybuddy.api.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {

    private String name;
    private int bic;
    private int iban;
}
