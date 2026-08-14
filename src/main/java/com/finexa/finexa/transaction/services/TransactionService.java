package com.finexa.finexa.transaction.services;

import com.finexa.finexa.res.Response;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import com.finexa.finexa.transaction.dtos.TransactionRequest;
import com.finexa.finexa.transaction.entity.Transaction;
import com.finexa.finexa.transaction.repo.TransactionRepo;

import java.util.List;

public interface TransactionService {

    Response<?>createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDTO>> getTransactionForMyAccount(String accountNumber, int page, int size);



}
