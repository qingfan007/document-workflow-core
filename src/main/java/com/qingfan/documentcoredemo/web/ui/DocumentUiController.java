package com.qingfan.documentcoredemo.web.ui;

import com.qingfan.documentcoredemo.domain.document.Document;
import com.qingfan.documentcoredemo.domain.document.DocumentType;
import com.qingfan.documentcoredemo.service.DocumentService;
import com.qingfan.documentcoredemo.web.api.dto.CreateDocumentRequest;
import com.qingfan.documentcoredemo.web.api.dto.UpdateDraftRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentUiController {

    private final DocumentService documentService;
    private final UiViewModelMapper mapper;

    // List page
    @GetMapping
    public String list(Model model) {
        model.addAttribute("documents",
                mapper.toViews(documentService.listAll()));
        return "document/list";
    }

    // Create form
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new CreateDocumentRequest());
        model.addAttribute("types", DocumentType.values());
        return "document/create";
    }

    // Create submit (create draft)
    @PostMapping
    public String create(@Valid @ModelAttribute("form") CreateDocumentRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("types", DocumentType.values());
            return "document/create";
        }
        Document created = documentService.createDraft(request);
        return "redirect:/documents/" + created.getId();
    }


    // Detail page
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("document", mapper.toView(documentService.getById(id)));

        model.addAttribute("actionForm", new ActionForm());
        model.addAttribute("updateDraftForm", mapper.toUpdateDraftForm(documentService.getById(id)));

        return "document/detail";
    }

    // Edit draft form (optional: separate page)
    @GetMapping("/{id}/edit")
    public String editDraftForm(@PathVariable Long id, Model model) {
        model.addAttribute("document", mapper.toView(documentService.getById(id)));
        model.addAttribute("form", mapper.toUpdateDraftForm(documentService.getById(id)));
        return "document/edit";
    }

    // Update draft submit
    @PostMapping("/{id}/edit")
    public String updateDraft(@PathVariable Long id,
                              @Valid @ModelAttribute("form") UpdateDraftRequest request,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("document", mapper.toView(documentService.getById(id)));
            return "document/edit";
        }
        documentService.updateDraft(id, request);
        return "redirect:/documents/" + id;
    }

    // DRAFT -> SUBMITTED
    @PostMapping("/{id}/submit")
    public String submit(@PathVariable Long id,
                         @ModelAttribute("actionForm") ActionForm form) {
        documentService.submit(id, mapper.actionRequest(form.getReasonOrNote()));
        return "redirect:/documents/" + id;
    }

    // SUBMITTED -> UNDER_REVIEW
    @PostMapping("/{id}/review")
    public String review(@PathVariable Long id,
                         @ModelAttribute("actionForm") ActionForm form) {
        documentService.review(id, mapper.actionRequest(form.getReasonOrNote()));
        return "redirect:/documents/" + id;
    }

    // UNDER_REVIEW -> DRAFT
    @PostMapping("/{id}/request-changes")
    public String requestChanges(@PathVariable Long id,
                                 @ModelAttribute("actionForm") ActionForm form) {
        documentService.requestChanges(id, mapper.actionRequest(form.getReasonOrNote()));
        return "redirect:/documents/" + id;
    }

    // UNDER_REVIEW -> APPROVED
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id,
                          @ModelAttribute("actionForm") ActionForm form) {
        documentService.approve(id, mapper.actionRequest(form.getReasonOrNote()));
        return "redirect:/documents/" + id;
    }

    // UNDER_REVIEW -> REJECTED
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id,
                         @ModelAttribute("actionForm") ActionForm form) {
        documentService.reject(id, mapper.actionRequest(form.getReasonOrNote()));
        return "redirect:/documents/" + id;
    }

    // APPROVED/REJECTED -> ARCHIVED
    @PostMapping("/{id}/archive")
    public String archive(@PathVariable Long id,
                          @ModelAttribute("actionForm") ActionForm form) {
        documentService.archive(id, mapper.actionRequest(form.getReasonOrNote()));
        return "redirect:/documents/" + id;
    }

    // Small helper form for UI
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActionForm {
        private String reasonOrNote;

    }


}
