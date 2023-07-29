package com.syed.code.requestsandresponses.role;

import com.syed.code.entities.role.Role;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleResponse extends BaseResponse {
    private Role role;
}
