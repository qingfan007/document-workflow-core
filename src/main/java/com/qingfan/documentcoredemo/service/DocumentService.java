package com.qingfan.documentcoredemo.service;

import com.qingfan.documentcoredemo.domain.audit.AuditAction;
import com.qingfan.documentcoredemo.domain.document.Document;
import com.qingfan.documentcoredemo.domain.document.DocumentStatus;
import com.qingfan.documentcoredemo.domain.role.Role;
import com.qingfan.documentcoredemo.exception.DomainException;
import com.qingfan.documentcoredemo.exception.ForbiddenOperationException;
import com.qingfan.documentcoredemo.exception.InvalidStateException;
import com.qingfan.documentcoredemo.repository.DocumentRepository;
import com.qingfan.documentcoredemo.util.SecurityUtil;
import com.qingfan.documentcoredemo.web.api.dto.ActionRequest;
import com.qingfan.documentcoredemo.web.api.dto.CreateDocumentRequest;
import com.qingfan.documentcoredemo.web.api.dto.UpdateDraftRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public Document get(Long id) {
        return requireDocument(id);
    }

    @Transactional(readOnly = true)
    public List<Document> list() {
        return documentRepository.findAll();
    }

    @Transactional
    public Document createDraft(CreateDocumentRequest req) {
        assertRole(Role.CREATOR);

        Document doc = new Document();
        doc.setTitle(req.getTitle());
        doc.setType(req.getType());
        doc.setAmount(req.getAmount());
        doc.setCreatedBy(SecurityUtil.currentUsername());

        doc.setStatus(DocumentStatus.DRAFT);
        LocalDateTime now = LocalDateTime.now();
        doc.setCreatedAt(now);
        doc.setUpdatedAt(now);

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.CREATE_DRAFT, null, DocumentStatus.DRAFT, null);
        return saved;
    }

    @Transactional
    public Document updateDraft(Long id, UpdateDraftRequest req) {
        assertRole(Role.CREATOR);

        Document doc = requireDocument(id);
        assertState(doc, DocumentStatus.DRAFT);

        // only DRAFT can change core field
        doc.setTitle(req.getTitle());
        doc.setAmount(req.getAmount());
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.UPDATE_DRAFT, DocumentStatus.DRAFT, DocumentStatus.DRAFT, null);
        return saved;
    }

    @Transactional
    public Document submit(Long id, ActionRequest req) {
        assertRole(Role.CREATOR);

        Document doc = requireDocument(id);
        assertState(doc, DocumentStatus.DRAFT);

        DocumentStatus from = doc.getStatus();
        doc.setStatus(DocumentStatus.SUBMITTED);
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.SUBMIT, from, DocumentStatus.SUBMITTED, safeReason(req));
        return saved;
    }

    @Transactional
    public Document review(Long id, ActionRequest req) {
        assertRole(Role.REVIEWER);

        Document doc = requireDocument(id);
        assertState(doc, DocumentStatus.SUBMITTED);

        DocumentStatus from = doc.getStatus();
        doc.setStatus(DocumentStatus.UNDER_REVIEW);
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.REVIEW, from, DocumentStatus.UNDER_REVIEW, safeReason(req));
        return saved;
    }

    @Transactional
    public Document requestChanges(Long id, ActionRequest req) {
        assertRole(Role.REVIEWER);

        Document doc = requireDocument(id);
        assertState(doc, DocumentStatus.UNDER_REVIEW);

        DocumentStatus from = doc.getStatus();
        doc.setStatus(DocumentStatus.DRAFT);
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.REQUEST_CHANGES, from, DocumentStatus.DRAFT, safeReason(req));
        return saved;
    }


    @Transactional
    public Document approve(Long id, ActionRequest req) {
        assertRole(Role.APPROVER);

        Document doc = requireDocument(id);
        assertState(doc, DocumentStatus.UNDER_REVIEW);

        DocumentStatus from = doc.getStatus();
        doc.setStatus(DocumentStatus.APPROVED);
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.APPROVE, from, DocumentStatus.APPROVED, safeReason(req));
        return saved;
    }

    @Transactional
    public Document reject(Long id, ActionRequest req) {
        assertRole(Role.APPROVER);

        Document doc = requireDocument(id);
        assertState(doc, DocumentStatus.UNDER_REVIEW);

        DocumentStatus from = doc.getStatus();
        doc.setStatus(DocumentStatus.REJECTED);
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.REJECT, from, DocumentStatus.REJECTED, safeReason(req));
        return saved;
    }

    @Transactional
    public Document archive(Long id, ActionRequest req) {
        assertRole(Role.APPROVER);

        Document doc = requireDocument(id);
        assertStateIn(doc, EnumSet.of(DocumentStatus.APPROVED, DocumentStatus.REJECTED));

        DocumentStatus from = doc.getStatus();
        doc.setStatus(DocumentStatus.ARCHIVED);
        doc.setUpdatedAt(LocalDateTime.now());

        Document saved = documentRepository.save(doc);

        auditLogService.append(saved, AuditAction.ARCHIVE, from, DocumentStatus.ARCHIVED, safeReason(req));
        return saved;
    }

    public Document getById(Long id) {
        return requireDocument(id);
    }

    public List<Document> listAll() {
        return documentRepository.findAllByOrderByCreatedAtDesc();
    }

    private Document requireDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DomainException("Document not found: " + id));
    }

    private void assertState(Document doc, DocumentStatus expected) {
        if (doc.getStatus() != expected) {
            throw new InvalidStateException("Invalid state. Expected " + expected + " but was " + doc.getStatus());
        }
    }

    private void assertStateIn(Document doc, Set<DocumentStatus> allowed) {
        if (!allowed.contains(doc.getStatus())) {
            throw new InvalidStateException("Invalid state. Allowed " + allowed + " but was " + doc.getStatus());
        }
    }

    private String safeReason(ActionRequest req) {
        return req == null ? null : req.getReason();
    }

    private void assertRole(Role required) {
        String current = SecurityUtil.currentRole();
        if (!required.matches(current)) {
            throw new ForbiddenOperationException("Forbidden. Required role: " + required + ", but was: " + current);
        }
    }

}
