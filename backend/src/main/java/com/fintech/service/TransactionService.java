package com.fintech.service;

import com.fintech.dto.TransactionDto;
import com.fintech.model.Account;
import com.fintech.model.LedgerEntry;
import com.fintech.model.Transaction;
import com.fintech.repository.AccountRepository;
import com.fintech.repository.LedgerEntryRepository;
import com.fintech.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              LedgerEntryRepository ledgerEntryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public List<TransactionDto.Response> getAllTransactions(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findAllByAccount(account)
                .stream()
                .map(TransactionDto.Response::fromTransaction)
                .collect(Collectors.toList());
    }

    /*
     * @Transactional is CRITICAL here.
     * If the database fails while saving the Ledger entries,
     * this annotation ensures the account balances are "rolled back"
     * so money doesn't disappear into thin air.
     */

    @Transactional
    public TransactionDto.Response createTransaction(TransactionDto.Request request, String username) {
        Account source = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account destination = accountRepository.findById(request.getDestinationAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (source.getId().equals(destination.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        BigDecimal amount = request.getAmount();
        if (source.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance. Available: " + source.getBalance());
        }

        // Record balances before transaction
        BigDecimal sourceBefore = source.getBalance();
        BigDecimal destBefore = destination.getBalance();

        // Update balances
        source.setBalance(sourceBefore.subtract(amount));
        destination.setBalance(destBefore.add(amount));

        BigDecimal sourceAfter = source.getBalance();
        BigDecimal destAfter = destination.getBalance();

        accountRepository.save(source);
        accountRepository.save(destination);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(Transaction.TransactionType.TRANSFER);
        transaction.setDescription(request.getDescription());
        transaction.setSourceAccount(source);
        transaction.setDestinationAccount(destination);
        transaction.setSourceBalanceBefore(sourceBefore);
        transaction.setSourceBalanceAfter(sourceAfter);
        transaction.setDestinationBalanceBefore(destBefore);
        transaction.setDestinationBalanceAfter(destAfter);
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);

        Transaction saved = transactionRepository.save(transaction);

        // Create ledger entries (double-entry bookkeeping)
        // Debit source account (money going out)
        LedgerEntry debitEntry = new LedgerEntry();
        debitEntry.setTransaction(saved);
        debitEntry.setAccount(source);
        debitEntry.setEntryType(LedgerEntry.EntryType.DEBIT);
        debitEntry.setAmount(amount);
        debitEntry.setRunningBalance(sourceAfter);
        ledgerEntryRepository.save(debitEntry);

        // Credit destination account (money coming in)
        LedgerEntry creditEntry = new LedgerEntry();
        creditEntry.setTransaction(saved);
        creditEntry.setAccount(destination);
        creditEntry.setEntryType(LedgerEntry.EntryType.CREDIT);
        creditEntry.setAmount(amount);
        creditEntry.setRunningBalance(destAfter);
        ledgerEntryRepository.save(creditEntry);

        return TransactionDto.Response.fromTransaction(saved);
    }

    /*
     * Calculates a financial summary by summing up ledger entries.
     * This is more accurate than just showing the current balance
     * because it shows the flow of money over time.
     */
    public TransactionDto.Summary getSummary(String username) throws AccountNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        List<LedgerEntry> entries = ledgerEntryRepository.findByAccountOrderByCreatedAtDesc(account);

        BigDecimal totalCredits = entries.stream()
                .filter(e -> e.getEntryType() == LedgerEntry.EntryType.CREDIT)
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebits = entries.stream()
                .filter(e -> e.getEntryType() == LedgerEntry.EntryType.DEBIT)
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TransactionDto.Summary summary = new TransactionDto.Summary();
        summary.setTotalCredits(totalCredits);
        summary.setTotalDebits(totalDebits);
        summary.setCurrentBalance(account.getBalance());
        summary.setTransactionCount(entries.size());
        return summary;
    }

    /*
     * Retrieves the full account details for the authenticated user.
     * * @param username The username extracted from the JWT.
     * @return The Account entity containing balance, full name, and ID.
     * @throws AccountNotFoundException if the database lookup fails.
     */
    public Account getAccountInfo(String username) throws AccountNotFoundException {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    /*
     * Fetches a list of all accounts EXCEPT the one belonging to the current user.
     * This is primarily used to populate a "Select Recipient" dropdown in the frontend.
     * * @param username The current user's username (to be excluded).
     * @return A list of potential transfer recipients.
     */
    public List<Account> getOtherAccounts(String username) {
        return accountRepository.findAll().stream()
                .filter(a -> !a.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}
