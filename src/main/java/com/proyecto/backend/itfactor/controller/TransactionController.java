package com.proyecto.backend.itfactor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.backend.itfactor.commons.exceptions.ConflictException;
import com.proyecto.backend.itfactor.entity.Transaction;
import com.proyecto.backend.itfactor.entity.User;
import com.proyecto.backend.itfactor.model.TransactionModel;
import com.proyecto.backend.itfactor.service.TransactionService;
import com.proyecto.backend.itfactor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path="transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity createTransaction(@RequestBody TransactionModel transaction) {
        Optional<User> user = userService.findByUsername(transaction.getUser().getUsername());
        if(!user.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al obtener usuario");
        if(transaction.getCoin().equals("")
                || transaction.getExchange().equals("")
                || transaction.getTotal().equals(""))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios");
        User transactionUser = objectMapper.convertValue(user, User.class);
        transaction.getUser().setId(transactionUser.getId());
        TransactionModel transactionModel = transactionService.createTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionModel);
    }

}
