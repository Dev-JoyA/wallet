package com.njasettlement.wallet.app.service;

import com.njasettlement.wallet.app.dto.request.TransactionRequestDto;
import com.njasettlement.wallet.app.dto.request.WalletRequestDto;
import com.njasettlement.wallet.app.dto.response.TransactionResponseDto;
import com.njasettlement.wallet.app.dto.response.WalletResponseDto;

public interface WalletService {
    WalletResponseDto createWallet (WalletRequestDto request);
    TransactionResponseDto updateBalance(Long walletId, TransactionRequestDto request);
    TransactionResponseDto transferFunds(Long walletId, TransactionRequestDto request);
    WalletResponseDto getOneWallet (Long walletId);
}
