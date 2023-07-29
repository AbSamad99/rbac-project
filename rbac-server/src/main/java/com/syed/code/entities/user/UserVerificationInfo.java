package com.syed.code.entities.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user_verification_info")
@SequenceGenerator(name = "user_verification_info_seq", sequenceName = "user_verification_info_seq", initialValue = 1024, allocationSize = 1)
public class UserVerificationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_verification_info_seq")
    private Long id;

    @Column(name = "user_key", nullable = false, precision = 10)
    private Long userKey;

    @Column(name = "hash", nullable = false, length = 255)
    private String hash;

    @Column(name = "status", nullable = false, precision = 10)
    private Integer status;

    @Column(name = "password_reset_code", length = 255)
    private String passwordResetCode;

    @Column(name = "password_reset_code_expiration")
    private Date passwordResetCodeExpiration;

    @Column(name = "lock_count", precision = 10)
    private Integer lockCount;

    @Column(name = "verification_code", length = 255)
    private String verificationCode;
}
