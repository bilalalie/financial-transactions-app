Fintech Project

How I Approached the Problem
Understanding the Requirements First
Before coding, I broke down the project into three layers:
1.	Data layer — Identify entities: Account, Transaction, LedgerEntry.
2.	Business logic layer — Define rules: balance checks, double-entry bookkeeping, authentication.
3.	Presentation layer — Decide what the user sees and does: login, send money, view transaction history with filters.
I mapped the relationships: a Transaction connects two Accounts, and each Transaction generates two LedgerEntries (one DEBIT, one CREDIT). This is the core of the ledger system.
________________________________________


How I Used AI Tools
1. Architecture Design
I used AI for prototyping the ledger model. It helped me weigh options:
•	Separate LedgerEntry table → accurate double-entry accounting, easier queries per account.
•	Fields on Transaction → simpler, less flexible.
I chose a separate table to match real-world accounting practices and keep the model extensible.
2. Security Architecture
Spring Security 6 changed the configuration style (no WebSecurityConfigurerAdapter). AI guidance helped me write the SecurityFilterChain bean, correctly position the JWT filter, and configure stateless sessions.
3. Validation Strategy
AI helped me plan validation layers:
•	DTO layer — Jakarta Bean Validation (amount > 0, not blank).
•	Service layer — business rules (sufficient balance, no self-transfer).
This kept controllers thin and rules testable.
4. Angular 17 Patterns
AI guidance helped me use standalone components and the new provideHttpClient(withInterceptors([...])) API with functional interceptors (HttpInterceptorFn), which is the Angular 17 way.
5. Database Profiles
For H2 and MySQL support, AI helped me structure Spring profiles: H2 as default for development, MySQL optional. Each profile is independent, avoiding conflicts.
________________________________________


Key Design Decisions
•	Ledger for audit trail — Each LedgerEntry records the running balance, allowing full history reconstruction.
•	JWT over sessions — Enables cross-origin auth between Angular frontend and Spring backend, avoiding CSRF issues.
•	Two hardcoded accounts — Requirement specified no account creation service. I seeded the database with a CommandLineRunner bean safely.
•	Frontend filtering — Kept client-side for small dataset; backend filtering would be needed for large-scale transactions.
________________________________________


Challenges Encountered
•	CORS 
•	Circular dependency risk
________________________________________


Possible Improvements
•	Backend + frontend pagination for transactions.
•	Search transactions by description or amount range.
•	Email notifications for transfers.
•	Password reset flow.
•	Export transaction history (CSV/PDF).
•	Unit and integration tests.
•	Docker Compose setup for MySQL.
•	Rate limiting on login endpoint.
________________________________________


Conclusion
AI was most valuable for:
•	Framework-specific boilerplate (Spring Security, Angular setup).
•	Architectural decisions (ledger design, validation layers).
•	Spotting potential issues early (circular dependencies, CORS).
The main business logic — balance checking, double-entry ledger, atomic transaction creation — was designed and implemented independently, with AI used only for guidance and prototyping.

Here’s a polished addition you can append at the end of your document, tying in your **learning outcomes** and reflecting on the project’s value:


Project Outcomes: Advanced FinTech Backend Development

1. Robust Security Architecture

   * Implemented stateless authentication using Spring Security 6 and JWT, supporting scalable microservices.
   * Applied BCrypt password hashing and HMAC-SHA256 token signing to protect credentials and ensure token integrity.
   * Developed a custom `JwtFilter` to validate requests and populate the SecurityContext only for authenticated users.

2. Financial Integrity & Auditability

   * Designed a double-entry ledger system where every transaction generates corresponding debit and credit entries, maintaining an immutable audit trail.
   * Ensured ACID compliance using Spring’s `@Transactional`, so fund transfers are atomic and consistent.

3. Advanced API & System Design

   * Built a global exception handler with `@ControllerAdvice` for consistent, user-friendly JSON error responses.
   * Used the DTO pattern to separate database entities from API contracts, protecting sensitive fields like passwords.
   * Designed JPQL queries to retrieve complete transaction history for users, covering both sent and received transactions.

4. Enterprise Best Practices

   * Configured CORS for secure communication with the Angular frontend and disabled CSRF for JWT-based endpoints.
   * Enforced validation at both DTO and service layers (e.g., positive transfer amounts, sufficient balance).



Key Learning Takeaways

* AI tools accelerated framework-specific setup, validation planning, and security configuration, but the **core business logic** — ledger design, atomic transfers, and balance checks — was implemented independently.
* Understanding **layered architecture** (data, service, presentation) is crucial for maintainable FinTech applications.
* Double-entry bookkeeping and ACID compliance are foundational for financial systems, ensuring integrity, traceability, and auditability.
* Modern frontend-backend integration (Angular + Spring Security + JWT) requires careful CORS, stateless session, and token management strategies.
* Thoughtful DTO design and exception handling improve API security, usability, and maintainability.


