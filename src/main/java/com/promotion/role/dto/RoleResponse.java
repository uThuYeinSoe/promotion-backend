package com.promotion.role.dto;

import com.promotion.role.entity.Role;
import com.promotion.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
public class RoleResponse extends BaseResponse {
    private List<Role> roleList;
}
