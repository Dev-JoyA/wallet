package com.njasettlement.wallet.dto.response;

import com.njasettlement.wallet.model.TransactionStatus;
import com.njasettlement.wallet.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponseDto {
    private Long transactionId;
    private Long amount;
    private String idempotencyKey;
    private Long walletId;
    private Long receiverWalletId;
    private String to;
    private String from;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}
