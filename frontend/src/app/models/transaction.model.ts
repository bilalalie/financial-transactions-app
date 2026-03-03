export interface Transaction {
  id: number;
  amount: number;
  type: 'CREDIT' | 'DEBIT' | 'TRANSFER';
  description: string;
  transactionDate: string;
  sourceAccountUsername: string;
  destinationAccountUsername: string;
  sourceBalanceBefore: number;
  sourceBalanceAfter: number;
  destinationBalanceBefore: number;
  destinationBalanceAfter: number;
  status: 'PENDING' | 'COMPLETED' | 'FAILED';
}

export interface TransactionRequest {
  amount: number;
  description: string;
  destinationAccountId: number;
}

export interface TransactionSummary {
  totalCredits: number;
  totalDebits: number;
  currentBalance: number;
  transactionCount: number;
}

export interface Account {
  id: number;
  username: string;
  fullName: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  fullName: string;
  accountId: number;
  balance: number;
}
