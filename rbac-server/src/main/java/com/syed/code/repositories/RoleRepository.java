package com.syed.code.repositories;

import com.syed.code.entities.role.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.isActive = 1")
    public List<Role> getActiveRoles();

    @Query("select lr from LiteRole lr where lr.isActive = 1")
    public List<LiteRole> getActiveLiteRoles();

    @Query("select r from Role r where r.id = :id")
    public Role getRoleById(@Param("id") Long id);

    @Query("select r from Role r where r.key = :key and r.isActive = 1")
    public Role getActiveRoleByKey(@Param("key") Long key);

    @Query("select r from Role r where r.name = :name and r.isActive = 1")
    public List<Role> getExistingRoleByName(@Param("name") String name);

    @Query("select rpm from RolePermissionMapping rpm where rpm.roleId = :roleId")
    public List<RolePermissionMapping> getRolePermissionMappingsByRoleId(@Param("roleId") Long roleId);

    @Query("select gp from GenericPermissions gp")
    public List<GenericPermissions> getAllGenericPermissions();

    @Query("select gp from GenericPermissions gp where gp.id = :id")
    public GenericPermissions getGenericPermissionById(@Param("id") Long id);

    @Query("select r from Role r where r.key in :roleKeys and r.isActive = 1")
    public List<Role> getRolesByRoleKeys(@Param("roleKeys") List<Long> roleKeys);

    @Query("select  ep from EntityPermissions ep where ep.id in :ids")
    public List<EntityPermissions> getEntityPermissionsByIds(@Param("ids") List<Long> ids);

    @Query("select ep from EntityPermissions ep " +
            "inner join RolePermissionMapping rpm on rpm.entityPermissionId = ep.id " +
            "inner join Role r on rpm.roleId = r.id " +
            "inner join UserRoleMapping urm on r.key = urm.roleKey " +
            "where r.isActive = 1 " +
            "and rpm.isAllowed = 1" +
            "and urm.userId = :userId " +
            "order by ep.id"
    )
    public List<EntityPermissions> getEntityPermissionsByUserId(@Param("userId") Long userId);

    @Query("select ep.id from EntityPermissions ep where ep.id not in " +
            "(select ep.id from EntityPermissions ep " +
            "inner join RolePermissionMapping rpm on rpm.entityPermissionId=ep.id " +
            "inner join Role r on rpm.roleId=r.id " +
            "inner join UserRoleMapping urm on r.key=urm.roleKey " +
            "where r.isActive=1 " +
            "and rpm.isAllowed = 1 " +
            "and urm.userId=:userId " +
            "order by ep.id)"
    )
    public List<Long> getUnassignedEntityPermissionIdsByUserId(@Param("userId") Long userId);
}
