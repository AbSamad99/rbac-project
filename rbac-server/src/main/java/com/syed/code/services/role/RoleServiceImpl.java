package com.syed.code.services.role;

import com.syed.code.entities.role.EntityPermissions;
import com.syed.code.entities.role.GenericPermissions;
import com.syed.code.entities.role.Role;
import com.syed.code.entities.role.RolePermissionMapping;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.enums.AuditEnums;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.enums.codes.SuccessCodes;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.repositories.RoleRepository;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.requestsandresponses.role.RoleMetadataResponse;
import com.syed.code.requestsandresponses.role.RoleResponse;
import com.syed.code.services.baseentity.BaseEntityServiceImpl;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl extends BaseEntityServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void clearFieldsForCopy(Role entity) {
        entity.setKey(null);
    }

    @Override
    public Boolean saveEntity(Role entity, Role previousEntity, Session session, BaseResponse response, PermissionEnums.GenericPerms genericPerm) throws PermissionMissingException {
        Boolean res = saveEntityInternal(entity, previousEntity, session, response, genericPerm);
        if (res == true) {
            if (entity.getRolePermissionMappings() != null && !entity.getRolePermissionMappings().isEmpty()) {
                Set<RolePermissionMapping> mappings = new HashSet<>(entity.getRolePermissionMappings());
                entity.getRolePermissionMappings().clear();
                try {
                    for (RolePermissionMapping mapping : mappings) {
                        mapping.setRoleId(entity.getId());
                        entity.getRolePermissionMappings().add(mapping);
                        session.persist(mapping);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return res;
    }

    @Override
    public Boolean isValid(Role entity, Role previousEntity) {
        if (previousEntity != null && previousEntity.getKey() == 1) return false;
        List<Role> roles = roleRepository.getExistingRoleByName(entity.getName());
        if (roles.size() != 0 && previousEntity == null) return false;
        for (Role role : roles)
            if (!role.getKey().equals(entity.getKey())) return false;

        CustomUserDetails userDetails = getLoggedInUserService().getLoggedInUser();
//      Users without any role should not be able to give permissions
        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) return false;
//      User should not be able to edit his own role
        if (entity.getKey() != null) {
            Role role = userDetails.getRoles().stream().filter(r -> r.getKey().equals(entity.getKey())).findAny().orElse(null);
            if (role != null) return false;
        }
//      User should not be able to give permissions he himself does not have
        if (entity.getRolePermissionMappings() != null && !entity.getRolePermissionMappings().isEmpty()) {
            List<Long> unassignedEntityPermissionIds = new ArrayList<>(roleRepository.getUnassignedEntityPermissionIdsByUserId(userDetails.getId()));
            List<Long> assignedEntityPermissionIds = new ArrayList<>(entity.getRolePermissionMappings().stream().map(RolePermissionMapping::getEntityPermissionId).toList());

            unassignedEntityPermissionIds.retainAll(assignedEntityPermissionIds);
            if (!unassignedEntityPermissionIds.isEmpty()) return false;
        }

        return true;
    }

    @Override
    public Role getPreviousEntity(Role entity) {
        Role previousEntity = null;
        if (entity.getKey() != null) {
            previousEntity = roleRepository.getActiveRoleByKey(entity.getKey());
            if (previousEntity != null) loadRole(previousEntity);
        }
        return previousEntity;
    }

    @Override
    public RoleResponse createRole(Role role) throws PermissionMissingException {
        RoleResponse response = new RoleResponse();
        Boolean res = saveEntity(role, response);
        return response;
    }

    @Override
    public RoleResponse getRole(Long id) throws PermissionMissingException {
        RoleResponse response = new RoleResponse();
        PermissionEnums.GenericPerms genericPerms = PermissionEnums.GenericPerms.View;

        Role role = roleRepository.getRoleById(id);

        if (!getLoggedInUserService().isActionAllowed(getEnt(), genericPerms)) {
            getAuditService().performLog(getEnt(), genericPerms, role, null, AuditEnums.AttemptStatus.FailedMissingPerms);
            throw new PermissionMissingException(getEnt(), "View role perms missing");
        }

        if (role != null) {
            loadRole(role);
        }
        MessageVariable messageVariable = getEntityPopulatedMessageVariable();
        messageVariable.setApplicationCode(SuccessCodes.Generic.S00_003.code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.OK);
        response.setRole(role);

        return response;
    }

    @Override
    public RoleMetadataResponse getRoleMetadata() {
        RoleMetadataResponse response = new RoleMetadataResponse();
        CustomUserDetails userDetails = getLoggedInUserService().getLoggedInUser();

        List<GenericPermissions> genericPermissions = roleRepository.getAllGenericPermissions();
        List<EntityPermissions> entityPermissions = roleRepository.getEntityPermissionsByUserId(userDetails.getId());

        response.setGenericPermissions(genericPermissions);
        response.setEntityPermissions(entityPermissions);
        response.setStatusCode(HttpStatus.OK);
        return response;
    }

    @Override
    public void loadRole(Role role) {
        List<RolePermissionMapping> rolePermissionMappings = roleRepository.getRolePermissionMappingsByRoleId(role.getId());
        role.setRolePermissionMappings(rolePermissionMappings);
    }
}
