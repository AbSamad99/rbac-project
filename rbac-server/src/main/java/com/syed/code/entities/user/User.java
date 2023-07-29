package com.syed.code.entities.user;

import com.syed.code.entities.baseentity.BaseEntity;
import com.syed.code.entities.role.EntityPermissions;
import com.syed.code.entities.role.Role;
import com.syed.code.entities.role.UserRoleMapping;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "users")
@SequenceGenerator(name = "users_seq", sequenceName = "users_seq", initialValue = 1024, allocationSize = 1)
public class User extends BaseEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "users_seq")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "verification_info_id")
    private Long verificationInfoId;

    @Transient
    private String password;

    @Transient
    private List<UserRoleMapping> userRoleMappings;

    @Transient
    private List<Role> roles;

    @Transient
    private UserVerificationInfo userVerificationInfo;

    @Transient
    private List<EntityPermissions> entityPermissions;

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
    public boolean objectEquals(User other) {
        userRoleMappings.sort(Comparator.comparing(UserRoleMapping::getRoleKey));
        other.userRoleMappings.sort(Comparator.comparing(UserRoleMapping::getRoleKey));
        return Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName)
                && Objects.equals(username, other.username)
                && Objects.equals(email, other.email)
//                && Objects.equals(password, other.password)
                && Objects.equals(phoneNumber, other.phoneNumber)
                && Objects.equals(address, other.address)
                && Objects.deepEquals(userRoleMappings, other.userRoleMappings);
    }

    @Override
    public String getEntityName() {
        return firstName + " " + lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                firstName,
                lastName,
                username,
                email,
                phoneNumber,
                address,
                verificationInfoId,
                password,
                super.getKey(),
                super.getCreatedAt(),
                super.getCreatedBy(),
                super.getModifiedAt(),
                super.getModifiedBy(),
                super.getVersion(),
                super.getIsActive()
        );
    }
}
