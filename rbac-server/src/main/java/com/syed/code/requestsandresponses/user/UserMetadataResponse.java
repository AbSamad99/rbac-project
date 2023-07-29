package com.syed.code.requestsandresponses.user;

import com.syed.code.entities.role.LiteRole;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class UserMetadataResponse extends BaseResponse {

    List<LiteRole> roles;
}
