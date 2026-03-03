package com.fintech.controller;

import com.fintech.dto.TransactionDto;
import com.fintech.model.Account;
import com.fintech.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /*
     * GET /api/transactions
     * Fetches the history of transactions for the currently logged-in user.
     * @AuthenticationPrincipal automatically extracts the UserDetails from the SecurityContext.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto.Response>> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transactionService.getAllTransactions(userDetails.getUsername()));
    }

    /*
     * POST /api/transactions
     * Creates a new transaction (Transfer, Deposit, etc.).
     * @Valid ensures the RequestBody meets the constraints defined in TransactionDto (like non-null, positive amounts).
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDto.Response> createTransaction(
            @Valid @RequestBody TransactionDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {
        // We pass the username from the JWT to ensure the user can only spend their own money
        TransactionDto.Response response = transactionService.createTransaction(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /*
     * GET /api/transactions/summary
     * Provides a high-level overview (total spent, total received, etc.) for the dashboard.
     */
    @GetMapping("/transactions/summary")
    public ResponseEntity<TransactionDto.Summary> getSummary(
            @AuthenticationPrincipal UserDetails userDetails) throws AccountNotFoundException {
        return ResponseEntity.ok(transactionService.getSummary(userDetails.getUsername()));
    }

    /*
     * GET /api/accounts/other
     * Returns a list of other users in the system.
     * Useful for a "Transfer to Friend" dropdown menu.
     * Note: It maps the Account entity to a Map to hide sensitive fields like passwords/balances.
     */
    @GetMapping("/accounts/other")
    public ResponseEntity<List<Map<String, Object>>> getOtherAccounts(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<Account> accounts = transactionService.getOtherAccounts(userDetails.getUsername());
        List<Map<String, Object>> result = accounts.stream()
                .map(a -> Map.<String, Object>of(
                        "id", a.getId(),
                        "username", a.getUsername(),
                        "fullName", a.getFullName()
                ))
                .toList();
        return ResponseEntity.ok(result);
    }


    /*
     * GET /api/accounts/me
     * Returns the current user's profile and current balance.
     */
    @GetMapping("/accounts/me")
    public ResponseEntity<Map<String, Object>> getMyAccount(
            @AuthenticationPrincipal UserDetails userDetails) throws AccountNotFoundException {
        Account account = transactionService.getAccountInfo(userDetails.getUsername());
        return ResponseEntity.ok(Map.of(
                "id", account.getId(),
                "username", account.getUsername(),
                "fullName", account.getFullName(),
                "balance", account.getBalance()
        ));
    }
}
