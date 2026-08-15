package com.finexa.finexa.transaction.controller;


import com.finexa.finexa.enums.TransactionStatus;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.transaction.dtos.TransactionRequest;
import com.finexa.finexa.transaction.entity.Transaction;
import com.finexa.finexa.transaction.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Response<?>> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest){
        return ResponseEntity.ok(transactionService.createTransaction(transactionRequest));
    }


    @GetMapping("/{accountNumber}")
    public ResponseEntity<Response<?>> createTransaction(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size){

        return ResponseEntity.ok(transactionService.getTransactionForMyAccount(accountNumber, page, size));
    }

}
