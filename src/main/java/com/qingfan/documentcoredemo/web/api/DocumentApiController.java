package com.qingfan.documentcoredemo.web.api;

import com.qingfan.documentcoredemo.service.DocumentService;
import com.qingfan.documentcoredemo.web.api.dto.ActionRequest;
import com.qingfan.documentcoredemo.web.api.dto.CreateDocumentRequest;
import com.qingfan.documentcoredemo.web.api.dto.DocumentResponse;
import com.qingfan.documentcoredemo.web.api.dto.UpdateDraftRequest;
import com.qingfan.documentcoredemo.web.api.mapper.DocumentResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentApiController {

    private final DocumentService documentService;
    private final DocumentResponseMapper documentResponseMapper;

    // Create DRAFT
    @PostMapping
    public DocumentResponse create(@Valid @RequestBody CreateDocumentRequest request) {
        return documentResponseMapper.toResponse(documentService.createDraft(request));
    }

    // Update DRAFT
    @PutMapping("/{id}")
    public DocumentResponse updateDraft(@PathVariable Long id, @Valid @RequestBody UpdateDraftRequest request) {
        return documentResponseMapper.toResponse(documentService.updateDraft(id, request));
    }

    // Submit
    @PostMapping("/{id}/submit")
    public DocumentResponse submit(@PathVariable Long id, @RequestBody(required = false) ActionRequest request) {
        return documentResponseMapper.toResponse(documentService.submit(id, request));
    }

    // Review → UNDER_REVIEW
    @PostMapping("/{id}/review")
    public DocumentResponse review(@PathVariable Long id, @RequestBody(required = false) ActionRequest request) {
        return documentResponseMapper.toResponse(documentService.review(id, request));
    }

    // Request changes → back to DRAFT
    @PostMapping("/{id}/request-changes")
    public DocumentResponse requestChanges(@PathVariable Long id, @RequestBody ActionRequest request) {
        return documentResponseMapper.toResponse(documentService.requestChanges(id, request));
    }

    // Approve
    @PostMapping("/{id}/approve")
    public DocumentResponse approve(@PathVariable Long id, @RequestBody(required = false) ActionRequest request) {
        return documentResponseMapper.toResponse(documentService.approve(id, request));
    }

    // Reject
    @PostMapping("/{id}/reject")
    public DocumentResponse reject(@PathVariable Long id, @RequestBody ActionRequest request) {
        return documentResponseMapper.toResponse(documentService.reject(id, request));
    }

    // Archive
    @PostMapping("/{id}/archive")
    public DocumentResponse archive(@PathVariable Long id, @RequestBody(required = false) ActionRequest request) {
        return documentResponseMapper.toResponse(documentService.archive(id, request));
    }

    // Get detail
    @GetMapping("/{id}")
    public DocumentResponse getById(@PathVariable Long id) {
        return documentResponseMapper.toResponse(documentService.getById(id));
    }

}
