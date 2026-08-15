package com.finexa.finexa.audit_dashboard.service;

import com.finexa.finexa.account.dtos.AccountDTO;
import com.finexa.finexa.auth_users.dtos.UserDTO;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import com.finexa.finexa.transaction.entity.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {


    Map<String, Long> getSystemTotals();
    Optional<UserDTO> findUserByEmail(String email);
    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);
    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);
    Optional<TransactionDTO> findTransactionById(Long transactionId);

}
