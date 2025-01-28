# Controllers

## Status Codes Used

- **200 OK**
- **201 Created**
- **204 No Content**
- **404 Not Found**
- **409 Conflict**
- **500 Internal Server Error**

## Bank Record (_/api/v1/bankrecord_)

**POST** to _/_: creates a new bank record based on the specified body, can return **201** or **500**

**GET** to _/{id}_: gets the bank record with the specified id, can return **200**, **404**, or **500**

**GET** to _/_: gets all of the bank records, can return **200** or **500**

**PUT** to _/{id}_: replaces the bank record with the specified id with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/{id}_: deletes the bank record with the specified id, can return **204**, **404**, or **500**

## Date Record (_/api/v1/daterecord_)

**POST** to _/_: create a new date record based on the specified body, can return **201**, **409**, or **500**

**GET** to _/_: gets all of the date records, can return **200** or **500**

**GET** to _/{year}_: gets all of the date records during the specified year, can return **200** or **500**

**GET** to _/{year}/{month}_: gets all of the date records during the specified month, can return **200** or **500**

**GET** to _/{year}/{month}/{day}_: gets all of the date records during the specified day, can return **200**, **404**, or **500**

**PUT** to _/{year}/{month}/{day}_: replaces the date record for the specified day with the specified body, can return **200**, **404**, or **500**

**DELETE** to _/{year}/{month}/{day}_: deletes the date record with the specified day, can return **204**, **404**, or **500**

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
