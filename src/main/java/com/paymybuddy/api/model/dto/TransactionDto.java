package com.paymybuddy.api.model.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private int idTransmitter;
    private int idBeneficiary;
    private String emailBeneficiary;
    private String description;

    @Max(value=999)
    @Min(value = 1)
    private double amount;
}
