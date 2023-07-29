package com.syed.code.entities.audit;

import com.syed.code.entities.role.GenericPermissions;
import com.syed.code.entities.user.User;
import com.syed.code.enums.EntityEnums.Entity;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class AuditDetails {

    private Entity entity;

    private GenericPermissions genericPermissions;

    private Map<String, String> editDetails;

    private User performedBy;

    private Date performedAt;
}
