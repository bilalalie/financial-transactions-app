Here’s a cleaner, more polished, and more professional rewrite of your README:

---

# 💎 FintechLedger

### Full-Stack Financial Ledger Application

**FintechLedger** is a production-style full-stack fintech application built with **Spring Boot (Java 17)** and **Angular 17**.
It implements a secure **double-entry bookkeeping system**, JWT-based authentication, and real-time transaction management.

Designed to demonstrate clean architecture, financial integrity, and modern full-stack development practices.

---

# 🏗️ System Architecture

```
fintech-app/
├── backend/          # Spring Boot 3.2 (Java 17)
│   └── src/main/java/com/fintech/
│       ├── config/        # Security & Data initialization
│       ├── controller/    # REST API controllers
│       ├── dto/           # Data transfer objects
│       ├── model/         # JPA entities (Account, Transaction, LedgerEntry)
│       ├── repository/    # Spring Data JPA repositories
│       ├── security/      # JWT filters & utilities
│       └── service/       # Business logic layer
└── frontend/         # Angular 17 (Standalone components)
    └── src/app/
        ├── components/    # Login & Dashboard
        ├── guards/        # Route protection (AuthGuard)
        ├── models/        # TypeScript interfaces
        └── services/      # Auth & transaction services
```

The project follows a clear layered architecture:

* **Controller Layer** → REST endpoints
* **Service Layer** → Business logic & validation
* **Repository Layer** → Database access
* **Security Layer** → JWT authentication & authorization

---

# 🚀 Quick Start

## ✅ Prerequisites

* Java 17+
* Maven 3.8+
* Node.js 18+
* npm 9+

---

## 🔧 Backend Setup

```bash
cd backend

# Run with H2 (in-memory database — recommended for development)
mvn spring-boot:run

# Run with MySQL (after configuring credentials)
mvn spring-boot:run -Dspring.profiles.active=mysql
```

**Backend URL:**
`http://localhost:8080`

**H2 Console (dev mode):**
`http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:fintechdb`

---

### 🐬 MySQL Configuration

Update:

```
src/main/resources/application-mysql.properties
```

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fintechdb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=yourpassword
```

---

## 🎨 Frontend Setup

```bash
cd frontend
npm install
ng serve
```

**Frontend URL:**
`http://localhost:4200`

The frontend communicates with the backend via REST APIs secured with JWT.

---

# 👤 Demo Accounts

These accounts are auto-created on first application startup:

| Username | Password | Starting Balance |
| -------- | -------- | ---------------- |
| alice    | alice123 | $10,000.00       |
| bob      | bob123   | $5,000.00        |

---

# 📡 REST API Overview

## 🔐 Authentication

### POST `/api/auth/login`

Authenticate user and receive JWT token.

### Request

```json
{
  "username": "alice",
  "password": "alice123"
}
```

### Response

```json
{
  "token": "eyJhbGc...",
  "username": "alice",
  "fullName": "Alice Johnson",
  "accountId": 1,
  "balance": 10000.00
}
```

---

## 💸 Transactions

Requires header:

```
Authorization: Bearer <token>
```

| Method | Endpoint                    | Description                      |
| ------ | --------------------------- | -------------------------------- |
| GET    | `/api/transactions`         | Retrieve user transactions       |
| POST   | `/api/transactions`         | Create new transfer              |
| GET    | `/api/transactions/summary` | Get debit/credit summary         |
| GET    | `/api/accounts/other`       | List available transfer accounts |
| GET    | `/api/accounts/me`          | Get current account details      |

---

### Create Transfer

```json
{
  "amount": 500.00,
  "description": "Payment for services",
  "destinationAccountId": 2
}
```

### Validation Rules

* Amount must be **greater than 0**
* Description cannot be blank
* Destination account is required
* Cannot transfer to self
* Source account must have sufficient funds

---

# 📖 Double-Entry Ledger System

FintechLedger follows **double-entry bookkeeping principles**.

Every transfer generates:

1. **Transaction Record**

   * Stores full audit trail
   * Captures balances before and after transfer

2. **Two Ledger Entries**

   * **DEBIT** → Source account
   * **CREDIT** → Destination account

This guarantees:

* Ledger consistency
* Financial traceability
* Immutable audit history
* Balanced books at all times

---

# 🔒 Security Architecture

* JWT authentication (24-hour expiration)
* BCrypt password hashing
* Stateless session management
* Role-based access (USER)
* CORS configured for Angular dev server (`localhost:4200`)
* H2 console enabled in development mode only

---

# 📊 Frontend Features

| Feature                | Description                                  |
| ---------------------- | -------------------------------------------- |
| 🔐 Authentication      | Login with quick-fill demo buttons           |
| 📊 Dashboard           | Real-time balance overview                   |
| 💸 Transfer Modal      | Send money with validation & balance updates |
| 📋 Transaction History | Detailed ledger breakdown                    |
| 🔎 Filtering           | Filter by type, date, or amount              |
| 📈 Summary Cards       | Credits, debits, total balance, count        |

---

# 🛠 Technology Stack

## Backend

* Spring Boot 3.2
* Spring Security 6 (JWT)
* Spring Data JPA (Hibernate)
* H2 (dev) / MySQL
* Jakarta Bean Validation
* Lombok
* JJWT 0.11.5

## Frontend

* Angular 17 (Standalone Components)
* TypeScript 5.2
* RxJS 7.8
* Angular Router
* Reactive Forms
* HttpClient

---

# 🗄 Database Schema

## accounts

| Column    | Type             | Description       |
| --------- | ---------------- | ----------------- |
| id        | BIGINT (PK)      | Auto-generated ID |
| username  | VARCHAR (UNIQUE) | Login username    |
| password  | VARCHAR          | BCrypt hash       |
| full_name | VARCHAR          | Display name      |
| balance   | DECIMAL(19,2)    | Current balance   |
| role      | VARCHAR          | USER              |

---

## transactions

| Column                     | Description                  |
| -------------------------- | ---------------------------- |
| id                         | Primary key                  |
| amount                     | Transfer amount              |
| type                       | TRANSFER                     |
| description                | Transaction note             |
| transaction_date           | Timestamp                    |
| source_account_id          | Sender                       |
| destination_account_id     | Receiver                     |
| source_balance_before      | Audit field                  |
| source_balance_after       | Audit field                  |
| destination_balance_before | Audit field                  |
| destination_balance_after  | Audit field                  |
| status                     | COMPLETED / PENDING / FAILED |

---

## ledger_entries

| Column          | Description         |
| --------------- | ------------------- |
| id              | Primary key         |
| transaction_id  | Parent transaction  |
| account_id      | Affected account    |
| entry_type      | DEBIT / CREDIT      |
| amount          | Entry amount        |
| running_balance | Balance after entry |
| created_at      | Timestamp           |

---

# 🎯 Project Goals

* Demonstrate clean financial domain modeling
* Implement real-world double-entry accounting
* Showcase secure JWT-based authentication
* Build a complete full-stack system
* Apply best practices in layered architecture

---

