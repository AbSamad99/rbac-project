package com.syed.code.services.audit.details;

import com.syed.code.entities.audit.AuditDetails;
import com.syed.code.entities.audit.AuditLog;
import com.syed.code.entities.baseentity.BaseEntity;

public interface GenericAuditDetailsService<T extends BaseEntity> {

    public AuditDetails performAudit(AuditLog auditLog);

    public void handleEditDetails(T entity, T previousEntity, AuditDetails auditDetails);
}
