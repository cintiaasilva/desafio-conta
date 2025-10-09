package com.cintia.desafio.Contas.service;

import com.cintia.desafio.Contas.dto.AccountRequestDTO;
import com.cintia.desafio.Contas.dto.AccountResponseDTO;
import com.cintia.desafio.Contas.model.Account;
import com.cintia.desafio.Contas.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AccountService accountService;

    private AccountRequestDTO createAccountRequestDTO(BigDecimal originalValue, LocalDate dueDate, LocalDate paymentDate) {
        return new AccountRequestDTO("Conta Teste", originalValue, dueDate, paymentDate);
    }
    private Account createAccountEntity(String name, BigDecimal originalValue, LocalDate dueDate, LocalDate paymentDate) {
        Account entity = new Account();
        entity.setName(name);
        entity.setDueDate(dueDate);
        entity.setOriginalValue(originalValue);
        entity.setPaymentDate(paymentDate);
        return entity;
    }

    @Test
    void mustIncludePaidAccount_WhenPaidOnTime() {
        BigDecimal originalValue = new BigDecimal("100.00");
        LocalDate dueDate = LocalDate.of(2025, Month.OCTOBER, 10);
        LocalDate paymentDate = LocalDate.of(2025, Month.OCTOBER, 10);
        AccountRequestDTO accountDTO = createAccountRequestDTO(originalValue, dueDate, paymentDate);
        Account accountEntity = createAccountEntity(accountDTO.getName(), originalValue, dueDate, paymentDate);

        when(mapper.map(any(AccountRequestDTO.class), any(Class.class))).thenReturn(accountEntity);

        accountService.savePaidAccount(accountDTO);

        verify(accountRepository, times(1)).save(accountEntity);
        assertEquals(originalValue.setScale(2, RoundingMode.HALF_UP), accountEntity.getAdjustedAmount(),
                "O valor ajustado deve ser igual ao original");
        assertEquals(0, accountEntity.getDaysLate(), "Os dias de atraso devem ser zero");
    }

    @Test
    void mustIncludePaidAccount_WhenLateUpTo3Days() {
        LocalDate dueDate = LocalDate.of(2025, Month.OCTOBER, 10);
        LocalDate paymentDate = LocalDate.of(2025, Month.OCTOBER, 12);
        BigDecimal originalValue = new BigDecimal("100.00");
        int expectedDaysLate = 2;
        BigDecimal expectedAdjustedValue = new BigDecimal("102.20").setScale(2, RoundingMode.HALF_UP);

        AccountRequestDTO accountDTO = createAccountRequestDTO(originalValue, dueDate, paymentDate);
        Account accountEntity = createAccountEntity(accountDTO.getName(), originalValue, dueDate, paymentDate);

        when(mapper.map(any(AccountRequestDTO.class), any(Class.class))).thenReturn(accountEntity);

        accountService.savePaidAccount(accountDTO);

        assertEquals(expectedDaysLate, accountEntity.getDaysLate(), "Os dias de atraso devem ser 2");
        assertEquals(expectedAdjustedValue, accountEntity.getAdjustedAmount(), "O valor ajustado deve ser R$ 102,20");
        verify(accountRepository, times(1)).save(accountEntity);
    }

    @Test
    void mustIncludePaidAccount_WhenLateBetween4And5Days() {
        LocalDate dueDate = LocalDate.of(2025, Month.OCTOBER, 10);
        LocalDate paymentDate = LocalDate.of(2025, Month.OCTOBER, 15);
        BigDecimal originalValue = new BigDecimal("100.00");
        int expectedDaysLate = 5;
        BigDecimal expectedAdjustedValue = new BigDecimal("104.00").setScale(2, RoundingMode.HALF_UP);

        AccountRequestDTO accountDTO = createAccountRequestDTO(originalValue, dueDate, paymentDate);
        Account accountEntity = createAccountEntity(accountDTO.getName(), originalValue, dueDate, paymentDate);

        when(mapper.map(any(AccountRequestDTO.class), any(Class.class))).thenReturn(accountEntity);

        accountService.savePaidAccount(accountDTO);

        assertEquals(expectedDaysLate, accountEntity.getDaysLate(), "Os dias de atraso devem ser 5");
        assertEquals(expectedAdjustedValue, accountEntity.getAdjustedAmount(), "O valor ajustado deve ser R$ 104.00");
        verify(accountRepository, times(1)).save(accountEntity);
    }

    @Test
    void mustIncludePaidAccount_WhenLateExceeds5Days() {
        // GIVEN (Dado)
        LocalDate dueDate = LocalDate.of(2025, Month.OCTOBER, 10);
        LocalDate paymentDate = LocalDate.of(2025, Month.OCTOBER, 16); // 6 dias de atraso
        BigDecimal originalValue = new BigDecimal("100.00");
        int expectedDaysLate = 6;
        BigDecimal expectedAdjustedValue = new BigDecimal("106.80").setScale(2, RoundingMode.HALF_UP);

        AccountRequestDTO accountDTO = createAccountRequestDTO(originalValue, dueDate, paymentDate);
        Account accountEntity = createAccountEntity(accountDTO.getName(), originalValue, dueDate, paymentDate);

        when(mapper.map(any(AccountRequestDTO.class), any(Class.class))).thenReturn(accountEntity);

        accountService.savePaidAccount(accountDTO);

        assertEquals(expectedDaysLate, accountEntity.getDaysLate(), "Os dias de atraso devem ser 6");
        assertEquals(expectedAdjustedValue, accountEntity.getAdjustedAmount(), "O valor ajustado deve ser R$ 106.80");
        verify(accountRepository, times(1)).save(accountEntity);
    }
    private Account createAccountEntity(String name, BigDecimal originalValue , LocalDate dueDate) {
        Account entity = new Account();
        entity.setName(name);
        entity.setDueDate(dueDate);
        entity.setOriginalValue(originalValue);
        entity.setAdjustedAmount(originalValue);
        entity.setDaysLate(0);
        return entity;
    }
    private AccountResponseDTO createAccountResponseDTO(String name, BigDecimal adjustedValue, int daysLate) {
        return new AccountResponseDTO(name, new BigDecimal("100.00"), LocalDate.now(), adjustedValue, daysLate);
    }


    @Test
    void mustReturnListOfAccounts_WhenListIsNotEmpty() {
        Account entity1 = createAccountEntity("Conta A", new BigDecimal("100.00"), LocalDate.now().minusDays(5));
        Account entity2 = createAccountEntity("Conta B",  new BigDecimal("200.00"), LocalDate.now().minusDays(1));
        List<Account> accountList = Arrays.asList(entity1, entity2);
        AccountResponseDTO dto1 = createAccountResponseDTO("Conta A", new BigDecimal("100.00"), 0);
        AccountResponseDTO dto2 = createAccountResponseDTO("Conta B", new BigDecimal("200.00"), 0);

        when(accountRepository.findAll()).thenReturn(accountList);

        when(mapper.map(entity1, AccountResponseDTO.class)).thenReturn(dto1);
        when(mapper.map(entity2, AccountResponseDTO.class)).thenReturn(dto2);

        List<AccountResponseDTO> resultList = accountService.listRegisteredAccounts();

        assertNotNull(resultList, "A lista não deve ser nula.");
        assertEquals(2, resultList.size(), "A lista deve conter 2 elementos.");
        verify(accountRepository, times(1)).findAll();
        verify(mapper, times(2)).map(any(Account.class), eq(AccountResponseDTO.class));
        assertEquals(dto1.getName(), resultList.get(0).getName());
        assertEquals(dto2.getName(), resultList.get(1).getName());
    }

    @Test
    void mustReturnEmptyList_WhenNoAccountsAreRegistered() {
        List<Account> emptyList = Collections.emptyList();

        when(accountRepository.findAll()).thenReturn(emptyList);

        List<AccountResponseDTO> resultList = accountService.listRegisteredAccounts();

        verify(accountRepository, times(1)).findAll();
        verify(mapper, never()).map(any(), any());
        assertNotNull(resultList, "A lista não deve ser nula, deve ser vazia.");
        assertTrue(resultList.isEmpty(), "A lista deve estar vazia.");
    }

}
