package com.finexa.finexa.transaction.services;


import com.finexa.finexa.account.entity.Account;
import com.finexa.finexa.account.repo.AccountRepo;
import com.finexa.finexa.account.services.AccountService;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.services.UserService;
import com.finexa.finexa.enums.TransactionStatus;
import com.finexa.finexa.exceptions.InsufficientBalanceException;
import com.finexa.finexa.exceptions.InvalidTransactionException;
import com.finexa.finexa.exceptions.NotFoundException;
import com.finexa.finexa.notification.services.NotificationService;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import com.finexa.finexa.transaction.dtos.TransactionRequest;
import com.finexa.finexa.transaction.entity.Transaction;
import com.finexa.finexa.transaction.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepo transactionRepo;

    private final AccountService accountService;

    private final NotificationService notificationService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final AccountRepo accountRepo;


    @Override
    public Response<?> createTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = new Transaction();

        transaction.setTransactionType(transactionRequest.getTransactionType());

        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDestinationAccountNumber());



        switch (transactionRequest.getTransactionType()){
            case DEPOSIT -> handleDeposit(transactionRequest, transaction);
            case WITHDRAWAL -> handleWithdrawal(transactionRequest, transaction);
            case TRANSFER -> handleTransfer(transactionRequest, transaction);
            default -> throw new InvalidTransactionException("Invalid transaction type");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        Transaction savedTransaction = transactionRepo.save(transaction);


        // send Notification out
        sendTransactionNotifications(savedTxn);

        return Response.builder()
                .statusCode(200)
                .message("Transfer successful")
                .build();



    }

    @Override
    public Response<List<TransactionDTO>> getTransactionForMyAccount(String accountNumber, int page, int size) {
        return null;
    }


    private void handleDeposit(TransactionRequest request, Transaction transaction){

        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(()-> new NotFoundException("Account not found"));


        account.setBalance(account.getBalance().add(request.getAmount()));
        transaction.setAccount(account);

        accountRepo.save(account);

    }


    private void handleWithdrawal(TransactionRequest request, Transaction transaction){
        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(()-> new NotFoundException("Account not found"));

        if((account.getBalance().compareTo(request.getAmount())  < 0 ) ){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);
    }


    private void handleTransfer(TransactionRequest request, Transaction transaction){

        Account sourceAccount = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(()-> new NotFoundException("Account not found"));

        Account destinationAccount = accountRepo.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(()-> new NotFoundException("Account not found"));


        if(sourceAccount.getBalance().compareTo(request.getAmount()) < 0)
                throw new InsufficientBalanceException("Insufficient balance in source account");

        // deduct from source
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        accountRepo.save(sourceAccount);

        // add to destination
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
        accountRepo.save(destinationAccount);

        transaction.setAccount(sourceAccount);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setSourceAccount(destinationAccount.getAccountNumber());



    }
}
