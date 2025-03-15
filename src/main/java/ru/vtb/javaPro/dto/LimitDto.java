package ru.vtb.javaPro.dto;

import java.math.BigDecimal;

public record LimitDto(Long userId, BigDecimal amount) {
}
