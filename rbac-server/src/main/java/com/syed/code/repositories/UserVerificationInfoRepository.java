package com.syed.code.repositories;

import com.syed.code.entities.user.UserVerificationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserVerificationInfoRepository extends JpaRepository<UserVerificationInfo, Long> {

    @Query("select uvi from UserVerificationInfo uvi where uvi.userKey = :key")
    public UserVerificationInfo getUserVerificationInfoByUserKey(@Param("key") Long key);

    @Query("select uvi from UserVerificationInfo uvi where uvi.passwordResetCode = :passwordResetCode")
    public UserVerificationInfo getUserVerificationInfoByPasswordResetCode(@Param("passwordResetCode") String passwordResetCode);

    @Query("select uvi from UserVerificationInfo uvi where uvi.verificationCode = :verificationCode")
    public UserVerificationInfo getUserVerificationInfoByVerificationCode(@Param("verificationCode") String verificationCode);
}
