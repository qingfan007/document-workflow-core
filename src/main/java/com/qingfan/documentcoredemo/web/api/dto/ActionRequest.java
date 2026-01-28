package com.qingfan.documentcoredemo.web.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionRequest {

    /**
     * Optional reason or comment for this action
     * e.g. reject reason, change request note
     */
    private String reason;
}
