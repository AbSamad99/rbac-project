package com.syed.code.entities.role;

import com.syed.code.entities.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "roles")
@SequenceGenerator(name = "roles_seq", sequenceName = "roles_seq", initialValue = 1024, allocationSize = 1)
public class Role extends BaseEntity<Role> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Transient
    private List<RolePermissionMapping> rolePermissionMappings;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public boolean objectEquals(Role other) {
        rolePermissionMappings.sort(Comparator.comparing(RolePermissionMapping::getEntityPermissionId));
        other.rolePermissionMappings.sort(Comparator.comparing(RolePermissionMapping::getEntityPermissionId));
        return Objects.equals(name, other.name) && Objects.equals(rolePermissionMappings, other.rolePermissionMappings);
    }

    @Override
    public String getEntityName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                super.getKey(),
                name,
                rolePermissionMappings,
                super.getCreatedAt(),
                super.getCreatedBy(),
                super.getModifiedAt(),
                super.getModifiedBy(),
                super.getVersion(),
                super.getIsActive()
        );
    }

}
