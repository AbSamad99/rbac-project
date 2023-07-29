package com.syed.code.services.audit.grid;

import com.syed.code.entities.audit.AuditLog;
import com.syed.code.entities.user.User;
import com.syed.code.enums.AuditEnums;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.repositories.UserRepository;
import com.syed.code.services.grid.sanitization.nonversioning.GenericNonVersioningGridSanitizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuditLogGridSanitizationServiceImpl extends GenericNonVersioningGridSanitizationServiceImpl<AuditLog> implements AuditLogGridSanitizationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sanitizeDataInternal(List<AuditLog> dataList) {
        Map<Long, User> userMap = new HashMap<>();
        Set<Long> userKeys = new HashSet<>(dataList.stream().map(ent -> ent.getPerformedBy()).toList());
        List<User> users = userRepository.getUsersByIds(userKeys.stream().toList());
        for (User user : users)
//            if (!userMap.containsKey(user.getKey()))
//                userMap.put(user.getKey(), user);
            if (!userMap.containsKey(user.getId()))
                userMap.put(user.getId(), user);

        for (AuditLog entity : dataList) {
            entity.setPerformedByName(userMap.get(entity.getPerformedBy()) != null ? userMap.get(entity.getPerformedBy()).getFirstName() : null);
            entity.setPermissionName(PermissionEnums.GenericPerms.getGenericPermissionById(entity.getPermissionId()).permissionName);
            entity.setEntityName(getEnt().entityDisplayName);
            entity.setAttemptStatusString(AuditEnums.AttemptStatus.getAttemptStatusById(entity.getAttemptStatus()).statusDisplayName);
        }
    }
}
