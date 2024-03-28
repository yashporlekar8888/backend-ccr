package com.ccr.mvp.service.impl;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
@Service
public class TokenService {
    private Set<String> invalidatedTokens = new HashSet<>();

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidatedTokens.contains(token);
    }
}