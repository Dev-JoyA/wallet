package com.njasettlement.wallet.service;

import com.njasettlement.wallet.dto.request.TransactionRequestDto;
import com.njasettlement.wallet.dto.request.WalletRequestDto;
import com.njasettlement.wallet.dto.response.TransactionResponseDto;
import com.njasettlement.wallet.dto.response.WalletResponseDto;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService{

    @Override
    public WalletResponseDto createWallet (WalletRequestDto request){
        return null;
    }

    @Override
    public TransactionResponseDto updateBalance(Long walletId, TransactionRequestDto request){
        return null;
    }

    @Override
    public TransactionResponseDto transferFunds(Long walletId, TransactionRequestDto request){
        return null;
    }

    @Override
    public WalletResponseDto getOneWallet (Long walletId){
        return null;
    }
}
