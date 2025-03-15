package ru.vtb.javaPro.response;

import lombok.Getter;

public record PaymentResponse(Boolean status, String message) {
}
