package com.cintia.desafio.Contas.controller;

import com.cintia.desafio.Contas.dto.AccountRequestDTO;
import com.cintia.desafio.Contas.dto.AccountResponseDTO;
import com.cintia.desafio.Contas.model.Account;
import com.cintia.desafio.Contas.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/conta")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/pagamento")
    public ResponseEntity<Account> savePaidAccount(@RequestBody @Valid AccountRequestDTO accountDTO){
        try {
            Account savedAccount = accountService.savePaidAccount(accountDTO);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> listRegisteredAccounts(){
        List<AccountResponseDTO> accountResponseDTOS = accountService.listRegisteredAccounts();
        return new ResponseEntity<>(accountResponseDTOS, HttpStatus.OK);
    }
}
