package com.promotion.role.controller;

import com.promotion.role.dto.RoleRequest;
import com.promotion.role.dto.RoleResponse;
import com.promotion.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotion/rc")
@RequiredArgsConstructor
public class RoleController {

    @Autowired private final RoleService roleService;

    @PostMapping("/role")
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest request){
        RoleResponse resObj = roleService.createRole(request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/role")
    public ResponseEntity<RoleResponse> getRole(){
        RoleResponse resObj = roleService.getAllRole();
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
