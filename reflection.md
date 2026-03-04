# Reflection – Fintech Project

## Project Overview

This project is a simplified FinTech transaction system built with:

- **Backend:** Spring Boot  
- **Frontend:** Angular  

The application simulates secure fund transfers between accounts using a double-entry ledger system, JWT-based authentication, and a clean layered architecture.

---

## How I Approached the Problem

### 1. Understanding the Requirements First

Before writing code, I divided the system into three architectural layers:

#### Data Layer
Core entities:
- `Account`
- `Transaction`
- `LedgerEntry`

Relationship design:
- A **Transaction** connects two **Accounts**.
- Each **Transaction** generates two **LedgerEntry** records:
  - One **DEBIT**
  - One **CREDIT**

This follows real-world double-entry bookkeeping principles.

#### Business Logic Layer
Defined strict business rules:
- Authentication required
- Transfer amount must be positive
- Sender must have sufficient balance
- No self-transfer allowed
- Transactions must be atomic

#### Presentation Layer
User flow design:
- Login
- Send money
- View transaction history
- Filter transactions

This layered approach ensured separation of concerns and maintainability.

---

## How I Used AI Tools

AI was used as a development assistant primarily for framework-specific configuration and architectural validation.

### Architecture Design

Compared two approaches:
- Storing debit/credit fields directly in `Transaction`
- Creating a separate `LedgerEntry` table

I chose a separate `LedgerEntry` table to:
- Match real-world accounting systems
- Maintain extensibility
- Enable per-account queries
- Preserve auditability

### Security Architecture (Spring Security 6 + JWT)

AI assistance helped with:
- Configuring `SecurityFilterChain`
- Positioning the JWT filter correctly
- Setting stateless session management
- Disabling CSRF appropriately
- Implementing BCrypt password hashing

### Validation Strategy

Planned validation across layers:

**DTO Layer (Jakarta Bean Validation)**
- Amount > 0
- Required fields

**Service Layer (Business Rules)**
- Sufficient balance
- No self-transfer

This kept controllers thin and business logic testable.

### Angular 17 Patterns

Used modern Angular practices:
- Standalone components
- `provideHttpClient(withInterceptors([...]))`
- Functional interceptors (`HttpInterceptorFn`)
- JWT token attachment via interceptor

### Database Profiles

Implemented Spring Profiles:
- H2 (default for development)
- MySQL (optional production-ready setup)

Profiles are isolated to prevent configuration conflicts.

---

## Key Design Decisions

- **Double-Entry Ledger System**  
  Each `LedgerEntry` stores the running balance, allowing complete financial history reconstruction.

- **JWT-Based Authentication (Stateless)**  
  Enables secure frontend-backend separation and avoids CSRF issues.

- **Seeded Accounts via CommandLineRunner**  
  Two predefined accounts initialized as per project requirements.

- **Client-Side Filtering**  
  Suitable for small datasets; backend pagination recommended for scalability.

---

## Challenges Encountered

- CORS configuration between Angular and Spring Boot
- Avoiding circular dependency in security configuration

---

## Project Outcomes: Advanced FinTech Backend Development

### 1. Robust Security Architecture
- Stateless authentication using JWT
- BCrypt password hashing
- Custom JWT filter
- Secure `SecurityContext` handling

### 2. Financial Integrity & Auditability
- Double-entry ledger system
- Immutable audit trail
- Atomic transfers using `@Transactional`
- ACID-compliant transaction handling

### 3. Clean API & System Design
- Global exception handling using `@ControllerAdvice`
- DTO pattern to protect sensitive fields
- JPQL queries for full transaction history
- Structured JSON error responses

### 4. Enterprise Best Practices
- Proper CORS configuration
- CSRF disabled for JWT endpoints
- Layered validation (DTO + Service)
- Clean separation of concerns

---

## Possible Improvements

- Backend + frontend pagination
- Search by description or amount range
- Email notifications
- Password reset flow
- CSV/PDF export
- Unit and integration tests
- Docker Compose setup
- Rate limiting on login endpoint

---

## Key Learning Takeaways

- AI accelerated framework setup and configuration.
- Core financial logic (ledger design, atomic transfers, balance validation) was implemented independently.
- Layered architecture improves scalability and maintainability.
- Double-entry bookkeeping is foundational in financial systems.
- Proper JWT integration requires careful security and CORS handling.
- Thoughtful DTO and exception design improves API clarity and safety.

---

## AI / LLM Session Links

Backend (Spring Boot):  
https://chatgpt.com/share/69a7a5cb-c3a4-8008-8d63-6cb197eb0e6c  

Frontend (Angular):  
https://chatgpt.com/share/69a6f09f-67fc-8008-8d97-83c3b4165144  
