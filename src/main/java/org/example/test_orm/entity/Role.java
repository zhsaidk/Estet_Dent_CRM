package org.example.test_orm.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    DOCTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
