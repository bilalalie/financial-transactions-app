package com.fintech.dto;

import com.fintech.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {

    @Data
    public static class Request {
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        private BigDecimal amount;

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Destination account ID is required")
        private Long destinationAccountId;
    }

    @Data
    public static class Response {
        private Long id;
        private BigDecimal amount;
        private Transaction.TransactionType type;
        private String description;
        private LocalDateTime transactionDate;
        private String sourceAccountUsername;
        private String destinationAccountUsername;
        private BigDecimal sourceBalanceBefore;
        private BigDecimal sourceBalanceAfter;
        private BigDecimal destinationBalanceBefore;
        private BigDecimal destinationBalanceAfter;
        private Transaction.TransactionStatus status;

        public static Response fromTransaction(Transaction t) {
            Response r = new Response();
            r.setId(t.getId());
            r.setAmount(t.getAmount());
            r.setType(t.getType());
            r.setDescription(t.getDescription());
            r.setTransactionDate(t.getTransactionDate());
            r.setSourceAccountUsername(t.getSourceAccount().getUsername());
            r.setDestinationAccountUsername(t.getDestinationAccount().getUsername());
            r.setSourceBalanceBefore(t.getSourceBalanceBefore());
            r.setSourceBalanceAfter(t.getSourceBalanceAfter());
            r.setDestinationBalanceBefore(t.getDestinationBalanceBefore());
            r.setDestinationBalanceAfter(t.getDestinationBalanceAfter());
            r.setStatus(t.getStatus());
            return r;
        }
    }

    @Data
    public static class Summary {
        private BigDecimal totalCredits;
        private BigDecimal totalDebits;
        private BigDecimal currentBalance;
        private long transactionCount;
    }
}
