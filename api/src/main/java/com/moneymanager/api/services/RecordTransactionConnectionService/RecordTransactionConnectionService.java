package com.moneymanager.api.services.RecordTransactionConnectionService;

public interface RecordTransactionConnectionService {
    void createConnection(Long recordId, Long transactionId);
    void deleteConnection(Long recordId, Long transactionId);
}
