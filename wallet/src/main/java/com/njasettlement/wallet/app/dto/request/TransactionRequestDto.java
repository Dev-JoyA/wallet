package com.njasettlement.wallet.app.dto.request;

import com.njasettlement.wallet.app.model.TransactionType;
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
