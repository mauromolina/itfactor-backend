package com.proyecto.backend.itfactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserModel {

    private Long id;
    private String username;
    private String email;
    private String password;


}
