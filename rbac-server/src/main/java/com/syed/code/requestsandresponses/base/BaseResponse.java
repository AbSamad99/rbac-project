package com.syed.code.requestsandresponses.base;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class BaseResponse {
    HttpStatus statusCode;

    List<MessageVariable> messageVariables = new ArrayList<>();
}
