package com.njasettlement.wallet.app.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WalletResponseDto {
    private Long id;
    private Long balance;
    private String firstName;
    private String lastName;
    private String userName;
    private LocalDateTime createdAt;
}

