package com.syed.code.utils;

import com.syed.code.enums.EntityEnums.Entity;
import com.syed.code.services.role.audit.RoleAuditDetailsService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityAuditDetailsServiceMapUtil {
    public Map<Entity, Class> entityAuditDetailsServiceMap;


    public void init() {
        entityAuditDetailsServiceMap = new HashMap<>();
//        entityAuditDetailsServiceMap.put(Entity.User, UserServiceImpl.class);
        entityAuditDetailsServiceMap.put(Entity.Role, RoleAuditDetailsService.class);
    }
}
