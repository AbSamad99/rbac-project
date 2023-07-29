package com.syed.code.entities.role;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "entity_permissions")
public class EntityPermissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    @Column(name = "entity_id", nullable = false, precision = 10)
    private Long entityId;

    @Column(name = "generic_permission_id", nullable = false, precision = 10)
    private Long genericPermissionId;

    @Column(name = "is_allowed", nullable = false)
    private Integer isAllowed;
}
