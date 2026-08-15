package com.finexa.finexa.audit_dashboard.controller;


import com.finexa.finexa.account.dtos.AccountDTO;
import com.finexa.finexa.audit_dashboard.service.AuditorService;
import com.finexa.finexa.auth_users.dtos.UserDTO;
import com.finexa.finexa.transaction.dtos.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUDITOR')")
public class AuditorController {

    private final AuditorService auditorService;

    @GetMapping("/totals")
    public ResponseEntity<Map<String, Long>> getSystemTotals(){
        return ResponseEntity.ok(auditorService.getSystemTotals());
    }

    @GetMapping("/users")
    public ResponseEntity<UserDTO> findUserByEmail(@RequestParam String email){
        Optional<UserDTO> userDTO = auditorService.findUserByEmail(email);

        return userDTO.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountDTO> findUserByAccountNumber(@RequestParam String accountNumber){
        Optional<AccountDTO> accountDTO = auditorService.findAccountDetailsByAccountNumber(accountNumber);

        return accountDTO.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/transactions/by-account")
    public ResponseEntity<List<TransactionDTO>> findTransactionByAccountNumber(@RequestParam String accountNumber){
        List<TransactionDTO> transactionDTOSList = auditorService.findTransactionsByAccountNumber(accountNumber);
        if(transactionDTOSList.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(transactionDTOSList);
    }



    @GetMapping("/transactions/by-id")
    public ResponseEntity<TransactionDTO> findTransactionById(@RequestParam Long id){

        Optional<TransactionDTO> transactionDTO = auditorService.findTransactionById(id);
        
        return transactionDTO.map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }






}
