package com.syed.code.entities.role;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "user_role_mapping")
@SequenceGenerator(name = "user_role_mapping_seq", sequenceName = "user_role_mapping_seq", initialValue = 1024, allocationSize = 1)
public class UserRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_mapping_seq")
    private Long id;

    @Column(name = "user_id", nullable = false, precision = 10)
    private Long userId;

    @Column(name = "role_key", nullable = false, precision = 10)
    private Long roleKey;

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof UserRoleMapping)) return false;
        UserRoleMapping other = (UserRoleMapping) object;

        return Objects.equals(roleKey, other.roleKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, roleKey);
    }
}
