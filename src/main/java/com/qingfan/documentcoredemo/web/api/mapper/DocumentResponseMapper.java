package com.qingfan.documentcoredemo.web.api.mapper;

import com.qingfan.documentcoredemo.domain.document.Document;
import com.qingfan.documentcoredemo.web.api.dto.DocumentResponse;
import org.springframework.stereotype.Component;

@Component
public class DocumentResponseMapper {

    public DocumentResponse toResponse(Document document) {
        DocumentResponse resp = new DocumentResponse();
        resp.setId(document.getId());
        resp.setTitle(document.getTitle());
        resp.setType(document.getType());
        resp.setAmount(document.getAmount());
        resp.setStatus(document.getStatus());
        resp.setCreatedBy(document.getCreatedBy());
        resp.setCreatedAt(document.getCreatedAt());
        resp.setLastModifiedAt(document.getUpdatedAt());
        return resp;
    }

}
