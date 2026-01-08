# Wallet Service API

A simple wallet management service built with **Spring Boot** that supports:

- Wallet creation
- Credit and debit transactions
- Wallet-to-wallet transfers
- Idempotent transaction handling
- Transaction history tracking

This project demonstrates clean service design, transactional integrity, and API best practices.

---

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL** (or H2 for local testing)
- **Springdoc OpenAPI (Swagger)**
- **Maven**

---

## Features

- Create wallets with unique usernames
- Credit and debit wallet balances
- Transfer funds between wallets atomically
- Idempotency support to prevent duplicate transactions
- Centralized error handling
- API documentation via Swagger UI

---

## Project Structure

src/main/java/com/njasettlement/wallet<br>
├── app<br>
│ ├── controller<br>
│ ├── dto<br>
│ ├── model<br>
│ ├── repository<br>
│ └── service<br>
└── WalletApplication.java<br>


---

## Getting Started

### Prerequisites

Make sure you have the following installed:

- **Java 17+**
- **Maven**
- **Intelli j**
- **Git**

---

## Clone the Repository

```bash
git clone https://github.com/Dev-JoyA/wallet.git
cd wallet
```

Run the Application
```bash
    mvn spring-boot:run
```

The application will start on:
```bash
http://localhost:8080
```

API Documentation (Swagger)

Once the app is running, open:
```bash
http://localhost:8080/swagger-ui/index.html
```

This page provides:

- All available endpoints
- Request/response schemas
- Enum values for transactions
- Interactive API testing  

## Idempotency Support

- Each transaction request can include an `idempotencyKey`
- Repeating the same request with the same key returns the original transaction
- Prevents duplicate balance updates on retries
- This is especially important for financial systems

---

## Design Considerations

- Transactions are wrapped with `@Transactional`
- Transfers debit and credit wallets atomically
- Clear separation of concerns:
    - **Controller** → API layer
    - **Service** → Business logic
    - **Repository** → Data access
- DTOs isolate internal models from external consumers

---






