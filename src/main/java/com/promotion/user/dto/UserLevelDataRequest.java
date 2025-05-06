package com.promotion.user.dto;

import com.promotion.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLevelDataRequest {
    private String parentId;
    private Integer ticketAmt;
    private String uniqueId;
    private String userName;
}
