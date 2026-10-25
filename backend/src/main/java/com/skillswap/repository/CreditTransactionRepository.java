package com.skillswap.repository;

import com.skillswap.entity.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, UUID> {
    List<CreditTransaction> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
