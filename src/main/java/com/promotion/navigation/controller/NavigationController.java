package com.promotion.navigation.controller;

import com.promotion.navigation.dto.NavigationRequest;
import com.promotion.navigation.dto.NavigationResponse;
import com.promotion.navigation.service.NavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion/nvc")
public class NavigationController {

    @Autowired private final NavigationService navigationService;

    @GetMapping("/navigation")
    public ResponseEntity<NavigationResponse> getNavigationAll(){
        System.out.println("Navigation Controller Working");
        return null;
    }

    @PostMapping("/navigation")
    public ResponseEntity<NavigationResponse> saveNavigation(@RequestBody NavigationRequest request){
        NavigationResponse resObj = navigationService.saveNavigation(request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
