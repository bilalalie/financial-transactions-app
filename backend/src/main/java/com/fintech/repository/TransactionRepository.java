package com.fintech.repository;

import com.fintech.model.Account;
import com.fintech.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /*
     * CUSTOM QUERY:
     * It finds every transaction where the given account was either:
     * 1. The sender (sourceAccount)
     * 2. The receiver (destinationAccount)
     * It then sorts them so the newest transactions appear at the top.
     */
    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.destinationAccount = :account ORDER BY t.transactionDate DESC")
    List<Transaction> findAllByAccount(@Param("account") Account account);

    /*
     * DERIVED QUERY:
     * Spring Data JPA parses the method name to generate the SQL.
     * This fetches EVERY transaction in the system, ordered by date.
     * Usually used by Admins or for global auditing.
     */
    List<Transaction> findAllByOrderByTransactionDateDesc();
}
