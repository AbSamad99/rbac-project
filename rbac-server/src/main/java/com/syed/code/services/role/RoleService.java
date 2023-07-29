package com.syed.code.services.role;

import com.syed.code.entities.role.Role;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.role.RoleMetadataResponse;
import com.syed.code.requestsandresponses.role.RoleResponse;
import com.syed.code.services.baseentity.BaseEntityService;

public interface RoleService extends BaseEntityService<Role> {

    public RoleResponse createRole(Role role) throws PermissionMissingException;

    public RoleResponse getRole(Long id) throws PermissionMissingException;

    public RoleMetadataResponse getRoleMetadata();

    public void loadRole(Role role);
}
