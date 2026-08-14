package com.finexa.finexa.transaction.services;


import com.finexa.finexa.account.services.AccountService;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.services.UserService;
import com.finexa.finexa.notification.services.NotificationService;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import com.finexa.finexa.transaction.dtos.TransactionRequest;
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


    @Override
    public Response<?> createTransaction(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public Response<List<TransactionDTO>> getTransactionForAccount(String accountNumber, int page, int size) {
        return null;
    }
}
