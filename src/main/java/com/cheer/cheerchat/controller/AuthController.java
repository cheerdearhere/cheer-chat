package com.cheer.cheerchat.controller;

import com.cheer.cheerchat.config.TokenProvider;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.repository.AuthResponse;
import com.cheer.cheerchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
        String email = user.getEmail();
        String fullName = user.getFullName();
        String password = user.getPassword();

        User isUser = userRepository.findByEmail(email);
        if(isUser != null)
            throw new UserException("Email is used with another Account already: "+email);
        User createdUser = User.builder()
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt,true);

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }




}
