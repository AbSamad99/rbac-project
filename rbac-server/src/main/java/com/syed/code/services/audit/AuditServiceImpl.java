package com.syed.code.services.audit;

import com.syed.code.entities.audit.AuditDetails;
import com.syed.code.entities.audit.AuditLog;
import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.enums.AuditEnums;
import com.syed.code.enums.EntityEnums;
import com.syed.code.enums.EntityEnums.Entity;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.enums.PermissionEnums.GenericPerms;
import com.syed.code.enums.codes.ErrorCodes;
import com.syed.code.enums.codes.SuccessCodes;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.repositories.AuditLogRepository;
import com.syed.code.requestsandresponses.audit.AuditResponse;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.services.audit.details.GenericAuditDetailsService;
import com.syed.code.services.loggedinuser.LoggedInUserService;
import com.syed.code.utils.EntityAuditDetailsServiceMapUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EntityAuditDetailsServiceMapUtil serviceMapUtil;

    @Autowired
    private LoggedInUserService loggedInUserService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void performLog(Entity entity, GenericPerms permission, BaseEntity newEntity, BaseEntity previousEntity, AuditEnums.AttemptStatus attemptStatus) {
        AuditLog auditLog = createAuditObject(entity, permission, newEntity, previousEntity, attemptStatus);
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(auditLog);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }

    public void performLog(Entity entity, GenericPerms permission, BaseEntity newEntity, BaseEntity previousEntity, AuditEnums.AttemptStatus attemptStatus, Session session) {
        AuditLog auditLog = createAuditObject(entity, permission, newEntity, previousEntity, attemptStatus);
        session.persist(auditLog);
    }

    @Override
    public AuditLog createAuditObject(Entity entity, GenericPerms permission, BaseEntity newEntity, BaseEntity previousEntity, AuditEnums.AttemptStatus attemptStatus) {
        CustomUserDetails userDetails = loggedInUserService.getLoggedInUser();
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityId(entity.entityId);
        auditLog.setPermissionId(permission.permissionId);
        auditLog.setEntityItemName(newEntity != null ? newEntity.getEntityName() : (previousEntity != null ? previousEntity.getEntityName() : null));
        auditLog.setNewId(newEntity != null ? newEntity.getId() : null);
        auditLog.setPreviousId(previousEntity != null ? previousEntity.getId() : null);
        auditLog.setPerformedAt(new Date());
        auditLog.setPerformedBy(userDetails.getKey());
        auditLog.setAttemptStatus(attemptStatus.statusId);

        return auditLog;
    }

    @Override
    public AuditResponse getAuditDetails(Long id) throws PermissionMissingException {
        AuditResponse response = new AuditResponse();
        MessageVariable messageVariable = getMessageVariable(Entity.AuditLog);

        AuditLog auditLog = auditLogRepository.getAuditLogById(id);
        if (!loggedInUserService.isActionAllowed(Entity.AuditLog, PermissionEnums.GenericPerms.View)) {
            throw new PermissionMissingException(Entity.AuditLog, "View audit log perms missing");
        }

        if (auditLog == null) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return response;
        }

        Entity ent = Entity.getByEntityId(auditLog.getEntityId());
        AuditDetails auditDetails = null;
        Object bean = null;
        GenericAuditDetailsService service;
        try {
            Class clazz = serviceMapUtil.entityAuditDetailsServiceMap.get(ent);
            if (clazz != null)
                bean = context.getBean(clazz);
            if (bean != null) {
                service = (GenericAuditDetailsService) bean;
                auditDetails = service.performAudit(auditLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (auditDetails == null) {
                System.out.println("Custom audit service not detected for the entity " + ent.entityName + " utilizing generic audit service.");
                bean = context.getBean(GenericAuditDetailsService.class);
                service = (GenericAuditDetailsService) bean;
                auditDetails = service.performAudit(auditLog);
            }
            messageVariable.setApplicationCode(SuccessCodes.AuditLog.S03_001.code);
        }

        response.setAuditDetails(auditDetails);
        response.setStatusCode(HttpStatus.OK);
        response.getMessageVariables().add(messageVariable);
        return response;
    }

    private MessageVariable getMessageVariable(EntityEnums.Entity entity) {
        MessageVariable messageVariable = new MessageVariable();
        messageVariable.setEntity(entity.entityName);
        messageVariable.setEntityId(entity.entityId);
        return messageVariable;
    }

    private void handlePermissionMissing(BaseResponse response) {
        MessageVariable messageVariable = new MessageVariable();
        messageVariable.setEntity(Entity.AuditLog.entityName);
        messageVariable.setEntityId(Entity.AuditLog.entityId);
        messageVariable.setApplicationCode(ErrorCodes.Generic.E00_002.code);
        response.getMessageVariables().add(messageVariable);
        response.setStatusCode(HttpStatus.FORBIDDEN);
    }
}
