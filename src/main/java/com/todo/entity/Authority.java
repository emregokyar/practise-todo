package com.todo.entity;

import jakarta.persistence.Embeddable;
import org.springframework.security.core.GrantedAuthority;

@Embeddable
// Indicates this class can be embedded as a component inside other entities, this means authority is the part of user object
public class Authority implements GrantedAuthority {
    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
