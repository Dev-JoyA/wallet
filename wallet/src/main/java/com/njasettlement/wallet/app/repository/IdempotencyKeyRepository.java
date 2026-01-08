package com.njasettlement.wallet.app.repository;

import com.njasettlement.wallet.app.model.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, Long> {
    Optional<IdempotencyKey> findByIkeyAndWallet_Id(String ikey, Long walletId);
}
