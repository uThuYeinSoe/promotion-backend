package com.promotion.dice.controller;

import com.promotion.config.JWTService;
import com.promotion.dice.dto.DiceResponse;
import com.promotion.dice.service.DiceService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion/dice")
public class DiceController {

    @Autowired private final JWTService jwtService;
    @Autowired private final DiceService diceService;

    @GetMapping("/winningValue")
    public ResponseEntity<DiceResponse> getWinNumber(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        DiceResponse resObj = diceService.getWinObj(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
