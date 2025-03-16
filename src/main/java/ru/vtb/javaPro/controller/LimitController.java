package ru.vtb.javaPro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.javaPro.dto.LimitDto;
import ru.vtb.javaPro.response.LimitResponse;
import ru.vtb.javaPro.service.LimitService;

@Slf4j
@RestController
public class LimitController {

    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @PostMapping("/check-limit")
    public LimitResponse checkLimit(@RequestBody LimitDto limitDto) {
        log.info("Проверить лимит у user с id: {}, переданная сумма платежа: {}", limitDto.userId(), limitDto.amount());
        Boolean checkLimit = limitService.checkLimit(limitDto);
        return new LimitResponse(checkLimit);
    }

    @PostMapping("/block-amount")
    public LimitResponse blockAmount(@RequestBody LimitDto limitDto) {
        log.info("Холдировать сумму у пользователя с id: {}, переданная сумма: {}", limitDto.userId(), limitDto.amount());
        Boolean boolBlockAmount = limitService.blockAmount(limitDto);
        return new LimitResponse(boolBlockAmount);
    }

    @PostMapping("/rollback")
    public LimitResponse rollback(@RequestBody LimitDto limitDto) {
        log.info("Восстановить сумму холдирования у пользователя с id: {}, переданная сумма {}", limitDto.userId(), limitDto.amount());
        Boolean boolRollback = limitService.rollback(limitDto);
        return new LimitResponse(boolRollback);
    }

    @PostMapping("/confirm-amount")
    public LimitResponse confirmAmount(@RequestBody LimitDto limitDto) {
        log.info("Подтверждение платежа у пользователя с id: {}, сумма {}", limitDto.userId(), limitDto.amount());
        Boolean boolConfirmAmount = limitService.confirmAmount(limitDto);
        return new LimitResponse(boolConfirmAmount);
    }
}
