package com.syed.code.entities.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class LiteRole {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "key")
    private Long key;

    @Column(name = "is_active", nullable = false, precision = 1)
    private Integer isActive;
}
