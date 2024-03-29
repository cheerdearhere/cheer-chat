package com.cheer.cheerchat.controller;

import com.cheer.cheerchat.config.TokenProvider;
import com.cheer.cheerchat.entity.User;
import com.cheer.cheerchat.exception.UserException;
import com.cheer.cheerchat.response.AuthResponse;
import com.cheer.cheerchat.request.LoginRequest;
import com.cheer.cheerchat.repository.UserRepository;
import com.cheer.cheerchat.service.CustomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CustomUserService customUserService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
        String email = user.getEmail();
        String fullName = user.getFullName();
        String password = user.getPassword();
        String salt = user.getSalt();

        User isUser = userRepository.findByEmail(email);
        if(isUser != null)
            throw new UserException("Email is used with another Account already: "+email);
        User createdUser = User.builder()
                .email(email)
                .fullName(fullName)
                .password(passwordEncoder.encode(salt+password))
                .regDate(LocalDateTime.now())
                .build();
        User savedUser = userRepository.save(createdUser);
        savedUser.setRegId(savedUser.getId());
        userRepository.save(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email,salt+password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt,true);

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

//    @PostMapping("/login/login-proc")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest)throws Exception{
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication =authenticate(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt,true);

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    public Authentication authenticate(String username, String password) throws Exception{
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        String salt = username.toLowerCase().substring(username.indexOf('@')+1,username.indexOf('@'+4));
        if(userDetails == null)
            throw new UserException("invalid username: "+username);
        if(!passwordEncoder.matches(salt+password, userDetails.getPassword()))
            throw new BadCredentialsException("invalid password or username");
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }



}
