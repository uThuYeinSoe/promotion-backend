package com.promotion.user.controller;

import com.promotion.config.JWTService;
import com.promotion.user.dto.AgentResponse;
import com.promotion.user.dto.Profile;
import com.promotion.user.dto.UserLevelDataRequest;
import com.promotion.user.dto.UserLevelDataResponse;
import com.promotion.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promotion/uc")
@RequiredArgsConstructor
public class UserController {

    @Autowired private final JWTService jwtService;
    @Autowired private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<Profile> getProfile(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        Profile resObj = profileService.getProfile(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/getAgent")
    public ResponseEntity<AgentResponse> getAgentAll(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        AgentResponse resObj = profileService.getAgentAll(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @PostMapping("/userOnly")
    public ResponseEntity<UserLevelDataResponse> userData(@RequestHeader("Authorization") String token, @RequestBody UserLevelDataRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        UserLevelDataResponse resObj = profileService.saveUserLevelData(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
