package com.njasettlement.wallet.dto.request;

import com.njasettlement.wallet.model.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequestDto {
    private TransactionType type;
    private Long amount;
    private Long receiverWalletId;
    private String idempotencyKey;
}
