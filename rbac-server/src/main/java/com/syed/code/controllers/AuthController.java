package com.syed.code.controllers;

import com.syed.code.requestsandresponses.auth.AuthRequest;
import com.syed.code.requestsandresponses.auth.AuthResponse;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.authenticateUser(authRequest);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> resetPassword(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.resetPassword(authRequest);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> forgotPassword(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.forgotPassword(authRequest);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/forgot-password-reset/{resetCode}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> forgotPasswordReset(@PathVariable String resetCode, @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.forgotPasswordReset(authRequest, resetCode);
        return new ResponseEntity<>(response, response.getStatusCode());
    }

    @RequestMapping(value = "/verify-password-reset-code/{resetCode}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> verifyPasswordResetCode(@PathVariable String resetCode) {
        AuthResponse response = authService.verifyPasswordResetCode(resetCode);
        return new ResponseEntity<>(response, response.getStatusCode());
    }
}
