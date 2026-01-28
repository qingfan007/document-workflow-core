package com.qingfan.documentcoredemo.repository;

import com.qingfan.documentcoredemo.domain.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByDocumentIdOrderByCreatedAtAsc(Long documentId);
}
