import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction, TransactionRequest, TransactionSummary, Account } from '../models/transaction.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TransactionService {
    constructor(private http: HttpClient) {}

    // Fetch all transactions
    getTransactions(): Observable<Transaction[]> {
        return this.http.get<Transaction[]>(`${environment.apiUrl}/transactions`);
    }

    // Create a new transaction
    createTransaction(request: TransactionRequest): Observable<Transaction> {
        return this.http.post<Transaction>(`${environment.apiUrl}/transactions`, request);
    }

    // Fetch transaction summary (e.g., totals, balances)
    getSummary(): Observable<TransactionSummary> {
        return this.http.get<TransactionSummary>(`${environment.apiUrl}/transactions/summary`);
    }

    // Fetch other accounts (e.g., for transfer or linking)
    getOtherAccounts(): Observable<Account[]> {
        return this.http.get<Account[]>(`${environment.apiUrl}/accounts/other`);
    }
}
