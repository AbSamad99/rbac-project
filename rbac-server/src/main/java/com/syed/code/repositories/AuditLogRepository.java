package com.syed.code.repositories;

import com.syed.code.entities.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("select a from AuditLog a where a.id = :id")
    public AuditLog getAuditLogById(@Param("id") Long id);
}
