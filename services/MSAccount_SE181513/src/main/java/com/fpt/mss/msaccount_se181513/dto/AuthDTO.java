package com.fpt.mss.msaccount_se181513.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fpt.mss.msaccount_se181513.domain.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid")
        @Schema(description = "User email", example = "admin@blindboxes.vn")
        private String email;

        @NotBlank(message = "Password is required")
        @Schema(description = "User password", example = "@2")
        private String password;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        @JsonIgnore
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
        @JsonIgnore
        private Long refreshExpiresIn;
        private Account user;
    }


}
