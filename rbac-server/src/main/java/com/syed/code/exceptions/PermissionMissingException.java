package com.syed.code.exceptions;

import com.syed.code.enums.EntityEnums;
import com.syed.code.requestsandresponses.base.MessageVariable;
import lombok.Data;

@Data
public class PermissionMissingException extends Exception {

    private EntityEnums.Entity entity;

    public PermissionMissingException(EntityEnums.Entity entity, String message) {
        super(message);
        this.entity = entity;
    }
}
