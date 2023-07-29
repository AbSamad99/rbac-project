package com.syed.code.requestsandresponses.user;

import com.syed.code.entities.user.User;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResponse extends BaseResponse {
    User user;
}
