package com.fpt.mss.msaccount_se181513.domain.auth;

import com.fpt.mss.msaccount_se181513.components.JwtTokenUtils;
import com.fpt.mss.msaccount_se181513.domain.user.Account;
import com.fpt.mss.msaccount_se181513.domain.user.AccountRepository;
import com.fpt.mss.msaccount_se181513.dto.AuthDTO.*;
import com.fpt.mss.msaccount_se181513.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.expiration}")
    private long expiration; // in seconds

    @Value("${jwt.expiration-refresh-token}")
    private long expirationRefreshToken; // in seconds

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request) {
        log.info("🔐 Starting login process for user: {}", loginRequest.getEmail());
        var user =
                accountRepository
                        .findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!user.isActive()) {
            log.warn("❌ Attempt to login to inactive account: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Account is inactive");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("❌ Invalid password for user: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        }

        var userInfo =
                new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        false,
                        user.getRole().name());

        String accessToken = jwtTokenUtils.generateToken(userInfo);
        String refreshToken = jwtTokenUtils.generateRefreshToken(userInfo);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiration) // 1 hour
                .refreshExpiresIn(expirationRefreshToken) // 24 hours
                .user(user)
                .build();
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }
}
