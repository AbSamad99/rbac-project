package com.syed.code.services.role.audit;

import com.syed.code.entities.audit.AuditDetails;
import com.syed.code.entities.role.Role;
import com.syed.code.entities.role.RolePermissionMapping;
import com.syed.code.services.audit.details.GenericAuditDetailsServiceImpl;
import com.syed.code.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class RoleAuditDetailsServiceImpl extends GenericAuditDetailsServiceImpl<Role> implements RoleAuditDetailsService {

    @Autowired
    private RoleService roleService;

    @Override
    public void handleEditDetails(Role entity, Role previousEntity, AuditDetails auditDetails) {
        HashMap<String, String> editDetails = new HashMap<>();
        roleService.loadRole(entity);
        roleService.loadRole(previousEntity);
        entity.getRolePermissionMappings().sort(Comparator.comparing(RolePermissionMapping::getEntityPermissionId));
        previousEntity.getRolePermissionMappings().sort(Comparator.comparing(RolePermissionMapping::getEntityPermissionId));
        if (!entity.getName().equals(previousEntity.getName()))
            editDetails.put("Name", previousEntity.getName() + SEPARATOR + entity.getName());
        if (!Objects.deepEquals(entity.getRolePermissionMappings(), previousEntity.getRolePermissionMappings())) {
            StringBuilder entStringBuilder = getRolePermissionMappings(entity.getRolePermissionMappings());
            StringBuilder prevEntStringBuilder = getRolePermissionMappings(previousEntity.getRolePermissionMappings());
            editDetails.put("Role Permission Mappings", prevEntStringBuilder.toString() + SEPARATOR + entStringBuilder.toString());
        }
        auditDetails.setEditDetails(editDetails);
    }

    private StringBuilder getRolePermissionMappings(List<RolePermissionMapping> rolePermissionMappings) {
        StringBuilder stringBuilder = new StringBuilder("-");
        if (rolePermissionMappings != null && !rolePermissionMappings.isEmpty()) {
            stringBuilder = new StringBuilder("");
            for (RolePermissionMapping mapping : rolePermissionMappings) {
                stringBuilder.append(TAB);
                stringBuilder.append("Permission Id:" + SPACE + mapping.getEntityPermissionId());
                stringBuilder.append(COMMA + SPACE);
                stringBuilder.append("Is Allowed:" + SPACE + mapping.getIsAllowed());
                stringBuilder.append(NEWLINE);
            }
        }
        return stringBuilder;
    }
}
