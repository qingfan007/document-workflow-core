package com.qingfan.documentcoredemo.web.ui;

import com.qingfan.documentcoredemo.domain.document.Document;
import com.qingfan.documentcoredemo.domain.document.DocumentStatus;
import com.qingfan.documentcoredemo.util.SecurityUtil;
import com.qingfan.documentcoredemo.web.api.dto.ActionRequest;
import com.qingfan.documentcoredemo.web.api.dto.UpdateDraftRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UiViewModelMapper {

    public DocumentView toView(Document doc) {
        DocumentView v = new DocumentView();
        v.setId(doc.getId());
        v.setTitle(doc.getTitle());
        v.setType(doc.getType());
        v.setAmount(doc.getAmount());
        v.setStatus(doc.getStatus());
        v.setCreatedBy(doc.getCreatedBy());
        v.setCreatedAt(doc.getCreatedAt());

        v.setStatusLabel(doc.getStatus().name());

        boolean isCreator = SecurityUtil.hasRole("CREATOR");
        boolean isReviewer = SecurityUtil.hasRole("REVIEWER");
        boolean isApprover = SecurityUtil.hasRole("APPROVER");

        DocumentStatus status = doc.getStatus();

        v.setEditable(isCreator && status == DocumentStatus.DRAFT);
        v.setCanSubmit(isCreator && status == DocumentStatus.DRAFT);

        v.setCanReview(isReviewer && status == DocumentStatus.SUBMITTED);

        v.setCanApprove(isApprover && status == DocumentStatus.UNDER_REVIEW);
        v.setCanReject(isApprover && status == DocumentStatus.UNDER_REVIEW);

        v.setCanArchive(isApprover && (status == DocumentStatus.APPROVED || status == DocumentStatus.REJECTED));

        return v;
    }

    public List<DocumentView> toViews(List<Document> documents) {
        return documents.stream().map(this::toView).collect(Collectors.toList());
    }

    public ActionRequest actionRequest(String reason) {
        ActionRequest req = new ActionRequest();
        req.setReason(reason);
        return req;
    }

    public UpdateDraftRequest toUpdateDraftForm(Document doc) {
        UpdateDraftRequest req = new UpdateDraftRequest();
        req.setTitle(doc.getTitle());
        req.setAmount(doc.getAmount());
        return req;
    }

}
