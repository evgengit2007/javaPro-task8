package ru.vtb.javaPro.service;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.spi.Limit;
import org.springframework.stereotype.Service;
import ru.vtb.javaPro.entity.Limits;
import ru.vtb.javaPro.repository.LimitRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class LimitService {

    private final LimitRepository limitRepository;

    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    private Limits getLimits(Long userId) {
        Limits limits;
        try {
            limits = limitRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException exception) {
            limits = new Limits();
            limits.setUserId(userId);
            limits.setLimitSumma(BigDecimal.valueOf(10000L)); // потом вынести в параметр
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
        if (limitAmount.compareTo(BigDecimal.valueOf(10000L)) > 0) {
            limitAmount = BigDecimal.valueOf(10000L); // подставить потом настройку
        }
        limits.setLimitSumma(limitAmount);
        limits.setBlockAmount(blockAmount);
        limitRepository.save(limits);
        return true;
    }
}
