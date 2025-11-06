package com.fpt.mss.msaccount_se181513.domain.auth;

import com.fpt.mss.msaccount_se181513.domain.user.Account;
import com.fpt.mss.msaccount_se181513.dto.AuthDTO.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest, HttpServletRequest request);
    List<Account> getAllAccount();
}
