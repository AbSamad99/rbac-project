package com.syed.code.services.auth;

import com.syed.code.entities.email.Email;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.entities.user.User;
import com.syed.code.entities.user.UserVerificationInfo;
import com.syed.code.enums.AuthEnums;
import com.syed.code.enums.codes.ErrorCodes;
import com.syed.code.enums.codes.SuccessCodes;
import com.syed.code.repositories.UserVerificationInfoRepository;
import com.syed.code.requestsandresponses.auth.AuthRequest;
import com.syed.code.requestsandresponses.auth.AuthResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.services.email.EmailService;
import com.syed.code.services.jwt.JwtService;
import com.syed.code.services.loggedinuser.LoggedInUserService;
import com.syed.code.services.user.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoggedInUserService loggedInUserService;

    @Autowired
    private EmailService emailService;

    private final String FORGOT_PASSWORD_TEMPLATE = "forgot-password-email.ftl";

    private final int MAX_LOCK_COUNT = 5;

    @Autowired
    private UserVerificationInfoRepository userVerificationInfoRepository;

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        AuthResponse response = new AuthResponse();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            handleInvalidCredentials(authRequest, response);
            return response;
        }
        handleValidCredentials(authRequest, response);
        return response;
    }

    private void handleInvalidCredentials(AuthRequest authRequest, AuthResponse response) {
        MessageVariable messageVariable = new MessageVariable();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        User user = userService.getVerificationLoadedUser(authRequest.getUsername());
        if (user != null) {
            UserVerificationInfo userVerificationInfo = user.getUserVerificationInfo();
            int newLockCount = userVerificationInfo.getLockCount() + 1;

            if (!user.getId().equals(1L)) {
                userVerificationInfo.setLockCount(newLockCount > MAX_LOCK_COUNT ? MAX_LOCK_COUNT : newLockCount);
                if (userVerificationInfo.getLockCount().intValue() >= MAX_LOCK_COUNT) {
                    userVerificationInfo.setStatus(AuthEnums.UserStatus.Locked.statusId);
                    messageVariable.setApplicationCode(ErrorCodes.Generic.E00_006.code);
                } else {
                    messageVariable.setApplicationCode(ErrorCodes.Generic.E00_005.code);
                }
                userVerificationInfoRepository.save(userVerificationInfo);
            } else {
                messageVariable.setApplicationCode(ErrorCodes.Generic.E00_005.code);
            }
        }
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.BAD_REQUEST);
    }

    private void handleValidCredentials(AuthRequest authRequest, AuthResponse response) {
        MessageVariable messageVariable = new MessageVariable();
        User user = userService.getVerificationLoadedUser(authRequest.getUsername());
        Map<String, Object> extraClaims = new HashMap<>();
        UserVerificationInfo userVerificationInfo = user.getUserVerificationInfo();
        if (userVerificationInfo.getStatus().equals(AuthEnums.UserStatus.Locked.statusId)) {
            messageVariable.setApplicationCode(ErrorCodes.Generic.E00_006.code);
            response.setStatusCode(HttpStatus.FORBIDDEN);
        } else if (userVerificationInfo.getStatus().equals(AuthEnums.UserStatus.Admin_Disabled.statusId)) {
            messageVariable.setApplicationCode((ErrorCodes.Generic.E00_007.code));
            response.setStatusCode(HttpStatus.FORBIDDEN);
        } else {
            if (userVerificationInfo.getLockCount().intValue() > 0) {
                userVerificationInfo.setLockCount(0);
                userVerificationInfoRepository.save(userVerificationInfo);
            }
            messageVariable.setApplicationCode(SuccessCodes.Generic.S00_006.code);
            response.setStatusCode(HttpStatus.OK);
            populateExtraClaims(extraClaims, user);
            String token = jwtService.generateToken(extraClaims);
            response.setToken(token);
        }
        response.getMessageVariables().add(messageVariable);
    }

    private void populateExtraClaims(Map<String, Object> extraClaims, User user) {
        extraClaims.put("id", user.getId());
        extraClaims.put("name", user.getFirstName() + " " + user.getLastName());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("email", user.getEmail());
    }

    @Override
    public AuthResponse resetPassword(AuthRequest authRequest) {
        AuthResponse response = new AuthResponse();
        MessageVariable messageVariable = new MessageVariable();

        CustomUserDetails userDetails = loggedInUserService.getLoggedInUser();
        if (userDetails == null || userDetails.getId() == 1 || authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response;
        }

        UserVerificationInfo verificationInfo = userVerificationInfoRepository.getUserVerificationInfoByUserKey(userDetails.getKey());
        verificationInfo.setHash(encoder.encode(authRequest.getPassword()));
        userVerificationInfoRepository.save(verificationInfo);

        messageVariable.setApplicationCode(SuccessCodes.Generic.S00_005.code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }

    @Override
    public AuthResponse forgotPassword(AuthRequest authRequest) {
        AuthResponse response = new AuthResponse();
//        MessageVariable messageVariable = new MessageVariable();

        User user = userService.getVerificationLoadedUser(authRequest.getUsername());
        if (user == null || user.getId() == 1 || user.getUserVerificationInfo().getStatus().equals(AuthEnums.UserStatus.Active_Verification_Pending.statusId)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response;
        }

        UserVerificationInfo verificationInfo = user.getUserVerificationInfo();
        String passwordResetCode = RandomStringUtils.randomAlphanumeric(40);
        verificationInfo.setPasswordResetCode(passwordResetCode);
        Date restCodeExpiration = new Date((new Date()).getTime() + (2 * 24 * 60 * 60 * 1000));
        verificationInfo.setPasswordResetCodeExpiration(restCodeExpiration);
        userVerificationInfoRepository.save(verificationInfo);

        handleForgotPasswordMail(user);

//        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }

    private void handleForgotPasswordMail(User user) {
        Email email = new Email();
        Map<String, Object> contentMap = new HashMap<>();
        String passwordResetLink = "localhost:8080/api/auth/forgot-password-reset/" + user.getUserVerificationInfo().getPasswordResetCode();

        email.setTemplateName(FORGOT_PASSWORD_TEMPLATE);
        email.setFrom("admin");
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");

        contentMap.put("firstName", user.getFirstName());
        contentMap.put("lastName", user.getLastName());
        contentMap.put("link", passwordResetLink);
        email.setContentMap(contentMap);

        Thread thread = new Thread(() -> {
            emailService.sendEmail(email);
        });
        thread.start();
    }

    @Override
    public AuthResponse verifyPasswordResetCode(String resetCode) {
        AuthResponse response = new AuthResponse();

        UserVerificationInfo verificationInfo = userVerificationInfoRepository.getUserVerificationInfoByPasswordResetCode(resetCode);
        if (verificationInfo == null || verificationInfo.getPasswordResetCodeExpiration().before(new Date()))
            handleCodeValidity(response, ErrorCodes.Generic.E00_008.code, HttpStatus.BAD_REQUEST);
        else
            handleCodeValidity(response, SuccessCodes.Generic.S00_007.code, HttpStatus.OK);

        return response;
    }

    @Override
    public AuthResponse verifyVerificationCode(String verificationCode) {
        AuthResponse response = new AuthResponse();

        UserVerificationInfo verificationInfo = userVerificationInfoRepository.getUserVerificationInfoByVerificationCode(verificationCode);
        if (verificationInfo == null)
            handleCodeValidity(response, ErrorCodes.Generic.E00_010.code, HttpStatus.BAD_REQUEST);
        else
            handleCodeValidity(response, SuccessCodes.Generic.S00_008.code, HttpStatus.OK);

        return response;
    }

    @Override
    public AuthResponse forgotPasswordReset(AuthRequest authRequest, String resetCode) {
        AuthResponse response = new AuthResponse();
        MessageVariable messageVariable = new MessageVariable();

        UserVerificationInfo verificationInfo = userVerificationInfoRepository.getUserVerificationInfoByPasswordResetCode(resetCode);
        if (verificationInfo == null || verificationInfo.getPasswordResetCodeExpiration().before(new Date()))
            handleCodeInvalid(response, messageVariable, ErrorCodes.Generic.E00_008);
        else
            handlePasswordReset(authRequest, response, verificationInfo);

        return response;
    }

    @Override
    public AuthResponse verificationPasswordReset(AuthRequest authRequest, String verificationCode) {
        AuthResponse response = new AuthResponse();

        UserVerificationInfo verificationInfo = userVerificationInfoRepository.getUserVerificationInfoByVerificationCode(verificationCode);
        if (verificationInfo == null)
            handleCodeValidity(response, ErrorCodes.Generic.E00_010.code, HttpStatus.BAD_REQUEST);
        else
            handlePasswordReset(authRequest, response, verificationInfo);

        return response;
    }

    private void handleCodeInvalid(AuthResponse response, MessageVariable messageVariable, ErrorCodes.Generic applicationCode) {
        messageVariable.setApplicationCode(applicationCode.code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.BAD_REQUEST);
    }

    private void handleCodeValidity(AuthResponse response, String code, HttpStatus status) {
        MessageVariable messageVariable = new MessageVariable();
        messageVariable.setApplicationCode(code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(status);
    }

    private void handlePasswordReset(AuthRequest authRequest, AuthResponse response, UserVerificationInfo verificationInfo) {
        MessageVariable messageVariable = new MessageVariable();
        if (authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
            messageVariable.setApplicationCode(ErrorCodes.Generic.E00_009.code);
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        } else {
            verificationInfo.setHash(encoder.encode(authRequest.getPassword()));
            verificationInfo.setPasswordResetCode(null);
            verificationInfo.setPasswordResetCodeExpiration(null);
            verificationInfo.setVerificationCode(null);
            userVerificationInfoRepository.save(verificationInfo);
            messageVariable.setApplicationCode(SuccessCodes.Generic.S00_005.code);
            response.setStatusCode(HttpStatus.OK);
        }

        response.getMessageVariables().add(messageVariable);
    }
}
