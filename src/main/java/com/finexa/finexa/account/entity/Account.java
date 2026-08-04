package com.finexa.finexa.account.entity;


import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.enums.AccountStatus;
import com.finexa.finexa.enums.AccountType;
import com.finexa.finexa.enums.Currency;
import com.finexa.finexa.transaction.entity.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.GeneratedReferenceTypeDelegate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction>transactions =  new ArrayList<>();


    private LocalDateTime closedAT;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updateAt;


}























