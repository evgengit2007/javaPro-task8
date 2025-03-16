package ru.vtb.javaPro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtb.javaPro.config.properties.LimitProperties;
import ru.vtb.javaPro.entity.Limits;
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

    private Limits getLimits(Long userId) {
        Limits limits;
        try {
            limits = limitRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException exception) {
            limits = new Limits();
            limits.setUserId(userId);
            limits.setLimitSumma(limitDefaultAmount);
            limits.setBlockAmount(BigDecimal.ZERO);
            limitRepository.save(limits);
        }
        return limits;
    }

    public Boolean checkLimit(Long userId, BigDecimal amount) {
        Limits limits = getLimits(userId);
        Boolean boolCheckLimit = limits.getLimitSumma().compareTo(limits.getBlockAmount().add(amount)) > 0;
        return boolCheckLimit;
    }

    public Boolean blockAmount(Long userId, BigDecimal amount) {
        Limits limits = getLimits(userId);
        BigDecimal blockAmount = limits.getBlockAmount().add(amount);
        if (blockAmount.compareTo(limits.getLimitSumma()) < 0) {
            limits.setBlockAmount(limits.getBlockAmount().add(amount));
            limitRepository.save(limits);
            return true;
        } else {
            return false;
        }
    }

    public Boolean rollback(Long userId, BigDecimal amount) {
        Limits limits = getLimits(userId);
        if (limits.getBlockAmount() == null || limits.getBlockAmount() == BigDecimal.ZERO) {
            return false;
        }
        BigDecimal blockAmount = limits.getBlockAmount().subtract(amount);
        if (blockAmount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        BigDecimal limitAmount = limits.getLimitSumma().add(amount);
        if (limitAmount.compareTo(limitAmount) > 0) {
            limitAmount = limitAmount;
        }
        limits.setLimitSumma(limitAmount);
        limits.setBlockAmount(blockAmount);
        limitRepository.save(limits);
        return true;
    }

    public Boolean confirmAmount(Long userId, BigDecimal amount) {
        Limits limits = getLimits(userId);
        BigDecimal blockAmount = limits.getBlockAmount().subtract(amount);
        if (blockAmount.compareTo(BigDecimal.ZERO) < 0) {
            blockAmount = BigDecimal.ZERO;
        }
        BigDecimal limitAmount = limits.getLimitSumma().subtract(amount);
        if (limitAmount.compareTo(BigDecimal.ZERO) < 0) {
            limitAmount = BigDecimal.ZERO;
        }
        limits.setLimitSumma(limitAmount);
        limits.setBlockAmount(blockAmount);
        limitRepository.save(limits);
        return true;
    }
}
