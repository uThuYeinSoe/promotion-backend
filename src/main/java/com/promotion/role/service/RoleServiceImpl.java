package com.promotion.role.service;

import com.promotion.role.dto.RoleRequest;
import com.promotion.role.dto.RoleResponse;
import com.promotion.role.entity.Role;
import com.promotion.role.repo.RoleRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    @Autowired private final RoleRepo roleRepo;

    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest request) {

        Role roleObj = Role
                .builder()
                .roleName(request.getRoleName())
                .build();

        Optional<Role> roleRes = Optional.of(roleRepo.save(roleObj));
        return RoleResponse
                .builder()
                .status(true)
                .statusCode(201)
                .statusMessage(request.getRoleName() + " Successfully")
                .build();
    }

    @Override
    @Transactional
    public RoleResponse getAllRole() {
        List<Role> roleList = roleRepo.findAll();
        return RoleResponse
                .builder()
                .statusMessage("API Good Working")
                .status(true)
                .statusCode(200)
                .roleList(roleList)
                .build();
    }
}
