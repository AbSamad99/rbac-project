package com.syed.code.requestsandresponses.role;

import com.syed.code.entities.role.EntityPermissions;
import com.syed.code.entities.role.GenericPermissions;
import com.syed.code.requestsandresponses.base.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class RoleMetadataResponse extends BaseResponse {

    List<GenericPermissions> genericPermissions;

    List<EntityPermissions> entityPermissions;
}
