package com.syed.code.requestsandresponses.user;

import lombok.Data;

@Data
public class ChangeUserPasswordRequest {
    Long userId;

    String password;
}
