package com.qingfan.documentcoredemo.service;

import com.qingfan.documentcoredemo.domain.audit.AuditAction;
import com.qingfan.documentcoredemo.domain.audit.AuditLog;
import com.qingfan.documentcoredemo.domain.document.Document;
import com.qingfan.documentcoredemo.domain.document.DocumentStatus;
import com.qingfan.documentcoredemo.repository.AuditLogRepository;
import com.qingfan.documentcoredemo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void append(Document document,
                       AuditAction action,
                       DocumentStatus fromStatus,
                       DocumentStatus toStatus,
                       String reasonOrNote) {

        AuditLog log = new AuditLog();
        log.setDocumentId(document.getId());
        log.setAction(action);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);

        log.setActor(SecurityUtil.currentUsername());
        log.setReason(reasonOrNote);

        log.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(log);
    }

}
