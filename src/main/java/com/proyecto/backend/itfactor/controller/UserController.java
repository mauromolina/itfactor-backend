package com.proyecto.backend.itfactor.controller;

import com.proyecto.backend.itfactor.entity.User;
import com.proyecto.backend.itfactor.model.UserModel;
import com.proyecto.backend.itfactor.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity createUser(@RequestBody UserModel userModel) {
        if(userModel.getEmail().equals("")
                || userModel.getUsername().equals("")
                || userModel.getPassword().equals(""))
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los campos no pueden viajar vacíos");
        String token = userService.signUp(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @GetMapping("")
    public ResponseEntity getUser(@RequestParam String token) {
        String hola = "";
        if(token.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No hay token.");
        Optional<User> user = userService.findByToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping()
    public ResponseEntity logIn(@RequestBody UserModel userModel) {
        if(userModel.getEmail().equals("")
                || userModel.getPassword().equals(""))
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los campos no pueden viajar vacíos");
        User userLogged = userService.login(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userLogged);
    }

}
