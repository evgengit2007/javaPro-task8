package ru.vtb.javaPro.repository;

import org.hibernate.annotations.SQLSelect;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.vtb.javaPro.entity.Limits;

import java.util.List;
import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limits, Long> {

    @Override
    Optional<Limits> findById(Long aLong);

}
