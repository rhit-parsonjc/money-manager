# Controllers

Base URL: _/api/v1_

## Status Codes Used

- **200 OK**
- **201 Created**
- **204 No Content**
- **400 Bad Request**
- **404 Not Found**
- **409 Conflict**
- **500 Internal Server Error**

## Accounts

**POST** to _/accounts_: creates a new account using the speciied body

**GET** to _/accounts/{id}_: gets the account with the specified id

**GET** to _/accounts_: gets all of the accounts

**PUT** to _/accounts/{id}_: replaces the account with the specified id with the name from the specified body

**DELETE** to _/accounts/{id}_: deletes the account with the specified id

**DELETE** to _/accounts/_: deletes all accounts

## Authentication

**POST** to _/auth/register_: creates a user with the specified username and password

## Bank Records

**POST** to _/bankrecords/{accountId}_: creates a new bank record for the specified account using the specified body

**GET** to _/bankrecords/{accountId}/{id}_: gets the bank record with the specified id from the specified account

**GET** to _/bankrecords/{accountId}?year={year}&month={month}&day={day}_: gets all of the bank records from the specified account, can filter by year, month, and day

**PUT** to _/bankrecords/{accountId}/{id}_: replaces the bank record with the specified id from the specified account with the specified body

**DELETE** to _/bankrecords/{accountId}/{id}_: deletes the bank record with the specified id from the specified account

**DELETE** to _/bankrecords/{accountId}_: deletes all bank records from the specified account

## Date Amounts

**POST** to _/dateamounts/{accountId}_: create a new date amount for the specified account using the specified body

**GET** to _/dateamounts/{accountId}?year={year}&month={month}&day={day}_: gets all of the date amounts from the specified account, can filter by year and month

**PUT** to _/dateamounts/{accountId}?year={year}&month={month}&day={day}_: replaces the date record for the specified account and day with the specified body

**DELETE** to _/dateamounts/{accountId}?year={year}&month={month}&day={day}_: deletes the date record for the specified account and day

**DELETE** to _/dateamounts?accountId={accountId}_: deletes all date amounts from the specified account

## Financial Transactions

**POST** to _/financialtransactions/{accountId}_: creates a new financial transaction for the specified account using the specified body

**GET** to _/financialtransactions/{accountId}/{id}_: gets the financial transaction with the specified id from the specified account

**GET** to _/financialtransactions/{accountId}?year={year}&month={month}&day={day}_: gets all of the financial transactions from the specified account, can filter by year, month, and day

**PUT** to _/financialtransactions/{accountId}/{id}_: replaces the financial transaction with the specified id from the specified account with the specified body

**DELETE** to _/financialtransactions/{accountId}/{id}_: deletes the financial transaction with the specified id from the specified account

**DELETE** to _/financialtransactions/{accountId}_: deletes all financial transactions from the specified account

## Record-Transaction Connections

**POST** to _/recordtransactions/{recordId}/{transactionId}_: creates a new connection between a bank record and a financial transaction for the specified account

**DELETE** to _/recordtransactions/{recordId}/{transactionId}_: deletes a connection between a bank record and a financial transaction for the specified account

## File Attachments

**POST** to _/fileattachments/bankrecords/{recordId}_: creates a new file attached to a bank record

**POST** to _/fileattachments/financialtransactions/{transactionId}_: creates a new file attaced to a financial transaction

**GET** to _/fileattachments/{id}_: gets the file attachment with the specified id

**GET** to _/fileattachments/bankrecords/{recordId}_: gets the file attachments for the specified bank record

**GET** to _/fileattachments/financialtransactions/{transactionId}_: gets the file attachments for the specified financial transaction

**DELETE** to _/fileattachments/{id}_: deletes the file attachment with the specified id

**DELETE** to _/fileattachments/bankrecords/{recordId}_: deletes the file attachments for the specified bank record

**DELETE** to _/fileattachments/financialtransactions/{transactionId}_: deletes the file attachments for the specified financial transaction

# Models

## Account

- id
- name
- userEntity (N - 1)
- bankRecords (1 - N)
- financialTransactions (1 - N)
- dateAmounts (1 - N)

## Bank Record

- id
- year
- month
- day
- amount
- name
- account (N - 1)
- financialTransactions (M - N)
- fileAttachments (1 - N)

## Date Amount

- id
- year
- month
- day
- amount
- account (N - 1)

## File Attachment

- id
- name
- type
- size
- contents
- financialTransaction (N - 0/1)
- bankRecord (N - 0/1)

## Financial Transaction

- id
- year
- month
- day
- amount
- name
- account (N - 1)
- bankRecords (N - M)
- fileAttachments (1 - N)

## Role

- id
- name

## User Entity

- id
- username
- password
- roles (M - N)
- accounts (1 - N)
