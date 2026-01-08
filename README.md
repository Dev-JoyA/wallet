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
- **Springdoc OpenAPI (Swagger)**
- **Maven**

---

## Features

- Create wallets with unique usernames
- Credit and debit wallet balances
- Transfer funds between wallets atomically
- Idempotency support to prevent duplicate transactions
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
- **An IDE (IntelliJ, VS Code, etc.)**
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

## Testing the API with cURL

You can test the Wallet API endpoints directly using **cURL** from Windows PowerShell, CMD, or Git Bash.  

---

### 1. Create Wallet

**Endpoint:** `POST /api/wallets/create`  
**Description:** Creates a new wallet with a unique username.

```bash
curl.exe -X POST http://localhost:8080/api/wallets/create ^
-H "Content-Type: application/json" ^
-d "{ \"firstName\": \"Alice\", \"lastName\": \"Johnson\", \"userName\": \"wallet1\" }"
```

expected Response
```json
{
  "id": 1,
  "balance": 0,
  "firstName": "Alice",
  "lastName": "Johnson",
  "userName": "wallet1",
  "createdAt": "2026-01-08T19:00:00"
}

```

### 2. Credit Wallet

**Endpoint:** `POST /api/wallets/{walletId}/balance`
**Description:** Adds funds to a wallet. Requires an idempotencyKey to prevent duplicate transactions.
```bash
curl.exe -X POST http://localhost:8080/api/wallets/1/balance ^
-H "Content-Type: application/json" ^
-d "{ \"type\": \"CREDIT\", \"amount\": 5000, \"idempotencyKey\": \"credit-1\" }"
```

**Expected Response**
```json
{
  "transactionId": 1,
  "amount": 5000,
  "type": "CREDIT",
  "status": "SUCCESS",
  "walletId": 1,
  "idempotencyKey": "credit-1",
  "createdAt": "2026-01-08T19:05:00"
}
```

### 3. Debit Wallet

**Endpoint:** `POST /api/wallets/{walletId}/balance`
**Description:** Deducts funds from a wallet, ensuring sufficient balance.
```bash
curl.exe -X POST http://localhost:8080/api/wallets/1/balance ^
-H "Content-Type: application/json" ^
-d "{ \"type\": \"DEBIT\", \"amount\": 2000, \"idempotencyKey\": \"debit-1\" }"
```


**Expected Response: **
```json
{
  "transactionId": 2,
  "amount": 2000,
  "type": "DEBIT",
  "status": "SUCCESS",
  "walletId": 1,
  "idempotencyKey": "debit-1",
  "createdAt": "2026-01-08T19:10:00"
}
```

### 4. Transfer Funds

**Endpoint:** `POST /api/wallets/transfer/{walletId}`
**Description:** Transfers funds from one wallet to another atomically. Requires the sender wallet ID in the URL.
```bash
curl.exe -X POST http://localhost:8080/api/wallets/transfer/1 ^
-H "Content-Type: application/json" ^
-d "{ \"type\": \"TRANSFER\", \"amount\": 1000, \"receiverWalletId\": 2, \"idempotencyKey\": \"transfer-1\" }"
```

**Expected Response:**
```json
{
  "transactionId": 3,
  "amount": 1000,
  "type": "TRANSFER",
  "status": "SUCCESS",
  "walletId": 1,
  "receiverWalletId": 2,
  "idempotencyKey": "transfer-1",
  "createdAt": "2026-01-08T19:15:00"
}
```
### 5. Get Wallet Details

**Endpoint:** `GET /api/wallets/one/{walletId}`
**Description:** Retrieves the current balance and details of a wallet.
```bash
curl.exe http://localhost:8080/api/wallets/one/1
```

**Expected Response**
```json
{
  "id": 1,
  "balance": 2000,
  "firstName": "Alice",
  "lastName": "Johnson",
  "userName": "wallet1",
  "createdAt": "2026-01-08T19:00:00"
}
```
---
### ✅ Notes

- On Linux/macOS, remove `curl.exe` and `^` line continuations.  
- `idempotencyKey` ensures that retrying the same request does not create duplicate transactions.  
- These examples assume your application is running locally on **port 8080**. Adjust the URL if different.





