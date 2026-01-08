package com.njasettlement.wallet.app.service;

import com.njasettlement.wallet.app.dto.request.TransactionRequestDto;
import com.njasettlement.wallet.app.dto.request.WalletRequestDto;
import com.njasettlement.wallet.app.dto.response.TransactionResponseDto;
import com.njasettlement.wallet.app.dto.response.WalletResponseDto;
import com.njasettlement.wallet.app.model.*;
import com.njasettlement.wallet.app.repository.IdempotencyKeyRepository;
import com.njasettlement.wallet.app.repository.WalletRepository;
import com.njasettlement.wallet.app.repository.WalletTransactionsRepository;
import com.njasettlement.wallet.middleware.exception.error.TransactionsException;
import com.njasettlement.wallet.middleware.exception.error.WalletException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionsRepository transactionsRepository;

    @Autowired
    private IdempotencyKeyRepository idempotencyRepository;

    @Override
    public WalletResponseDto createWallet(WalletRequestDto request) {
        Optional<Wallet> existingWallet = walletRepository.findByUserName(request.getUserName());
        if (existingWallet.isPresent()) {
            throw new WalletException("User already exists");
        }

        Wallet wallet = new Wallet();

        if (request.getFirstName() != null) {
            wallet.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            wallet.setLastName(request.getLastName());
        }

        if (request.getUserName() == null || request.getUserName().isBlank()) {
            throw new WalletException("Username is required");
        }
        wallet.setUserName(request.getUserName());

        wallet.setBalance(0L);


        Wallet savedWallet = walletRepository.save(wallet);

        WalletResponseDto response = new WalletResponseDto();
        response.setId(savedWallet.getId());
        response.setBalance(savedWallet.getBalance());
        response.setFirstName(savedWallet.getFirstName());
        response.setLastName(savedWallet.getLastName());
        response.setUserName(savedWallet.getUserName());
        response.setCreatedAt(savedWallet.getCreatedAt());

        return response;
    }


    @Override
    @Transactional
    public TransactionResponseDto updateBalance(Long walletId, TransactionRequestDto request) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException("No wallet found with id: " + walletId));

        Long amount = request.getAmount();
        if (amount == null || amount <= 0) {
            throw new TransactionsException("Amount must be greater than 0");
        }

        String idempotencyKeyValue = request.getIdempotencyKey();
        if (idempotencyKeyValue != null && !idempotencyKeyValue.isBlank()) {
            Optional<IdempotencyKey> existingKey = idempotencyRepository.findByIkeyAndWallet_Id(
                    idempotencyKeyValue, walletId);

            if (existingKey.isPresent()) {
                WalletTransaction existingTransaction = existingKey.get().getTransaction();
                return buildTransactionResponse(existingTransaction, existingKey.get().getIkey());
            }
        } else {
            idempotencyKeyValue = UUID.randomUUID().toString();
        }

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        if(request.getReceiverWalletId() != null){
            transaction.setReceiverWalletId(request.getReceiverWalletId());
        }
        transaction.setType(request.getType());
        transaction.setStatus(TransactionStatus.PENDING);

        if (request.getType() == TransactionType.DEBIT) {
            if (amount > wallet.getBalance()) {
                transaction.setStatus(TransactionStatus.FAILED);
                throw new TransactionsException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance() - amount);
        } else if (request.getType() == TransactionType.CREDIT) {
            wallet.setBalance(wallet.getBalance() + amount);
        } else if (request.getType() == TransactionType.TRANSFER) {
            throw new UnsupportedOperationException("Transfer not implemented yet");
        }

        walletRepository.save(wallet);
        transaction.setStatus(TransactionStatus.SUCCESS);
        WalletTransaction savedTransaction = transactionsRepository.save(transaction);

        IdempotencyKey key = new IdempotencyKey();
        key.setWallet(wallet);
        key.setTransaction(savedTransaction);
        key.setIkey(idempotencyKeyValue);
        idempotencyRepository.save(key);

        return buildTransactionResponse(savedTransaction, idempotencyKeyValue);
    }


    @Override
    @Transactional
    public TransactionResponseDto transferFunds(Long walletId, TransactionRequestDto request){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException("No wallet found with id: " + walletId));

        Long amount = request.getAmount();
        if (amount == null || amount <= 0) {
            throw new TransactionsException("Amount must be greater than 0");
        }

        String idempotencyKeyValue = request.getIdempotencyKey();
        if (idempotencyKeyValue != null && !idempotencyKeyValue.isBlank()) {
            Optional<IdempotencyKey> existingKey = idempotencyRepository.findByIkeyAndWallet_Id(
                    idempotencyKeyValue, walletId);

            if (existingKey.isPresent()) {
                WalletTransaction existingTransaction = existingKey.get().getTransaction();
                return buildTransactionResponse(existingTransaction, existingKey.get().getIkey());
            }
        } else {
            idempotencyKeyValue = UUID.randomUUID().toString();
        }

        Wallet receiverWallet = walletRepository.findById(request.getReceiverWalletId())
                .orElseThrow(() -> new TransactionsException("No receiver with ID: " + request.getReceiverWalletId()));

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setReceiverWalletId(receiverWallet.getId());
        transaction.setType(request.getType());
        transaction.setStatus(TransactionStatus.PENDING);



        if (request.getType() == TransactionType.DEBIT) {
            throw new UnsupportedOperationException("Wrong endpoint ");
        } else if (request.getType() == TransactionType.CREDIT) {
            throw new UnsupportedOperationException("Wrong endpoint");
        } else if (request.getType() == TransactionType.TRANSFER) {
            wallet.setBalance(wallet.getBalance() - amount);
            receiverWallet.setBalance(receiverWallet.getBalance() + amount);
        }

        walletRepository.save(wallet);
        walletRepository.save(receiverWallet);
        transaction.setStatus(TransactionStatus.SUCCESS);
        WalletTransaction savedTransaction = transactionsRepository.save(transaction);

        IdempotencyKey key = new IdempotencyKey();
        key.setWallet(wallet);
        key.setTransaction(savedTransaction);
        key.setIkey(idempotencyKeyValue);
        idempotencyRepository.save(key);

        return buildTransactionResponse(savedTransaction, idempotencyKeyValue);
    }

    @Override
    public WalletResponseDto getOneWallet (Long walletId){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException("No wallet found with id: " + walletId));
        WalletResponseDto response = new WalletResponseDto();
        response.setId(wallet.getId());
        response.setBalance(wallet.getBalance());
        response.setFirstName(wallet.getFirstName());
        response.setLastName(wallet.getLastName());
        response.setUserName(wallet.getUserName());
        response.setCreatedAt(wallet.getCreatedAt());

        return response;
    }

    private TransactionResponseDto buildTransactionResponse(WalletTransaction transaction, String idempotencyKey) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setTransactionId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setStatus(transaction.getStatus());
        dto.setWalletId(transaction.getWallet().getId());
        dto.setReceiverWalletId(transaction.getReceiverWalletId());
        if(transaction.getType() != TransactionType.TRANSFER){
            dto.setTo(transaction.getWallet().getFirstName());
        }else{
            Wallet wallet = walletRepository.findById(transaction.getReceiverWalletId())
                    .orElseThrow(() -> new TransactionsException("not found"));
            dto.setTo(wallet.getFirstName());
        }
        dto.setFrom(transaction.getWallet().getFirstName());
        dto.setIdempotencyKey(idempotencyKey);
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }

}
