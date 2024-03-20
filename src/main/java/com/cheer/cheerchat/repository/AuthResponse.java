package com.cheer.cheerchat.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private boolean isAuth;

}
