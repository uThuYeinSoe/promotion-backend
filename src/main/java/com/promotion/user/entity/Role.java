package com.promotion.user.entity;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.promotion.user.entity.Permission.*;

@Getter
public enum Role {
    USER(
        Set.of(
            USER_READ,
            USER_UPDATE,
            USER_CREATE,
            USER_DELETE
        )
    ),
    ADMIN(
        Set.of(
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_CREATE,
            ADMIN_DELETE
        )

    ),

    AGENT(
        Set.of(
           AGENT_READ,
            AGENT_UPDATE,
            AGENT_CREATE,
            AGENT_DELETE
        )
    );

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = new ArrayList<SimpleGrantedAuthority>(
                permissions.stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.name()))
                        .toList()
        );

        authorities.add(new SimpleGrantedAuthority("ROLE_" +this.name()));
        return authorities;
    }

}
