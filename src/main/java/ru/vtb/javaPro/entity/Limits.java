package ru.vtb.javaPro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "limits")
public class Limits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "limit_summa")
    private BigDecimal limitSumma;

    @Column(name = "block_amount")
    private BigDecimal blockAmount;
}
