package com.syed.code.services.auth;

import com.syed.code.requestsandresponses.auth.AuthRequest;
import com.syed.code.requestsandresponses.auth.AuthResponse;

public interface AuthService {

    public AuthResponse authenticateUser(AuthRequest authRequest);

    public AuthResponse resetPassword(AuthRequest authRequest);

    public AuthResponse forgotPassword(AuthRequest authRequest);

    public AuthResponse verifyPasswordResetCode(String resetCode);

    public AuthResponse forgotPasswordReset(AuthRequest authRequest, String resetCode);

    AuthResponse verifyVerificationCode(String verificationCode);

    AuthResponse verificationPasswordReset(AuthRequest authRequest, String verificationCode);
}
