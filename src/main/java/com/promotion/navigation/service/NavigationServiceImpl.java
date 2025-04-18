package com.promotion.navigation.service;

import com.promotion.navigation.dto.NavigationRequest;
import com.promotion.navigation.dto.NavigationResponse;
import com.promotion.navigation.entity.Navigation;
import com.promotion.navigation.repo.NavigationRepo;
import com.promotion.role.entity.Role;
import com.promotion.role.repo.RoleRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class NavigationServiceImpl implements NavigationService{

    @Autowired private final NavigationRepo navigationRepo;
    @Autowired private final RoleRepo roleRepo;

    @Override
    @Transactional
    public NavigationResponse saveNavigation(NavigationRequest request) {
        List<Role> roleList = roleRepo.findAllById(request.getRoleIds());
        Set<Role> roles = new HashSet<>(roleList);

        System.out.println("Roles Found: " + roleList.size());

        var saveObj = Navigation
                .builder()
                .roles(roles)
                .navCode(request.getNavCode())
                .navName(request.getNavName())
                .build();
        Navigation resObj = navigationRepo.save(saveObj);
        return NavigationResponse
                .builder()
                .status(true)
                .statusCode(201)
                .statusMessage("Save Navigation " + request.getNavName())
                .build();
    }
}
