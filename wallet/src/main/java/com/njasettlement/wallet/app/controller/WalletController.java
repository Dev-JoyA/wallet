package com.njasettlement.wallet.app.controller;

import com.njasettlement.wallet.app.dto.request.TransactionRequestDto;
import com.njasettlement.wallet.app.dto.request.WalletRequestDto;
import com.njasettlement.wallet.app.dto.response.TransactionResponseDto;
import com.njasettlement.wallet.app.dto.response.WalletResponseDto;
import com.njasettlement.wallet.app.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<WalletResponseDto> createWallet(
            @RequestBody WalletRequestDto request
            ){
        return ResponseEntity.ok(walletService.createWallet(request));
    }

    @PostMapping("/{walletId}/balance")
    public ResponseEntity<TransactionResponseDto> updateWalletBalance(
            @PathVariable Long walletId,
            @RequestBody TransactionRequestDto request
            ){
        return ResponseEntity.ok(walletService.updateBalance(walletId, request));
    }

    @PostMapping("/transfer/{walletId}")
    public ResponseEntity<TransactionResponseDto> transferFunds(
            @PathVariable Long walletId,
            @RequestBody TransactionRequestDto request
    ){
        return ResponseEntity.ok(walletService.transferFunds(walletId, request));
    }

    @GetMapping("/one/{walletId}")
    public ResponseEntity<WalletResponseDto> findById (@PathVariable Long walletId){
        return ResponseEntity.ok(walletService.getOneWallet(walletId));
    }
}
