package com.finexa.finexa.account.services;

import com.finexa.finexa.account.dtos.AccountDTO;
import com.finexa.finexa.account.entity.Account;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.enums.AccountType;
import com.finexa.finexa.res.Response;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountType accountType, User user);

    Response<List<AccountDTO>> getMyAccount();

    Response<?> closeAccount(String accountNumber);




}
