package com.cintia.desafio.Contas.service;

import com.cintia.desafio.Contas.dto.AccountRequestDTO;
import com.cintia.desafio.Contas.model.Account;
import com.cintia.desafio.Contas.repository.AccountRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper mapper;

    private static final BigDecimal FINE_UP_TO_3 = new BigDecimal("0.02");
    private static final BigDecimal INTEREST_UP_TO_3 = new BigDecimal("0.001");

    private static final BigDecimal FINE_EXCEEDING_3 = new BigDecimal("0.03");
    private static final BigDecimal INTEREST_EXCEEDING_3 = new BigDecimal("0.002");

    private static final BigDecimal FINE_EXCEEDING_5 = new BigDecimal("0.05");
    private static final BigDecimal INTEREST_EXCEEDING_5 = new BigDecimal("0.003");

    private static final int SCALE = 2;


    @Autowired
    public AccountService(AccountRepository accountRepository, ModelMapper mapper) {
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public Account savePaidAccount(@Valid AccountRequestDTO accountDTO){
        Account accountEntity = mapper.map(accountDTO, Account.class);

        LocalDate paymentDate = accountDTO.getPaymentDate();
        LocalDate dueDate = accountDTO.getDueDate();

        if (paymentDate.isAfter(dueDate)){
            long daysLate = ChronoUnit.DAYS.between(dueDate, paymentDate);
            accountEntity.setDaysLate((int) daysLate);
            latePaymentRules(accountEntity);
            accountRepository.save(accountEntity);
        }else {
            accountEntity.setDaysLate(0);
            accountEntity.setAdjustedAmount(accountDTO.getOriginalValue());
            accountRepository.save(accountEntity);
        }
        return accountEntity;
    }

    private void latePaymentRules(Account accountEntity){
        int daysLate = accountEntity.getDaysLate();
        BigDecimal originalValue = accountEntity.getOriginalValue();

        BigDecimal percentageFine;
        BigDecimal percentageInterestPerDay;
        BigDecimal adjustedAmount;

        if (daysLate <= 3){
            percentageFine = FINE_UP_TO_3;
            percentageInterestPerDay = INTEREST_UP_TO_3;
            adjustedAmount = calculateCorrectedValue(originalValue, percentageFine, percentageInterestPerDay, daysLate);
        } else if (daysLate <= 5){
            percentageFine = FINE_EXCEEDING_3;
            percentageInterestPerDay = INTEREST_EXCEEDING_3;
            adjustedAmount = calculateCorrectedValue(originalValue, percentageFine, percentageInterestPerDay, daysLate);
        }else {
            percentageFine = FINE_EXCEEDING_5;
            percentageInterestPerDay = INTEREST_EXCEEDING_5;
            adjustedAmount = calculateCorrectedValue(originalValue, percentageFine, percentageInterestPerDay, daysLate);
        }
        accountEntity.setAdjustedAmount(adjustedAmount);
    }

    private BigDecimal calculateCorrectedValue(BigDecimal originalValue, BigDecimal percentageFine,
                                              BigDecimal percentageInterestPerDay, int daysLate ){

        BigDecimal valueFine = originalValue.multiply(percentageFine);
        BigDecimal valueInterestPerDay = originalValue.multiply(percentageInterestPerDay);
        BigDecimal totalInterestAmount = valueInterestPerDay.multiply(BigDecimal.valueOf(daysLate));

        return originalValue.add(valueFine).add(totalInterestAmount).setScale(SCALE, RoundingMode.HALF_UP);
    }

}
