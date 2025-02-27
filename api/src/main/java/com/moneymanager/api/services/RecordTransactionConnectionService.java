package com.moneymanager.api.services;

public interface RecordTransactionConnectionService {
    public void createConnection(Long recordId, Long transactionId);
    public void deleteConnection(Long recordId, Long transactionId);
}
