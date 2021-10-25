package com.proyecto.backend.itfactor.repository;

import com.proyecto.backend.itfactor.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByUserId(Long id);
    List<Transaction> findAllByUserId(Long id);


}
