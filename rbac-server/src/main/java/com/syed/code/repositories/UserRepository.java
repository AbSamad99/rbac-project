package com.syed.code.repositories;

import com.syed.code.entities.role.UserRoleMapping;
import com.syed.code.entities.user.User;
import com.syed.code.entities.user.UserVerificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id = :id")
    public User getUserById(@Param("id") Long id);

    @Query("select u from User u where u.id in :ids and u.isActive = 1 order by u.id")
    public List<User> getUsersByIds(@Param("ids") List<Long> ids);

    @Query("select u from User u where u.key = :key and u.isActive = 1")
    public User getUserByKey(@Param("key") Long key);

    @Query("select u from User u where u.key in :keys and u.isActive = 1 order by u.key")
    public List<User> getUsersByKeys(@Param("keys") List<Long> keys);

    @Query("select u from User u where u.username = :username and u.isActive = 1")
    public List<User> getExistingUserByUsername(@Param("username") String username);

    @Query("select um from UserRoleMapping um where um.userId = :id")
    public List<UserRoleMapping> getUserRoleMappingsByUserId(@Param("id") Long id);
}
