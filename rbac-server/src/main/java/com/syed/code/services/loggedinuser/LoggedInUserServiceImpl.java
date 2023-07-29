package com.syed.code.services.loggedinuser;

import com.syed.code.entities.role.EntityPermissions;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.enums.EntityEnums;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggedInUserServiceImpl implements LoggedInUserService {

    @Autowired
    private RoleRepository roleRepository;

    public CustomUserDetails getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails;
    }

    public boolean isActionAllowed(CustomUserDetails userDetails, EntityEnums.Entity entity, PermissionEnums.GenericPerms genericPerms) {
        List<EntityPermissions> entityPermissions = roleRepository.getEntityPermissionsByUserId(userDetails.getId());
        if (entityPermissions != null && !entityPermissions.isEmpty()) {
            EntityPermissions relevantPermission = entityPermissions
                    .stream()
                    .filter(entityPermission ->
                            (
                                    entityPermission.getEntityId().equals(Integer.toUnsignedLong(entity.entityId)) && entityPermission.getGenericPermissionId().equals(Integer.toUnsignedLong(genericPerms.permissionId))
                            )
                    )
                    .findAny()
                    .orElse(null);

            if (relevantPermission != null && relevantPermission.getIsAllowed().equals(1)) return true;
        }
        return false;
    }

    @Override
    public boolean isActionAllowed(EntityEnums.Entity entity, PermissionEnums.GenericPerms genericPerms) {
        CustomUserDetails userDetails = getLoggedInUser();
        if (userDetails != null && isActionAllowed(userDetails, entity, genericPerms)) return true;
        return false;
    }
}
