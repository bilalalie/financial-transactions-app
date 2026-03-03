import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TransactionService } from '../../services/transaction.service';
import { Transaction, TransactionSummary, Account, LoginResponse } from '../../models/transaction.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: `./dashboard.component.html`,
  styleUrls: [`./dashboard.component.css`]
})
export class DashboardComponent implements OnInit {
  currentUser: LoginResponse | null = null;
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  summary: TransactionSummary | null = null;
  otherAccounts: Account[] = [];
  loading = false;

    // Transfer modal state
  showTransferModal = false;
  submitting = false;
  transferError = '';
  transferSuccess = false;

    filterType = '';        // sent | received
    sortOrder = 'newest';   // newest | oldest | highest | lowest
    filterDate = '';        // date string (YYYY-MM-DD)

    // Transfer form model
    transferForm = {
        destinationAccountId: '' as any,
        amount: null as any,
        description: ''
    };

  constructor(
    private authService: AuthService,
    private transactionService: TransactionService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadData();
  }

  // Get all transactions
  loadData() {
    this.loading = true;
    this.transactionService.getTransactions().subscribe({
      next: (txs) => {
        this.transactions = txs;
        this.applyFilters();
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });

    this.transactionService.getSummary().subscribe(s => this.summary = s);
    this.transactionService.getOtherAccounts().subscribe(a => this.otherAccounts = a);
  }

    // Apply filtering and sorting to transactions
    applyFilters() {
        let result = [...this.transactions]; // Clone array - new array in memory

        // Filter by transaction type (sent or received)
        if (this.filterType === 'sent') {
            result = result.filter(
                tx => tx.sourceAccountUsername === this.currentUser?.username
            );
        } else if (this.filterType === 'received') {
            result = result.filter(
                tx => tx.destinationAccountUsername === this.currentUser?.username
            );
        }

        // Filter by selected date (matches beginning of ISO date string)
        if (this.filterDate) {
            result = result.filter(
                tx => tx.transactionDate.startsWith(this.filterDate)
            );
        }

        // Apply sorting
        if (this.sortOrder === 'newest') {
            result.sort(
                (a, b) => new Date(b.transactionDate).getTime() - new Date(a.transactionDate).getTime()
            );
        } else if (this.sortOrder === 'oldest') {
            result.sort(
                (a, b) => new Date(a.transactionDate).getTime() - new Date(b.transactionDate).getTime()
            );
        } else if (this.sortOrder === 'highest') {
            result.sort((a, b) => b.amount - a.amount);
        } else if (this.sortOrder === 'lowest') {
            result.sort((a, b) => a.amount - b.amount);
        }

        // Update filtered list
        this.filteredTransactions = result;
    }
// Reset all filters to default
  clearFilters() {
    this.filterType = '';
    this.sortOrder = 'newest';
    this.filterDate = '';
    this.applyFilters();
  }

    // Get balance after a specific transaction
    // Determines whether user was sender or receiver
  getBalanceAfter(tx: Transaction): number {
    if (tx.sourceAccountUsername === this.currentUser?.username) {
      return tx.sourceBalanceAfter;
    }
    return tx.destinationBalanceAfter;
  }

  submitTransfer() {
    this.transferError = '';
    this.transferSuccess = false;

      // Basic validation
    if (!this.transferForm.destinationAccountId || !this.transferForm.amount || !this.transferForm.description) {
      this.transferError = 'Please fill in all fields.';
      return;
    }
    if (this.transferForm.amount <= 0) {
      this.transferError = 'Amount must be greater than zero.';
      return;
    }

    this.submitting = true;
      // Call API to create transaction
    this.transactionService.createTransaction({
      destinationAccountId: parseInt(this.transferForm.destinationAccountId),
      amount: this.transferForm.amount,
      description: this.transferForm.description
    }).subscribe({
      next: () => {
        this.transferSuccess = true;
        this.submitting = false;
        this.transferForm = { destinationAccountId: '', amount: null, description: '' };
        this.loadData();
        setTimeout(() => {
          this.showTransferModal = false;
          this.transferSuccess = false;
        }, 1500);
      },
      error: (err) => {
        this.transferError = err.error?.message || err.error || 'Transfer failed. Please try again.';
        this.submitting = false;
      }
    });
  }

    // Close modal when clicking on overlay background
  closeModal(event: MouseEvent) {
    if ((event.target as Element).classList.contains('modal-overlay')) {
      this.showTransferModal = false;
    }
  }
    // Log out current user
  logout() {
    this.authService.logout();
  }
}
