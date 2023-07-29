package com.syed.code.entities.navbar;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "navbar_items")
public class NavbarItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    @Column(name = "navbar_id", nullable = false, precision = 10)
    private Long navbarId;

    @Column(name = "entity_permission_id", nullable = false, precision = 10)
    private Long entityPermissionId;

    @Column(name = "link", nullable = false, length = 50)
    private String link;

    @Column(name = "is_shown", nullable = false)
    private Integer isShown;
}
