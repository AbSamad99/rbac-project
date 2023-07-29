package com.syed.code.repositories;

import com.syed.code.entities.navbar.Navbar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NavbarRepository extends JpaRepository<Navbar, Long> {

    @Query("select n,ni from Navbar n " +
            "inner join NavbarItem ni on n.id = ni.navbarId " +
            "inner join EntityPermissions ep on ni.entityPermissionId = ep.id " +
            "inner join RolePermissionMapping rpm on ep.id = rpm.entityPermissionId " +
            "inner join Role r on rpm.roleId = r.id " +
            "inner join UserRoleMapping urm on r.key = urm.roleKey " +
            "where n.isShown = 1" +
            "and ni.isShown = 1" +
            "and ep.isAllowed = 1 " +
            "and rpm.isAllowed = 1 " +
            "and r.isActive = 1 " +
            "and urm.userId = :userId " +
            "order by n.id"
    )
    public List<List<Object>> getNavbarsByUserId(@Param("userId") Long userId);
}
