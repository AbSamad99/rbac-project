package com.syed.code.entities.role;


import com.syed.code.entities.baseentity.NonVersioningEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "role_permission_mapping")
@SequenceGenerator(name = "role_permission_mapping_seq", sequenceName = "role_permission_mapping_seq", initialValue = 1024, allocationSize = 1)
public class RolePermissionMapping extends NonVersioningEntity<RolePermissionMapping> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_permission_mapping_seq")
    private Long id;

    @Column(name = "role_id", nullable = false, precision = 10)
    private Long roleId;

    @Column(name = "entity_permission_id", nullable = false, precision = 10)
    private Long entityPermissionId;

    @Column(name = "is_allowed", nullable = false)
    private Integer isAllowed;

    @Transient
    private Role role;

    @Transient
    private EntityPermissions entityPermissions;

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public boolean objectEquals(RolePermissionMapping other) {
        return Objects.equals(entityPermissionId, other.entityPermissionId) && Objects.equals(isAllowed, other.isAllowed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                roleId,
                entityPermissionId,
                isAllowed,
                role,
                entityPermissions
        );
    }
}