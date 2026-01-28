package com.qingfan.documentcoredemo.web.ui;

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
public class DocumentView {

    private Long id;
    private String title;
    private DocumentType type;
    private BigDecimal amount;
    private DocumentStatus status;
    private String createdBy;
    private LocalDateTime createdAt;

    // UI-friendly helpers
    private boolean editable;      // show Edit button
    private boolean canSubmit;     // show Submit button
    private boolean canReview;     // show Review button
    private boolean canApprove;    // show Approve button
    private boolean canReject;     // show Reject button
    private boolean canArchive;    // show Archive button

    // optional: UI display text
    private String statusLabel;

}
