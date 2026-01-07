package com.njasettlement.wallet.service;

import com.njasettlement.wallet.dto.request.TransactionRequestDto;
import com.njasettlement.wallet.dto.request.WalletRequestDto;
import com.njasettlement.wallet.dto.response.TransactionResponseDto;
import com.njasettlement.wallet.dto.response.WalletResponseDto;

public interface WalletService {
    WalletResponseDto createWallet (WalletRequestDto request);
    TransactionResponseDto updateBalance(Long walletId, TransactionRequestDto request);
    TransactionResponseDto transferFunds(Long walletId, TransactionRequestDto request);
    WalletResponseDto getOneWallet (Long walletId);
}
