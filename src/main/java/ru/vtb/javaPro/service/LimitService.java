package ru.vtb.javaPro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vtb.javaPro.config.properties.LimitProperties;
import ru.vtb.javaPro.dto.LimitDto;
import ru.vtb.javaPro.entity.Limits;
import ru.vtb.javaPro.exception.ExceptionRequest;
import ru.vtb.javaPro.repository.LimitRepository;

import java.math.BigDecimal;

@Slf4j
@Service
public class LimitService {

    private final LimitRepository limitRepository;
    private final LimitProperties limitProperties;
    private final BigDecimal limitDefaultAmount;

    public LimitService(LimitRepository limitRepository, LimitProperties limitProperties) {
        this.limitRepository = limitRepository;
        this.limitProperties = limitProperties;
        this.limitDefaultAmount = limitProperties.getAmount();
    }

    private Limits getLimits(LimitDto limitDto) {
        Limits limits;
        try {
            limits = limitRepository.findById(limitDto.userId()).orElseThrow(EntityNotFoundException::new);
            if ((limits.getLimitSumma() == null) || (limits.getBlockAmount() == null)) {
                throw new ExceptionRequest(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка проверки: лимит или сумма блокировки пустые");
            }
        } catch (EntityNotFoundException exception) {
            limits = new Limits();
            limits.setUserId(limitDto.userId());
            limits.setLimitSumma(limitDefaultAmount);
            limits.setBlockAmount(BigDecimal.ZERO);
            limitRepository.save(limits);
        }
        return limits;
    }

    public Boolean checkLimit(LimitDto limitDto) {
        Limits limits = getLimits(limitDto);
        Boolean boolCheckLimit = limits.getLimitSumma().compareTo(limits.getBlockAmount().add(limitDto.amount())) > 0;
        return boolCheckLimit;
    }

    public Boolean blockAmount(LimitDto limitDto) {
        Limits limits = getLimits(limitDto);
        try {
            BigDecimal blockAmount = limits.getBlockAmount().add(limitDto.amount());
            if (blockAmount.compareTo(limits.getLimitSumma()) < 0) {
                limits.setBlockAmount(limits.getBlockAmount().add(limitDto.amount()));
                limitRepository.save(limits);
                return true;
            } else {
                throw new ExceptionRequest("Лимит исчерпан. Блокировка средств не удалась на сумму " + limitDto.amount() + " рублей");
            }
        } catch (RuntimeException exception) {
            throw new ExceptionRequest(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка проверки блокировки");
        }
    }

    public Boolean rollback(LimitDto limitDto) {
        Limits limits = getLimits(limitDto);
        if (limits.getBlockAmount() == BigDecimal.ZERO) {
            throw new ExceptionRequest("Восстановить лимит невозможно. Сумма заблокированных средств равна нулю");
        }
        BigDecimal blockAmount = limits.getBlockAmount().subtract(limitDto.amount());
        if (blockAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ExceptionRequest("Восстановить лимит невозможно. Сумма заблокированных средств меньше суммы платежа");
        }
        BigDecimal limitAmount = limits.getLimitSumma().add(limitDto.amount());
        if (limitAmount.compareTo(limitDefaultAmount) > 0) {
            limitAmount = limitDefaultAmount;
        }
        limits.setLimitSumma(limitAmount);
        limits.setBlockAmount(blockAmount);
        limitRepository.save(limits);
        return true;
    }

    public Boolean confirmAmount(LimitDto limitDto) {
        Limits limits = getLimits(limitDto);
        BigDecimal confirmAmount = limits.getBlockAmount().subtract(limitDto.amount());
        if (confirmAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ExceptionRequest("Сумма для подтверждения превышает сумму заблокированных средств");
        }
        BigDecimal limitAmount = limits.getLimitSumma().subtract(limitDto.amount());
        if (limitAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ExceptionRequest("Сумма для подтверждения превышает оставшийся лимит");
        }
        limits.setLimitSumma(limitAmount);
        limits.setBlockAmount(confirmAmount);
        limitRepository.save(limits);
        return true;
    }
}
