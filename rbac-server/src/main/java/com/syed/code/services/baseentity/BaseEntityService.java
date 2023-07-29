package com.syed.code.services.baseentity;

import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.enums.PermissionEnums;
import com.syed.code.exceptions.PermissionMissingException;
import com.syed.code.requestsandresponses.base.BaseResponse;
import com.syed.code.requestsandresponses.base.MessageVariable;
import org.hibernate.Session;

// Base interface for the entity services, which defines common methods that must be implemented by every entity service.
// It takes in a generic  type parameter T which extends BaseEntity, this is handy when methods are implemented by
// individual entity services as there will be type checking based on the entity.
public interface BaseEntityService<T extends BaseEntity> {
    Boolean saveEntity(T entity, BaseResponse response) throws PermissionMissingException;

//    Boolean saveEntity(T entity, Session session, BaseResponse response);

    void clearFieldsForCopy(T entity);

    Boolean saveEntity(T entity, T previousEntity, Session session, BaseResponse response, PermissionEnums.GenericPerms genericPerm) throws PermissionMissingException;

    Boolean saveEntityInternal(T entity, Session session, BaseResponse response) throws PermissionMissingException;

    Boolean saveEntityInternal(T entity, T previousEntity, Session session, BaseResponse response, PermissionEnums.GenericPerms genericPerm) throws PermissionMissingException;

    MessageVariable getEntityPopulatedMessageVariable();

    //    Used to determine if the new entity is valid in order to be saved
    Boolean isValid(T entity, T previousEntity);

    //    Fetches previous version of the entity if it exists in db
    T getPreviousEntity(T entity);
}
