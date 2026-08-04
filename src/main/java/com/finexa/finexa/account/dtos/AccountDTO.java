package com.finexa.finexa.account.dtos;


import com.fasterxml.jackson.annotation.*;
import com.finexa.finexa.auth_users.dtos.UserDTO;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.enums.AccountStatus;
import com.finexa.finexa.enums.AccountType;
import com.finexa.finexa.enums.Currency;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {



    private Long id;


    private String accountNumber;

    private BigDecimal balance;

    private AccountType accountType;

    @JsonBackReference // This Will not be added to the account dta. It will be ignored because it is a back refernce
    
    private UserDTO user;

    private Currency currency;

    private AccountStatus status;


    @JsonManagedReference // it helps avoid recursion loop by ignoring the AccountDTO within the TransactionDTO
    private List<TransactionDTO>transactions;

    private LocalDateTime closedAT;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt;

}























