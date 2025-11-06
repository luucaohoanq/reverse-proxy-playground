package com.fpt.mss.msaccount_se181513.domain.auth;

import com.fpt.mss.annotations.RequireRole;
import com.fpt.mss.api.ApiResponse;
import com.fpt.mss.enums.Role;
import com.fpt.mss.msaccount_se181513.dto.AuthDTO.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());
        LoginResponse response = authService.login(loginRequest, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    @GetMapping("/users")
    @RequireRole(Role.ADMIN)
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(authService.getAllAccount());
    }

    @GetMapping("/users/test-only")
    public ResponseEntity<?> getAllUsersTestOnly() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(authService.getAllAccount());
    }
}
