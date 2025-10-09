package com.cintia.desafio.Contas.controller;

import com.cintia.desafio.Contas.dto.AccountRequestDTO;
import com.cintia.desafio.Contas.dto.AccountResponseDTO;
import com.cintia.desafio.Contas.model.Account;
import com.cintia.desafio.Contas.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    private AccountRequestDTO createValidAccountDTO() {
        return new AccountRequestDTO(
                "Conta de Teste",
                new BigDecimal("100.00"),
                LocalDate.of(2025, Month.OCTOBER, 10),
                LocalDate.of(2025, Month.OCTOBER, 10)
        );
    }
    private Account createSavedAccountEntity() {
        Account account = new Account();
        account.setId(1L);
        account.setName("Conta de Teste");
        account.setOriginalValue(new BigDecimal("100.00"));
        account.setDueDate(LocalDate.of(2025, Month.OCTOBER, 10));
        account.setPaymentDate(LocalDate.of(2025, Month.OCTOBER, 10));
        account.setAdjustedAmount(new BigDecimal("100.00"));
        account.setDaysLate(0);
        return account;
    }

    @Test
    void mustReturn201Created_WhenAccountIsSavedSuccessfully() throws Exception {
        AccountRequestDTO requestDTO = createValidAccountDTO();
        Account savedEntity = createSavedAccountEntity();

        when(accountService.savePaidAccount(any(AccountRequestDTO.class)))
                .thenReturn(savedEntity);

        mockMvc.perform(post("/api/conta/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Espera o status 201 CREATED
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Conta de Teste"))
                .andExpect(jsonPath("$.adjustedAmount").value(100.00))
                .andExpect(jsonPath("$.daysLate").value(0));
    }

    @Test
    void mustReturn400BadRequest_WhenServiceThrowsIllegalArgumentException() throws Exception {
        AccountRequestDTO requestDTO = createValidAccountDTO();

        when(accountService.savePaidAccount(any(AccountRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Erro de regra de negócio no Service."));

        mockMvc.perform(post("/api/conta/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());
    }


    @Test
    void mustReturn400BadRequest_WhenDTOIsInvalid_MissingName() throws Exception {
        AccountRequestDTO invalidDTO = new AccountRequestDTO(
                null, // Nome nulo
                new BigDecimal("100.00"),
                LocalDate.of(2025, Month.OCTOBER, 10),
                LocalDate.of(2025, Month.OCTOBER, 10)
        );

        mockMvc.perform(post("/api/conta/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void mustReturn200Ok_WhenListingAccounts() throws Exception {
        AccountResponseDTO dto = new AccountResponseDTO("Conta Teste", new BigDecimal("100.00"), LocalDate.now(), new BigDecimal("100.00"), 0);
        List<AccountResponseDTO> mockList = Collections.singletonList(dto);

        when(accountService.listRegisteredAccounts()).thenReturn(mockList);

        mockMvc.perform(get("/api/conta")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Conta Teste"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void mustReturn200OkAndEmptyList_WhenNoAccountsAreFound() throws Exception {
        when(accountService.listRegisteredAccounts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/conta")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}