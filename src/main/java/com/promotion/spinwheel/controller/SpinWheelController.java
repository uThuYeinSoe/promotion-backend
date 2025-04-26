package com.promotion.spinwheel.controller;

import com.promotion.config.JWTService;
import com.promotion.spinwheel.dto.SpanWheelResponse;
import com.promotion.spinwheel.service.SpanWheelService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@Builder
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion/pinwheel")
public class SpinWheelController {

    @Autowired private final JWTService jwtService;
    @Autowired private final SpanWheelService spanWheelService;

    @GetMapping("/winningValue")
    public ResponseEntity<SpanWheelResponse> getWinNumber(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        SpanWheelResponse resObj = spanWheelService.getWinObj(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
