# test-wallet.ps1
# PowerShell script to test Wallet API endpoints (Windows-friendly)

$baseUrl = "http://localhost:8080/api/wallets"

# -------------------------------
# 0. Create Wallet 1
# -------------------------------
Write-Host "`n=== 0. Creating Wallet 1 ==="
$wallet1Payload = @{
    firstName = "Alice"
    lastName  = "Johnson"
    userName  = "wallet1"
} | ConvertTo-Json

$response1 = Invoke-RestMethod -Uri "$baseUrl/create" -Method POST -Body $wallet1Payload -ContentType "application/json"
$wallet1 = $response1
Write-Host $wallet1

# -------------------------------
# 1. Create Wallet 2
# -------------------------------
Write-Host "`n=== 1. Creating Wallet 2 ==="
$wallet2Payload = @{
    firstName = "Bob"
    lastName  = "Smith"
    userName  = "wallet2"
} | ConvertTo-Json

$response2 = Invoke-RestMethod -Uri "$baseUrl/create" -Method POST -Body $wallet2Payload -ContentType "application/json"
$wallet2 = $response2
Write-Host $wallet2

# -------------------------------
# 2. Credit Wallet 1
# -------------------------------
Write-Host "`n=== 2. Credit Wallet 1 ==="
$creditPayload = @{
    type = "CREDIT"
    amount = 5000
    idempotencyKey = "credit-1"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/$($wallet1.id)/balance" -Method POST -Body $creditPayload -ContentType "application/json" | Write-Host

# -------------------------------
# 3. Debit Wallet 1
# -------------------------------
Write-Host "`n=== 3. Debit Wallet 1 ==="
$debitPayload = @{
    type = "DEBIT"
    amount = 2000
    idempotencyKey = "debit-1"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/$($wallet1.id)/balance" -Method POST -Body $debitPayload -ContentType "application/json" | Write-Host

# -------------------------------
# 4. Transfer from Wallet 1 to Wallet 2
# -------------------------------
Write-Host "`n=== 4. Transfer 1000 from Wallet 1 to Wallet 2 ==="
$transferPayload = @{
    type = "TRANSFER"
    amount = 1000
    receiverWalletId = $wallet2.id
    idempotencyKey = "transfer-1"
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/transfer/$($wallet1.id)" -Method POST -Body $transferPayload -ContentType "application/json" | Write-Host

# -------------------------------
# 5. Get Wallet 1 Details
# -------------------------------
Write-Host "`n=== 5. Wallet 1 Details ==="
Invoke-RestMethod -Uri "$baseUrl/one/$($wallet1.id)" -Method GET | Write-Host

# -------------------------------
# 6. Get Wallet 2 Details
# -------------------------------
Write-Host "`n=== 6. Wallet 2 Details ==="
Invoke-RestMethod -Uri "$baseUrl/one/$($wallet2.id)" -Method GET | Write-Host

Write-Host "`n=== Test Complete ==="
