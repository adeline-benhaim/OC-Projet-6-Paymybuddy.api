package com.paymybuddy.api.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDto {

    private String name;
    private String emailOfUserLinked;
}
