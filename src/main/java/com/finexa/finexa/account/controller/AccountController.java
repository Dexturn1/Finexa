package com.finexa.finexa.account.controller;


import com.finexa.finexa.account.services.AccountService;
import com.finexa.finexa.enums.AccountStatus;
import com.finexa.finexa.res.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<Response<?>> getMyAccount(){
        return ResponseEntity.ok(accountService.getMyAccounts());
    }


    @DeleteMapping("/cose/{accountNumber}")
    public ResponseEntity<Response<?>> closeAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(accountService.closeAccount(accountNumber));
    }

}
