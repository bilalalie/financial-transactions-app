package com.fintech.repository;

import com.fintech.model.Account;
import com.fintech.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByAccountOrderByCreatedAtDesc(Account account);
}
