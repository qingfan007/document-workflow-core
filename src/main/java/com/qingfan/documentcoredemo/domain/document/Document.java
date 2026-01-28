package com.qingfan.documentcoredemo.domain.document;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DocumentType type = DocumentType.GENERIC;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DocumentStatus status = DocumentStatus.DRAFT;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 80)
    private String createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Document(String title, DocumentType type, BigDecimal amount, String createdBy) {
        this.title = title;
        this.type = type == null ? DocumentType.GENERIC : type;
        this.amount = amount;
        this.createdBy = createdBy;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = DocumentStatus.DRAFT;
        if (this.type == null) this.type = DocumentType.GENERIC;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
