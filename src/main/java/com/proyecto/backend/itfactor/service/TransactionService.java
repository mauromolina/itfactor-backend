package com.proyecto.backend.itfactor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.backend.itfactor.entity.Transaction;
import com.proyecto.backend.itfactor.model.TransactionModel;
import com.proyecto.backend.itfactor.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public TransactionModel createTransaction(TransactionModel transactionModel) {
        Transaction newTransaction = objectMapper.convertValue(transactionModel, Transaction.class);
        Transaction transaction = transactionRepository.save(newTransaction);
        return objectMapper.convertValue(transaction, TransactionModel.class);
    }

    public Optional<Transaction> getTransactionByUserId(Long id) {
        return transactionRepository.findByUserId(id);
    }

    public List<Transaction> getTransactionsByUserId(Long id) {
        return transactionRepository.findAllByUserId(id);
    }

}
