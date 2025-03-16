package ru.vtb.javaPro.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vtb.javaPro.config.properties.LimitProperties;
import ru.vtb.javaPro.entity.Limits;
import ru.vtb.javaPro.repository.LimitRepository;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerConfig {

    private final LimitRepository limitRepository;
    private final LimitProperties limitProperties;

    public SchedulerConfig(LimitRepository limitRepository, LimitProperties limitProperties) {
        this.limitRepository = limitRepository;
        this.limitProperties = limitProperties;
    }

    @Scheduled(cron = "${service.limits.default.update-period}")
    @Async
    public void resetLimitDefault() {
        log.info("Запустился планировщик");
        List<Limits> limitsList = limitRepository.findAll();
        for (Limits limits: limitsList) {
            limits.setLimitSumma(limitProperties.getAmount());
        }
        limitRepository.saveAll(limitsList);
    }
}
