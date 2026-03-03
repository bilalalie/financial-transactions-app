package com.fintech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive(message = "Amount must be positive")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private String description;

//    @Column(name = "reference_id",nullable = false, unique = true)
//    private String referenceId; // idempotency key

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;

    // Ledger fields
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal sourceBalanceBefore;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal sourceBalanceAfter;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal destinationBalanceBefore;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal destinationBalanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.COMPLETED;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
    }

    public enum TransactionType {
        CREDIT, DEBIT, TRANSFER
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED
    }
}
