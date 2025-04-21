package com.promotion.gameTicket.controller;

import com.promotion.config.JWTService;
import com.promotion.gameTicket.dto.GameTicketRequest;
import com.promotion.gameTicket.dto.GameTicketResp;
import com.promotion.gameTicket.service.GameTicketService;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion/gtc")
public class GameTicketController {

    @Autowired private final JWTService jwtService;
    @Autowired private final GameTicketService gameTicketService;

    @PostMapping("/gameTicket")
    private ResponseEntity<GameTicketResp> saveGameTicket(@RequestHeader("Authorization") String token, @RequestBody GameTicketRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        GameTicketResp resObj = gameTicketService.saveGameTicket(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/gameTicket")
    private ResponseEntity<GameTicketResp> getGameTicketByAgent(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        GameTicketResp resObj = gameTicketService.getTicketAllByAgent(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

}
