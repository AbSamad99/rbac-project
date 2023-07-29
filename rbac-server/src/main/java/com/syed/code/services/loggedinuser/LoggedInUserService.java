package com.syed.code.services.loggedinuser;

import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.enums.EntityEnums;
import com.syed.code.enums.PermissionEnums;


public interface LoggedInUserService {

    public CustomUserDetails getLoggedInUser();

    public boolean isActionAllowed(CustomUserDetails userDetails, EntityEnums.Entity entity, PermissionEnums.GenericPerms genericPerms);

    public boolean isActionAllowed(EntityEnums.Entity entity, PermissionEnums.GenericPerms genericPerms);
}
