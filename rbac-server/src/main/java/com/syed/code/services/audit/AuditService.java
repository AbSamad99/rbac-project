package com.syed.code.services.audit;

import com.syed.code.entities.audit.AuditLog;
import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.enums.AuditEnums;
import com.syed.code.enums.EntityEnums;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.audit.AuditResponse;
import org.hibernate.Session;

public interface AuditService {
    public void performLog(EntityEnums.Entity entity, PermissionEnums.GenericPerms permission, BaseEntity newEntity, BaseEntity previousEntity, AuditEnums.AttemptStatus attemptStatus);

    public void performLog(EntityEnums.Entity entity, PermissionEnums.GenericPerms permission, BaseEntity newEntity, BaseEntity previousEntity, AuditEnums.AttemptStatus attemptStatus, Session session);

    public AuditLog createAuditObject(EntityEnums.Entity entity, PermissionEnums.GenericPerms permission, BaseEntity newEntity, BaseEntity previousEntity, AuditEnums.AttemptStatus attemptStatus);

    public AuditResponse getAuditDetails(Long id) throws PermissionMissingException;
}
