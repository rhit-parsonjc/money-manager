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

## Bank Records

**POST** to _/bankrecords/{accountId}_: creates a new bank record for the specified account using the specified body, can return **201**, **404**, or **500**

**GET** to _/bankrecords/{accountId}/{id}_: gets the bank record with the specified id from the specified account, can return **200**, **404**, or **500**

**GET** to _/bankrecords/{accountId}?year={year}&month={month}&day={day}_: gets all of the bank records from the specified account, can filter by year, month, and day, can return **200**, **400**, **404**, or **500**

**PUT** to _/bankrecords/{accountId}/{id}_: replaces the bank record with the specified id from the specified account with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/bankrecords/{accountId}/{id}_: deletes the bank record with the specified id from the specified account, can return **204**, **404**, or **500**

**DELETE** to _/bankrecords/{accountId}_: deletes all bank records from the specified account, can return **204**, **404**, or **500**

## Date Amounts

**POST** to _/dateamounts/{accountId}_: create a new date amount for the specified account using the specified body, can return **201**, **404**, **409**, or **500**

**GET** to _/dateamounts/{accountId}?year={year}&month={month}&day={day}_: gets all of the date amounts from the specified account, can filter by year and month, can return **200**, **400**, **404**, or **500**

**PUT** to _/dateamounts/{accountId}?year={year}&month={month}&day={day}_: replaces the date record for the specified account and day with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/dateamounts/{accountId}?year={year}&month={month}&day={day}_: deletes the date record for the specified account and day, can return **204**, **404**, or **500**

**DELETE** to _/dateamounts/{accountId}_: deletes all date amounts from the specified account, can return **204**, **404**, or **500**

## Financial Transactions

**POST** to _/financialtransactions/{accountId}_: creates a new financial transaction for the specified account using the specified body, can return **201**, **404**, or **500**

**GET** to _/financialtransactions/{accountId}/{id}_: gets the financial transaction with the specified id from the specified account, can return **200**, **404**, or **500**

**GET** to _/financialtransactions/{accountId}?year={year}&month={month}&day={day}_: gets all of the financial transactions from the specified account, can filter by year, month, and day, can return **200**, **400**, **404**, or **500**

**PUT** to _/financialtransactions/{accountId}/{id}_: replaces the financial transaction with the specified id from the specified account with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/financialtransactions/{accountId}/{id}_: deletes the financial transaction with the specified id from the specified account, can return **204**, **404**, or **500**

**DELETE** to _/financialtransactions/{accountId}_: deletes all financial transactions from the specified account, can return **204**, **404**, or **500**

## Record-Transaction Connections

**POST** to _/recordtransactions/{recordId}/{transactionId}_: creates a new connection between a bank record and a financial transaction for the specified account, can return **201**, **404**, **409**, or **500**

**DELETE** to _/recordtransactions/{recordId}/{transactionId}_: deletes a connection between a bank record and a financial transaction for the specified account, can return **204**, **404**, or **500**

## File Attachments

**POST** to _/fileattachments/bankrecords/{recordId}_: creates a new file attached to a bank record, can return **201**, **404**, or **500**

**POST** to _/fileattachments/financialtransactions/{transactionId}_: creates a new file attaced to a financial transaction, can return **201**, **404**, or **500**

**GET** to _/fileattachments/{id}_: gets the file attachent with the specified id, can return **200**, **404**, or **500**

**GET** to _/fileattachments/bankrecords/{recordId}_: gets the file attachments for the specified bank record, can return **200**, **404**, or **500**

**GET** to _/fileattachments/financialtransactions/{transactionId}_: gets the file attachments for the specified financial transaction, can return **200**, **404**, or **500**

**DELETE** to _/fileattachments/{id}_: deletes the file attachment with the specified id, can return **204**, **404**, or **500**

**DELETE** to _/fileattachments/bankrecords/{recordId}_: deletes the file attachments for the specified bank record, can return **204**, **404**, or **500**

**DELETE** to _/fileattachments/financialtransactions/{transactionId}_: deletes the file attachments for the specified financial transaction, can return **204**, **404**, or **500**

# Models

## Bank Record

- id
- year
- month
- day
- amount
- name

## Date Record

- id
- year
- month
- day
- amount

## Financial Transaction

- id
- year
- month
- day
- amount
- name
