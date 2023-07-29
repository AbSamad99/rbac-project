package com.syed.code.utils;

import com.syed.code.entities.audit.AuditLog;
import com.syed.code.entities.role.Role;
import com.syed.code.entities.user.User;
import com.syed.code.enums.EntityEnums.Entity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityClassMapUtil {
    public Map<Entity, Class> entityClassMap;

    public void init() {
        entityClassMap = new HashMap<>();
        entityClassMap.put(Entity.User, User.class);
        entityClassMap.put(Entity.Role, Role.class);
        entityClassMap.put(Entity.AuditLog, AuditLog.class);
    }
}
