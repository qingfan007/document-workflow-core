package com.qingfan.documentcoredemo.domain.audit;

public enum AuditAction {
    CREATE_DRAFT,
    UPDATE_DRAFT,
    SUBMIT,
    REVIEW,
    REQUEST_CHANGES,
    APPROVE,
    REJECT,
    ARCHIVE
}
