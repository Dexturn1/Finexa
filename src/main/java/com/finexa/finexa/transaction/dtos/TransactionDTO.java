package com.finexa.finexa.transaction.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.finexa.finexa.account.dtos.AccountDTO;
import com.finexa.finexa.account.entity.Account;
import com.finexa.finexa.enums.TransactionStatus;
import com.finexa.finexa.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;

    private BigDecimal amount;


    private TransactionType transactionType;


    private LocalDateTime transactionDate;

    private String description;

    private TransactionStatus status;


    @JsonBackReference
    private AccountDTO account;

    // for transfer
    private String sourceAccount;
    private String destinationAccount;
}
