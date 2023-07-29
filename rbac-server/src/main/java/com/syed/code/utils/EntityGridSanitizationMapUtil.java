package com.syed.code.utils;

import com.syed.code.enums.EntityEnums.Entity;
import com.syed.code.services.audit.grid.AuditLogGridSanitizationService;
import com.syed.code.services.grid.sanitization.BaseGridSanitizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityGridSanitizationMapUtil {

    @Autowired
    private ApplicationContext context;

    public Map<Entity, Class> entityGridSanitizationMap;

    public void init() {
        entityGridSanitizationMap = new HashMap<>();
//        entityGridSanitizationMap.put(EntityEnums.Entity.User, User.class);
        entityGridSanitizationMap.put(Entity.AuditLog, AuditLogGridSanitizationService.class);

        for (Map.Entry<Entity, Class> entry : entityGridSanitizationMap.entrySet()) {
            BaseGridSanitizationServiceImpl service = (BaseGridSanitizationServiceImpl) context.getBean(entry.getValue());
            service.setEnt(entry.getKey());
        }
    }
}
