package com.promotion.navigation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promotion.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "navigation")
public class Navigation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nav_code")
    private String navCode;
    @Column(name = "nav_name")
    private String navName;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "navigation_roles",
            joinColumns = @JoinColumn(name = "navigation_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();
}
