package com.promotion.game.controller;

import com.promotion.config.JWTService;
import com.promotion.game.dto.GameAndAgentJoinRequest;
import com.promotion.game.dto.GameAndAgentJoinResponse;
import com.promotion.game.dto.GameRequest;
import com.promotion.game.dto.GameResponse;
import com.promotion.game.service.GameService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotion/g")
public class GameController {

    @Autowired private final GameService gameService;
    @Autowired private final JWTService jwtService;

    @PostMapping("/game")
    public ResponseEntity<GameResponse> saveGame(@RequestHeader("Authorization") String token, @RequestBody GameRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        GameResponse resObj =gameService.saveGame(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @PutMapping("/game")
    public ResponseEntity<GameResponse> updateGame(@RequestHeader("Authorization") String token, @RequestBody GameRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        GameResponse resObj =gameService.updateGame(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/gameAll")
    public ResponseEntity<GameResponse> getGameAll(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        GameResponse resObj = gameService.getGameAll(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @PostMapping("/assignGame")
    public ResponseEntity<GameAndAgentJoinResponse> assignGameToAgent(@RequestHeader("Authorization") String token, @RequestBody GameAndAgentJoinRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        GameAndAgentJoinResponse resObj = gameService.assignGameToAgent(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
