# meCash API

Spring Boot REST API for multi-currency money transfers between currency **A** and **B**.

## Requirements

- Java 17+
- Maven 3.9+

## Run

```bash
./mvnw spring-boot:run
```

Optional JWT secret:

```bash
export JWT_SECRET=your-secret-at-least-32-characters-long
./mvnw spring-boot:run
```

App: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`  
H2 console: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:mecash`, user: `sa`, no password)

## Test

```bash
./mvnw test
```

## Seed accounts

| Account Number | Currency | Balance | Login Username    | Password   |
| -------------- | -------- | ------- | ----------------- | ---------- |
| `1234567890`   | A        | 1000    | `user_1234567890` | `password` |
| `6574839201`   | B        | 1000    | `user_6574839201` | `password` |

## API examples

### Signup

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "skillzo",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "skillzo",
    "password": "password123"
  }'
```

### Current user

```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <token>"
```

### Transfer

```bash
curl -X POST http://localhost:8080/api/transfer \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountNumber": "1234567890",
    "toAccountNumber": "6574839201",
    "amount": 100
  }'
```

### Balance

```bash
curl http://localhost:8080/api/accounts/balance \
  -H "Authorization: Bearer <token>"
```

### Transaction history

```bash
curl http://localhost:8080/api/transactions/history \
  -H "Authorization: Bearer <token>"
```

## Assumptions

- Only two currencies exist: **A** and **B**
- Exchange rate A → B is fixed at **1.3455**; B → A is `1 / 1.3455`
- Each user gets one account on signup with balance **1000** in a randomly assigned currency
- Account numbers are unique 10-digit strings
- Amount in transfer request is always in the sender's currency
- JWT protects all `/api/**` routes except signup and login

## Limitations

- H2 in-memory database — data resets on restart
- Exchange rate is hardcoded, not fetched from an external service
- No pagination or filtering on transaction history
- No transfer reversal or refund flow
- No rate limiting or account lockout on failed logins
- One account per user

## Error format

All API errors return:

```json
{
  "timestamp": "2026-06-15T08:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "details here",
  "path": "/api/transfer"
}
```
