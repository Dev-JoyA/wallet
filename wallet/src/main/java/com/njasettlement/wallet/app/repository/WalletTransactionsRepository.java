package com.njasettlement.wallet.app.repository;

import com.njasettlement.wallet.app.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionsRepository extends JpaRepository<WalletTransaction, Long> {
}
