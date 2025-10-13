package com.cintia.desafio.Contas.controller;

import com.cintia.desafio.Contas.dto.AccountRequestDTO;
import com.cintia.desafio.Contas.dto.AccountResponseDTO;
import com.cintia.desafio.Contas.model.Account;
import com.cintia.desafio.Contas.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conta")
@Tag(name = "Contas a pagar", description = "Endpoints para criar e listar contas.")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(description = "Inclui uma conta a pagar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna que a conta foi cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Retorna qual campo ou campos estão com os dados incorretos")
    })
    @PostMapping("/pagamento")
    public ResponseEntity<Account> savePaidAccount(@RequestBody @Valid AccountRequestDTO accountDTO){
        try {
            Account savedAccount = accountService.savePaidAccount(accountDTO);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(description = "Lista todos os registros das contas pagas")
    @ApiResponse(responseCode = "200", description = "Retorna lista de contas pagas, ou uma lista vazia")
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> listRegisteredAccounts(){
        List<AccountResponseDTO> accountResponseDTOS = accountService.listRegisteredAccounts();
        return new ResponseEntity<>(accountResponseDTOS, HttpStatus.OK);
    }
}
