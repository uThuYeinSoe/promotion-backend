package com.promotion.gameItem.controller;

import com.promotion.config.JWTService;
import com.promotion.gameItem.dto.GameItemRequest;
import com.promotion.gameItem.dto.GameItemResp;
import com.promotion.gameItem.service.GameItemService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Data
@RequestMapping("/api/v1/promotion/gi")
public class GameItemController {

    @Autowired private final JWTService jwtService;
    @Autowired private final GameItemService gameItemService;

    @PostMapping("/gameItem")
    public ResponseEntity<GameItemResp> saveGameItem(@RequestHeader("Authorization") String token, @RequestBody GameItemRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);
        GameItemResp resObj = gameItemService.saveGameItem(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/gameItem")
    public ResponseEntity<GameItemResp> getGameItemAll(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);
        GameItemResp resObj = gameItemService.getGameItemAll(randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/gameItem/{gameId}")
    public ResponseEntity<GameItemResp> getGameItemByGameId(@RequestHeader("Authorization") String token,@PathVariable Long gameId){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);
        GameItemResp resObj = gameItemService.getGameItemByGame(gameId,randomId);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @PutMapping("/gameItem")
    public ResponseEntity<GameItemResp> updateGameItemStatus(@RequestHeader("Authorization") String token,@RequestBody GameItemRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);
        System.out.println(randomId);
        System.out.println(request);
        GameItemResp resObj = gameItemService.updateStatusGameItemS(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
