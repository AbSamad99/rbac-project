package com.syed.code.entities.audit;

import com.syed.code.entities.baseentity.NonVersioningEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "audit_log")
@SequenceGenerator(name = "audit_log_seq", sequenceName = "audit_log_seq", initialValue = 1024, allocationSize = 1)
public class AuditLog extends NonVersioningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "audit_log_seq")
    private Long id;

    @Column(name = "entity_id", nullable = false, precision = 10)
    private Integer entityId;

    @Column(name = "entity_item_name")
    private String entityItemName;

    @Column(name = "permission_id", nullable = false, precision = 10)
    private Integer permissionId;

    @Column(name = "new_id", precision = 10)
    private Long newId;

    @Column(name = "previous_id", precision = 10)
    private Long previousId;

    @Column(name = "performed_at", nullable = false)
    private Date performedAt;

    @Column(name = "performed_by", nullable = false)
    private Long performedBy;

    @Column(name = "attempt_status", nullable = false)
    private Integer attemptStatus;

    @Transient
    private String performedByName;

    @Transient
    private String entityName;

    @Transient
    private String permissionName;

    @Transient
    private String attemptStatusString;

    @Override
    public boolean objectEquals(Object other) {
        return false;
    }
}
