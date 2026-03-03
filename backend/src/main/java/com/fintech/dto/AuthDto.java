package com.fintech.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

public class AuthDto {

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String username;
        private String fullName;
        private Long accountId;
        private BigDecimal balance;
    }
}
