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

**POST** to _/bankrecords_: creates a new bank record based on the specified body, can return **201** or **500**

**GET** to _/bankrecords/{id}_: gets the bank record with the specified id, can return **200**, **404**, or **500**

**GET** to _/bankrecords?year={year}&month={month}&day={day}_: gets all of the bank records, can filter by year, month, and day, can return **200**, **400**, or **500**

**PUT** to _/bankrecords/{id}_: replaces the bank record with the specified id with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/bankrecords/{id}_: deletes the bank record with the specified id, can return **204**, **404**, or **500**

**DELETE** to _/bankrecords_: deletes all bank records, can return **204** or **500**

## Date Amounts (_/api/v1/daterecord_)

**POST** to _/dateamounts_: create a new date amount based on the specified body, can return **201**, **409**, or **500**

**GET** to _/dateamounts?year={year}&month={month}&day={day}_: gets all of the date amounts, can filter by year and month, can return **200**, **400**, or **500**

**PUT** to _/dateamounts/{year}/{month}/{day}_: replaces the date record for the specified day with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/dateamounts/{year}/{month}/{day}_: deletes the date record with the specified day, can return **204**, **404**, or **500**

**DELETE** to _/dateamounts_: deletes all date amounts, can return **204** or **500**

## Financial Transactions

**POST** to _/financialtransactions_: creates a new financial transaction based on the specified body, can return **201** or **500**

**GET** to _/financialtransactions/{id}_: gets the financial transaction with the specified id, can return **200**, **404**, or **500**

**GET** to _/financialtransactions?year={year}&month={month}&day={day}_: gets all of the financial transactions, can filter by year, month, and day, can return **200**, **400**, or **500**

**PUT** to _/financialtransactions/{id}_: replaces the financial transaction with the specified id with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/financialtransactions/{id}_: deletes the financial transaction with the specified id, can return **204**, **404**, or **500**

**DELETE** to _/financialtransactions_: deletes all financial transactions, can return **204** or **500**

## Record-Transaction Connections

**POST** to _/recordtransactions/{recordId}/{transactionId}_: creates a new connection between a bank record and a financial transaction, can return **201**, **404**, **409**, or **500**

**DELETE** to _/recordtransactions/{recordId}/{transactionId}_: deletes a connection between a bank record and a financial transaction, can return **204**, **404**, or **500**

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
