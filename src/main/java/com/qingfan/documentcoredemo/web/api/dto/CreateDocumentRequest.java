package com.qingfan.documentcoredemo.web.api.dto;

import com.qingfan.documentcoredemo.domain.document.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentRequest {

    @NotBlank
    private String title;

    @NotNull
    private DocumentType type;

    @NotNull
    @Positive
    private BigDecimal amount;
}
