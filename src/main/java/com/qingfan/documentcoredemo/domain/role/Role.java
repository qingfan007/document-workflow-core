package com.qingfan.documentcoredemo.domain.role;

public enum Role {
    CREATOR("ROLE_CREATOR"),
    REVIEWER("ROLE_REVIEWER"),
    APPROVER("ROLE_APPROVER");

    private final String springRole;

    Role(String springRole) {
        this.springRole = springRole;
    }

    public boolean matches(String currentRole) {
        return springRole.equalsIgnoreCase(currentRole);
    }
}
