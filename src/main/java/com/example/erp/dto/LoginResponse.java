package com.example.erp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private String username;
    private String role;
    private String message;

    public LoginResponse(String accessToken, String tokenType, String username, String role) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.username = username;
        this.role = role;
    }

    public LoginResponse(String message) {
        this.message = message;
    }
}
