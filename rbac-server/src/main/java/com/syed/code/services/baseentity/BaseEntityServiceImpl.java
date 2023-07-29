package com.syed.code.services.baseentity;

import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.user.CustomUserDetails;
import com.syed.code.enums.AuditEnums;
import com.syed.code.enums.EntityEnums.Entity;
import com.syed.code.enums.PermissionEnums.GenericPerms;
import com.syed.code.enums.codes.ErrorCodes;
import com.syed.code.enums.codes.SuccessCodes;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import com.syed.code.services.audit.AuditService;
import com.syed.code.services.loggedinuser.LoggedInUserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

// An abstract class implementation of BaseEntityService which defines the common logic that will be used for each and
// every versioned entity. It also defines abstract methods which must be implemented by the individual entity services.
// Generic type parameter T extends BaseEntity, which allows for calling setters/getters of BaseEntity instances in
// methods
@Service
public abstract class BaseEntityServiceImpl<T extends BaseEntity> implements BaseEntityService<T> {

    public Entity ent = null;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private AuditService auditService;

    @Autowired
    private LoggedInUserService loggedInUserService;

    public Entity getEnt() {
        return ent;
    }

    public void setEnt(Entity ent) {
        this.ent = ent;
    }

    //    This is where the save logic starts, these methods opens a session and calls the saveEntityInternal method and commits
    //    the transaction based on boolean response.
    @Override
    public Boolean saveEntity(T entity, BaseResponse response) throws PermissionMissingException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Boolean res = false;
        try {
            transaction = session.beginTransaction();
            res = saveEntityInternal(entity, session, response);
            if (!res) transaction.rollback();
            else transaction.commit();
        } catch (PermissionMissingException e) {
            if (transaction != null) transaction.rollback();
            throw new PermissionMissingException(e.getEntity(), e.getMessage());
        } catch (IllegalStateException e) {
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            entity.setId(null);
            auditService.performLog(getEnt(), GenericPerms.Create, entity, null, AuditEnums.AttemptStatus.FailedError);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (session != null) session.close();
        }
        return res;
    }

    //    This method calls the entity specific saveEntity method, which contains the customs logic required by each entity.
    //    A check is performed at the end, which compares the current and previous versions of an entity to decide if a new
    //    entry must be made in the table.
    @Override
    public Boolean saveEntityInternal(T entity, Session session, BaseResponse response) throws PermissionMissingException {
        GenericPerms genericPerm = GenericPerms.Create;
        T previousEntity = getPreviousEntity(entity);
        if (previousEntity != null) genericPerm = GenericPerms.Edit;
        if (entity.getCopy()) {
            clearFieldsForCopy(entity);
            genericPerm = GenericPerms.Copy;
        }

        if (!loggedInUserService.isActionAllowed(getEnt(), genericPerm)) {
            getAuditService().performLog(getEnt(), genericPerm, entity, previousEntity, AuditEnums.AttemptStatus.FailedMissingPerms);
            throw new PermissionMissingException(getEnt(), "Save perm missing");
        }

        Boolean res = saveEntity(entity, previousEntity, session, response, genericPerm);

        if (!isValid(entity, previousEntity)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            auditService.performLog(getEnt(), genericPerm, entity, previousEntity, AuditEnums.AttemptStatus.FailedBadRequest);
            return false;
        }

        if (genericPerm.equals(GenericPerms.Edit) && previousEntity.equals(entity)) {
            session.getTransaction().rollback();
            return true;
        }

        return res;
    }

    //    This method must be called by the entity specific saveEntity method, it handles the versioning and calls the audit
    //    service
    @Override
    public Boolean saveEntityInternal(T entity, T previousEntity, Session session, BaseResponse response, GenericPerms genericPerm) throws PermissionMissingException {
        MessageVariable messageVariable = getEntityPopulatedMessageVariable();
        messageVariable.setEntityName(entity.getEntityName());

        CustomUserDetails userDetails = loggedInUserService.getLoggedInUser();

        try {
            if (genericPerm.equals(GenericPerms.Edit)) {
                entity.setCreatedAt(previousEntity.getCreatedAt());
                entity.setCreatedBy(previousEntity.getCreatedBy());
                entity.setModifiedAt(new Date());
                entity.setModifiedBy(userDetails.getKey());
                entity.setVersion(previousEntity.getVersion() + 1);
                entity.setIsActive(1);
                previousEntity.setIsActive(0);
                session.update(previousEntity);
            } else {
                entity.setCreatedAt(new Date());
                entity.setCreatedBy(userDetails.getKey());
                entity.setModifiedAt(new Date());
                entity.setModifiedBy(userDetails.getKey());
                entity.setVersion(1L);
                entity.setIsActive(1);
            }
            session.persist(entity);
            if (!genericPerm.equals(GenericPerms.Edit)) entity.setKey(entity.getId());
            session.update(entity);
            messageVariable.setApplicationCode(SuccessCodes.Generic.S00_001.code);
            response.setStatusCode(HttpStatus.OK);
            auditService.performLog(getEnt(), genericPerm, entity, previousEntity, AuditEnums.AttemptStatus.Success, session);
        } catch (Exception e) {
            messageVariable.setApplicationCode(ErrorCodes.Generic.E00_001.code);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            auditService.performLog(getEnt(), genericPerm, entity, previousEntity, AuditEnums.AttemptStatus.FailedError, session);
            return false;
        } finally {
            response.getMessageVariables().add(messageVariable);
        }

        return true;
    }

    public MessageVariable getEntityPopulatedMessageVariable() {
        MessageVariable messageVariable = new MessageVariable();
        messageVariable.setEntity(getEnt().entityName);
        messageVariable.setEntityId(getEnt().entityId);
        return messageVariable;
    }

    public AuditService getAuditService() {
        return auditService;
    }

    public LoggedInUserService getLoggedInUserService() {
        return loggedInUserService;
    }
}
