package com.syed.code.entities.navbar;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "navbar")
public class Navbar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 50)
    private String description;

    @Column(name = "is_shown", nullable = false)
    private Integer isShown;

    @Transient
    List<NavbarItem> navbarItems = new ArrayList<>();
}
