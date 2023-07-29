package com.syed.code.entities.role;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "generic_permissions")
public class GenericPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 50)
    private String description;
}
