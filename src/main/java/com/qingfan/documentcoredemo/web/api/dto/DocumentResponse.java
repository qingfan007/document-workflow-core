package com.qingfan.documentcoredemo.web.api.dto;

import com.qingfan.documentcoredemo.domain.document.DocumentStatus;
import com.qingfan.documentcoredemo.domain.document.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

    private Long id;
    private String title;
    private DocumentType type;
    private BigDecimal amount;
    private DocumentStatus status;

    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
