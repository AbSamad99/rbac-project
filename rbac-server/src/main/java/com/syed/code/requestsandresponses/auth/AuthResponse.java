package com.syed.code.requestsandresponses.auth;

import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthResponse extends BaseResponse {
    private String token;
}
