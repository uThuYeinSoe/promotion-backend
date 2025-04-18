package com.promotion.role.service;

import com.promotion.role.dto.RoleRequest;
import com.promotion.role.dto.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
    RoleResponse getAllRole();
}
