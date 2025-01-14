# Loading Data

Data is loaded asynchronously from the back end.
This requires particular care to ensure proper concurrency.

## Data States

- Not Loaded: No data has been loaded.
- Loading: A request for data has been sent.
- Error: The request for data has failed.
- Loaded: The data has been loaded and is still valid.
- Expired: The data that has been loaded is no longer valid.

Not Loaded and Expired can be viewed the same.

## Data Guidelines

1. Originally upon visiting a page, the data is not loaded.
2. Whenever data is needed, it should go into a Loading state.
3. When the data loads, it should go into an Error or Loaded state.
4. Whenever any data is modified, the data loaded should be Expired.

## Steps to Load Data

### Initial Visit

Initially upon visiting the site,

1. The data is marked as Not Loaded.
2. A request is sent to the database to load data.
3. The data is marked as Loading.
4. The data is returned.
5. The data is marked as Loaded.

Alternatively,

4. The data fails to return.
5. The data is marked as Error.

### From the Same Page

If there is an edit on the same page that does not involve a redirection,
the following steps should occur:

1. The data is marked as Expired.
2. A request is sent to the database to load data.
3. The data is marked as Loading.
4. The data is returned.
5. The data is marked as Loaded.

Alternatively,

4. The data fails to return.
5. The data is marked as Error.

### From one Page to Another

If there is an edit on a page that causes a redirect to a new page,
the following steps should occur:

1. The data is marked as Expired.
2. The page redirection occurs.
3. The data is marked as Not Loaded.
4. A request is sent to the database to load data.
5. The data is marked as Loading.
6. The data is returned.
7. The data is marked as Loaded.

Alternatively,

6. The data fails to return.
7. The data is marked as Error.

### Other Considerations

If, somehow, multiple requests are pending to the database, all prior requests for data should be cancelled.

## Rules to Follow

In order to allow these steps to be followed in this order,
follow the following rules:

1. Whenever a request is sent to the database to load data, once it suceeds, mark the data as Loaded.
2. Whenever a request is sent to the database to load data, if it fails, mark the data as Error.
3. Whenever the data's status is Not Loaded, request the data from the database.
4. Whenever an update is made to the data, mark the data as Expired.
5. Redirect to a page before marking the data as Not Loaded.
