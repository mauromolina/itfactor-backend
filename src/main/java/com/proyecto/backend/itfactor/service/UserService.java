package com.proyecto.backend.itfactor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.backend.itfactor.commons.exceptions.ConflictException;
import com.proyecto.backend.itfactor.model.UserModel;
import com.proyecto.backend.itfactor.repository.UserRepository;
import com.proyecto.backend.itfactor.entity.User;
import com.proyecto.backend.itfactor.registration.token.ConfirmationToken;
import com.proyecto.backend.itfactor.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final static String USER_NOT_FOUND_MESSAGE =
            "No se encontró el usuario con email: %s ";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectMapper objectMapper;
    private final ConfirmationTokenService confirmationTokenService;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    public User login(UserModel userModel) {
        Optional<User> user = userRepository.findByEmail(userModel.getEmail());
        if(!user.isPresent()) throw new ConflictException("El email no existe");
        User presentUser = objectMapper.convertValue(user, User.class);
        boolean isPasswordOk = bCryptPasswordEncoder.matches(userModel.getPassword(), presentUser.getPassword());
        if(isPasswordOk) {
            String token = UUID.randomUUID().toString();
            return presentUser;
        }
        throw new ConflictException("La contraseña es incorrecta");
    }

    public String signUp(UserModel user) {
        System.out.println(user);
        boolean emailExists = userRepository.findByEmail(user.getEmail())
                .isPresent();
        boolean usernameExists = userRepository.findByUsername(user.getUsername()).isPresent();
        if(emailExists) throw new ConflictException("El email ya está en uso");
        if(usernameExists) throw new ConflictException("El nombre de usuario ya está en uso");
        User newUser = objectMapper.convertValue(user, User.class);
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        newUser.setPassword(encodedPassword);
        try{
            String token = UUID.randomUUID().toString();
            newUser.setToken(token);
            userRepository.save(newUser);
            return token;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }


        //TODO: Si queda tiempo, implementar token de confirmación + mails
//        ;
//        ConfirmationToken confirmationToken = new ConfirmationToken(
//                token,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(15),
//                user
//        );
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//        //TODO: Enviar email de confirmación
//        return token;
    }

}
