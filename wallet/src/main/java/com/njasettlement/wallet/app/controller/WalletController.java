package com.njasettlement.wallet.app.controller;

import com.njasettlement.wallet.app.dto.request.TransactionRequestDto;
import com.njasettlement.wallet.app.dto.request.WalletRequestDto;
import com.njasettlement.wallet.app.dto.response.TransactionResponseDto;
import com.njasettlement.wallet.app.dto.response.WalletResponseDto;
import com.njasettlement.wallet.app.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Wallet API", description = "A Simple wallet operation service")
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Operation(summary = "Create a new wallet")
    @PostMapping("/create")
    public ResponseEntity<WalletResponseDto> createWallet(
            @RequestBody WalletRequestDto request
            ){
        return ResponseEntity.ok(walletService.createWallet(request));
    }

    @Operation(
            summary = "Credit or debit a wallet",
            description = "Creates a CREDIT or DEBIT transaction. Idempotency is supported."
    )
    @PostMapping("/{walletId}/balance")
    public ResponseEntity<TransactionResponseDto> updateWalletBalance(
            @PathVariable Long walletId,
            @RequestBody TransactionRequestDto request
            ){
        return ResponseEntity.ok(walletService.updateBalance(walletId, request));
    }

    @Operation(
            summary = "Transfer funds between wallets",
            description = "Debits sender and credits receiver atomically."
    )
    @PostMapping("/transfer/{walletId}")
    public ResponseEntity<TransactionResponseDto> transferFunds(
            @PathVariable Long walletId,
            @RequestBody TransactionRequestDto request
    ){
        return ResponseEntity.ok(walletService.transferFunds(walletId, request));
    }

    @Operation(summary = "Get wallet details by ID")
    @GetMapping("/one/{walletId}")
    public ResponseEntity<WalletResponseDto> findById (@PathVariable Long walletId){
        return ResponseEntity.ok(walletService.getOneWallet(walletId));
    }
}
