package com.paymybuddy.api.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {

    private String name;
    private String bic;
    private String iban;
    private int accountId;
}
