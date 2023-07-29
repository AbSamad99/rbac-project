package com.syed.code.utils;

import com.syed.code.enums.EntityEnums.Entity;
import com.syed.code.services.baseentity.BaseEntityServiceImpl;
import com.syed.code.services.role.RoleService;
import com.syed.code.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityServiceMapUtils {

    @Autowired
    private ApplicationContext context;

    Map<Entity, Class> entityServiceMap;

    public void init() {
        entityServiceMap = new HashMap<>();
        entityServiceMap.put(Entity.User, UserService.class);
        entityServiceMap.put(Entity.Role, RoleService.class);


        for (Map.Entry<Entity, Class> entry : entityServiceMap.entrySet()) {
            BaseEntityServiceImpl service = (BaseEntityServiceImpl) context.getBean(entry.getValue());
            service.setEnt(entry.getKey());
        }
    }
}
