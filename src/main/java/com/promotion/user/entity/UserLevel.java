package com.promotion.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_level")
public class UserLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "random_id")
    private String randomId;
    @Column(name = "parent_id")
    private String parentId;
    @Column(name =" outstanding_unique_id")
    private String outStandingUniqueId;
    @Column(name ="outstanding_user_name")
    private String outStandingUserName;
}
