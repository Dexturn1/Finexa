package com.finexa.finexa.transaction.services;


import com.finexa.finexa.account.entity.Account;
import com.finexa.finexa.account.repo.AccountRepo;
import com.finexa.finexa.account.services.AccountService;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.services.UserService;
import com.finexa.finexa.enums.TransactionStatus;
import com.finexa.finexa.enums.TransactionType;
import com.finexa.finexa.exceptions.BadRequestException;
import com.finexa.finexa.exceptions.InsufficientBalanceException;
import com.finexa.finexa.exceptions.InvalidTransactionException;
import com.finexa.finexa.exceptions.NotFoundException;
import com.finexa.finexa.notification.dtos.NotificationDTO;
import com.finexa.finexa.notification.services.NotificationService;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import com.finexa.finexa.transaction.dtos.TransactionRequest;
import com.finexa.finexa.transaction.entity.Transaction;
import com.finexa.finexa.transaction.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Transactional
    public Response<?> createTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = new Transaction();

        transaction.setTransactionType(transactionRequest.getTransactionType());

        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDestinationAccountNumber());


        switch (transactionRequest.getTransactionType()) {

            case DEPOSIT -> handleDeposit(transactionRequest, transaction);

            case WITHDRAWAL -> handleWithdrawal(transactionRequest, transaction);

            case TRANSFER -> handleTransfer(transactionRequest, transaction);

            default -> throw new InvalidTransactionException("Invalid transaction type");
        }


        transaction.setStatus(TransactionStatus.SUCCESS);

        Transaction savedTransaction = transactionRepo.save(transaction);


        // send Notification out
        sendTransactionNotifications(savedTransaction);


        return Response.builder()
                .statusCode(200)
                .message("Transaction successful")
                .build();

    }


    @Override
    public Response<List<TransactionDTO>> getTransactionForMyAccount(String accountNumber, int page, int size) {


        User user = userService.getCurrentLoggedInUser();


        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));


        if (!account.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Account does not belong to the authenticated user");
        }


        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("transactionDate").descending()
        );


        Page<Transaction> transactions =
                transactionRepo.findByAccount_AccountNumber(accountNumber, pageable);



        List<TransactionDTO> transactionDTOS = transactions.getContent()
                .stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .toList();



        return Response.<List<TransactionDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction retrieved")
                .data(transactionDTOS)
                .meta(Map.of(
                        "currentPage", transactions.getNumber(),
                        "totalItems", transactions.getTotalElements(),
                        "totalPages", transactions.getTotalPages(),
                        "pageSize", transactions.getSize()
                ))
                .build();

    }


    private void handleDeposit(TransactionRequest request, Transaction transaction) {


        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));


        account.setBalance(
                account.getBalance().add(request.getAmount())
        );


        transaction.setAccount(account);


        accountRepo.save(account);

    }



    private void handleWithdrawal(TransactionRequest request, Transaction transaction) {


        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));



        if (account.getBalance().compareTo(request.getAmount()) < 0) {

            throw new InsufficientBalanceException("Insufficient balance");

        }


        account.setBalance(
                account.getBalance().subtract(request.getAmount())
        );


        transaction.setAccount(account);


        accountRepo.save(account);

    }



    private void handleTransfer(TransactionRequest request, Transaction transaction) {


        Account sourceAccount = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));



        Account destinationAccount = accountRepo.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));



        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance in source account"
            );

        }



        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(request.getAmount())
        );


        accountRepo.save(sourceAccount);



        destinationAccount.setBalance(
                destinationAccount.getBalance().add(request.getAmount())
        );


        accountRepo.save(destinationAccount);



        transaction.setAccount(sourceAccount);

        transaction.setSourceAccount(
                sourceAccount.getAccountNumber()
        );

        transaction.setDestinationAccount(
                destinationAccount.getAccountNumber()
        );

    }




    private void sendTransactionNotifications(Transaction tnx) {


        User user = tnx.getAccount().getUser();



        String subject;

        String template;



        Map<String, Object> templateVariables = new HashMap<>();

        templateVariables.put("name", user.getFirstName());

        templateVariables.put("amount", tnx.getAmount());

        templateVariables.put(
                "accountNumber",
                tnx.getAccount().getAccountNumber()
        );

        templateVariables.put(
                "date",
                tnx.getTransactionDate()
        );

        templateVariables.put(
                "balance",
                tnx.getAccount().getBalance()
        );



        if (tnx.getTransactionType() == TransactionType.DEPOSIT) {


            subject = "Credit Alert";

            template = "credit-alert";


            NotificationDTO notificationEmailToSendout =
                    NotificationDTO.builder()
                            .recipient(user.getEmail())
                            .subject(subject)
                            .templateName(template)
                            .templateVariables(templateVariables)
                            .build();


            notificationService.sendEmail(
                    notificationEmailToSendout,
                    user
            );


        } else if (tnx.getTransactionType() == TransactionType.WITHDRAWAL) {


            subject = "Debit Alert";

            template = "debit-alert";


            NotificationDTO notificationEmailToSendout =
                    NotificationDTO.builder()
                            .recipient(user.getEmail())
                            .subject(subject)
                            .templateName(template)
                            .templateVariables(templateVariables)
                            .build();


            notificationService.sendEmail(
                    notificationEmailToSendout,
                    user
            );


        } else if (tnx.getTransactionType() == TransactionType.TRANSFER) {


            subject = "Debit Alert";

            template = "debit-alert";



            NotificationDTO notificationEmailToSendout =
                    NotificationDTO.builder()
                            .recipient(user.getEmail())
                            .subject(subject)
                            .templateName(template)
                            .templateVariables(templateVariables)
                            .build();



            notificationService.sendEmail(
                    notificationEmailToSendout,
                    user
            );



            Account destination =
                    accountRepo.findByAccountNumber(tnx.getDestinationAccount())
                            .orElseThrow(
                                    () -> new NotFoundException(
                                            "Destination account not found"
                                    )
                            );



            User receiver = destination.getUser();



            Map<String, Object> recvVars = new HashMap<>();

            recvVars.put(
                    "name",
                    receiver.getFirstName()
            );

            recvVars.put(
                    "amount",
                    tnx.getAmount()
            );

            recvVars.put(
                    "accountNumber",
                    destination.getAccountNumber()
            );

            recvVars.put(
                    "date",
                    tnx.getTransactionDate()
            );

            recvVars.put(
                    "balance",
                    destination.getBalance()
            );



            NotificationDTO notificationEmailToSendoutToReceiver =
                    NotificationDTO.builder()
                            .recipient(receiver.getEmail())
                            .subject("Credit Alert")
                            .templateName("credit-alert")
                            .templateVariables(recvVars)
                            .build();



            notificationService.sendEmail(
                    notificationEmailToSendoutToReceiver,
                    receiver
            );

        }

    }

}